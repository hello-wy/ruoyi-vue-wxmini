package com.ruoyi.wxmini.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.domain.vo.JobAttendanceAdminOrderVo;
import com.ruoyi.system.domain.vo.JobScheduleRecordVo;
import com.ruoyi.system.domain.vo.JobSignupUserRecordVo;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobAttendanceAdminService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.vo.WxJobScheduleVo;
import com.ruoyi.wxmini.vo.WxMerchantJobVo;
import com.ruoyi.wxmini.vo.WxSignupUserVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxJobScheduleServiceImplTest {

    @Mock
    private IJobSignupOrderService jobSignupOrderService;
    @Mock
    private IDailyJobsService dailyJobsService;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private IJobAttendanceAdminService jobAttendanceAdminService;

    @InjectMocks
    private WxJobScheduleServiceImpl service;

    @Test
    void should_return_attendance_fields_for_my_schedules() {
        JobScheduleRecordVo record = new JobScheduleRecordVo();
        Date signTime = new Date();
        record.setJobId(10L);
        record.setOrderNo("order-1");
        record.setTitle("日结助教");
        record.setSalaryDay(new BigDecimal("200.00"));
        record.setAttendanceStatus(1);
        record.setAttendanceStatusLabel("已签到");
        record.setSignTime(signTime);
        when(jobSignupOrderService.selectMyPaidJobSchedules("student-1", 1))
                .thenReturn(Collections.singletonList(record));

        List<WxJobScheduleVo> schedules = service.listMySchedules("student-1");

        assertEquals(1, schedules.size());
        assertEquals(1, schedules.get(0).getAttendanceStatus());
        assertEquals("已签到", schedules.get(0).getAttendanceStatusLabel());
        assertEquals(signTime, schedules.get(0).getSignTime());
    }

    @Test
    void should_list_only_current_merchant_published_jobs() {
        UserInfo merchant = merchantUser();
        DailyJobs job = new DailyJobs();
        job.setId(10L);
        job.setTitle("日结助教");
        job.setSalaryDay(new BigDecimal("200.00"));
        when(userInfoService.selectUserInfoByUserId("merchant-1")).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsList(org.mockito.ArgumentMatchers.any(DailyJobs.class)))
                .thenReturn(Collections.singletonList(job));
        when(dailyJobsService.countPaidSignupOrders(10L, 1)).thenReturn(2);

        List<WxMerchantJobVo> jobs = service.listMerchantJobs("merchant-1");

        assertEquals(1, jobs.size());
        assertEquals("日结助教", jobs.get(0).getTitle());
        assertEquals(2, jobs.get(0).getPaidSignupCount());
    }

    @Test
    void should_reject_non_merchant_when_listing_published_jobs() {
        UserInfo user = merchantUser();
        user.setUserType(3);
        when(userInfoService.selectUserInfoByUserId("user-1")).thenReturn(user);

        ServiceException ex = assertThrows(ServiceException.class, () -> service.listMerchantJobs("user-1"));

        assertEquals("仅商家可查看兼职日结查询", ex.getMessage());
    }

    @Test
    void should_return_paid_signup_users_for_owned_job() {
        UserInfo merchant = merchantUser();
        DailyJobs job = new DailyJobs();
        job.setId(10L);
        job.setPublisherUid(100L);
        JobSignupUserRecordVo record = new JobSignupUserRecordVo();
        Date signTime = new Date();
        record.setUserInfoId(200L);
        record.setDisplayName("张三");
        record.setOrderNo("order-1");
        record.setAttendanceStatus(1);
        record.setAttendanceStatusLabel("已签到");
        record.setSignTime(signTime);
        record.setSignedCount(1);
        when(userInfoService.selectUserInfoByUserId("merchant-1")).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsById(10L)).thenReturn(job);
        when(jobSignupOrderService.selectPaidSignupUsersByJobId(10L, 1, null))
                .thenReturn(Collections.singletonList(record));

        List<WxSignupUserVo> users = service.listSignupUsers("merchant-1", 10L, null);

        assertEquals(1, users.size());
        assertEquals("张三", users.get(0).getDisplayName());
        assertEquals("order-1", users.get(0).getOrderNo());
        assertEquals(1, users.get(0).getAttendanceStatus());
        assertEquals("已签到", users.get(0).getAttendanceStatusLabel());
        assertEquals(signTime, users.get(0).getSignTime());
        assertEquals(1, users.get(0).getSignedCount());
    }

    @Test
    void should_reject_signup_users_when_job_not_owned() {
        UserInfo merchant = merchantUser();
        DailyJobs job = new DailyJobs();
        job.setId(10L);
        job.setPublisherUid(999L);
        when(userInfoService.selectUserInfoByUserId("merchant-1")).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsById(10L)).thenReturn(job);

        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.listSignupUsers("merchant-1", 10L, null));

        assertEquals("仅岗位发布商家可查看报名用户", ex.getMessage());
    }

    @Test
    void should_submit_own_order_sign_image() {
        JobSignupOrder order = new JobSignupOrder();
        order.setOrderNo("order-1");
        order.setUserId("student-1");
        JobAttendanceAdminOrderVo detail = new JobAttendanceAdminOrderVo();
        detail.setOrderNo("order-1");
        detail.setSignImageUrl("/profile/job-sign/321/10.jpg");
        when(jobSignupOrderService.selectJobSignupOrderByOrderNo("order-1")).thenReturn(order);
        when(jobAttendanceAdminService.submitSignImage("order-1", "/profile/job-sign/321/10.jpg")).thenReturn(detail);

        JobAttendanceAdminOrderVo result = service.submitSignImage("student-1", "order-1", "/profile/job-sign/321/10.jpg");

        assertEquals("order-1", result.getOrderNo());
        assertEquals("/profile/job-sign/321/10.jpg", result.getSignImageUrl());
    }

    @Test
    void should_reject_other_user_order_sign_image() {
        JobSignupOrder order = new JobSignupOrder();
        order.setOrderNo("order-1");
        order.setUserId("student-2");
        when(jobSignupOrderService.selectJobSignupOrderByOrderNo("order-1")).thenReturn(order);

        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.submitSignImage("student-1", "order-1", "/profile/job-sign/321/10.jpg"));

        assertEquals("只能提交自己的兼职签到图片", ex.getMessage());
    }

    private UserInfo merchantUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(100L);
        userInfo.setUserId("merchant-1");
        userInfo.setUserType(2);
        return userInfo;
    }
}
