package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.CourseCashbackConfig;
import com.ruoyi.system.domain.CourseCashbackLedger;
import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.domain.StudentStaffAssignment;
import com.ruoyi.system.domain.bo.CourseCashbackDeductionBo;
import com.ruoyi.system.domain.bo.CourseFinanceQueryBo;
import com.ruoyi.system.domain.bo.StudentAccessScope;
import com.ruoyi.system.domain.vo.CourseFinanceSummaryVo;
import com.ruoyi.system.mapper.CourseCashbackMapper;
import com.ruoyi.system.mapper.StudentStaffAssignmentMapper;
import com.ruoyi.system.service.ICourseCashbackService;
import com.ruoyi.system.service.ICoursePayOrderService;
import com.ruoyi.system.service.ILecturesService;
import com.ruoyi.system.service.IStudentAccessService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CourseCashbackServiceImpl implements ICourseCashbackService {
    @Resource
    private CourseCashbackMapper mapper;
    @Resource
    private ICoursePayOrderService orders;
    @Resource
    private ILecturesService lectures;
    @Resource
    private IUserInfoService users;
    @Resource
    private StudentStaffAssignmentMapper assignments;
    @Resource
    private IStudentAccessService access;

    @Override
    public CourseCashbackConfig selectConfig() {
        return mapper.selectConfig();
    }

    @Override
    public void saveConfig(CourseCashbackConfig config) {
        if (config == null || config.getCashbackRatio() == null
                || config.getCashbackRatio().compareTo(BigDecimal.ZERO) < 0
                || config.getCashbackRatio().compareTo(BigDecimal.ONE) > 0) {
            throw new ServiceException("返现比例必须在0到1之间");
        }
        mapper.saveConfig(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordPaidCourseCashback(String orderNo) {
        // The singleton config row serializes order-level ledger creation and makes callback retries idempotent.
        mapper.lockFinanceConfig();
        if (mapper.selectLedgerByOrderNo(orderNo) != null) {
            return;
        }

        CoursePayOrder order = orders.selectCoursePayOrderByOrderNo(orderNo);
        if (order == null || !CoursePayOrder.STATUS_PAID_WAIT_SIGN.equals(order.getStatus())) {
            return;
        }

        CourseCashbackConfig config = selectConfig();
        UserInfo user = users.selectUserInfoByUserId(order.getUserId());
        if (config == null || user == null || user.getId() == null) {
            return;
        }

        StudentStaffAssignment assignment = assignments.selectByStudentId(user.getId());
        Lectures course = lectures.selectLecturesById(order.getCourseId());
        BigDecimal courseTotal = course == null || course.getCoursePrice() == null
                ? BigDecimal.ZERO : course.getCoursePrice();
        BigDecimal paidAmount = order.getAmount() == null ? BigDecimal.ZERO : order.getAmount();
        boolean unattributed = assignment == null || assignment.getEmployeeUserId() == null;

        CourseCashbackLedger ledger = new CourseCashbackLedger();
        ledger.setStudentId(user.getId());
        ledger.setEmployeeUserId(unattributed ? null : assignment.getEmployeeUserId());
        ledger.setOwnerDeptId(assignment == null ? null : assignment.getOwnerDeptId());
        ledger.setCourseId(order.getCourseId());
        ledger.setOrderNo(orderNo);
        ledger.setPaidAmount(paidAmount);
        ledger.setCourseTotal(courseTotal);
        ledger.setOutstandingAmount(courseTotal.subtract(paidAmount).max(BigDecimal.ZERO));
        ledger.setCashbackRatio(config.getCashbackRatio());
        ledger.setGrossCashback(unattributed ? BigDecimal.ZERO
                : paidAmount.multiply(config.getCashbackRatio()).setScale(2, RoundingMode.HALF_UP));
        ledger.setStatus(unattributed ? CourseCashbackLedger.STATUS_UNATTRIBUTED : CourseCashbackLedger.STATUS_ACTIVE);
        ledger.setCreateTime(DateUtils.getNowDate());
        mapper.insertLedger(ledger);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reversePaidCourseCashback(String orderNo) {
        mapper.reverseLedger(orderNo);
    }

    @Override
    public List<CourseCashbackLedger> selectLedgerList(CourseFinanceQueryBo query) {
        return mapper.selectLedgerList(access.resolveCurrentScope(), query == null ? new CourseFinanceQueryBo() : query);
    }

    @Override
    public List<CourseCashbackLedger> selectMyLedgerList() {
        return mapper.selectMyLedgerList(SecurityUtils.getUserId());
    }

    @Override
    public CourseCashbackLedger selectPayment(String orderNo) {
        CourseCashbackLedger ledger = mapper.selectLedgerByOrderNo(orderNo);
        if (ledger == null) {
            throw new ServiceException("支付记录不存在");
        }
        StudentAccessScope scope = access.resolveCurrentScope();
        boolean allowed = scope.isFullAccess()
                || (!CourseCashbackLedger.STATUS_UNATTRIBUTED.equals(ledger.getStatus())
                && (scope.isEmployeeAccess()
                ? SecurityUtils.getUserId().equals(ledger.getEmployeeUserId())
                : scope.getVisibleUserIds() != null && scope.getVisibleUserIds().contains(ledger.getEmployeeUserId())));
        if (!allowed) {
            throw new ServiceException("无权限访问支付记录");
        }
        return ledger;
    }

    @Override
    public List<Map<String, Object>> selectDeductionHistory(String orderNo) {
        selectPayment(orderNo);
        return mapper.selectDeductionHistory(orderNo);
    }

    @Override
    public CourseFinanceSummaryVo selectSummary() {
        return summarize(selectLedgerList(new CourseFinanceQueryBo()));
    }

    @Override
    public CourseFinanceSummaryVo selectMySummary() {
        return summarize(selectMyLedgerList());
    }

    private CourseFinanceSummaryVo summarize(List<CourseCashbackLedger> rows) {
        CourseFinanceSummaryVo summary = new CourseFinanceSummaryVo();
        Set<Long> employeeUserIds = new HashSet<>();
        summary.setPaymentCount(rows.size());
        for (CourseCashbackLedger ledger : rows) {
            BigDecimal paid = amount(ledger.getPaidAmount());
            BigDecimal gross = amount(ledger.getGrossCashback());
            if (CourseCashbackLedger.STATUS_REVERSED.equals(ledger.getStatus())) {
                summary.setRefundedAmount(summary.getRefundedAmount().add(paid));
                summary.setReversedCashbackAmount(summary.getReversedCashbackAmount().add(gross));
            } else {
                summary.setPaidAmount(summary.getPaidAmount().add(paid));
                summary.setCourseTotalAmount(summary.getCourseTotalAmount().add(amount(ledger.getCourseTotal())));
                summary.setOutstandingAmount(summary.getOutstandingAmount().add(amount(ledger.getOutstandingAmount())));
                summary.setGrossCashbackAmount(summary.getGrossCashbackAmount().add(gross));
            }
            if (CourseCashbackLedger.STATUS_UNATTRIBUTED.equals(ledger.getStatus())) {
                summary.setUnattributedPaidAmount(summary.getUnattributedPaidAmount().add(paid));
            }
            if (ledger.getEmployeeUserId() != null) {
                employeeUserIds.add(ledger.getEmployeeUserId());
            }
        }

        BigDecimal available = BigDecimal.ZERO;
        for (Long employeeUserId : employeeUserIds) {
            available = available.add(amount(mapper.selectEmployeeAvailable(employeeUserId)));
        }
        summary.setAvailableCashbackAmount(available);
        summary.setDeductedCashbackAmount(summary.getGrossCashbackAmount()
                .subtract(summary.getReversedCashbackAmount()).subtract(available));
        return summary;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deduct(CourseCashbackDeductionBo deduction) {
        if (deduction == null || deduction.getEmployeeUserId() == null || deduction.getAmount() == null
                || deduction.getAmount().compareTo(BigDecimal.ZERO) <= 0
                || deduction.getAmount().scale() > 2
                || deduction.getReason() == null || deduction.getReason().trim().isEmpty()) {
            throw new ServiceException("扣减信息不完整或金额不合法");
        }

        StudentAccessScope scope = access.resolveCurrentScope();
        if (scope.isEmployeeAccess() || (!scope.isFullAccess()
                && (scope.getVisibleUserIds() == null || !scope.getVisibleUserIds().contains(deduction.getEmployeeUserId())))) {
            throw new ServiceException("无权限扣减员工返现");
        }

        // Serialize every balance-changing deduction on the singleton finance row.
        mapper.lockFinanceConfig();
        BigDecimal available = amount(mapper.selectEmployeeAvailable(deduction.getEmployeeUserId()));
        if (available.compareTo(deduction.getAmount()) < 0) {
            throw new ServiceException("扣减金额超过员工可用返现");
        }
        mapper.insertDeduction(deduction.getEmployeeUserId(), deduction.getAmount(), deduction.getReason().trim(), SecurityUtils.getUserId());
    }

    private BigDecimal amount(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
