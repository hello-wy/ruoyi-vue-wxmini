package com.ruoyi.web.controller.system;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.bo.PermissionAssignmentBo;
import com.ruoyi.system.service.IPermissionManagementService;

@RestController
@RequestMapping("/system/permission-management")
public class PermissionManagementController extends BaseController
{
    @Autowired
    private IPermissionManagementService permissionManagementService;

    @GetMapping("/catalog")
    public AjaxResult catalog()
    {
        List<SysMenu> catalog = permissionManagementService.getPermissionCatalog();
        Map<String, Object> group = new HashMap<>();
        group.put("key", "system");
        group.put("name", "系统权限");
        group.put("permissions", catalog);
        Map<String, Object> result = new HashMap<>();
        result.put("groups", Collections.singletonList(group));
        result.put("grantablePermissionIds", permissionManagementService.getGrantablePermissionIds());
        return success(result);
    }

    @GetMapping("/users")
    public TableDataInfoVo<SysUser> users(
            @RequestParam(value = "keyword", required = false) String keyword)
    {
        permissionManagementService.checkActor();
        startPage();
        return getDataTable(permissionManagementService.selectManageableUsers(keyword));
    }

    @GetMapping({ "/users/{userId}", "/users/{userId}/permissions" })
    public AjaxResult permissions(@PathVariable Long userId)
    {
        return success(permissionManagementService.getUserPermissions(userId));
    }

    @Log(title = "用户权限管理", businessType = BusinessType.GRANT)
    @PutMapping({ "/users/{userId}", "/users/{userId}/permissions" })
    public AjaxResult updatePermissions(@PathVariable Long userId,
            @Valid @RequestBody PermissionAssignmentBo request)
    {
        return success(permissionManagementService.updateUserPermissions(userId,
                request == null ? null : request.getPermissionIds()));
    }
}
