package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.MerchantUserTypeWhitelist;
import com.ruoyi.system.service.IMerchantUserTypeWhitelistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "商家身份白名单")
@RestController
@RequestMapping("/system/merchant-user-type-whitelist")
public class MerchantUserTypeWhitelistController extends BaseController {

    private static final int STATUS_PENDING = 0;
    private static final int STATUS_APPROVED = 1;
    private static final int STATUS_REJECTED = 2;

    @Autowired
    private IMerchantUserTypeWhitelistService whitelistService;

    @ApiOperation("查询商家身份白名单列表")
    @PreAuthorize("@ss.hasRole('admin')")
    @GetMapping("/list")
    public TableDataInfoVo<MerchantUserTypeWhitelist> list(MerchantUserTypeWhitelist whitelist) {
        normalizeQuery(whitelist);
        startPage();
        List<MerchantUserTypeWhitelist> list = whitelistService.selectMerchantUserTypeWhitelistList(whitelist);
        return getDataTable(list);
    }

    @ApiOperation("新增商家身份白名单")
    @PreAuthorize("@ss.hasRole('admin')")
    @Log(title = "商家身份白名单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MerchantUserTypeWhitelist whitelist) {
        if (whitelist == null) {
            return AjaxResult.error("参数不能为空");
        }
        String realName = StringUtils.trimToEmpty(whitelist.getRealName());
        if (StringUtils.isBlank(realName)) {
            return AjaxResult.error("请填写真实姓名");
        }
        String idCard = normalizeIdCard(whitelist.getIdCard());
        if (!idCard.matches("^\\d{17}[\\dX]$")) {
            return AjaxResult.error("请填写正确的18位身份证号");
        }
        if (whitelistService.selectMerchantUserTypeWhitelistByIdCard(idCard) != null) {
            return AjaxResult.error("该身份证已存在白名单记录");
        }
        whitelist.setId(SnowflakeIdWorker.nextIdDefault());
        whitelist.setRealName(realName);
        whitelist.setIdCard(idCard);
        whitelist.setRemark(StringUtils.trimToEmpty(whitelist.getRemark()));
        whitelist.setStatus(whitelist.getStatus() == null ? STATUS_APPROVED : whitelist.getStatus());
        if (!isValidStatus(whitelist.getStatus())) {
            return AjaxResult.error("状态值只能为0、1或2");
        }
        whitelist.setCreateBy(resolveCreateBy());
        whitelist.setCreateTime(DateUtils.getNowDate());
        return toAjax(whitelistService.insertMerchantUserTypeWhitelist(whitelist));
    }

    @ApiOperation("审核商家身份申请")
    @PreAuthorize("@ss.hasRole('admin')")
    @Log(title = "商家身份白名单", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/audit")
    public AjaxResult audit(@PathVariable Long id, @RequestBody MerchantUserTypeWhitelist whitelist) {
        if (whitelist == null || !isValidStatus(whitelist.getStatus())) {
            return AjaxResult.error("状态值只能为0、1或2");
        }
        whitelist.setId(id);
        whitelist.setRemark(StringUtils.trimToEmpty(whitelist.getRemark()));
        whitelist.setUpdateBy(resolveCreateBy());
        whitelist.setUpdateTime(DateUtils.getNowDate());
        return toAjax(whitelistService.updateMerchantUserTypeWhitelistAudit(whitelist));
    }

    @ApiOperation("删除商家身份白名单")
    @ApiImplicitParam(name = "ids", value = "白名单ID数组", required = true, dataType = "Long[]", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasRole('admin')")
    @Log(title = "商家身份白名单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(whitelistService.deleteMerchantUserTypeWhitelistByIds(ids));
    }

    private void normalizeQuery(MerchantUserTypeWhitelist whitelist) {
        if (whitelist == null) {
            return;
        }
        whitelist.setRealName(StringUtils.trimToNull(whitelist.getRealName()));
        whitelist.setIdCard(StringUtils.trimToNull(normalizeIdCard(whitelist.getIdCard())));
    }

    private String normalizeIdCard(String idCard) {
        return StringUtils.upperCase(StringUtils.trimToEmpty(idCard));
    }

    private boolean isValidStatus(Integer status) {
        return Integer.valueOf(STATUS_PENDING).equals(status)
                || Integer.valueOf(STATUS_APPROVED).equals(status)
                || Integer.valueOf(STATUS_REJECTED).equals(status);
    }

    private String resolveCreateBy() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception e) {
            return null;
        }
    }
}
