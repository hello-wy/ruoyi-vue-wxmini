package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysRoleMenu;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysMenuMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.service.IPermissionManagementService;

@Service
public class PermissionManagementServiceImpl implements IPermissionManagementService
{
    public static final String MANAGED_ROLE_KEY_PREFIX = "permission_user_";
    private static final String NATIONAL_MANAGER = "national_general_manager";
    private static final String MANAGED_ROLE_REMARK = "权限管理专用角色，请勿手工维护";
    private static final String NORMAL = "0";

    @Autowired private SysUserMapper userMapper;
    @Autowired private SysMenuMapper menuMapper;
    @Autowired private SysRoleMapper roleMapper;
    @Autowired private SysRoleMenuMapper roleMenuMapper;
    @Autowired private SysUserRoleMapper userRoleMapper;
    @Autowired private RedisCache redisCache;

    @Override
    public List<SysMenu> getPermissionCatalog()
    {
        requireActor();
        return menuMapper.selectActivePermissionCatalog();
    }

    @Override
    public List<SysMenu> getDelegableCatalog()
    {
        return getPermissionCatalog();
    }

    @Override
    public List<Long> getGrantablePermissionIds()
    {
        SysUser actor = requireActor();
        List<SysMenu> menus = actor.isAdmin() ? menuMapper.selectActivePermissionCatalog()
                : menuMapper.selectActivePermissionCatalogByUserId(actor.getUserId());
        return menus.stream().map(SysMenu::getMenuId).collect(Collectors.toList());
    }

    @Override
    public void checkActor()
    {
        requireActor();
    }

    @Override
    public List<SysUser> selectManageableUsers(String keyword)
    {
        List<SysUser> users = userMapper.selectPermissionManageableUsers(StringUtils.trim(keyword));
        users.forEach(user -> {
            user.setPassword(null);
            user.setRoles(null);
            user.setRoleIds(null);
        });
        return users;
    }

    @Override
    public Map<String, Object> getUserPermissions(Long userId)
    {
        requireActor();
        SysUser target = requireTarget(userId, false);
        if (!isActive(target))
        {
            return assignmentResult(Collections.emptyList(), true, "目标用户账号已停用");
        }
        if (target.isAdmin())
        {
            List<Long> allPermissionIds = menuMapper.selectActivePermissionCatalog().stream()
                    .map(SysMenu::getMenuId).collect(Collectors.toList());
            Map<String, Object> result = assignmentResult(allPermissionIds, true,
                    "超级管理员默认拥有全部权限，权限不可修改");
            result.put("inheritedPermissionIds", Collections.emptyList());
            return result;
        }
        if (isProtected(target))
        {
            Map<String, Object> result = assignmentResult(Collections.emptyList(), true,
                    "该账号为受保护管理员，权限不可修改");
            result.put("inheritedPermissionIds", getInheritedPermissionIds(userId, null));
            return result;
        }

        SysRole role = findAndValidateManagedRole(userId, false);
        if (role == null)
        {
            Map<String, Object> result = assignmentResult(Collections.emptyList(), false, "");
            result.put("inheritedPermissionIds", getInheritedPermissionIds(userId, null));
            return result;
        }
        if (!NORMAL.equals(role.getStatus()))
        {
            Map<String, Object> result = assignmentResult(Collections.emptyList(), true,
                    "用户托管角色已停用");
            result.put("inheritedPermissionIds", getInheritedPermissionIds(userId, role.getRoleId()));
            return result;
        }
        Set<Long> active = menuMapper.selectActivePermissionCatalog().stream()
                .map(SysMenu::getMenuId).collect(Collectors.toSet());
        List<Long> ids = menuMapper.selectMenuListByRoleId(role.getRoleId(), false).stream()
                .filter(active::contains).collect(Collectors.toList());
        Map<String, Object> result = assignmentResult(ids, false, "");
        result.put("inheritedPermissionIds", getInheritedPermissionIds(userId, role.getRoleId()));
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> updateUserPermissions(Long userId, List<Long> permissionIds)
    {
        SysUser actor = requireActor();
        SysUser target = requireTarget(userId, true);
        if (isProtected(target))
        {
            throw new ServiceException("不允许修改受保护管理员权限");
        }
        if (permissionIds == null)
        {
            throw new ServiceException("权限列表不能为空");
        }
        Set<Long> requested = new HashSet<>(permissionIds);
        if (requested.contains(null))
        {
            throw new ServiceException("权限编号不能为空");
        }
        Set<Long> catalog = menuMapper.selectActivePermissionCatalog().stream()
                .map(SysMenu::getMenuId).collect(Collectors.toSet());
        if (!catalog.containsAll(requested))
        {
            throw new ServiceException("包含无效或已停用的权限");
        }
        Set<Long> owned = Collections.emptySet();
        if (!actor.isAdmin())
        {
            owned = menuMapper.selectActivePermissionCatalogByUserId(actor.getUserId()).stream()
                    .map(SysMenu::getMenuId).collect(Collectors.toSet());
            if (!owned.containsAll(requested))
            {
                throw new ServiceException("不能授予超出自身范围的权限");
            }
        }

        SysRole role = getOrCreateManagedRole(target, actor.getUserName());
        if (!NORMAL.equals(role.getStatus()))
        {
            throw new ServiceException("用户托管角色已停用，不能修改权限");
        }
        if (!actor.isAdmin())
        {
            Set<Long> finalRequested = new HashSet<>(requested);
            for (Long existing : menuMapper.selectMenuListByRoleId(role.getRoleId(), false))
            {
                if (catalog.contains(existing) && !owned.contains(existing))
                {
                    finalRequested.add(existing);
                }
            }
            requested = finalRequested;
        }
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        if (!requested.isEmpty())
        {
            List<SysRoleMenu> rows = new ArrayList<>();
            for (Long menuId : requested.stream().sorted().collect(Collectors.toList()))
            {
                SysRoleMenu row = new SysRoleMenu();
                row.setRoleId(role.getRoleId());
                row.setMenuId(menuId);
                rows.add(row);
            }
            roleMenuMapper.batchRoleMenu(rows);
        }

        boolean invalidated = invalidateSessions(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("sessionInvalidated", invalidated);
        result.put("message", invalidated ? "权限已更新，用户会话已失效" : "权限已更新，未发现活动会话");
        return result;
    }

    @Override
    public boolean isManagedRoleKey(String roleKey)
    {
        return managedRoleOwner(roleKey) != null;
    }

    private SysUser requireActor()
    {
        Long actorId = SecurityUtils.getUserId();
        SysUser actor = userMapper.selectUserById(actorId);
        if (actor == null || (!actor.isAdmin() && !NATIONAL_MANAGER.equals(actor.getAdminLevel())))
        {
            throw new ServiceException("无权限进行权限管理");
        }
        if (!isActive(actor))
        {
            throw new ServiceException("当前管理员账号不可用");
        }
        return actor;
    }

    private SysUser requireTarget(Long userId, boolean activeRequired)
    {
        if (userId == null)
        {
            throw new ServiceException("用户编号不能为空");
        }
        SysUser target = userMapper.selectUserById(userId);
        if (target == null || !NORMAL.equals(target.getDelFlag()))
        {
            throw new ServiceException("用户不存在");
        }
        if (activeRequired && !isActive(target))
        {
            throw new ServiceException("目标用户账号已停用");
        }
        return target;
    }

    private boolean isActive(SysUser user)
    {
        return NORMAL.equals(user.getStatus()) && NORMAL.equals(user.getDelFlag());
    }

    private boolean isProtected(SysUser user)
    {
        return user.isAdmin() || NATIONAL_MANAGER.equals(user.getAdminLevel());
    }

    private SysRole getOrCreateManagedRole(SysUser target, String operator)
    {
        userMapper.lockUserById(target.getUserId());
        SysRole role = findAndValidateManagedRole(target.getUserId(), true);
        if (role == null)
        {
            role = newManagedRole(target, operator);
            try
            {
                roleMapper.insertRole(role);
            }
            catch (DuplicateKeyException e)
            {
                // A concurrent creator may win after the target row lock is released by another transaction.
                role = findAndValidateManagedRole(target.getUserId(), true);
                if (role == null)
                {
                    throw e;
                }
            }
        }
        SysUserRole link = new SysUserRole();
        link.setUserId(target.getUserId());
        link.setRoleId(role.getRoleId());
        userRoleMapper.insertUserRoleIfAbsent(link);
        validateManagedRoleLinks(role, target.getUserId());
        return role;
    }

    private SysRole findAndValidateManagedRole(Long userId, boolean rejectDisabled)
    {
        String expectedKey = managedRoleKey(userId);
        List<SysRole> exact = roleMapper.selectRolesByRoleKey(expectedKey);
        if (exact.size() > 1)
        {
            throw new ServiceException("用户托管角色键存在重复记录，请联系管理员");
        }
        List<SysRole> linked = roleMapper.selectManagedRolesByUserId(userId);
        if (linked.size() > 1)
        {
            throw new ServiceException("用户关联了多个托管角色，请联系管理员");
        }
        SysRole role = exact.isEmpty() ? null : exact.get(0);
        if (role == null)
        {
            if (!linked.isEmpty())
            {
                throw new ServiceException("用户关联了归属冲突的托管角色，请联系管理员");
            }
            return null;
        }
        Long owner = managedRoleOwner(role.getRoleKey());
        if (!userId.equals(owner))
        {
            throw new ServiceException("用户托管角色键归属异常，请联系管理员");
        }
        if (StringUtils.isNotEmpty(role.getRemark()) && !MANAGED_ROLE_REMARK.equals(role.getRemark()))
        {
            throw new ServiceException("用户托管角色标识异常，请联系管理员");
        }
        validateManagedRoleLinks(role, userId);
        if (!linked.isEmpty() && !role.getRoleId().equals(linked.get(0).getRoleId()))
        {
            throw new ServiceException("用户关联了归属冲突的托管角色，请联系管理员");
        }
        if (rejectDisabled && !NORMAL.equals(role.getStatus()))
        {
            throw new ServiceException("用户托管角色已停用，不能修改权限");
        }
        return role;
    }

    private void validateManagedRoleLinks(SysRole role, Long ownerUserId)
    {
        int ownerLinks = userRoleMapper.countLinksByUserAndRole(ownerUserId, role.getRoleId());
        int allLinks = userRoleMapper.countLinksByRoleId(role.getRoleId());
        if (ownerLinks > 1)
        {
            throw new ServiceException("用户托管角色关联存在重复记录，请联系管理员");
        }
        if (allLinks != ownerLinks)
        {
            throw new ServiceException("用户托管角色被错误关联到其他用户，请联系管理员");
        }
    }

    private SysRole newManagedRole(SysUser target, String operator)
    {
        SysRole role = new SysRole();
        role.setRoleName("用户权限-" + target.getUserId());
        role.setRoleKey(managedRoleKey(target.getUserId()));
        role.setRoleSort(99);
        role.setDataScope("5");
        role.setMenuCheckStrictly(false);
        role.setDeptCheckStrictly(false);
        role.setStatus(NORMAL);
        role.setCreateBy(operator);
        role.setRemark("权限管理专用角色，请勿手工维护");
        return role;
    }

    private String managedRoleKey(Long userId)
    {
        return MANAGED_ROLE_KEY_PREFIX + userId;
    }

    private Long managedRoleOwner(String roleKey)
    {
        if (roleKey == null || !roleKey.startsWith(MANAGED_ROLE_KEY_PREFIX))
        {
            return null;
        }
        String suffix = roleKey.substring(MANAGED_ROLE_KEY_PREFIX.length());
        if (suffix.isEmpty() || !suffix.chars().allMatch(Character::isDigit))
        {
            return null;
        }
        try
        {
            return Long.valueOf(suffix);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    private List<Long> getInheritedPermissionIds(Long userId, Long managedRoleId)
    {
        Set<Long> active = menuMapper.selectActivePermissionCatalog().stream()
                .map(SysMenu::getMenuId).collect(Collectors.toSet());
        Set<Long> inherited = menuMapper.selectActivePermissionCatalogByUserId(userId).stream()
                .map(SysMenu::getMenuId).filter(active::contains).collect(Collectors.toSet());
        if (managedRoleId != null)
        {
            inherited.removeAll(menuMapper.selectMenuListByRoleId(managedRoleId, false));
        }
        return inherited.stream().sorted().collect(Collectors.toList());
    }

    private Map<String, Object> assignmentResult(List<Long> ids, boolean readOnly, String reason)
    {
        Map<String, Object> result = new HashMap<>();
        result.put("permissionIds", ids);
        result.put("readOnly", readOnly);
        result.put("readOnlyReason", reason == null ? "" : reason);
        return result;
    }

    private boolean invalidateSessions(Long userId)
    {
        Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        if (keys == null || keys.isEmpty())
        {
            return false;
        }
        List<String> targetKeys = new ArrayList<>();
        for (String key : keys)
        {
            LoginUser loginUser = redisCache.getCacheObject(key);
            if (loginUser != null && userId.equals(loginUser.getUserId()))
            {
                targetKeys.add(key);
            }
        }
        return !targetKeys.isEmpty() && redisCache.deleteObject(targetKeys);
    }
}
