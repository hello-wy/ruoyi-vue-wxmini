package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.domain.SignInRecord;
import com.ruoyi.system.domain.vo.JobAttendanceAdminOrderVo;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobAttendanceAdminService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.ISignInRecordService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JobAttendanceAdminServiceImpl implements IJobAttendanceAdminService {

    private static final Integer PAID_STATUS = 1;
    private static final Integer RECORD_TYPE_JOB = 3;
    private static final Integer SIGNED_STATUS = 1;
    private static final Integer AUDIT_STATUS_PENDING = 1;

    @Autowired
    private IJobSignupOrderService jobSignupOrderService;

    @Autowired
    private IDailyJobsService dailyJobsService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ISignInRecordService signInRecordService;

    @Override
    public JobAttendanceAdminOrderVo getOrderAttendance(String orderNo) {
        JobSignupOrder order = requirePaidOrder(orderNo);
        DailyJobs job = requireJob(order.getJobId());
        UserInfo userInfo = requireUserInfo(order.getUserId());
        SignInRecord record = signInRecordService.selectJobSignInRecord(order.getJobId(), userInfo.getId());
        return toOrderVo(order, job, userInfo, record);
    }

    @Override
    public JobAttendanceAdminOrderVo confirmAttendance(String orderNo, String operator) {
        JobSignupOrder order = requirePaidOrder(orderNo);
        UserInfo userInfo = requireUserInfo(order.getUserId());
        SignInRecord existing = signInRecordService.selectJobSignInRecord(order.getJobId(), userInfo.getId());
        if (existing != null && existing.getSignTime() != null && isSameDay(existing.getSignTime(), DateUtils.getNowDate())) {
            throw new ServiceException("当前订单今天已签到");
        }
        DailyJobs job = requireJob(order.getJobId());
        SignInRecord record = new SignInRecord();
        record.setUid(userInfo.getId());
        record.setJobId(order.getJobId());
        record.setRecordType(RECORD_TYPE_JOB);
        record.setSignStatus(SIGNED_STATUS);
        record.setSignTime(DateUtils.getNowDate());
        record.setRemark(operator);
        signInRecordService.insertSignInRecord(record);
        return toOrderVo(order, job, userInfo, record);
    }

    @Override
    public JobAttendanceAdminOrderVo submitSignImage(String orderNo, String signImageUrl) {
        if (StringUtils.isBlank(signImageUrl)) {
            throw new ServiceException("签到图片不能为空");
        }
        JobSignupOrder order = requirePaidOrder(orderNo);
        UserInfo userInfo = requireUserInfo(order.getUserId());
        DailyJobs job = requireJob(order.getJobId());
        SignInRecord record = signInRecordService.selectJobSignInRecord(order.getJobId(), userInfo.getId());
        if (record == null) {
            record = new SignInRecord();
            record.setUid(userInfo.getId());
            record.setJobId(order.getJobId());
            record.setRecordType(RECORD_TYPE_JOB);
            record.setSignStatus(0);
            record.setSignImageUrl(signImageUrl);
            record.setSignImageName(extractFileName(signImageUrl));
            record.setAuditStatus(AUDIT_STATUS_PENDING);
            record.setSubmitTime(DateUtils.getNowDate());
            signInRecordService.insertSignInRecord(record);
            return toOrderVo(order, job, userInfo, record);
        }
        record.setSignImageUrl(signImageUrl);
        record.setSignImageName(extractFileName(signImageUrl));
        record.setAuditStatus(AUDIT_STATUS_PENDING);
        record.setAuditRemark(null);
        record.setAuditTime(null);
        record.setAuditBy(null);
        record.setSubmitTime(DateUtils.getNowDate());
        signInRecordService.updateSignInRecord(record);
        return toOrderVo(order, job, userInfo, record);
    }
    private JobSignupOrder requirePaidOrder(String orderNo) {
        JobSignupOrder order = jobSignupOrderService.selectJobSignupOrderByOrderNo(orderNo);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        if (!PAID_STATUS.equals(order.getStatus())) {
            throw new ServiceException("当前订单未支付，无法签到");
        }
        if (order.getJobId() == null) {
            throw new ServiceException("当前订单未关联兼职岗位");
        }
        return order;
    }

    private DailyJobs requireJob(Long jobId) {
        DailyJobs job = dailyJobsService.selectDailyJobsById(jobId);
        if (job == null) {
            throw new ServiceException("岗位不存在");
        }
        return job;
    }

    private UserInfo requireUserInfo(String userId) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null || userInfo.getId() == null) {
            throw new ServiceException("报名用户不存在");
        }
        return userInfo;
    }

    private JobAttendanceAdminOrderVo toOrderVo(JobSignupOrder order, DailyJobs job, UserInfo userInfo, SignInRecord record) {
        JobAttendanceAdminOrderVo vo = new JobAttendanceAdminOrderVo();
        vo.setOrderNo(order.getOrderNo());
        vo.setJobId(job.getId());
        vo.setJobTitle(job.getTitle());
        vo.setUserInfoId(userInfo.getId());
        vo.setDisplayName(resolveDisplayName(userInfo));
        if (record == null) {
            vo.setAttendanceStatus(0);
            vo.setAttendanceStatusLabel("未签到");
            return vo;
        }
        vo.setAttendanceStatus(record.getSignStatus());
        vo.setAttendanceStatusLabel(resolveAttendanceStatusLabel(record.getSignStatus()));
        vo.setSignImageUrl(record.getSignImageUrl());
        vo.setAuditStatus(record.getAuditStatus());
        vo.setAuditStatusLabel(resolveAuditStatusLabel(record.getAuditStatus()));
        vo.setAuditRemark(record.getAuditRemark());
        vo.setSubmitTime(record.getSubmitTime());
        vo.setAuditTime(record.getAuditTime());
        vo.setSignTime(record.getSignTime());
        return vo;
    }

    private String resolveDisplayName(UserInfo userInfo) {
        if (userInfo.getUserName() != null && !userInfo.getUserName().isEmpty()) {
            return userInfo.getUserName();
        }
        return userInfo.getUserId();
    }

    private String resolveAttendanceStatusLabel(Integer status) {
        if (Integer.valueOf(1).equals(status)) {
            return "已签到";
        }
        if (Integer.valueOf(2).equals(status)) {
            return "迟到";
        }
        if (Integer.valueOf(3).equals(status)) {
            return "已取消";
        }
        return "未签到";
    }

    private String resolveAuditStatusLabel(Integer status) {
        if (Integer.valueOf(1).equals(status)) {
            return "待审核";
        }
        if (Integer.valueOf(2).equals(status)) {
            return "已通过";
        }
        if (Integer.valueOf(3).equals(status)) {
            return "已驳回";
        }
        return "未提交";
    }

    private String extractFileName(String signImageUrl) {
        int index = signImageUrl.lastIndexOf('/');
        return index >= 0 ? signImageUrl.substring(index + 1) : signImageUrl;
    }

    private boolean isSameDay(Date left, Date right) {
        return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, left)
                .equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, right));
    }
}
