package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.TutoringPayrollItem;
import com.ruoyi.system.mapper.TutoringPayrollItemMapper;
import com.ruoyi.system.service.IWalletService;
import com.ruoyi.system.service.ITutoringAdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TutoringSettlementServiceImpl implements ITutoringAdminService {

    static final int BINDING_STATUS_PENDING_ORDER = 0;
    static final int BINDING_STATUS_ORDERED = 1;
    static final int SCHEDULE_STATUS_PENDING = 0;
    static final int SCHEDULE_STATUS_WAIT_PARENT_CONFIRM = 1;
    static final int SCHEDULE_STATUS_WAIT_SETTLEMENT = 2;
    static final int SCHEDULE_STATUS_SETTLED = 3;
    static final int PAYROLL_STATUS_PENDING = 0;
    static final int PAYROLL_STATUS_PAID = 1;

    @Resource
    private TutoringBindingServiceSupport tutoringBindingServiceSupport;
    @Resource
    private TutoringPayrollItemMapper tutoringPayrollItemMapper;
    @Resource
    private IWalletService walletService;

    @Override
    public com.ruoyi.system.domain.TutoringBinding bindTutor(Long parentId, Long tutorId, String operator) {
        return tutoringBindingServiceSupport.bindTutor(parentId, tutorId, operator);
    }

    @Override
    public com.ruoyi.system.domain.TutoringOrder createPendingOrder(Long parentId, Long tutorId, String operator) {
        return tutoringBindingServiceSupport.createPendingOrder(parentId, tutorId, operator);
    }

    @Override
    public List<com.ruoyi.system.domain.TutoringBinding> listBindings(com.ruoyi.system.domain.TutoringBinding query) {
        return tutoringBindingServiceSupport.listBindings(query);
    }

    @Override
    public List<com.ruoyi.system.domain.TutoringSchedule> listSchedules(com.ruoyi.system.domain.TutoringSchedule query) {
        return tutoringBindingServiceSupport.listSchedules(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditSchedule(Long scheduleId, Integer targetStatus, String remark, String operator) {
        tutoringBindingServiceSupport.auditSchedule(scheduleId, targetStatus, remark, operator);
        payAuditedSchedule(scheduleId, operator);
    }

    @Override
    public List<TutoringPayrollItem> listPayrollItems(TutoringPayrollItem query) {
        return tutoringPayrollItemMapper.selectPendingPayrollItems(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchPay(List<Long> itemIds, String operator) {
        if (itemIds == null || itemIds.isEmpty()) {
            throw new ServiceException("请选择待发放结算单");
        }
        List<TutoringPayrollItem> items = tutoringPayrollItemMapper.selectByIdsForUpdate(itemIds);
        if (items == null || items.isEmpty()) {
            throw new ServiceException("结算单不存在");
        }
        for (TutoringPayrollItem item : items) {
            payPayrollItem(item, operator);
        }
    }

    private void payAuditedSchedule(Long scheduleId, String operator) {
        TutoringPayrollItem item = tutoringPayrollItemMapper.selectByScheduleId(scheduleId);
        if (item == null) {
            throw new ServiceException("结算单生成失败");
        }
        payPayrollItem(item, operator);
    }

    private void payPayrollItem(TutoringPayrollItem item, String operator) {
        if (PAYROLL_STATUS_PAID == item.getStatus()) {
            return;
        }
        walletService.creditPayroll(item.getTutorUserId(), item.getNetAmount(), item.getWalletBizId(), "家教课酬入账");
        item.setStatus(PAYROLL_STATUS_PAID);
        item.setPaidBy(operator);
        item.setPaidTime(DateUtils.getNowDate());
        item.setUpdateBy(operator);
        item.setUpdateTime(DateUtils.getNowDate());
        tutoringPayrollItemMapper.updateTutoringPayrollItem(item);
        tutoringBindingServiceSupport.markScheduleSettled(item.getScheduleId(), operator);
    }
}
