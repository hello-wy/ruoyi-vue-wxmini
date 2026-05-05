package com.ruoyi.wxmini.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.vo.JobScheduleRecordVo;
import com.ruoyi.system.domain.vo.JobSignupUserRecordVo;
import com.ruoyi.system.mapper.JobPayrollItemMapper;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IWxJobScheduleService;
import com.ruoyi.wxmini.vo.WxJobScheduleVo;
import com.ruoyi.wxmini.vo.WxMerchantJobVo;
import com.ruoyi.wxmini.vo.WxSignupUserVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class WxJobScheduleServiceImpl implements IWxJobScheduleService {

    private static final Integer PAID_STATUS = 1;
    private static final Integer USER_TYPE_MERCHANT = 2;
    private static final Integer PAYROLL_ITEM_STATUS_CREDITED = 1;
    private static final Long JOB_STATUS_RECRUITING = 0L;
    private static final Long JOB_STATUS_FULL = 1L;
    private static final Long JOB_STATUS_FINISHED = 2L;
    private static final Long JOB_STATUS_CANCELLED = 3L;
    private static final String SETTLEMENT_NONE = "NONE";
    private static final String SETTLEMENT_PENDING = "PENDING";
    private static final String SETTLEMENT_SETTLED = "SETTLED";

    @Resource
    private IJobSignupOrderService jobSignupOrderService;

    @Resource
    private IDailyJobsService dailyJobsService;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private JobPayrollItemMapper jobPayrollItemMapper;

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
        List<WxMerchantJobVo> result = new ArrayList<>();
        for (DailyJobs job : jobs) {
            result.add(toMerchantJobVo(job));
        }
        return result;
    }

    @Override
    public List<WxSignupUserVo> listSignupUsers(String currentUserId, Long jobId, String keyword) {
        UserInfo currentUser = requireMerchant(currentUserId, "仅商家可查看报名用户");
        DailyJobs job = requireOwnedJob(currentUser, jobId, "仅岗位发布商家可查看报名用户");
        return buildSignupUsers(job.getId(), keyword);
    }

    @Override
    public void updateMerchantJobStatus(String currentUserId, Long jobId, Long targetStatus) {
        UserInfo merchant = requireMerchant(currentUserId, "仅商家可修改岗位状态");
        DailyJobs job = requireOwnedJob(merchant, jobId, "仅岗位发布商家可修改岗位状态");
        if (targetStatus == null) {
            throw new ServiceException("岗位状态不能为空");
        }
        boolean canCancel = JOB_STATUS_RECRUITING.equals(job.getStatus()) && JOB_STATUS_CANCELLED.equals(targetStatus);
        boolean canResume = JOB_STATUS_CANCELLED.equals(job.getStatus()) && JOB_STATUS_RECRUITING.equals(targetStatus);
        if (!canCancel && !canResume) {
            throw new ServiceException("当前岗位状态不可修改");
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

    private DailyJobs requireOwnedJob(UserInfo currentUser, Long jobId, String message) {
        DailyJobs job = dailyJobsService.selectDailyJobsById(jobId);
        if (job == null) {
            throw new ServiceException("岗位不存在");
        }
        if (!currentUser.getId().equals(job.getPublisherUid())) {
            throw new ServiceException(message);
        }
        return job;
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
        fillActionFlags(vo, job);
        fillSettlement(vo, job);
        return vo;
    }

    private void fillActionFlags(WxMerchantJobVo vo, DailyJobs job) {
        vo.setCanCancel(JOB_STATUS_RECRUITING.equals(job.getStatus()));
        vo.setCanResumeRecruiting(JOB_STATUS_CANCELLED.equals(job.getStatus()));
    }

    private void fillSettlement(WxMerchantJobVo vo, DailyJobs job) {
        boolean eligible = JOB_STATUS_FULL.equals(job.getStatus()) || JOB_STATUS_CANCELLED.equals(job.getStatus());
        if (!eligible || !isWorkEnded(job.getWorkDate())) {
            vo.setSettlementStatus(SETTLEMENT_NONE);
            vo.setSettlementStatusLabel("不可结账");
            vo.setCanSettle(false);
            return;
        }
        List<WxSignupUserVo> users = buildSignupUsers(job.getId(), null);
        long unpaidCount = users.stream().filter(item -> !Boolean.TRUE.equals(item.getPayrollPaid())).count();
        if (unpaidCount > 0) {
            vo.setSettlementStatus(SETTLEMENT_PENDING);
            vo.setSettlementStatusLabel("待结账");
            vo.setCanSettle(true);
            return;
        }
        vo.setSettlementStatus(SETTLEMENT_SETTLED);
        vo.setSettlementStatusLabel("已结清");
        vo.setCanSettle(false);
    }

    private List<WxSignupUserVo> buildSignupUsers(Long jobId, String keyword) {
        List<JobSignupUserRecordVo> records = jobSignupOrderService.selectPaidSignupUsersByJobId(jobId, PAID_STATUS, keyword);
        Set<Long> creditedIds = new HashSet<>(resolveCreditedEmployeeIds(jobId));
        List<WxSignupUserVo> result = new ArrayList<>();
        for (JobSignupUserRecordVo record : records) {
            WxSignupUserVo vo = new WxSignupUserVo();
            vo.setUserInfoId(record.getUserInfoId());
            vo.setDisplayName(record.getDisplayName());
            vo.setPhoneMasked(record.getPhoneMasked());
            boolean payrollPaid = creditedIds.contains(record.getUserInfoId());
            vo.setPayrollPaid(payrollPaid);
            vo.setPayrollItemStatus(payrollPaid ? PAYROLL_ITEM_STATUS_CREDITED : 0);
            result.add(vo);
        }
        return result;
    }

    private List<Long> resolveCreditedEmployeeIds(Long jobId) {
        List<Long> employeeIds = jobPayrollItemMapper.selectCreditedEmployeeIdsByJobId(jobId);
        return employeeIds == null ? Collections.emptyList() : employeeIds;
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
