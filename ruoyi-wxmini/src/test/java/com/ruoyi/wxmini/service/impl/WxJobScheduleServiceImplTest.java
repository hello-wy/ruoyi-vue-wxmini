package com.ruoyi.wxmini.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.vo.JobSignupUserRecordVo;
import com.ruoyi.system.mapper.JobPayrollItemMapper;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.vo.WxMerchantJobVo;
import com.ruoyi.wxmini.vo.WxSignupUserVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
    private JobPayrollItemMapper jobPayrollItemMapper;

    @InjectMocks
    private WxJobScheduleServiceImpl service;

    @Test
    void should_list_only_current_merchant_published_jobs() {
        UserInfo merchant = merchantUser();
        DailyJobs job = new DailyJobs();
        job.setId(10L);
        job.setTitle("日结助教");
        job.setSalaryDay(new BigDecimal("200.00"));
        when(userInfoService.selectUserInfoByUserId("merchant-1")).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsList(any(DailyJobs.class)))
                .thenReturn(Collections.singletonList(job));
        when(dailyJobsService.countPaidSignupOrders(10L, 1)).thenReturn(2);
        when(jobSignupOrderService.selectPaidSignupUsersByJobId(10L, 1, null)).thenReturn(Collections.emptyList());
        when(jobPayrollItemMapper.selectCreditedEmployeeIdsByJobId(10L)).thenReturn(Collections.emptyList());

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
        record.setUserInfoId(200L);
        record.setDisplayName("张三");
        when(userInfoService.selectUserInfoByUserId("merchant-1")).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsById(10L)).thenReturn(job);
        when(jobSignupOrderService.selectPaidSignupUsersByJobId(10L, 1, null))
                .thenReturn(Collections.singletonList(record));
        when(jobPayrollItemMapper.selectCreditedEmployeeIdsByJobId(10L)).thenReturn(Collections.emptyList());

        List<WxSignupUserVo> users = service.listSignupUsers("merchant-1", 10L, null);

        assertEquals(1, users.size());
        assertEquals("张三", users.get(0).getDisplayName());
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
    void should_mark_job_as_pending_settlement_when_work_ended_and_unpaid_signup_user_exists() {
        when(userInfoService.selectUserInfoByUserId("merchant-1")).thenReturn(merchantUser());
        DailyJobs job = ownedJob(9001L, 1L);
        job.setWorkDate(daysAgo(1));
        when(dailyJobsService.selectDailyJobsList(any(DailyJobs.class))).thenReturn(Collections.singletonList(job));
        when(dailyJobsService.countPaidSignupOrders(9001L, 1)).thenReturn(1);
        when(jobSignupOrderService.selectPaidSignupUsersByJobId(9001L, 1, null))
                .thenReturn(Collections.singletonList(signupUser(2001L, "张三")));
        when(jobPayrollItemMapper.selectCreditedEmployeeIdsByJobId(9001L)).thenReturn(Collections.emptyList());

        List<WxMerchantJobVo> result = service.listMerchantJobs("merchant-1");

        assertEquals("PENDING", result.get(0).getSettlementStatus());
        assertEquals("待结账", result.get(0).getSettlementStatusLabel());
        assertTrue(result.get(0).getCanSettle());
    }

    @Test
    void should_mark_job_as_settled_when_all_paid_signup_users_have_payroll_records() {
        when(userInfoService.selectUserInfoByUserId("merchant-1")).thenReturn(merchantUser());
        DailyJobs job = ownedJob(9002L, 3L);
        job.setWorkDate(daysAgo(1));
        when(dailyJobsService.selectDailyJobsList(any(DailyJobs.class))).thenReturn(Collections.singletonList(job));
        when(dailyJobsService.countPaidSignupOrders(9002L, 1)).thenReturn(2);
        when(jobSignupOrderService.selectPaidSignupUsersByJobId(9002L, 1, null))
                .thenReturn(List.of(signupUser(2001L, "张三"), signupUser(2002L, "李四")));
        when(jobPayrollItemMapper.selectCreditedEmployeeIdsByJobId(9002L)).thenReturn(List.of(2001L, 2002L));

        List<WxMerchantJobVo> result = service.listMerchantJobs("merchant-1");

        assertEquals("SETTLED", result.get(0).getSettlementStatus());
        assertEquals("已结清", result.get(0).getSettlementStatusLabel());
        assertFalse(result.get(0).getCanSettle());
    }

    @Test
    void should_mark_signup_user_as_payroll_paid_when_credited_item_exists() {
        when(userInfoService.selectUserInfoByUserId("merchant-1")).thenReturn(merchantUser());
        DailyJobs job = ownedJob(9004L, 3L);
        when(dailyJobsService.selectDailyJobsById(9004L)).thenReturn(job);
        when(jobSignupOrderService.selectPaidSignupUsersByJobId(9004L, 1, null))
                .thenReturn(List.of(signupUser(2001L, "张三"), signupUser(2002L, "李四")));
        when(jobPayrollItemMapper.selectCreditedEmployeeIdsByJobId(9004L)).thenReturn(List.of(2001L));

        List<WxSignupUserVo> result = service.listSignupUsers("merchant-1", 9004L, null);

        assertTrue(result.get(0).getPayrollPaid());
        assertFalse(result.get(1).getPayrollPaid());
    }

    @Test
    void should_reject_invalid_job_status_transition() {
        when(userInfoService.selectUserInfoByUserId("merchant-1")).thenReturn(merchantUser());
        DailyJobs job = ownedJob(9003L, 1L);
        when(dailyJobsService.selectDailyJobsById(9003L)).thenReturn(job);

        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.updateMerchantJobStatus("merchant-1", 9003L, 3L));

        assertEquals("当前岗位状态不可修改", ex.getMessage());
    }

    private UserInfo merchantUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(100L);
        userInfo.setUserId("merchant-1");
        userInfo.setUserType(2);
        return userInfo;
    }

    private DailyJobs ownedJob(Long jobId, Long status) {
        DailyJobs job = new DailyJobs();
        job.setId(jobId);
        job.setPublisherUid(100L);
        job.setStatus(status);
        return job;
    }

    private JobSignupUserRecordVo signupUser(Long userInfoId, String displayName) {
        JobSignupUserRecordVo record = new JobSignupUserRecordVo();
        record.setUserInfoId(userInfoId);
        record.setDisplayName(displayName);
        return record;
    }

    private Date daysAgo(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        return calendar.getTime();
    }
}
