package com.ruoyi.wxmini.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.vo.JobScheduleRecordVo;
import com.ruoyi.system.domain.vo.JobSignupUserRecordVo;
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
import java.util.Date;
import java.util.List;

@Service
public class WxJobScheduleServiceImpl implements IWxJobScheduleService {

    private static final Integer PAID_STATUS = 1;
    private static final Integer USER_TYPE_MERCHANT = 2;

    @Resource
    private IJobSignupOrderService jobSignupOrderService;

    @Resource
    private IDailyJobsService dailyJobsService;

    @Resource
    private IUserInfoService userInfoService;

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
            result.add(vo);
        }
        return result;
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
