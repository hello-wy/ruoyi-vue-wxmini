package com.ruoyi.wxmini.integration;

import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.vo.JobSignupUserRecordVo;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobAttendanceAdminService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.ISignInRecordService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.impl.WxJobScheduleServiceImpl;
import com.ruoyi.wxmini.vo.WxSignupUserVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Preservation baseline · after a job is offshelved ({@code status=3}), the
 * publisher merchant can still hit
 * {@code GET /wxmini/jobs/{jobId}/signup-users} and receive the same payload
 * (same field shape) as before.
 *
 * <p>This locks the {@code byte-for-byte equivalence} requirement of P5 with
 * respect to the per-job direct-access path. The post-Task-5.1 frontend will
 * direct-link via {@code pages/jobs/signup-users?jobId=14}; the backend
 * service path that fulfills that frontend request is asserted here.
 *
 * <p>Validates: P5 (Preservation), supports P3 direct-access guarantee;
 * Requirements: 2.3, 3.7.
 *
 * <p>Mock-only — no SpringBoot, no DB.
 */
@ExtendWith(MockitoExtension.class)
class DirectAccessAfterOffshelfTest {

    private static final String MERCHANT_USER_ID = "merchant-1";
    private static final Long MERCHANT_USER_INFO_ID = 100L;
    private static final Long JOB_ID = 14L;
    private static final Integer USER_TYPE_MERCHANT = 2;

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
    @DisplayName("Baseline · listSignupUsers returns same payload regardless of daily_jobs.status")
    void direct_access_returns_same_payload_before_and_after_offshelf() {
        UserInfo merchant = merchantUser();
        JobSignupUserRecordVo record = new JobSignupUserRecordVo();
        record.setUserInfoId(2001L);
        record.setDisplayName("张三");
        record.setPhoneMasked("138****0000");
        record.setOrderNo("JOB202605020001");
        record.setAttendanceStatus(0);
        record.setAttendanceStatusLabel("未签到");
        record.setSignedCount(0);
        record.setAuditStatus(0);
        record.setAuditStatusLabel("未提交");
        when(userInfoService.selectUserInfoByUserId(MERCHANT_USER_ID)).thenReturn(merchant);
        when(jobSignupOrderService.selectPaidSignupUsersByJobId(JOB_ID, 1, null))
                .thenReturn(Collections.singletonList(record));

        // Before offshelf (status=0)
        DailyJobs jobBefore = new DailyJobs();
        jobBefore.setId(JOB_ID);
        jobBefore.setPublisherUid(MERCHANT_USER_INFO_ID);
        jobBefore.setStatus(0L);
        when(dailyJobsService.selectDailyJobsById(JOB_ID)).thenReturn(jobBefore);
        List<WxSignupUserVo> before = service.listSignupUsers(MERCHANT_USER_ID, JOB_ID, null);

        // After offshelf (status=3) — service must still serve.
        DailyJobs jobAfter = new DailyJobs();
        jobAfter.setId(JOB_ID);
        jobAfter.setPublisherUid(MERCHANT_USER_INFO_ID);
        jobAfter.setStatus(3L);
        when(dailyJobsService.selectDailyJobsById(JOB_ID)).thenReturn(jobAfter);
        List<WxSignupUserVo> after = service.listSignupUsers(MERCHANT_USER_ID, JOB_ID, null);

        assertNotNull(before);
        assertNotNull(after);
        assertEquals(1, before.size());
        assertEquals(1, after.size());

        // Field shape equivalence (the property of "byte-for-byte equivalence" reduced
        // to the user-visible field set, which is what P5 actually constrains).
        WxSignupUserVo b = before.get(0);
        WxSignupUserVo a = after.get(0);
        assertEquals(b.getUserInfoId(), a.getUserInfoId());
        assertEquals(b.getDisplayName(), a.getDisplayName());
        assertEquals(b.getPhoneMasked(), a.getPhoneMasked());
        assertEquals(b.getOrderNo(), a.getOrderNo());
        assertEquals(b.getAttendanceStatus(), a.getAttendanceStatus());
        assertEquals(b.getAttendanceStatusLabel(), a.getAttendanceStatusLabel());
        assertEquals(b.getAuditStatus(), a.getAuditStatus());
        assertEquals(b.getAuditStatusLabel(), a.getAuditStatusLabel());
    }

    private UserInfo merchantUser() {
        UserInfo u = new UserInfo();
        u.setId(MERCHANT_USER_INFO_ID);
        u.setUserId(MERCHANT_USER_ID);
        u.setUserType(USER_TYPE_MERCHANT);
        return u;
    }
}
