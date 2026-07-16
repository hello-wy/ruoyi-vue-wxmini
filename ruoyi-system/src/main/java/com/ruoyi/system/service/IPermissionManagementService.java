package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;

/** Per-user permission management. */
public interface IPermissionManagementService
{
    List<SysMenu> getPermissionCatalog();

    /** Compatibility name retained for existing callers. */
    default List<SysMenu> getDelegableCatalog()
    {
        return getPermissionCatalog();
    }

    List<Long> getGrantablePermissionIds();

    void checkActor();

    List<SysUser> selectManageableUsers(String keyword);

    Map<String, Object> getUserPermissions(Long userId);

    Map<String, Object> updateUserPermissions(Long userId, List<Long> permissionIds);

    boolean isManagedRoleKey(String roleKey);
}
