package com.ruoyi.wxmini.service;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.vo.JobSignupUserRecordVo;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobAttendanceAdminService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.ISignInRecordService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.impl.WxJobScheduleServiceImpl;
import com.ruoyi.wxmini.vo.WxSignupUserVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

/**
 * Exploration test for design.md §Exploratory Bug Condition Checking (A 类·列表为空).
 *
 * <p>This test is the bugfix workflow Task 1. It is designed to FAIL on UNFIXED code —
 * a failure is the SUCCESS case, because it confirms the bug exists and locates the
 * structural root cause.
 *
 * <p>The local environment does not have a SpringBoot integration database profile,
 * so this test follows the design's documented fallback strategy:
 * <ul>
 *   <li><b>A1·DataPresence</b> — the SQL count check requires the real production DB;
 *       this test only records the verification command and documents the fact.</li>
 *   <li><b>A2·MerchantIdentity</b> — exercised via mocks of {@link WxJobScheduleServiceImpl};
 *       happy-path identity matching is asserted to not throw.</li>
 *   <li><b>A3·SqlJoin</b> — STATIC inspection of {@code JobSignupOrderMapper.xml}.
 *       Asserts that {@code selectPaidSignupUsersByJobId} must use
 *       {@code left join user_info} so that paid orders with missing
 *       {@code user_info} rows are not silently dropped. Fails on UNFIXED code,
 *       which currently uses {@code inner join user_info}. This is the structural
 *       proof that A3 is hit.</li>
 *   <li><b>A4·FrontendInterception</b> — covered by the companion vitest spec
 *       {@code RuoYi-App/src/__tests__/pages/jobs/signup-users.exploration.spec.js}.</li>
 * </ul>
 *
 * <p>Decision tree per design.md:
 * <ul>
 *   <li>Only A3 hits → fix is to switch SQL to {@code left join} + null-safety fallback.</li>
 *   <li>Multiple hit → fix in order A3 → A2 → A4 → A1.</li>
 * </ul>
 *
 * <p>Validates: P1 (Bug Condition · 列表非空); Requirements: 2.1, 1.1.
 */
@ExtendWith(MockitoExtension.class)
class WxJobScheduleSignupUsersExplorationTest {

    private static final Long EXPLORATION_JOB_ID = 14L;
    private static final String MERCHANT_USER_ID = "merchant-1";
    private static final Long MERCHANT_USER_INFO_ID = 100L;
    private static final Integer USER_TYPE_MERCHANT = 2;
    private static final Integer PAID_STATUS = 1;

    @Mock
    private IJobSignupOrderService jobSignupOrderService;
    @Mock
    private IDailyJobsService dailyJobsService;
    @Mock
    private com.ruoyi.wxmini.service.IUserInfoService userInfoService;
    @Mock
    private ISignInRecordService signInRecordService;
    @Mock
    private IJobAttendanceAdminService jobAttendanceAdminService;

    @InjectMocks
    private WxJobScheduleServiceImpl service;

    /**
     * A1·DataPresence.
     *
     * <p>The truly authoritative check is a direct SQL on the production DB:
     * <pre>{@code select count(*) from job_signup_order where job_id = 14 and status = 1;}</pre>
     * If the count is 0, the empty list is purely a data condition and no code fix is required.
     *
     * <p>This unit test cannot connect to the production DB. We record the command and
     * mark this candidate as MANUAL_VERIFICATION_REQUIRED. The result is captured in
     * {@code .kiro/specs/signup-users-page-fix/exploration-result.md}.
     */
    @Test
    @DisplayName("A1 · DataPresence requires production DB and is recorded as manual verification")
    void a1_dataPresence_requires_manual_verification_against_production_db() {
        String diagnosticSql = "select count(*) from job_signup_order where job_id = "
                + EXPLORATION_JOB_ID + " and status = " + PAID_STATUS + ";";
        // No DataSource is available in this test scope; assert the diagnostic command
        // is well-formed and document that this candidate must be checked on the real DB.
        assertTrue(diagnosticSql.contains("job_id = 14"));
        assertTrue(diagnosticSql.contains("status = 1"));
        // Decision: cannot determine A1 hit/miss from a unit test. Recorded in
        // exploration-result.md. Whether A1 is hit is orthogonal to A3 — both could be true.
    }

    /**
     * A2·MerchantIdentity.
     *
     * <p>Mocks a properly-configured merchant whose {@code UserInfo.id} matches
     * {@code daily_jobs.publisher_uid}. The service should NOT throw a
     * {@code ServiceException} in this happy path. If it did, that would prove
     * A2 is structurally broken (e.g., the equality check is wrong). Passing means
     * A2 is not structurally hit; whether the actual logged-in user happens to
     * mismatch in production is a runtime-data question, not a code-defect question.
     */
    @Test
    @DisplayName("A2 · MerchantIdentity happy-path does not throw — A2 not a structural defect")
    void a2_merchantIdentity_happyPath_doesNotThrow() {
        UserInfo merchant = new UserInfo();
        merchant.setId(MERCHANT_USER_INFO_ID);
        merchant.setUserId(MERCHANT_USER_ID);
        merchant.setUserType(USER_TYPE_MERCHANT);

        DailyJobs job = new DailyJobs();
        job.setId(EXPLORATION_JOB_ID);
        job.setPublisherUid(MERCHANT_USER_INFO_ID);

        when(userInfoService.selectUserInfoByUserId(MERCHANT_USER_ID)).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsById(EXPLORATION_JOB_ID)).thenReturn(job);
        when(jobSignupOrderService.selectPaidSignupUsersByJobId(EXPLORATION_JOB_ID, PAID_STATUS, null))
                .thenReturn(Collections.emptyList());

        assertDoesNotThrow(() ->
                service.listSignupUsers(MERCHANT_USER_ID, EXPLORATION_JOB_ID, null),
                "happy-path merchant identity must not raise ServiceException; "
                        + "if it did, A2 (merchant identity mismatch) would be a structural defect");
    }

    /**
     * A2·MerchantIdentity (negative control).
     *
     * <p>When the merchant is NOT the publisher, the service must throw
     * {@code 仅岗位发布商家可查看报名用户}. This documents the existing rejection
     * path so that any future regression of this guard is caught.
     */
    @Test
    @DisplayName("A2 · negative control — non-publisher merchant is rejected as expected")
    void a2_merchantIdentity_negativeControl_rejectsNonPublisher() {
        UserInfo merchant = new UserInfo();
        merchant.setId(MERCHANT_USER_INFO_ID);
        merchant.setUserId(MERCHANT_USER_ID);
        merchant.setUserType(USER_TYPE_MERCHANT);

        DailyJobs job = new DailyJobs();
        job.setId(EXPLORATION_JOB_ID);
        job.setPublisherUid(999L); // intentionally different

        when(userInfoService.selectUserInfoByUserId(MERCHANT_USER_ID)).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsById(EXPLORATION_JOB_ID)).thenReturn(job);

        ServiceException ex = org.junit.jupiter.api.Assertions.assertThrows(
                ServiceException.class,
                () -> service.listSignupUsers(MERCHANT_USER_ID, EXPLORATION_JOB_ID, null));
        assertEquals("仅岗位发布商家可查看报名用户", ex.getMessage());
    }

    /**
     * A2·service-contract sanity (positive).
     *
     * <p>When the mapper returns a non-empty list, the service must propagate every record
     * unchanged. This isolates the empty-list symptom to either the mapper (A3) or the
     * underlying data (A1). If this test ever fails, the bug is in the service layer
     * itself — a fifth root-cause candidate not currently in the design.
     */
    @Test
    @DisplayName("A2 · service propagates mapper records — empty-list symptom is not a service bug")
    void a2_servicePropagatesMapperRecords() {
        UserInfo merchant = new UserInfo();
        merchant.setId(MERCHANT_USER_INFO_ID);
        merchant.setUserId(MERCHANT_USER_ID);
        merchant.setUserType(USER_TYPE_MERCHANT);

        DailyJobs job = new DailyJobs();
        job.setId(EXPLORATION_JOB_ID);
        job.setPublisherUid(MERCHANT_USER_INFO_ID);

        JobSignupUserRecordVo record = new JobSignupUserRecordVo();
        record.setUserInfoId(200L);
        record.setDisplayName("张三");
        record.setOrderNo("order-1");
        record.setAttendanceStatus(0);
        record.setAttendanceStatusLabel("未签到");

        when(userInfoService.selectUserInfoByUserId(MERCHANT_USER_ID)).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsById(EXPLORATION_JOB_ID)).thenReturn(job);
        when(jobSignupOrderService.selectPaidSignupUsersByJobId(EXPLORATION_JOB_ID, PAID_STATUS, null))
                .thenReturn(Collections.singletonList(record));

        List<WxSignupUserVo> result = service.listSignupUsers(MERCHANT_USER_ID, EXPLORATION_JOB_ID, null);
        assertEquals(1, result.size(), "service must propagate mapper records 1:1");
        assertEquals("张三", result.get(0).getDisplayName());
        assertEquals("order-1", result.get(0).getOrderNo());
    }

    /**
     * A3·SqlJoin (the deterministic structural assertion).
     *
     * <p>Reads {@code JobSignupOrderMapper.xml} and inspects the body of
     * {@code selectPaidSignupUsersByJobId}. If the join clause uses
     * {@code inner join user_info}, every paid order whose corresponding
     * {@code user_info} row is missing (or whose {@code user_id} value does not
     * exactly match) is silently filtered out — producing an empty list even
     * though the order exists in {@code job_signup_order}.
     *
     * <p>Expected behavior after fix: the SQL uses {@code left join user_info} with
     * fallback values for missing {@code user_info} columns.
     *
     * <p><b>This assertion is INTENTIONALLY designed to FAIL on UNFIXED code</b> —
     * the failure proves the A3 candidate is the structural root cause of the
     * empty list at {@code jobId=14}.
     */
    @Test
    @DisplayName("A3 · SQL must use `left join user_info` to tolerate missing user_info rows (FAILS on UNFIXED code)")
    void a3_sqlJoin_selectPaidSignupUsersByJobId_mustUseLeftJoinUserInfo() throws Exception {
        String mapperXml = readMapperXml();
        String selectBody = extractSelectBody(mapperXml, "selectPaidSignupUsersByJobId");
        assertNotNull(selectBody, "selectPaidSignupUsersByJobId must exist in JobSignupOrderMapper.xml");

        String normalized = collapseWhitespace(selectBody).toLowerCase();

        // Structural assertion: the join with user_info must be a `left join` to keep paid
        // orders visible even when the user_info row is missing or mismatched.
        boolean hasLeftJoinUserInfo =
                normalized.contains("left join user_info ui")
                        || normalized.contains("left outer join user_info ui");
        boolean hasInnerJoinUserInfo =
                normalized.contains("inner join user_info ui")
                        // Mybatis allows bare `join` which is INNER by default.
                        || (normalized.contains("from job_signup_order jso")
                            && normalized.contains(" join user_info ui")
                            && !normalized.contains("left join user_info ui")
                            && !normalized.contains("left outer join user_info ui"));

        if (!hasLeftJoinUserInfo) {
            // The SQL drops paid orders whose user_info is missing — A3 hit.
            fail("A3 candidate hit: selectPaidSignupUsersByJobId currently does NOT use "
                    + "`left join user_info`. Detected inner join with user_info: " + hasInnerJoinUserInfo
                    + ". On UNFIXED code this is the structural root cause of the empty list "
                    + "at jobId=14 when matching user_info rows are missing or have a mismatched user_id. "
                    + "Counterexample: any paid order whose user_id does not appear in user_info "
                    + "(e.g. legacy data, soft-deleted user, user_id type mismatch) is filtered out.");
        }

        // Defensive: also confirm the inner-join variant is no longer present once the fix lands.
        assertFalse(hasInnerJoinUserInfo,
                "After the A3 fix, `inner join user_info` must be removed from "
                        + "selectPaidSignupUsersByJobId.");
    }

    // -- helpers ----------------------------------------------------------------------

    private String readMapperXml() throws Exception {
        // Try classpath first (mapper xml is shipped via ruoyi-system jar).
        InputStream classpathStream = getClass().getResourceAsStream("/mapper/system/JobSignupOrderMapper.xml");
        if (classpathStream != null) {
            try (InputStream in = classpathStream) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] tmp = new byte[8192];
                int read;
                while ((read = in.read(tmp)) != -1) {
                    buffer.write(tmp, 0, read);
                }
                return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
            }
        }
        // Fallback: resolve via repository-relative path so the test still runs when
        // ruoyi-system has not been packaged onto the test classpath.
        Path candidate = locateMapperXmlOnFilesystem();
        if (candidate != null) {
            return new String(Files.readAllBytes(candidate), StandardCharsets.UTF_8);
        }
        throw new IllegalStateException("JobSignupOrderMapper.xml not found on classpath or filesystem; "
                + "cannot perform A3 SQL inspection");
    }

    private Path locateMapperXmlOnFilesystem() {
        Path cwd = Paths.get("").toAbsolutePath();
        // Walk up looking for the ruoyi-vue-wxmini repo root.
        Path cursor = cwd;
        for (int i = 0; i < 6 && cursor != null; i++) {
            Path candidate = cursor.resolve(
                    "ruoyi-system/src/main/resources/mapper/system/JobSignupOrderMapper.xml");
            if (Files.exists(candidate)) {
                return candidate;
            }
            cursor = cursor.getParent();
        }
        return null;
    }

    private String extractSelectBody(String xml, String selectId) {
        int idx = xml.indexOf("id=\"" + selectId + "\"");
        if (idx < 0) {
            return null;
        }
        int end = xml.indexOf("</select>", idx);
        if (end < 0) {
            return null;
        }
        return xml.substring(idx, end);
    }

    private String collapseWhitespace(String input) {
        return input.replaceAll("\\s+", " ").trim();
    }
}
