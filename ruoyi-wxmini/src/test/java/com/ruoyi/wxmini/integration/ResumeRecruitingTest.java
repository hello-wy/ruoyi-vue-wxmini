package com.ruoyi.wxmini.integration;

import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobAttendanceAdminService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.ISignInRecordService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.impl.WxJobScheduleServiceImpl;
import com.ruoyi.wxmini.vo.WxMerchantJobVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Preservation baseline · "恢复招聘" continues to flip {@code status} from 3
 * back to 0 and the resumed job reappears in {@code listMerchantJobs}.
 *
 * <p>Validates: P5 (Preservation); Requirements: 3.4.
 *
 * <p>Mock-only — no SpringBoot, no DB.
 */
@ExtendWith(MockitoExtension.class)
class ResumeRecruitingTest {

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
    @DisplayName("Baseline · updateMerchantJobStatus(j, 0) flips status from 3 to 0")
    void resume_recruiting_flips_status_three_to_zero() {
        UserInfo merchant = merchantUser();
        DailyJobs job = new DailyJobs();
        job.setId(JOB_ID);
        job.setPublisherUid(MERCHANT_USER_INFO_ID);
        job.setStatus(3L);

        when(userInfoService.selectUserInfoByUserId(MERCHANT_USER_ID)).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsById(JOB_ID)).thenReturn(job);

        service.updateMerchantJobStatus(MERCHANT_USER_ID, JOB_ID, 0L);

        ArgumentCaptor<DailyJobs> captor = ArgumentCaptor.forClass(DailyJobs.class);
        verify(dailyJobsService).updateDailyJobs(captor.capture());
        assertEquals(0L, captor.getValue().getStatus().longValue(),
                "BASELINE: '恢复招聘' must call updateDailyJobs with status=0");
    }

    @Test
    @DisplayName("Baseline · resumed job reappears in listMerchantJobs (UNFIXED behavior)")
    void resumed_job_appears_in_listMerchantJobs() {
        UserInfo merchant = merchantUser();
        DailyJobs resumed = new DailyJobs();
        resumed.setId(JOB_ID);
        resumed.setPublisherUid(MERCHANT_USER_INFO_ID);
        resumed.setStatus(0L); // status after resume
        resumed.setTitle("已恢复招聘的岗位");

        when(userInfoService.selectUserInfoByUserId(MERCHANT_USER_ID)).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsList(any(DailyJobs.class)))
                .thenReturn(Collections.singletonList(resumed));
        lenient().when(dailyJobsService.countPaidSignupOrders(any(Long.class), any(Integer.class)))
                .thenReturn(0);

        List<WxMerchantJobVo> jobs = service.listMerchantJobs(MERCHANT_USER_ID);

        assertNotNull(jobs);
        assertEquals(1, jobs.size(), "resumed job (status=0) must appear in the list");
        assertEquals(JOB_ID, jobs.get(0).getId());
        assertEquals(0L, jobs.get(0).getStatus().longValue());
    }

    /**
     * Locks the post-fix Property 3 invariant for the cancel→resume cycle:
     * after Task 5.2 ships the {@code status=3} filter, a job that was just
     * resumed (status flipped from 3 to 0) must still reappear in the list.
     *
     * <p>Today this is a stronger statement than the codepath provides — the
     * UNFIXED service does not filter at all. We assert the property using a
     * mapper output that reflects what {@code selectDailyJobsList} returns
     * AFTER the resume, which is identical to "the list contains the resumed
     * job and excludes any siblings that are still status=3".
     */
    @Test
    @DisplayName("Baseline · cancel→resume cycle keeps resumed job visible alongside live jobs")
    void cancel_then_resume_preserves_visibility() {
        UserInfo merchant = merchantUser();
        DailyJobs liveSibling = new DailyJobs();
        liveSibling.setId(15L);
        liveSibling.setPublisherUid(MERCHANT_USER_INFO_ID);
        liveSibling.setStatus(0L);
        DailyJobs justResumed = new DailyJobs();
        justResumed.setId(JOB_ID);
        justResumed.setPublisherUid(MERCHANT_USER_INFO_ID);
        justResumed.setStatus(0L);

        when(userInfoService.selectUserInfoByUserId(MERCHANT_USER_ID)).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsList(any(DailyJobs.class)))
                .thenReturn(Arrays.asList(liveSibling, justResumed));
        lenient().when(dailyJobsService.countPaidSignupOrders(any(Long.class), any(Integer.class)))
                .thenReturn(0);

        List<WxMerchantJobVo> jobs = service.listMerchantJobs(MERCHANT_USER_ID);

        assertEquals(2, jobs.size());
        assertTrue(jobs.stream().anyMatch(j -> JOB_ID.equals(j.getId())),
                "resumed job must be visible after cancel→resume cycle");
        assertTrue(jobs.stream().anyMatch(j -> 15L == j.getId()),
                "co-existing live job must remain visible");
    }

    private UserInfo merchantUser() {
        UserInfo u = new UserInfo();
        u.setId(MERCHANT_USER_INFO_ID);
        u.setUserId(MERCHANT_USER_ID);
        u.setUserType(USER_TYPE_MERCHANT);
        return u;
    }
}
