package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.ParttimeSignupWhitelist;
import com.ruoyi.system.service.IParttimeSignupWhitelistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "兼职报名白名单")
@RestController
@RequestMapping("/system/parttime-whitelist")
public class ParttimeSignupWhitelistController extends BaseController {

    @Autowired
    private IParttimeSignupWhitelistService whitelistService;

    @ApiOperation("查询兼职报名白名单列表")
    @PreAuthorize("@ss.hasRole('admin')")
    @GetMapping("/list")
    public TableDataInfoVo<ParttimeSignupWhitelist> list(ParttimeSignupWhitelist whitelist) {
        startPage();
        List<ParttimeSignupWhitelist> list = whitelistService.selectParttimeSignupWhitelistList(whitelist);
        return getDataTable(list);
    }

    @ApiOperation("新增兼职报名白名单")
    @PreAuthorize("@ss.hasRole('admin')")
    @Log(title = "兼职报名白名单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParttimeSignupWhitelist whitelist) {
        if (whitelist == null) {
            return AjaxResult.error("参数不能为空");
        }
        String realName = StringUtils.trimToEmpty(whitelist.getRealName());
        if (StringUtils.isBlank(realName)) {
            return AjaxResult.error("请填写真实姓名");
        }
        String idCard = StringUtils.upperCase(StringUtils.trimToEmpty(whitelist.getIdCard()));
        if (!idCard.matches("^\\d{17}[\\dX]$")) {
            return AjaxResult.error("请填写正确的18位身份证号");
        }
        if (whitelistService.selectParttimeSignupWhitelistByIdCard(idCard) != null) {
            return AjaxResult.error("该身份证已存在白名单记录");
        }
        whitelist.setId(SnowflakeIdWorker.nextIdDefault());
        whitelist.setRealName(realName);
        whitelist.setIdCard(idCard);
        whitelist.setRemark(StringUtils.trimToEmpty(whitelist.getRemark()));
        whitelist.setStatus(whitelist.getStatus() == null ? 1 : whitelist.getStatus());
        whitelist.setCreateBy(resolveCreateBy());
        whitelist.setCreateTime(DateUtils.getNowDate());
        return toAjax(whitelistService.insertParttimeSignupWhitelist(whitelist));
    }

    @ApiOperation("删除兼职报名白名单")
    @PreAuthorize("@ss.hasRole('admin')")
    @Log(title = "兼职报名白名单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable("id") Long id) {
        return toAjax(whitelistService.deleteParttimeSignupWhitelistById(id));
    }

    private String resolveCreateBy() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception e) {
            return null;
        }
    }
}
