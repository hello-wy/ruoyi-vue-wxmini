package com.ruoyi.wxmini.property;

import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobAttendanceAdminService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.ISignInRecordService;
import com.ruoyi.system.domain.vo.JobSignupUserRecordVo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.impl.WxJobScheduleServiceImpl;
import com.ruoyi.wxmini.vo.WxSignupUserVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Post-fix property test for the SQL join behavior of
 * {@code selectPaidSignupUsersByJobId}.
 *
 * <p>Enabled in Task 6.2 after Task 3.3 shipped the {@code inner join → left join}
 * fix. Validates that:
 * <ul>
 *   <li>The SQL now uses {@code left join user_info} (structural assertion).</li>
 *   <li>The service propagates all paid orders including those without a matching
 *       {@code user_info} row, using fallback fields
 *       ({@code displayName='未注册用户'}, {@code phoneMasked=''}).</li>
 * </ul>
 *
 * <p>**Validates: Requirements 2.1**
 *
 * <p>Validates: P1 (Bug Condition · A3 fix), P5 (Preservation); Requirements: 2.1, 3.7.
 */
@ExtendWith(MockitoExtension.class)
class SignupUsersJoinTolerancePropertyTest {

    private static final String MERCHANT_USER_ID = "merchant-1";
    private static final Long MERCHANT_USER_INFO_ID = 100L;
    private static final Integer USER_TYPE_MERCHANT = 2;
    private static final long PROPERTY_SEED = 0xA3_F1_5EEDL;

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

    /**
     * Post-fix structural assertion: after Task 3.3, the SQL uses
     * {@code left join user_info} instead of {@code inner join user_info}.
     */
    @Test
    @DisplayName("Post-fix · selectPaidSignupUsersByJobId uses `left join user_info` (A3 fix verified)")
    void postFix_sqlUsesLeftJoinUserInfo() throws Exception {
        String mapperXml = readMapperXml();
        String selectBody = extractSelectBody(mapperXml, "selectPaidSignupUsersByJobId");
        assertNotNull(selectBody, "selectPaidSignupUsersByJobId must exist in JobSignupOrderMapper.xml");

        String normalized = collapseWhitespace(selectBody).toLowerCase();

        boolean hasLeftJoinUserInfo =
                normalized.contains("left join user_info ui")
                        || normalized.contains("left outer join user_info ui");

        assertTrue(hasLeftJoinUserInfo,
                "POST-FIX: selectPaidSignupUsersByJobId must use `left join user_info ui` "
                        + "after Task 3.3 fix. Detected SQL body: " + normalized);
    }

    /**
     * Post-fix PBT (100 runs): randomly generate paid orders where some have
     * matching user_info and some don't. Assert that the service returns ALL
     * orders (count = input count), with fallback fields for missing user_info.
     *
     * <p>Since we cannot run actual SQL in a mock-only test, we simulate the
     * post-fix mapper behavior: the mapper returns records for ALL paid orders,
     * with fallback values for those missing user_info. The service must
     * propagate them 1:1 into WxSignupUserVo.
     */
    @RepeatedTest(value = 100, name = "post-fix-join-tolerance: case {currentRepetition}/{totalRepetitions}")
    @DisplayName("Post-fix PBT · listSignupUsers returns all paid orders including orphans with fallback fields")
    void postFix_listSignupUsers_returnsAllPaidOrders_withFallback() {
        int m = rng.nextInt(31); // [0, 30] orders
        int k = rng.nextInt(m + 1); // [0, m] have matching user_info

        // Simulate post-fix mapper output: all m orders are returned,
        // first k have real user_info, remaining (m-k) have fallback values
        List<JobSignupUserRecordVo> mapperOutput = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            JobSignupUserRecordVo record = new JobSignupUserRecordVo();
            record.setOrderNo("order-" + (i + 1));
            record.setAttendanceStatus(0);
            record.setAttendanceStatusLabel("未签到");
            record.setAuditStatus(0);
            record.setAuditStatusLabel("未提交");
            record.setSignedCount(0);

            if (i < k) {
                // Has matching user_info
                record.setUserInfoId(200L + i);
                record.setDisplayName("用户" + (i + 1));
                record.setPhoneMasked("138****" + String.format("%04d", i));
            } else {
                // Missing user_info — mapper returns fallback values (post-fix behavior)
                record.setUserInfoId(0L);
                record.setDisplayName("未注册用户");
                record.setPhoneMasked("");
            }
            mapperOutput.add(record);
        }

        UserInfo merchant = new UserInfo();
        merchant.setId(MERCHANT_USER_INFO_ID);
        merchant.setUserId(MERCHANT_USER_ID);
        merchant.setUserType(USER_TYPE_MERCHANT);

        DailyJobs job = new DailyJobs();
        job.setId(14L);
        job.setPublisherUid(MERCHANT_USER_INFO_ID);

        when(userInfoService.selectUserInfoByUserId(MERCHANT_USER_ID)).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsById(14L)).thenReturn(job);
        when(jobSignupOrderService.selectPaidSignupUsersByJobId(eq(14L), eq(1), any()))
                .thenReturn(mapperOutput);

        List<WxSignupUserVo> result = service.listSignupUsers(MERCHANT_USER_ID, 14L, null);

        // Property: ALL paid orders are returned (count = m)
        assertEquals(m, result.size(),
                "post-fix: listSignupUsers must return ALL paid orders (including orphans)");

        // Property: orders with user_info have real fields
        for (int i = 0; i < k; i++) {
            WxSignupUserVo vo = result.get(i);
            assertEquals(200L + i, vo.getUserInfoId());
            assertEquals("用户" + (i + 1), vo.getDisplayName());
            assertTrue(vo.getPhoneMasked() != null && !vo.getPhoneMasked().isEmpty(),
                    "orders with user_info must have non-empty phoneMasked");
        }

        // Property: orphan orders have fallback fields
        for (int i = k; i < m; i++) {
            WxSignupUserVo vo = result.get(i);
            assertEquals(0L, vo.getUserInfoId(),
                    "orphan order must have userInfoId=0 (fallback)");
            assertEquals("未注册用户", vo.getDisplayName(),
                    "orphan order must have displayName='未注册用户' (fallback)");
            assertEquals("", vo.getPhoneMasked(),
                    "orphan order must have phoneMasked='' (fallback)");
        }

        // Property: orderNo is always present
        for (int i = 0; i < m; i++) {
            assertNotNull(result.get(i).getOrderNo(),
                    "every returned record must have a non-null orderNo");
        }
    }

    // -- helpers --------------------------------------------------------------

    private String readMapperXml() throws Exception {
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
        Path candidate = locateMapperXmlOnFilesystem();
        if (candidate != null) {
            return new String(Files.readAllBytes(candidate), StandardCharsets.UTF_8);
        }
        throw new IllegalStateException(
                "JobSignupOrderMapper.xml not found on classpath or filesystem; test cannot run");
    }

    private Path locateMapperXmlOnFilesystem() {
        Path cwd = Paths.get("").toAbsolutePath();
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
