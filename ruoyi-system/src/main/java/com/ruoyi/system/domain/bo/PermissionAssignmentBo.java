package com.ruoyi.system.domain.bo;

import java.util.List;
import javax.validation.constraints.NotNull;

/** Permission assignment request. */
public class PermissionAssignmentBo
{
    @NotNull(message = "权限列表不能为空")
    private List<Long> permissionIds;

    public List<Long> getPermissionIds()
    {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds)
    {
        this.permissionIds = permissionIds;
    }
}
