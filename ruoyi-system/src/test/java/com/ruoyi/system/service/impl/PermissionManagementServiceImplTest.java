package com.ruoyi.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.mapper.SysMenuMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;

@ExtendWith(MockitoExtension.class)
class PermissionManagementServiceImplTest
{
    @Mock private SysUserMapper userMapper;
    @Mock private SysMenuMapper menuMapper;
    @Mock private SysRoleMapper roleMapper;
    @Mock private SysRoleMenuMapper roleMenuMapper;
    @Mock private SysUserRoleMapper userRoleMapper;
    @Mock private RedisCache redisCache;
    @InjectMocks private PermissionManagementServiceImpl service;

    @BeforeEach
    void setUp()
    {
        authenticate(2L);
    }

    @AfterEach
    void tearDown()
    {
        SecurityContextHolder.clearContext();
    }

    @Test
    void rejectsStaleCachedNationalManagerWhenDatabaseActorIsOrdinary()
    {
        when(userMapper.selectUserById(2L)).thenReturn(user(2L, "employee"));
        ServiceException error = assertThrows(ServiceException.class, service::getDelegableCatalog);
        assertEquals("无权限进行权限管理", error.getMessage());
        verify(menuMapper, never()).selectActivePermissionCatalogByUserId(2L);
    }

    @Test
    void nationalManagerCannotDelegatePermissionOutsideOwnEffectiveSet()
    {
        when(userMapper.selectUserById(2L)).thenReturn(user(2L, "national_general_manager"));
        when(userMapper.selectUserById(3L)).thenReturn(user(3L, "employee"));
        when(menuMapper.selectActivePermissionCatalog()).thenReturn(Arrays.asList(menu(10L), menu(20L)));
        when(menuMapper.selectActivePermissionCatalogByUserId(2L)).thenReturn(Collections.singletonList(menu(10L)));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.updateUserPermissions(3L, Collections.singletonList(20L)));
        assertEquals("不能授予超出自身范围的权限", error.getMessage());
        verify(roleMapper, never()).selectRolesByRoleKey(anyString());
    }

    @Test
    void disabledTargetGetReturnsReadOnlyMetadataWithoutIds()
    {
        authenticate(1L);
        when(userMapper.selectUserById(1L)).thenReturn(user(1L, null));
        SysUser target = user(3L, "employee");
        target.setStatus("1");
        when(userMapper.selectUserById(3L)).thenReturn(target);

        Map<String, Object> result = service.getUserPermissions(3L);
        assertEquals(Collections.emptyList(), result.get("permissionIds"));
        assertEquals(true, result.get("readOnly"));
        assertEquals("目标用户账号已停用", result.get("readOnlyReason"));
        verify(roleMapper, never()).selectRolesByRoleKey(anyString());
    }

    @Test
    void disabledTargetPutIsRejected()
    {
        authenticate(1L);
        when(userMapper.selectUserById(1L)).thenReturn(user(1L, null));
        SysUser target = user(3L, "employee");
        target.setStatus("1");
        when(userMapper.selectUserById(3L)).thenReturn(target);

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.updateUserPermissions(3L, Collections.emptyList()));
        assertEquals("目标用户账号已停用", error.getMessage());
    }

    @Test
    void prefixCollisionIsNotAValidManagedRoleKey()
    {
        assertFalse(service.isManagedRoleKey("permission_user_3_extra"));
        assertFalse(service.isManagedRoleKey("permissionXuserX3"));
        assertEquals(true, service.isManagedRoleKey("permission_user_3"));
    }

    @Test
    void rejectsMultipleManagedRolesLinkedToOneUser()
    {
        authenticate(1L);
        when(userMapper.selectUserById(1L)).thenReturn(user(1L, null));
        when(userMapper.selectUserById(3L)).thenReturn(user(3L, "employee"));
        when(roleMapper.selectRolesByRoleKey("permission_user_3"))
                .thenReturn(Collections.singletonList(role(30L, "permission_user_3", "0")));
        when(roleMapper.selectManagedRolesByUserId(3L)).thenReturn(Arrays.asList(
                role(30L, "permission_user_3", "0"), role(31L, "permission_user_4", "0")));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.getUserPermissions(3L));
        assertEquals("用户关联了多个托管角色，请联系管理员", error.getMessage());
    }

    @Test
    void disabledManagedRoleGetReturnsNoIdsAndPutRejects()
    {
        authenticate(1L);
        when(userMapper.selectUserById(1L)).thenReturn(user(1L, null));
        when(userMapper.selectUserById(3L)).thenReturn(user(3L, "employee"));
        SysRole disabled = role(30L, "permission_user_3", "1");
        when(roleMapper.selectRolesByRoleKey("permission_user_3")).thenReturn(Collections.singletonList(disabled));
        when(roleMapper.selectManagedRolesByUserId(3L)).thenReturn(Collections.singletonList(disabled));
        when(userRoleMapper.countLinksByUserAndRole(3L, 30L)).thenReturn(1);
        when(userRoleMapper.countLinksByRoleId(30L)).thenReturn(1);

        Map<String, Object> result = service.getUserPermissions(3L);
        assertEquals(Collections.emptyList(), result.get("permissionIds"));
        assertEquals("用户托管角色已停用", result.get("readOnlyReason"));

        when(menuMapper.selectActivePermissionCatalog()).thenReturn(Collections.emptyList());
        when(userMapper.lockUserById(3L)).thenReturn(3L);
        ServiceException error = assertThrows(ServiceException.class,
                () -> service.updateUserPermissions(3L, Collections.emptyList()));
        assertEquals("用户托管角色已停用，不能修改权限", error.getMessage());
    }

    private void authenticate(Long userId)
    {
        SysUser cached = user(userId, "national_general_manager");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                new LoginUser(userId, 100L, cached, Collections.emptySet()), null));
    }

    private SysUser user(Long id, String level)
    {
        SysUser user = new SysUser();
        user.setUserId(id);
        user.setUserName("user" + id);
        user.setAdminLevel(level);
        user.setStatus("0");
        user.setDelFlag("0");
        return user;
    }

    private SysMenu menu(Long id)
    {
        SysMenu menu = new SysMenu();
        menu.setMenuId(id);
        return menu;
    }

    private SysRole role(Long id, String key, String status)
    {
        SysRole role = new SysRole();
        role.setRoleId(id);
        role.setRoleKey(key);
        role.setStatus(status);
        role.setDelFlag("0");
        return role;
    }
}
