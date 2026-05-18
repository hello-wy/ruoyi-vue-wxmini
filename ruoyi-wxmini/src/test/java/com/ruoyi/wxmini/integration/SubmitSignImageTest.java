package com.ruoyi.wxmini.integration;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.domain.SignInRecord;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobAttendanceAdminService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.ISignInRecordService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.impl.WxJobScheduleServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Preservation baseline · {@code submitJobSignImage} continues to either
 * insert a fresh {@link SignInRecord} (record_type=3) or update the existing
 * one with the same field set, regardless of the bugfix workflow.
 *
 * <p>Property domain: random {@code (orderNo, signImageUrl)} input pairs;
 * the service is invoked through mocks and the captured {@link SignInRecord}
 * is asserted to carry the unchanged baseline field shape:
 * {@code recordType=3, signStatus=0, auditStatus=1, signImageUrl=signImageName=signImageUrl input}.
 *
 * <p>Validates: P5 (Preservation); Requirements: 3.6.
 *
 * <p>Mock-only — no SpringBoot, no DB.
 */
@ExtendWith(MockitoExtension.class)
class SubmitSignImageTest {

    private static final long PROPERTY_SEED = 0xCAFEBABEL;
    private static final String STUDENT_USER_ID = "student-1";
    private static final Long STUDENT_USER_INFO_ID = 321L;
    private static final Long JOB_ID = 10L;
    private static final Integer RECORD_TYPE_JOB = 3;
    private static final Integer SIGN_STATUS_PENDING = 0;
    private static final Integer AUDIT_STATUS_PENDING = 1;

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

    private final Random rng = new Random(PROPERTY_SEED);

    @RepeatedTest(value = 30, name = "insert-baseline: case {currentRepetition}/{totalRepetitions}")
    @DisplayName("Baseline · insert path writes record_type=3 + signStatus=0 + auditStatus=1")
    void insertPath_writes_baseline_field_shape() {
        String orderNo = "JOB" + Math.abs(rng.nextLong());
        String signImageUrl = randomSignImageUrl();

        primeOrderAndUser(orderNo);
        // No existing record → service inserts a new one.
        when(signInRecordService.selectJobSignInRecord(JOB_ID, STUDENT_USER_INFO_ID))
                .thenReturn(null);

        service.submitJobSignImage(STUDENT_USER_ID, JOB_ID, signImageUrl);

        ArgumentCaptor<SignInRecord> captor = ArgumentCaptor.forClass(SignInRecord.class);
        verify(signInRecordService).insertSignInRecord(captor.capture());
        verify(signInRecordService, never()).updateJobSignSubmitFields(any(SignInRecord.class));

        SignInRecord written = captor.getValue();
        assertNotNull(written);
        assertEquals(STUDENT_USER_INFO_ID, written.getUid());
        assertEquals(JOB_ID, written.getJobId());
        assertEquals(RECORD_TYPE_JOB, written.getRecordType());
        assertEquals(SIGN_STATUS_PENDING, written.getSignStatus());
        assertEquals(AUDIT_STATUS_PENDING, written.getAuditStatus());
        assertEquals(signImageUrl, written.getSignImageUrl());
        assertEquals(signImageUrl, written.getSignImageName());
        assertNotNull(written.getSignTime());
        assertNotNull(written.getSubmitTime());
    }

    @RepeatedTest(value = 30, name = "update-baseline: case {currentRepetition}/{totalRepetitions}")
    @DisplayName("Baseline · update path resets signStatus / auditStatus and refreshes timestamps")
    void updatePath_resets_baseline_fields() {
        String orderNo = "JOB" + Math.abs(rng.nextLong());
        String signImageUrl = randomSignImageUrl();

        primeOrderAndUser(orderNo);

        // Existing record present (already audited or rejected) → service updates in place.
        SignInRecord existing = new SignInRecord();
        existing.setId(7777L);
        existing.setUid(STUDENT_USER_INFO_ID);
        existing.setJobId(JOB_ID);
        existing.setRecordType(RECORD_TYPE_JOB);
        existing.setSignStatus(1);
        existing.setAuditStatus(3); // 已驳回
        when(signInRecordService.selectJobSignInRecord(JOB_ID, STUDENT_USER_INFO_ID))
                .thenReturn(existing);

        service.submitJobSignImage(STUDENT_USER_ID, JOB_ID, signImageUrl);

        ArgumentCaptor<SignInRecord> captor = ArgumentCaptor.forClass(SignInRecord.class);
        verify(signInRecordService).updateJobSignSubmitFields(captor.capture());
        verify(signInRecordService, never()).insertSignInRecord(any(SignInRecord.class));

        SignInRecord written = captor.getValue();
        assertNotNull(written);
        assertEquals(7777L, written.getId().longValue(),
                "BASELINE: update path keeps the existing record id");
        assertEquals(SIGN_STATUS_PENDING, written.getSignStatus(),
                "BASELINE: update resets signStatus to 0 (待签到)");
        assertEquals(AUDIT_STATUS_PENDING, written.getAuditStatus(),
                "BASELINE: update resets auditStatus to 1 (待审核)");
        assertEquals(signImageUrl, written.getSignImageUrl());
        assertEquals(signImageUrl, written.getSignImageName());
        assertNotNull(written.getSignTime());
        assertNotNull(written.getSubmitTime());
    }

    @Test
    @DisplayName("Baseline · null jobId raises 岗位不能为空 (unchanged validation)")
    void nullJobId_raisesValidation() {
        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.submitJobSignImage(STUDENT_USER_ID, null, "/profile/x.jpg"));
        assertEquals("岗位不能为空", ex.getMessage());
        verifyNoSignInRecordWrite();
    }

    @Test
    @DisplayName("Baseline · blank signImageUrl raises 签到图片不能为空 (unchanged validation)")
    void blankSignImageUrl_raisesValidation() {
        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.submitJobSignImage(STUDENT_USER_ID, JOB_ID, "  "));
        assertEquals("签到图片不能为空", ex.getMessage());
        verifyNoSignInRecordWrite();
    }

    @Test
    @DisplayName("Baseline · missing paid order raises 未找到已支付报名订单")
    void missingPaidOrder_raisesNotFound() {
        when(jobSignupOrderService.selectLatestPaidOrder(STUDENT_USER_ID, JOB_ID))
                .thenReturn(null);

        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.submitJobSignImage(STUDENT_USER_ID, JOB_ID, "/profile/x.jpg"));
        assertEquals("未找到已支付报名订单", ex.getMessage());
        verifyNoSignInRecordWrite();
    }

    private void primeOrderAndUser(String orderNo) {
        JobSignupOrder paidOrder = new JobSignupOrder();
        paidOrder.setOrderNo(orderNo);
        paidOrder.setUserId(STUDENT_USER_ID);
        paidOrder.setJobId(JOB_ID);
        paidOrder.setStatus(1);
        when(jobSignupOrderService.selectLatestPaidOrder(STUDENT_USER_ID, JOB_ID))
                .thenReturn(paidOrder);

        UserInfo userInfo = new UserInfo();
        userInfo.setId(STUDENT_USER_INFO_ID);
        userInfo.setUserId(STUDENT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(STUDENT_USER_ID)).thenReturn(userInfo);
    }

    private void verifyNoSignInRecordWrite() {
        verify(signInRecordService, never()).insertSignInRecord(any(SignInRecord.class));
        verify(signInRecordService, never()).updateJobSignSubmitFields(any(SignInRecord.class));
    }

    private String randomSignImageUrl() {
        long ts = Math.abs(rng.nextLong());
        return "/profile/job-sign/" + STUDENT_USER_INFO_ID + "/" + ts + ".jpg";
    }

    private static SignInRecord any(Class<SignInRecord> clazz) {
        return org.mockito.ArgumentMatchers.any(clazz);
    }
}
