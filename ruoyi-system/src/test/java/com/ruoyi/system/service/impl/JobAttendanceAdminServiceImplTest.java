package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.domain.SignInRecord;
import com.ruoyi.system.domain.vo.JobAttendanceAdminOrderVo;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.ISignInRecordService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobAttendanceAdminServiceImplTest {

    @Mock
    private IJobSignupOrderService jobSignupOrderService;
    @Mock
    private IDailyJobsService dailyJobsService;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private ISignInRecordService signInRecordService;

    @InjectMocks
    private JobAttendanceAdminServiceImpl service;

    @Test
    void should_query_order_attendance_detail() {
        JobSignupOrder order = new JobSignupOrder();
        order.setId(1001L);
        order.setOrderNo("order-1");
        order.setUserId("student-1");
        order.setJobId(10L);
        order.setStatus(1);
        DailyJobs job = new DailyJobs();
        job.setId(10L);
        job.setTitle("日结助教");
        UserInfo userInfo = new UserInfo();
        userInfo.setId(200L);
        userInfo.setUserId("student-1");
        userInfo.setUserName("张三");
        SignInRecord record = new SignInRecord();
        Date signTime = new Date();
        record.setSignStatus(1);
        record.setSignTime(signTime);
        when(jobSignupOrderService.selectJobSignupOrderByOrderNo("order-1")).thenReturn(order);
        when(dailyJobsService.selectDailyJobsById(10L)).thenReturn(job);
        when(userInfoService.selectUserInfoByUserId("student-1")).thenReturn(userInfo);
        when(signInRecordService.selectJobSignInRecordByOrderId(1001L)).thenReturn(record);

        JobAttendanceAdminOrderVo detail = service.getOrderAttendance("order-1");

        assertEquals("order-1", detail.getOrderNo());
        assertEquals("日结助教", detail.getJobTitle());
        assertEquals("张三", detail.getDisplayName());
        assertEquals(Integer.valueOf(1), detail.getAttendanceStatus());
        assertEquals("已签到", detail.getAttendanceStatusLabel());
        assertEquals(signTime, detail.getSignTime());
    }

    @Test
    void should_reject_sign_in_when_already_signed_today() {
        JobSignupOrder order = new JobSignupOrder();
        order.setId(1001L);
        order.setOrderNo("order-1");
        order.setUserId("student-1");
        order.setJobId(10L);
        order.setStatus(1);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(200L);
        userInfo.setUserId("student-1");
        SignInRecord todayRecord = new SignInRecord();
        todayRecord.setSignTime(new Date());
        when(jobSignupOrderService.selectJobSignupOrderByOrderNo("order-1")).thenReturn(order);
        when(userInfoService.selectUserInfoByUserId("student-1")).thenReturn(userInfo);
        when(signInRecordService.selectJobSignInRecordByOrderId(1001L)).thenReturn(todayRecord);

        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.confirmAttendance("order-1", "admin-user"));

        assertEquals("当前订单今天已签到", ex.getMessage());
    }

    @Test
    void should_submit_job_sign_image_as_pending_audit_record() {
        JobSignupOrder order = new JobSignupOrder();
        order.setId(1001L);
        order.setOrderNo("order-1");
        order.setUserId("student-1");
        order.setJobId(10L);
        order.setStatus(1);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(200L);
        userInfo.setUserId("student-1");
        DailyJobs job = new DailyJobs();
        job.setId(10L);
        job.setTitle("日结助教");
        when(jobSignupOrderService.selectJobSignupOrderByOrderNo("order-1")).thenReturn(order);
        when(userInfoService.selectUserInfoByUserId("student-1")).thenReturn(userInfo);
        when(dailyJobsService.selectDailyJobsById(10L)).thenReturn(job);
        when(signInRecordService.selectJobSignInRecordByOrderId(1001L)).thenReturn(null);
        when(signInRecordService.insertSignInRecord(any(SignInRecord.class))).thenReturn(1);

        JobAttendanceAdminOrderVo detail = service.submitSignImage("order-1", "/profile/job-sign/321/10.jpg");

        assertEquals("order-1", detail.getOrderNo());
        assertEquals(Integer.valueOf(1), detail.getAuditStatus());
        assertEquals("待审核", detail.getAuditStatusLabel());
        assertEquals("/profile/job-sign/321/10.jpg", detail.getSignImageUrl());
        ArgumentCaptor<SignInRecord> captor = ArgumentCaptor.forClass(SignInRecord.class);
        verify(signInRecordService).insertSignInRecord(captor.capture());
        SignInRecord inserted = captor.getValue();
        assertEquals(Long.valueOf(200L), inserted.getUid());
        assertEquals(Long.valueOf(10L), inserted.getJobId());
        assertEquals(Long.valueOf(1001L), inserted.getJobOrderId());
        assertEquals(Integer.valueOf(3), inserted.getRecordType());
        assertEquals("/profile/job-sign/321/10.jpg", inserted.getSignImageUrl());
        assertEquals(Integer.valueOf(1), inserted.getAuditStatus());
        assertNotNull(inserted.getSubmitTime());
    }

    @Test
    void should_insert_sign_record_bound_to_current_order() {
        JobSignupOrder order = new JobSignupOrder();
        order.setId(2002L);
        order.setOrderNo("order-2");
        order.setUserId("student-1");
        order.setJobId(10L);
        order.setStatus(1);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(200L);
        userInfo.setUserId("student-1");
        DailyJobs job = new DailyJobs();
        job.setId(10L);
        job.setTitle("日结助教");
        when(jobSignupOrderService.selectJobSignupOrderByOrderNo("order-2")).thenReturn(order);
        when(userInfoService.selectUserInfoByUserId("student-1")).thenReturn(userInfo);
        when(dailyJobsService.selectDailyJobsById(10L)).thenReturn(job);
        when(signInRecordService.selectJobSignInRecordByOrderId(2002L)).thenReturn(null);
        when(signInRecordService.insertSignInRecord(any(SignInRecord.class))).thenReturn(1);

        JobAttendanceAdminOrderVo detail = service.submitSignImage("order-2", "/profile/job-sign/321/20.jpg");

        assertEquals("order-2", detail.getOrderNo());
        ArgumentCaptor<SignInRecord> captor = ArgumentCaptor.forClass(SignInRecord.class);
        verify(signInRecordService).insertSignInRecord(captor.capture());
        SignInRecord inserted = captor.getValue();
        assertEquals(Long.valueOf(2002L), inserted.getJobOrderId());
        assertEquals(Long.valueOf(10L), inserted.getJobId());
    }

    @Test
    void should_insert_sign_in_record_when_confirming_attendance() {
        JobSignupOrder order = new JobSignupOrder();
        order.setId(1001L);
        order.setOrderNo("order-1");
        order.setUserId("student-1");
        order.setJobId(10L);
        order.setStatus(1);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(200L);
        userInfo.setUserId("student-1");
        DailyJobs job = new DailyJobs();
        job.setId(10L);
        job.setTitle("日结助教");
        when(jobSignupOrderService.selectJobSignupOrderByOrderNo("order-1")).thenReturn(order);
        when(userInfoService.selectUserInfoByUserId("student-1")).thenReturn(userInfo);
        when(dailyJobsService.selectDailyJobsById(10L)).thenReturn(job);
        when(signInRecordService.selectJobSignInRecordByOrderId(1001L)).thenReturn(null);
        when(signInRecordService.insertSignInRecord(any(SignInRecord.class))).thenReturn(1);

        JobAttendanceAdminOrderVo detail = service.confirmAttendance("order-1", "admin-user");

        assertEquals("order-1", detail.getOrderNo());
        assertEquals(Integer.valueOf(1), detail.getAttendanceStatus());
        assertEquals("已签到", detail.getAttendanceStatusLabel());
        assertNotNull(detail.getSignTime());
        ArgumentCaptor<SignInRecord> captor = ArgumentCaptor.forClass(SignInRecord.class);
        verify(signInRecordService).insertSignInRecord(captor.capture());
        SignInRecord inserted = captor.getValue();
        assertEquals(Long.valueOf(200L), inserted.getUid());
        assertEquals(Long.valueOf(10L), inserted.getJobId());
        assertEquals(Long.valueOf(1001L), inserted.getJobOrderId());
        assertEquals(Integer.valueOf(3), inserted.getRecordType());
        assertEquals(Integer.valueOf(1), inserted.getSignStatus());
    }
}
