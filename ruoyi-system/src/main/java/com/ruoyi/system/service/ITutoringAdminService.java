package com.ruoyi.system.service;

import com.ruoyi.system.domain.TutoringBinding;
import com.ruoyi.system.domain.TutoringOrder;
import com.ruoyi.system.domain.TutoringPayrollItem;
import com.ruoyi.system.domain.TutoringSchedule;

import java.util.List;

public interface ITutoringAdminService {
    TutoringBinding bindTutor(Long parentId, Long tutorId, String operator);

    TutoringOrder createPendingOrder(Long parentId, Long tutorId, String operator);

    List<TutoringBinding> listBindings(TutoringBinding query);

    List<TutoringSchedule> listSchedules(TutoringSchedule query);

    void auditSchedule(Long scheduleId, Integer targetStatus, String remark, String operator);

    List<TutoringPayrollItem> listPayrollItems(TutoringPayrollItem query);

    void batchPay(List<Long> itemIds, String operator);
}
