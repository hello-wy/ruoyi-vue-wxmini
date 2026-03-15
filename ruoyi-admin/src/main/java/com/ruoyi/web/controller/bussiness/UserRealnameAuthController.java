package com.ruoyi.web.controller.bussiness;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.UserRealnameAuth;
import com.ruoyi.system.service.IUserRealnameAuthService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfoVo;

/**
 * 用户实名认证Controller
 * 
 * @author ruoyi
 * @date 2026-03-06
 */
@Api(tags = "用户实名认证管理")
@RestController
@RequestMapping("/system/auth")
public class UserRealnameAuthController extends BaseController
{
    @Autowired
    private IUserRealnameAuthService userRealnameAuthService;

    /**
     * 查询用户实名认证列表
     */
    @ApiOperation("查询用户实名认证列表")
    @PreAuthorize("@ss.hasPermi('system:auth:list')")
    @GetMapping("/list")
    public TableDataInfoVo<UserRealnameAuth> list(UserRealnameAuth userRealnameAuth)
    {
        startPage();
        List<UserRealnameAuth> list = userRealnameAuthService.selectUserRealnameAuthList(userRealnameAuth);
        return getDataTable(list);
    }

    /**
     * 导出用户实名认证列表
     */
    @ApiOperation("导出用户实名认证列表")
    @PreAuthorize("@ss.hasPermi('system:auth:export')")
    @Log(title = "用户实名认证", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserRealnameAuth userRealnameAuth)
    {
        List<UserRealnameAuth> list = userRealnameAuthService.selectUserRealnameAuthList(userRealnameAuth);
        ExcelUtil<UserRealnameAuth> util = new ExcelUtil<UserRealnameAuth>(UserRealnameAuth.class);
        util.exportExcel(response, list, "用户实名认证数据");
    }

    /**
     * 获取用户实名认证详细信息
     */
    @ApiOperation("获取用户实名认证详细信息")
    @ApiImplicitParam(name = "id", value = "认证记录ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('system:auth:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userRealnameAuthService.selectUserRealnameAuthById(id));
    }

    /**
     * 新增用户实名认证
     */
    @ApiOperation("新增用户实名认证")
    @PreAuthorize("@ss.hasPermi('system:auth:add')")
    @Log(title = "用户实名认证", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserRealnameAuth userRealnameAuth)
    {
        return toAjax(userRealnameAuthService.insertUserRealnameAuth(userRealnameAuth));
    }

    /**
     * 修改用户实名认证
     */
    @ApiOperation("修改用户实名认证")
    @PreAuthorize("@ss.hasPermi('system:auth:edit')")
    @Log(title = "用户实名认证", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserRealnameAuth userRealnameAuth)
    {
        return toAjax(userRealnameAuthService.updateUserRealnameAuth(userRealnameAuth));
    }

    /**
     * 删除用户实名认证
     */
    @ApiOperation("删除用户实名认证")
    @ApiImplicitParam(name = "ids", value = "认证记录ID数组", required = true, dataType = "Long[]", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('system:auth:remove')")
    @Log(title = "用户实名认证", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userRealnameAuthService.deleteUserRealnameAuthByIds(ids));
    }
}
