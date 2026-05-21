package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.domain.TutoringBinding;
import com.ruoyi.system.domain.TutoringOrder;
import com.ruoyi.system.domain.TutoringPayrollItem;
import com.ruoyi.system.domain.TutoringSchedule;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.mapper.ParentsMapper;
import com.ruoyi.system.mapper.TutoringBindingMapper;
import com.ruoyi.system.mapper.TutoringPayrollItemMapper;
import com.ruoyi.system.mapper.TutoringScheduleMapper;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TutoringBindingServiceSupport {

    static final int BINDING_STATUS_PENDING_ORDER = 0;
    static final int BINDING_STATUS_ORDERED = 1;
    static final int BINDING_STATUS_CLOSED = 2;
    static final int SCHEDULE_STATUS_PENDING = 0;
    static final int SCHEDULE_STATUS_WAIT_PARENT_CONFIRM = 1;
    static final int SCHEDULE_STATUS_WAIT_SETTLEMENT = 2;
    static final int SCHEDULE_STATUS_SETTLED = 3;
    static final int PAYROLL_STATUS_PENDING = 0;

    @Resource
    private ParentsMapper parentsMapper;
    @Resource
    private com.ruoyi.system.mapper.TutorsMapper tutorsMapper;
    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private TutoringBindingMapper tutoringBindingMapper;
    @Resource
    private TutoringScheduleMapper tutoringScheduleMapper;
    @Resource
    private TutoringPayrollItemMapper tutoringPayrollItemMapper;
    @Resource
    private ISysConfigService sysConfigService;

    @Transactional(rollbackFor = Exception.class)
    public TutoringBinding bindTutor(Long parentId, Long tutorId, String operator) {
        if (parentId == null || tutorId == null) {
            throw new ServiceException("需求和教员不能为空");
        }
        Parents parent = parentsMapper.selectParentsById(parentId);
        if (parent == null) {
            throw new ServiceException("家长需求不存在");
        }
        Tutors tutor = tutorsMapper.selectTutorsById(tutorId);
        if (tutor == null || tutor.getStatus() == null || tutor.getStatus() != 1L) {
            throw new ServiceException("教员不存在或未通过审核");
        }
        if (parent.getSystemUid() == null) {
            throw new ServiceException("家长需求未关联平台用户");
        }
        UserInfo tutorUser = userInfoService.selectUserInfoByUserId(tutor.getUid());
        if (tutorUser == null || tutorUser.getId() == null) {
            throw new ServiceException("教员用户不存在");
        }
        TutoringBinding existing = tutoringBindingMapper.selectByParentAndTutor(parentId, tutorId);
        if (existing != null && existing.getStatus() != null && existing.getStatus() != BINDING_STATUS_CLOSED) {
            return existing;
        }
        TutoringBinding binding = new TutoringBinding();
        binding.setId(SnowflakeIdWorker.nextIdDefault());
        binding.setParentId(parentId);
        binding.setParentUserId(parent.getSystemUid());
        binding.setTutorId(tutorId);
        binding.setTutorUserId(tutorUser.getId());
        binding.setStatus(BINDING_STATUS_PENDING_ORDER);
        binding.setServiceTimesSnapshot(parent.getServiceTimes());
        binding.setCreateBy(operator);
        binding.setCreateTime(DateUtils.getNowDate());
        binding.setUpdateBy(operator);
        binding.setUpdateTime(DateUtils.getNowDate());
        tutoringBindingMapper.insertTutoringBinding(binding);
        return tutoringBindingMapper.selectById(binding.getId());
    }

    public List<TutoringBinding> listBindings(TutoringBinding query) {
        return tutoringBindingMapper.selectBindingsForAdmin(query);
    }

    public List<TutoringSchedule> listSchedules(TutoringSchedule query) {
        return tutoringScheduleMapper.selectAdminSchedules(query);
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditSchedule(Long scheduleId, Integer targetStatus, String remark, String operator) {
        TutoringSchedule schedule = tutoringScheduleMapper.selectByIdForUpdate(scheduleId);
        if (schedule == null) {
            throw new ServiceException("课表不存在");
        }
        if (targetStatus == null || targetStatus != SCHEDULE_STATUS_WAIT_SETTLEMENT) {
            throw new ServiceException("仅支持审核为待结算");
        }
        if (schedule.getStatus() == null || schedule.getStatus() != SCHEDULE_STATUS_WAIT_PARENT_CONFIRM) {
            throw new ServiceException("当前课表状态不可审核");
        }
        if (schedule.getConfirmTime() == null) {
            throw new ServiceException("家长尚未确认完课");
        }
        schedule.setStatus(SCHEDULE_STATUS_WAIT_SETTLEMENT);
        schedule.setUpdateBy(operator);
        schedule.setUpdateTime(DateUtils.getNowDate());
        tutoringScheduleMapper.updateTutoringSchedule(schedule);

        TutoringPayrollItem existing = tutoringPayrollItemMapper.selectByScheduleId(schedule.getId());
        if (existing != null) {
            return;
        }
        TutoringPayrollItem item = new TutoringPayrollItem();
        item.setId(SnowflakeIdWorker.nextIdDefault());
        item.setPayrollNo("TPAY-" + schedule.getId());
        item.setScheduleId(schedule.getId());
        item.setOrderId(schedule.getOrderId());
        item.setOrderNo(schedule.getOrderNo());
        item.setBindingId(schedule.getBindingId());
        item.setParentId(schedule.getParentId());
        item.setParentUserId(schedule.getParentUserId());
        item.setTutorId(schedule.getTutorId());
        item.setTutorUserId(schedule.getTutorUserId());
        item.setGrossAmount(schedule.getGrossAmount());
        item.setCommissionAmount(schedule.getCommissionAmount());
        item.setNetAmount(schedule.getNetAmount());
        item.setCommissionRate(resolveCommissionRate(schedule));
        item.setStatus(PAYROLL_STATUS_PENDING);
        item.setWalletBizId("TUTORING_PAYROLL:" + schedule.getId());
        item.setRemark(StringUtils.defaultString(remark));
        item.setCreateBy(operator);
        item.setCreateTime(DateUtils.getNowDate());
        item.setUpdateBy(operator);
        item.setUpdateTime(DateUtils.getNowDate());
        tutoringPayrollItemMapper.insertTutoringPayrollItem(item);
    }

    @Transactional(rollbackFor = Exception.class)
    public void markScheduleSettled(Long scheduleId, String operator) {
        TutoringSchedule schedule = tutoringScheduleMapper.selectByIdForUpdate(scheduleId);
        if (schedule == null) {
            throw new ServiceException("课表不存在");
        }
        schedule.setStatus(SCHEDULE_STATUS_SETTLED);
        schedule.setUpdateBy(operator);
        schedule.setUpdateTime(DateUtils.getNowDate());
        tutoringScheduleMapper.updateTutoringSchedule(schedule);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createSchedulesForPaidOrder(TutoringOrder order, String operator) {
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        if (tutoringScheduleMapper.countByOrderId(order.getId()) > 0) {
            return;
        }
        JSONArray array = JSON.parseArray(StringUtils.defaultString(order.getServiceTimesSnapshot()));
        if (array == null || array.isEmpty()) {
            throw new ServiceException("订单缺少服务时段");
        }
        List<TutoringSchedule> items = new ArrayList<>();
        BigDecimal lessonAmount = order.getLessonCount() == null || order.getLessonCount() <= 0
                ? order.getTotalAmount()
                : order.getTotalAmount().divide(new BigDecimal(order.getLessonCount()), 2, RoundingMode.HALF_UP);
        for (int i = 0; i < array.size(); i++) {
            JSONObject slot = array.getJSONObject(i);
            LocalDate serviceDate = LocalDate.parse(slot.getString("serviceDate"));
            LocalTime startTime = LocalTime.parse(slot.getString("startTime"));
            LocalTime endTime = LocalTime.parse(slot.getString("endTime"));
            BigDecimal hours = BigDecimal.valueOf(Duration.between(startTime, endTime).toMinutes())
                    .divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP);
            BigDecimal gross = lessonAmount;
            BigDecimal commissionAmount = gross.multiply(order.getCommissionRate())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal netAmount = gross.subtract(commissionAmount);
            TutoringSchedule schedule = new TutoringSchedule();
            schedule.setId(SnowflakeIdWorker.nextIdDefault());
            schedule.setOrderId(order.getId());
            schedule.setOrderNo(order.getOrderNo());
            schedule.setBindingId(order.getBindingId());
            schedule.setParentId(order.getParentId());
            schedule.setParentUserId(order.getParentUserId());
            schedule.setTutorId(order.getTutorId());
            schedule.setTutorUserId(order.getTutorUserId());
            schedule.setServiceDate(Date.from(serviceDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            schedule.setStartTime(startTime.toString());
            schedule.setEndTime(endTime.toString());
            schedule.setHours(hours);
            schedule.setGrossAmount(gross);
            schedule.setCommissionAmount(commissionAmount);
            schedule.setNetAmount(netAmount);
            schedule.setStatus(SCHEDULE_STATUS_PENDING);
            schedule.setCreateBy(operator);
            schedule.setCreateTime(DateUtils.getNowDate());
            schedule.setUpdateBy(operator);
            schedule.setUpdateTime(DateUtils.getNowDate());
            items.add(schedule);
        }
        tutoringScheduleMapper.batchInsertTutoringSchedules(items);
    }

    public BigDecimal defaultCommissionRate() {
        String value = sysConfigService.selectConfigByKey("wxmini.tutoring.defaultCommissionRate");
        if (StringUtils.isBlank(value)) {
            return new BigDecimal("10.00");
        }
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal resolveCommissionRate(TutoringSchedule schedule) {
        if (schedule.getGrossAmount() == null || schedule.getGrossAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return defaultCommissionRate();
        }
        return schedule.getCommissionAmount().multiply(new BigDecimal("100"))
                .divide(schedule.getGrossAmount(), 2, RoundingMode.HALF_UP);
    }
}
