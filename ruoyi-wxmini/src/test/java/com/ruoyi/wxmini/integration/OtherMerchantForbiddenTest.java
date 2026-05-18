package com.ruoyi.wxmini.integration;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobAttendanceAdminService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.ISignInRecordService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.impl.WxJobScheduleServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Preservation baseline · merchant A is forbidden from accessing merchant B's
 * job, on both {@code listSignupUsers} and {@code updateMerchantJobStatus}.
 *
 * <p>Validates: P5 (Preservation); Requirements: 3.3.
 *
 * <p>Mock-only — no SpringBoot, no DB.
 */
@ExtendWith(MockitoExtension.class)
class OtherMerchantForbiddenTest {

    private static final String MERCHANT_A_USER_ID = "merchant-A";
    private static final Long MERCHANT_A_USER_INFO_ID = 100L;
    private static final Long MERCHANT_B_USER_INFO_ID = 200L;
    private static final Long FOREIGN_JOB_ID = 14L;
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
    @DisplayName("Baseline · merchant A cannot list signup users of merchant B's job")
    void merchantA_cannotList_merchantB_signupUsers() {
        UserInfo merchantA = merchantUser(MERCHANT_A_USER_INFO_ID, MERCHANT_A_USER_ID);
        DailyJobs foreignJob = new DailyJobs();
        foreignJob.setId(FOREIGN_JOB_ID);
        foreignJob.setPublisherUid(MERCHANT_B_USER_INFO_ID);

        when(userInfoService.selectUserInfoByUserId(MERCHANT_A_USER_ID)).thenReturn(merchantA);
        when(dailyJobsService.selectDailyJobsById(FOREIGN_JOB_ID)).thenReturn(foreignJob);

        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.listSignupUsers(MERCHANT_A_USER_ID, FOREIGN_JOB_ID, null));
        assertEquals("仅岗位发布商家可查看报名用户", ex.getMessage());

        // Rejected before reaching the data layer.
        verify(jobSignupOrderService, never())
                .selectPaidSignupUsersByJobId(FOREIGN_JOB_ID, 1, null);
    }

    @Test
    @DisplayName("Baseline · merchant A cannot update status of merchant B's job")
    void merchantA_cannotUpdate_merchantB_jobStatus() {
        UserInfo merchantA = merchantUser(MERCHANT_A_USER_INFO_ID, MERCHANT_A_USER_ID);
        DailyJobs foreignJob = new DailyJobs();
        foreignJob.setId(FOREIGN_JOB_ID);
        foreignJob.setPublisherUid(MERCHANT_B_USER_INFO_ID);
        foreignJob.setStatus(0L);

        when(userInfoService.selectUserInfoByUserId(MERCHANT_A_USER_ID)).thenReturn(merchantA);
        when(dailyJobsService.selectDailyJobsById(FOREIGN_JOB_ID)).thenReturn(foreignJob);

        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.updateMerchantJobStatus(MERCHANT_A_USER_ID, FOREIGN_JOB_ID, 3L));
        assertEquals("仅岗位发布商家可修改岗位状态", ex.getMessage());

        verify(dailyJobsService, never()).updateDailyJobs(foreignJob);
    }

    @Test
    @DisplayName("Baseline · job-not-found raises 岗位不存在 unchanged")
    void missingJob_raisesJobNotFound() {
        UserInfo merchantA = merchantUser(MERCHANT_A_USER_INFO_ID, MERCHANT_A_USER_ID);
        when(userInfoService.selectUserInfoByUserId(MERCHANT_A_USER_ID)).thenReturn(merchantA);
        when(dailyJobsService.selectDailyJobsById(9999L)).thenReturn(null);

        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.listSignupUsers(MERCHANT_A_USER_ID, 9999L, null));
        assertEquals("岗位不存在", ex.getMessage());
    }

    private UserInfo merchantUser(Long id, String userId) {
        UserInfo u = new UserInfo();
        u.setId(id);
        u.setUserId(userId);
        u.setUserType(USER_TYPE_MERCHANT);
        return u;
    }
}
