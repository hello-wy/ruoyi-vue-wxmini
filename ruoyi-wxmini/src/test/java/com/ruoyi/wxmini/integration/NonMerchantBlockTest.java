package com.ruoyi.wxmini.integration;

import com.ruoyi.common.exception.ServiceException;
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
 * Preservation baseline · non-merchant identities are blocked at the service
 * boundary by {@code requireMerchant}.
 *
 * <p>Validates: P5 (Preservation); Requirements: 3.2.
 *
 * <p>The frontend renders a "仅商家可查看 / 请先切换为商家身份" guard card and
 * does not even call the API for {@code userType ∈ {0, 1, 3}}; that path is
 * covered by the deferred frontend snapshot test (see
 * {@code .kiro/specs/signup-users-page-fix/preservation-baseline.md}).
 *
 * <p>This backend integration test is the preservation baseline for the
 * service-layer half of the non-merchant guard: even if a non-merchant
 * somehow reaches the service, the existing rejection message is unchanged.
 *
 * <p>Mock-only — no SpringBoot, no DB.
 */
@ExtendWith(MockitoExtension.class)
class NonMerchantBlockTest {

    private static final Long EXPLORATION_JOB_ID = 14L;
    private static final Integer USER_TYPE_PARENT = 0;
    private static final Integer USER_TYPE_STUDENT = 1;
    private static final Integer USER_TYPE_AUNT = 3;

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
    @DisplayName("Baseline · userType=0 (PARENT) is rejected with the existing merchant guard message")
    void parentIsRejectedAsNonMerchant() {
        verifyRejectsAsNonMerchant("user-parent", USER_TYPE_PARENT);
    }

    @Test
    @DisplayName("Baseline · userType=1 (STUDENT) is rejected with the existing merchant guard message")
    void studentIsRejectedAsNonMerchant() {
        verifyRejectsAsNonMerchant("user-student", USER_TYPE_STUDENT);
    }

    @Test
    @DisplayName("Baseline · userType=3 (AUNT) is rejected with the existing merchant guard message")
    void auntIsRejectedAsNonMerchant() {
        verifyRejectsAsNonMerchant("user-aunt", USER_TYPE_AUNT);
    }

    /**
     * Negative path: when the user record itself is missing, the service must
     * raise {@code 用户不存在} rather than continuing into the merchant guard.
     */
    @Test
    @DisplayName("Baseline · missing UserInfo raises 用户不存在 (unchanged behavior)")
    void missingUserInfoRaisesUserNotFound() {
        when(userInfoService.selectUserInfoByUserId("ghost")).thenReturn(null);

        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.listSignupUsers("ghost", EXPLORATION_JOB_ID, null));
        assertEquals("用户不存在", ex.getMessage());

        verify(dailyJobsService, never()).selectDailyJobsById(EXPLORATION_JOB_ID);
        verify(jobSignupOrderService, never()).selectPaidSignupUsersByJobId(
                EXPLORATION_JOB_ID, 1, null);
    }

    private void verifyRejectsAsNonMerchant(String userId, Integer userType) {
        UserInfo user = new UserInfo();
        user.setId(900L);
        user.setUserId(userId);
        user.setUserType(userType);
        when(userInfoService.selectUserInfoByUserId(userId)).thenReturn(user);

        // listSignupUsers
        ServiceException listEx = assertThrows(ServiceException.class,
                () -> service.listSignupUsers(userId, EXPLORATION_JOB_ID, null));
        assertEquals("仅商家可查看报名用户", listEx.getMessage(),
                "BASELINE: rejection message must remain '仅商家可查看报名用户' for userType=" + userType);

        // listMerchantJobs (different message; this is also part of the preservation baseline)
        ServiceException jobsEx = assertThrows(ServiceException.class,
                () -> service.listMerchantJobs(userId));
        assertEquals("仅商家可查看兼职日结查询", jobsEx.getMessage(),
                "BASELINE: rejection message must remain '仅商家可查看兼职日结查询' for userType=" + userType);

        // updateMerchantJobStatus
        ServiceException statusEx = assertThrows(ServiceException.class,
                () -> service.updateMerchantJobStatus(userId, EXPLORATION_JOB_ID, 3L));
        assertEquals("仅商家可修改岗位状态", statusEx.getMessage(),
                "BASELINE: rejection message must remain '仅商家可修改岗位状态' for userType=" + userType);

        // None of the rejected calls reach the data layer.
        verify(jobSignupOrderService, never()).selectPaidSignupUsersByJobId(
                EXPLORATION_JOB_ID, 1, null);
        verify(dailyJobsService, never()).selectDailyJobsList(
                org.mockito.ArgumentMatchers.any(com.ruoyi.system.domain.DailyJobs.class));
    }
}
