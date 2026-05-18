package com.ruoyi.wxmini.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.domain.SignInRecord;
import com.ruoyi.system.domain.vo.JobAttendanceAdminOrderVo;
import com.ruoyi.system.domain.vo.JobScheduleRecordVo;
import com.ruoyi.system.domain.vo.JobSignupUserRecordVo;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobAttendanceAdminService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.ISignInRecordService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IWxJobScheduleService;
import com.ruoyi.wxmini.vo.WxJobScheduleVo;
import com.ruoyi.wxmini.vo.WxMerchantJobVo;
import com.ruoyi.wxmini.vo.WxSignupUserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WxJobScheduleServiceImpl implements IWxJobScheduleService {

    private static final Integer PAID_STATUS = 1;
    private static final Integer USER_TYPE_MERCHANT = 2;
    private static final Integer RECORD_TYPE_JOB = 3;
    private static final Integer SIGN_STATUS_PENDING = 0;
    private static final Integer AUDIT_STATUS_PENDING = 1;

    @Resource
    private IJobSignupOrderService jobSignupOrderService;

    @Resource
    private IDailyJobsService dailyJobsService;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private ISignInRecordService signInRecordService;

    @Resource
    private IJobAttendanceAdminService jobAttendanceAdminService;

    @Override
    public List<WxJobScheduleVo> listMySchedules(String userId) {
        List<JobScheduleRecordVo> records = jobSignupOrderService.selectMyPaidJobSchedules(userId, PAID_STATUS);
        List<WxJobScheduleVo> result = new ArrayList<>();
        for (JobScheduleRecordVo record : records) {
            WxJobScheduleVo vo = new WxJobScheduleVo();
            vo.setJobId(record.getJobId());
            vo.setOrderNo(record.getOrderNo());
            vo.setTitle(record.getTitle());
            vo.setWorkDate(record.getWorkDate());
            vo.setWorkTime(record.getWorkTime());
            vo.setLocation(record.getLocation());
            vo.setSalaryDay(record.getSalaryDay());
            vo.setStatus(record.getStatus());
            vo.setAttendanceStatus(record.getAttendanceStatus());
            vo.setAttendanceStatusLabel(record.getAttendanceStatusLabel());
            vo.setSignTime(record.getSignTime());
            vo.setSignImageUrl(record.getSignImageUrl());
            vo.setAuditStatus(record.getAuditStatus());
            vo.setAuditStatusLabel(record.getAuditStatusLabel());
            vo.setAuditRemark(record.getAuditRemark());
            vo.setCanUploadSignImage(record.getCanUploadSignImage());
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<WxMerchantJobVo> listMerchantJobs(String currentUserId) {
        UserInfo merchant = requireMerchant(currentUserId, "仅商家可查看兼职日结查询");
        DailyJobs query = new DailyJobs();
        query.setPublisherUid(merchant.getId());
        List<DailyJobs> jobs = dailyJobsService.selectDailyJobsList(query);
        jobs = jobs.stream()
                .filter(j -> !Long.valueOf(3L).equals(j.getStatus()))
                .collect(Collectors.toList());
        List<WxMerchantJobVo> result = new ArrayList<>();
        for (DailyJobs job : jobs) {
            result.add(toMerchantJobVo(job));
        }
        return result;
    }

    @Override
    public List<WxSignupUserVo> listSignupUsers(String currentUserId, Long jobId, String keyword) {
        UserInfo currentUser = requireMerchant(currentUserId, "仅商家可查看报名用户");
        DailyJobs job = dailyJobsService.selectDailyJobsById(jobId);
        if (job == null) {
            throw new ServiceException("岗位不存在");
        }
        if (!currentUser.getId().equals(job.getPublisherUid())) {
            throw new ServiceException("仅岗位发布商家可查看报名用户");
        }
        List<JobSignupUserRecordVo> records = jobSignupOrderService.selectPaidSignupUsersByJobId(jobId, PAID_STATUS, keyword);
        List<WxSignupUserVo> result = new ArrayList<>();
        for (JobSignupUserRecordVo record : records) {
            WxSignupUserVo vo = new WxSignupUserVo();
            vo.setUserInfoId(record.getUserInfoId());
            vo.setDisplayName(record.getDisplayName());
            vo.setPhoneMasked(record.getPhoneMasked());
            vo.setOrderNo(record.getOrderNo());
            vo.setAttendanceStatus(record.getAttendanceStatus());
            vo.setAttendanceStatusLabel(record.getAttendanceStatusLabel());
            vo.setSignTime(record.getSignTime());
            vo.setSignedCount(record.getSignedCount());
            vo.setSignImageUrl(record.getSignImageUrl());
            vo.setAuditStatus(record.getAuditStatus());
            vo.setAuditStatusLabel(record.getAuditStatusLabel());
            vo.setAuditRemark(record.getAuditRemark());
            vo.setSubmitTime(record.getSubmitTime());
            result.add(vo);
        }
        return result;
    }

    @Override
    public void submitJobSignImage(String currentUserId, Long jobId, String signImageUrl) {
        if (jobId == null) {
            throw new ServiceException("岗位不能为空");
        }
        if (StringUtils.isBlank(signImageUrl)) {
            throw new ServiceException("签到图片不能为空");
        }
        JobSignupOrder order = jobSignupOrderService.selectLatestPaidOrder(currentUserId, jobId);
        if (order == null) {
            throw new ServiceException("未找到已支付报名订单");
        }
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(currentUserId);
        if (userInfo == null || userInfo.getId() == null) {
            throw new ServiceException("用户不存在");
        }
        SignInRecord record = signInRecordService.selectJobSignInRecord(jobId, userInfo.getId());
        if (record == null) {
            record = new SignInRecord();
            record.setUid(userInfo.getId());
            record.setJobId(jobId);
            record.setRecordType(RECORD_TYPE_JOB);
            record.setSignStatus(SIGN_STATUS_PENDING);
            record.setSignTime(DateUtils.getNowDate());
            record.setSignImageUrl(signImageUrl);
            record.setSignImageName(signImageUrl);
            record.setAuditStatus(AUDIT_STATUS_PENDING);
            record.setSubmitTime(DateUtils.getNowDate());
            signInRecordService.insertSignInRecord(record);
            return;
        }
        record.setSignStatus(SIGN_STATUS_PENDING);
        record.setSignTime(DateUtils.getNowDate());
        record.setSignImageUrl(signImageUrl);
        record.setSignImageName(signImageUrl);
        record.setAuditStatus(AUDIT_STATUS_PENDING);
        record.setSubmitTime(DateUtils.getNowDate());
        signInRecordService.updateJobSignSubmitFields(record);
    }

    @Override
    public JobAttendanceAdminOrderVo submitSignImage(String currentUserId, String orderNo, String signImageUrl) {
        JobSignupOrder order = jobSignupOrderService.selectJobSignupOrderByOrderNo(orderNo);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        if (!currentUserId.equals(order.getUserId())) {
            throw new ServiceException("只能提交自己的兼职签到图片");
        }
        return jobAttendanceAdminService.submitSignImage(orderNo, signImageUrl);
    }

    @Override
    public void updateMerchantJobStatus(String currentUserId, Long jobId, Long targetStatus) {
        UserInfo currentUser = requireMerchant(currentUserId, "仅商家可修改岗位状态");
        DailyJobs job = dailyJobsService.selectDailyJobsById(jobId);
        if (job == null) {
            throw new ServiceException("岗位不存在");
        }
        if (!currentUser.getId().equals(job.getPublisherUid())) {
            throw new ServiceException("仅岗位发布商家可修改岗位状态");
        }
        if (targetStatus == null || targetStatus.equals(job.getStatus())) {
            return;
        }
        job.setStatus(targetStatus);
        dailyJobsService.updateDailyJobs(job);
    }

    private UserInfo requireMerchant(String userId, String message) {
        UserInfo user = userInfoService.selectUserInfoByUserId(userId);
        if (user == null || user.getId() == null) {
            throw new ServiceException("用户不存在");
        }
        if (!USER_TYPE_MERCHANT.equals(user.getUserType())) {
            throw new ServiceException(message);
        }
        return user;
    }

    private WxMerchantJobVo toMerchantJobVo(DailyJobs job) {
        WxMerchantJobVo vo = new WxMerchantJobVo();
        vo.setId(job.getId());
        vo.setTitle(job.getTitle());
        vo.setSalaryDay(job.getSalaryDay());
        vo.setWorkDate(job.getWorkDate());
        vo.setWorkTime(job.getWorkTime());
        vo.setLocation(job.getLocation());
        vo.setStatus(job.getStatus());
        vo.setSignupLimit(job.getSignupLimit());
        vo.setPaidSignupCount(dailyJobsService.countPaidSignupOrders(job.getId(), PAID_STATUS));
        vo.setPayrollReminder(isWorkEnded(job.getWorkDate()));
        return vo;
    }

    private boolean isWorkEnded(Date workDate) {
        if (workDate == null) {
            return false;
        }
        return startOfDay(workDate).compareTo(startOfDay(new Date())) <= 0;
    }

    private Date startOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
