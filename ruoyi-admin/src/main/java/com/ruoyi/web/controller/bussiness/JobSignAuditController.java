package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.domain.SignInRecord;
import com.ruoyi.system.domain.vo.JobRefundOrderVo;
import com.ruoyi.system.domain.vo.JobSignAuditRecordVo;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.ISignInRecordService;
import com.ruoyi.web.controller.bussiness.bo.JobSignAuditBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "兼职签到审核")
@RestController
@RequestMapping("/system/job-sign-audit")
public class JobSignAuditController extends BaseController {

    private static final Integer AUDIT_STATUS_PENDING = 1;
    private static final Integer AUDIT_STATUS_APPROVED = 2;
    private static final Integer AUDIT_STATUS_REJECTED = 3;
    private static final Integer SIGN_STATUS_PENDING = 0;
    private static final Integer SIGN_STATUS_APPROVED = 1;

    @Autowired
    private IJobSignupOrderService jobSignupOrderService;

    @Autowired
    private ISignInRecordService signInRecordService;

    @ApiOperation("兼职签到审核列表")
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfoVo<JobSignAuditRecordVo> list(@RequestParam("jobId") Long jobId) {
        startPage();
        List<JobSignAuditRecordVo> list = jobSignupOrderService.selectJobSignAuditRecords(jobId);
        return getDataTable(list);
    }

    @ApiOperation("审核兼职签到材料")
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "兼职签到审核", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/audit")
    public AjaxResult audit(@PathVariable("id") Long id, @RequestBody(required = false) JobSignAuditBo bo) {
        if (id == null) {
            return error("签到记录不能为空");
        }
        Integer auditStatus = bo == null ? null : bo.getAuditStatus();
        if (!AUDIT_STATUS_APPROVED.equals(auditStatus) && !AUDIT_STATUS_REJECTED.equals(auditStatus)) {
            return error("审核状态不合法");
        }

        SignInRecord record = signInRecordService.selectSignInRecordById(id);
        if (record == null || record.getJobId() == null || record.getRecordType() == null || record.getRecordType() != 3) {
            return error("签到记录不存在");
        }
        if (StringUtils.isBlank(record.getSignImageUrl())) {
            return error("该记录尚未提交签到材料");
        }
        if (!AUDIT_STATUS_PENDING.equals(record.getAuditStatus())) {
            return error("该记录当前不可审核");
        }

        record.setAuditStatus(auditStatus);
        record.setAuditRemark(normalizeAuditRemark(auditStatus, bo == null ? null : bo.getAuditRemark()));
        record.setAuditTime(DateUtils.getNowDate());
        record.setAuditBy(getUsername());
        record.setSignStatus(AUDIT_STATUS_APPROVED.equals(auditStatus) ? SIGN_STATUS_APPROVED : SIGN_STATUS_PENDING);
        signInRecordService.updateJobSignAuditFields(record);
        return success();
    }

    @ApiOperation("校验兼职订单是否允许退款")
    @PreAuthorize("@ss.hasPermi('system:refund:edit')")
    @GetMapping("/refund-check/{orderNo}")
    public AjaxResult refundCheck(@PathVariable("orderNo") String orderNo) {
        JobSignupOrder order = jobSignupOrderService.selectJobSignupOrderByOrderNo(orderNo);
        validateRefundOrder(order, orderNo);
        return success();
    }

    public void validateRefundOrder(JobSignupOrder order, String orderNo) {
        if (order == null || order.getJobId() == null) {
            throw new ServiceException("订单不存在");
        }
        List<JobRefundOrderVo> refundOrders = jobSignupOrderService.selectPaidOrdersWithSignIn(order.getJobId());
        for (JobRefundOrderVo refundOrder : refundOrders) {
            if (StringUtils.equals(orderNo, refundOrder.getOrderNo())) {
                if (Boolean.TRUE.equals(refundOrder.getCanRefund())) {
                    return;
                }
                throw new ServiceException("签到审核通过后才可退款");
            }
        }
        throw new ServiceException("订单不存在");
    }

    private String normalizeAuditRemark(Integer auditStatus, String auditRemark) {
        if (AUDIT_STATUS_REJECTED.equals(auditStatus) && StringUtils.isBlank(auditRemark)) {
            return "签到材料审核未通过";
        }
        return StringUtils.trimToNull(auditRemark);
    }
}
