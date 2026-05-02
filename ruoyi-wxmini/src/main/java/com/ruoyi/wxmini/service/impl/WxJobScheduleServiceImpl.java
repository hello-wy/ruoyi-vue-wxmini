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
import com.ruoyi.wxmini.vo.WxSignupUserVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class WxJobScheduleServiceImpl implements IWxJobScheduleService {

    private static final Integer PAID_STATUS = 1;

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
    public List<WxSignupUserVo> listSignupUsers(String currentUserId, Long jobId, String keyword) {
        UserInfo currentUser = userInfoService.selectUserInfoByUserId(currentUserId);
        if (currentUser == null || currentUser.getId() == null) {
            throw new ServiceException("用户不存在");
        }
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
}
