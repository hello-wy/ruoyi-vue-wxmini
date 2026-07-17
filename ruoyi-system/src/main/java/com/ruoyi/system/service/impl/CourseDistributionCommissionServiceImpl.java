package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.CourseDistributionCommissionConfig;
import com.ruoyi.system.domain.CourseDistributionCommissionLedger;
import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.bo.CourseDistributionManualRecordBo;
import com.ruoyi.system.domain.vo.CourseDistributionCommissionSummaryVo;
import com.ruoyi.system.mapper.CourseDistributionCommissionMapper;
import com.ruoyi.system.service.ICourseDistributionCommissionService;
import com.ruoyi.system.service.ICoursePayOrderService;
import com.ruoyi.wxmini.domain.UserReferral;
import com.ruoyi.wxmini.mapper.UserReferralMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
public class CourseDistributionCommissionServiceImpl implements ICourseDistributionCommissionService {
    @Resource private CourseDistributionCommissionMapper mapper;
    @Resource private ICoursePayOrderService orders;
    @Resource private UserReferralMapper referrals;

    @Override
    public CourseDistributionCommissionConfig selectConfig() { return mapper.selectConfig(); }

    @Override
    public void saveConfig(CourseDistributionCommissionConfig config) {
        if (config == null || invalidRatio(config.getLevel1Ratio()) || invalidRatio(config.getLevel2Ratio())
                || config.getLevel1Ratio().add(config.getLevel2Ratio()).compareTo(BigDecimal.ONE) > 0) {
            throw new ServiceException("一级、二级返现比例必须在0到1之间且合计不超过1");
        }
        mapper.saveConfig(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordPaidCourseCommission(String orderNo) {
        mapper.lockConfig();
        CoursePayOrder order = orders.selectCoursePayOrderByOrderNo(orderNo);
        if (order == null || !(CoursePayOrder.STATUS_PAID_WAIT_SIGN.equals(order.getStatus())
                || CoursePayOrder.STATUS_SIGNED.equals(order.getStatus()))) return;
        CourseDistributionCommissionConfig config = mapper.selectConfig();
        if (config == null) return;
        UserReferral direct = referrals.selectReferralByInviteeUserId(order.getUserId());
        if (direct == null) return;
        insertCommission(order, direct.getInviterUserId(), 1, config.getLevel1Ratio());
        UserReferral indirect = referrals.selectReferralByInviteeUserId(direct.getInviterUserId());
        if (indirect != null) insertCommission(order, indirect.getInviterUserId(), 2, config.getLevel2Ratio());
    }

    private void insertCommission(CoursePayOrder order, String beneficiaryUserId, int level, BigDecimal ratio) {
        if (ratio == null || ratio.compareTo(BigDecimal.ZERO) <= 0 || beneficiaryUserId == null || beneficiaryUserId.isEmpty()) return;
        BigDecimal paidAmount = order.getAmount() == null ? BigDecimal.ZERO : order.getAmount();
        BigDecimal amount = paidAmount.multiply(ratio).setScale(2, RoundingMode.HALF_UP);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return;
        CourseDistributionCommissionLedger ledger = new CourseDistributionCommissionLedger();
        ledger.setOrderNo(order.getOrderNo());
        ledger.setCourseId(order.getCourseId());
        ledger.setBuyerUserId(order.getUserId());
        ledger.setBeneficiaryUserId(beneficiaryUserId);
        ledger.setCommissionLevel(level);
        ledger.setPaidAmount(paidAmount);
        ledger.setCommissionRatio(ratio);
        ledger.setCommissionAmount(amount);
        ledger.setStatus(CourseDistributionCommissionLedger.STATUS_ACTIVE);
        ledger.setCreateTime(DateUtils.getNowDate());
        mapper.insertLedger(ledger);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reversePaidCourseCommission(String orderNo) { mapper.reverseLedgers(orderNo); }

    @Override
    public CourseDistributionCommissionSummaryVo selectSummary() { return mapper.selectSummary(); }

    @Override
    public List<Map<String, Object>> selectRecords() { return mapper.selectRecords(); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createManualRecord(CourseDistributionManualRecordBo record) {
        if (record == null || !("CREDIT".equals(record.getDirection()) || "DEBIT".equals(record.getDirection()))
                || record.getAmount() == null || record.getAmount().compareTo(BigDecimal.ZERO) <= 0 || record.getAmount().scale() > 2
                || record.getReason() == null || record.getReason().trim().isEmpty()) {
            throw new ServiceException("手工财务记录信息不完整或金额不合法");
        }
        record.setReason(record.getReason().trim());
        if (record.getOccurredAt() == null) record.setOccurredAt(DateUtils.getNowDate());
        mapper.insertManualRecord(record, SecurityUtils.getUserId());
    }

    private boolean invalidRatio(BigDecimal ratio) { return ratio == null || ratio.compareTo(BigDecimal.ZERO) < 0 || ratio.compareTo(BigDecimal.ONE) > 0; }
}
