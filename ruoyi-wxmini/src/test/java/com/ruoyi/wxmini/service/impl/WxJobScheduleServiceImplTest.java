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
import com.ruoyi.system.service.ISignInRecordService;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private ISignInRecordService signInRecordService;
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
        record.setSignImageUrl("/profile/job-sign/321/10.jpg");
        record.setAuditStatus(1);
        record.setAuditStatusLabel("待审核");
        record.setCanUploadSignImage(false);
        when(jobSignupOrderService.selectMyPaidJobSchedules("student-1", 1))
                .thenReturn(Collections.singletonList(record));

        List<WxJobScheduleVo> schedules = service.listMySchedules("student-1");

        assertEquals(1, schedules.size());
        assertEquals(1, schedules.get(0).getAttendanceStatus());
        assertEquals("已签到", schedules.get(0).getAttendanceStatusLabel());
        assertEquals(signTime, schedules.get(0).getSignTime());
        assertEquals("/profile/job-sign/321/10.jpg", schedules.get(0).getSignImageUrl());
        assertEquals(1, schedules.get(0).getAuditStatus());
        assertEquals("待审核", schedules.get(0).getAuditStatusLabel());
        assertEquals(false, schedules.get(0).getCanUploadSignImage());
    }

    @Test
    void should_return_new_order_schedule_without_old_sign_result() {
        JobScheduleRecordVo record = new JobScheduleRecordVo();
        record.setJobId(10L);
        record.setOrderNo("order-new");
        record.setTitle("日结助教");
        record.setSalaryDay(new BigDecimal("200.00"));
        record.setAttendanceStatus(0);
        record.setAttendanceStatusLabel("未签到");
        record.setAuditStatus(0);
        record.setAuditStatusLabel("未提交");
        record.setCanUploadSignImage(true);
        when(jobSignupOrderService.selectMyPaidJobSchedules("student-1", 1))
                .thenReturn(Collections.singletonList(record));

        List<WxJobScheduleVo> schedules = service.listMySchedules("student-1");

        assertEquals(1, schedules.size());
        assertEquals("order-new", schedules.get(0).getOrderNo());
        assertEquals(0, schedules.get(0).getAttendanceStatus());
        assertEquals("未签到", schedules.get(0).getAttendanceStatusLabel());
        assertEquals(0, schedules.get(0).getAuditStatus());
        assertEquals("未提交", schedules.get(0).getAuditStatusLabel());
        assertNull(schedules.get(0).getSignImageUrl());
        assertNull(schedules.get(0).getSignTime());
        assertTrue(schedules.get(0).getCanUploadSignImage());
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
        Date submitTime = new Date();
        record.setUserInfoId(200L);
        record.setDisplayName("张三");
        record.setOrderNo("order-1");
        record.setAttendanceStatus(1);
        record.setAttendanceStatusLabel("已签到");
        record.setSignTime(signTime);
        record.setSignedCount(1);
        record.setSignImageUrl("/profile/job-sign/321/10.jpg");
        record.setAuditStatus(2);
        record.setAuditStatusLabel("已通过");
        record.setSubmitTime(submitTime);
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
        assertEquals("/profile/job-sign/321/10.jpg", users.get(0).getSignImageUrl());
        assertEquals(2, users.get(0).getAuditStatus());
        assertEquals("已通过", users.get(0).getAuditStatusLabel());
        assertEquals(submitTime, users.get(0).getSubmitTime());
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

    /**
     * Task 3.3 · A3 fix: when the SQL left-joins user_info and the matching user_info
     * row is missing, the mapper returns a record with fallback fields
     * ({@code userInfoId=0}, {@code displayName='未注册用户'}, {@code phoneMasked=''}).
     * The service must propagate those fallbacks into {@link WxSignupUserVo} unchanged
     * so paid orders without a user_info row still show up on the merchant panel.
     *
     * <p>Validates: P1 (Bug Condition · 列表非空 · A3 fallback path); Requirements: 2.1, 3.7.
     */
    @Test
    void selectPaidSignupUsersByJobId_userInfoMissing_returnsOrderWithFallback() {
        UserInfo merchant = merchantUser();
        DailyJobs job = new DailyJobs();
        job.setId(10L);
        job.setPublisherUid(100L);

        // Simulates the post-fix SQL output for an order whose user_info row is missing:
        // coalesce(ui.id, 0) -> userInfoId = 0
        // case when ui.user_name is not null ... else '未注册用户' -> displayName = '未注册用户'
        // case when ui.phone is null or ui.phone = '' then '' ... -> phoneMasked = ''
        // jso.user_id -> userId = legacy user id (non-null, taken from order)
        JobSignupUserRecordVo orphanRecord = new JobSignupUserRecordVo();
        orphanRecord.setUserInfoId(0L);
        orphanRecord.setUserId("u-legacy-1");
        orphanRecord.setDisplayName("未注册用户");
        orphanRecord.setPhoneMasked("");
        orphanRecord.setOrderNo("order-legacy");
        orphanRecord.setAttendanceStatus(0);
        orphanRecord.setAttendanceStatusLabel("未签到");
        orphanRecord.setSignedCount(0);
        orphanRecord.setAuditStatus(0);
        orphanRecord.setAuditStatusLabel("未提交");

        when(userInfoService.selectUserInfoByUserId("merchant-1")).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsById(10L)).thenReturn(job);
        when(jobSignupOrderService.selectPaidSignupUsersByJobId(10L, 1, null))
                .thenReturn(Collections.singletonList(orphanRecord));

        List<WxSignupUserVo> users = service.listSignupUsers("merchant-1", 10L, null);

        assertEquals(1, users.size(),
                "paid order must remain visible even when its user_info row is missing");
        WxSignupUserVo vo = users.get(0);
        assertEquals(0L, vo.getUserInfoId());
        assertEquals("未注册用户", vo.getDisplayName());
        assertEquals("", vo.getPhoneMasked());
        assertEquals("order-legacy", vo.getOrderNo());
        assertEquals(0, vo.getAttendanceStatus());
        assertEquals("未签到", vo.getAttendanceStatusLabel());
        assertEquals(0, vo.getAuditStatus());
        assertEquals("未提交", vo.getAuditStatusLabel());
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

    /**
     * Task 5.3: listMerchantJobs filters out jobs with status=3 (offshelf).
     * Input a list of DailyJobs with mixed statuses (0,1,2,3), assert that
     * the result does NOT contain any job with status=3.
     *
     * <p>Validates: P3 (Bug Condition · 下架入口与下架后列表过滤); Requirements: 2.3, 3.3.
     */
    @Test
    void listMerchantJobs_filtersStatus3() {
        UserInfo merchant = merchantUser();
        when(userInfoService.selectUserInfoByUserId("merchant-1")).thenReturn(merchant);

        List<DailyJobs> mixedJobs = new ArrayList<>();
        DailyJobs job0 = new DailyJobs();
        job0.setId(1L);
        job0.setTitle("招聘中岗位");
        job0.setStatus(0L);
        job0.setSalaryDay(new BigDecimal("100.00"));
        mixedJobs.add(job0);

        DailyJobs job1 = new DailyJobs();
        job1.setId(2L);
        job1.setTitle("已满员岗位");
        job1.setStatus(1L);
        job1.setSalaryDay(new BigDecimal("150.00"));
        mixedJobs.add(job1);

        DailyJobs job2 = new DailyJobs();
        job2.setId(3L);
        job2.setTitle("已结束岗位");
        job2.setStatus(2L);
        job2.setSalaryDay(new BigDecimal("200.00"));
        mixedJobs.add(job2);

        DailyJobs job3 = new DailyJobs();
        job3.setId(4L);
        job3.setTitle("已下架岗位");
        job3.setStatus(3L);
        job3.setSalaryDay(new BigDecimal("250.00"));
        mixedJobs.add(job3);

        when(dailyJobsService.selectDailyJobsList(org.mockito.ArgumentMatchers.any(DailyJobs.class)))
                .thenReturn(mixedJobs);
        when(dailyJobsService.countPaidSignupOrders(org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.eq(1))).thenReturn(0);

        List<WxMerchantJobVo> result = service.listMerchantJobs("merchant-1");

        assertEquals(3, result.size(), "status=3 job should be filtered out");
        for (WxMerchantJobVo vo : result) {
            assertFalse(Long.valueOf(3L).equals(vo.getStatus()),
                    "No job with status=3 should appear in the result");
        }
        // Verify the remaining jobs are the ones with status 0, 1, 2
        assertTrue(result.stream().anyMatch(v -> v.getId().equals(1L)));
        assertTrue(result.stream().anyMatch(v -> v.getId().equals(2L)));
        assertTrue(result.stream().anyMatch(v -> v.getId().equals(3L)));
    }

    /**
     * Task 5.3: listMerchantJobs keeps all jobs with status ∈ {0,1,2}.
     * Input only jobs with status 0, 1, 2 and assert all are returned.
     *
     * <p>Validates: P5 (Preservation · status=0/1/2 岗位继续返回); Requirements: 3.3, 3.4.
     */
    @Test
    void listMerchantJobs_keepsStatus0_1_2() {
        UserInfo merchant = merchantUser();
        when(userInfoService.selectUserInfoByUserId("merchant-1")).thenReturn(merchant);

        List<DailyJobs> jobs = new ArrayList<>();
        DailyJobs job0 = new DailyJobs();
        job0.setId(10L);
        job0.setTitle("岗位A");
        job0.setStatus(0L);
        job0.setSalaryDay(new BigDecimal("100.00"));
        jobs.add(job0);

        DailyJobs job1 = new DailyJobs();
        job1.setId(11L);
        job1.setTitle("岗位B");
        job1.setStatus(1L);
        job1.setSalaryDay(new BigDecimal("150.00"));
        jobs.add(job1);

        DailyJobs job2 = new DailyJobs();
        job2.setId(12L);
        job2.setTitle("岗位C");
        job2.setStatus(2L);
        job2.setSalaryDay(new BigDecimal("200.00"));
        jobs.add(job2);

        when(dailyJobsService.selectDailyJobsList(org.mockito.ArgumentMatchers.any(DailyJobs.class)))
                .thenReturn(jobs);
        when(dailyJobsService.countPaidSignupOrders(org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.eq(1))).thenReturn(1);

        List<WxMerchantJobVo> result = service.listMerchantJobs("merchant-1");

        assertEquals(3, result.size(), "All jobs with status 0, 1, 2 should be returned");
        assertTrue(result.stream().anyMatch(v -> v.getId().equals(10L) && v.getStatus().equals(0L)));
        assertTrue(result.stream().anyMatch(v -> v.getId().equals(11L) && v.getStatus().equals(1L)));
        assertTrue(result.stream().anyMatch(v -> v.getId().equals(12L) && v.getStatus().equals(2L)));
    }

    private UserInfo merchantUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(100L);
        userInfo.setUserId("merchant-1");
        userInfo.setUserType(2);
        return userInfo;
    }
}
