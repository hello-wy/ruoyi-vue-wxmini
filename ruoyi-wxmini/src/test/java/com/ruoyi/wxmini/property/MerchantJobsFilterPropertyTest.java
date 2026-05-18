package com.ruoyi.wxmini.property;

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
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * Preservation baseline · property test for {@code listMerchantJobs} status filter.
 *
 * <p>Validates: P5 (Preservation); Requirements: 3.3, 3.4, 3.7.
 *
 * <p>Property domain: random {@code n ∈ [0, 50]} {@link DailyJobs} entries with
 * {@code status} sampled uniformly over {@code {0, 1, 2, 3}}; the service
 * {@link WxJobScheduleServiceImpl#listMerchantJobs(String)} is invoked through
 * mocks (no SpringBoot, no DB).
 *
 * <p>Phase = baseline observation on UNFIXED code:
 * <ul>
 *   <li>{@link #baseline_listMerchantJobs_includesAllInputJobs} runs the property
 *       and asserts the CURRENT (pre-fix) behavior — returned size matches input
 *       size and {@code status=3} jobs ARE included. This locks the baseline so
 *       that any accidental drift before Task 5.2 lands is caught.</li>
 *   <li>{@link #postFix_listMerchantJobs_filtersStatus3} encodes the
 *       Property 3 (post-fix) assertion. It is {@link Disabled} during the
 *       baseline phase and will be enabled in Task 6.2 after Task 5.2 ships
 *       the {@code status=3} filter.</li>
 * </ul>
 *
 * <p>Mock-only — does not boot Spring or touch a database. Uses
 * {@link RepeatedTest} as a lightweight stand-in for jqwik (the project does
 * not currently depend on jqwik).
 */
@ExtendWith(MockitoExtension.class)
class MerchantJobsFilterPropertyTest {

    private static final String MERCHANT_USER_ID = "merchant-1";
    private static final Long MERCHANT_USER_INFO_ID = 100L;
    private static final Integer USER_TYPE_MERCHANT = 2;
    private static final long PROPERTY_SEED = 0xBA5E11_BEEFL;

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
     * Post-fix PBT (100 runs): after Task 5.2 shipped the {@code status=3} filter,
     * {@code listMerchantJobs} returns only jobs with {@code status ∈ {0, 1, 2}},
     * preserving relative order. This replaces the former baseline observation test.
     *
     * <p>Validates: P3 (Bug Condition · 下架入口与下架后列表过滤), P5 (Preservation);
     * Requirements: 2.3, 3.3, 3.4, 3.7.
     */
    @RepeatedTest(value = 100, name = "post-fix-property: case {currentRepetition}/{totalRepetitions}")
    @DisplayName("Post-fix PBT · listMerchantJobs filters status=3, keeps {0,1,2} in order")
    void postFix_listMerchantJobs_filtersStatus3_pbt() {
        int n = rng.nextInt(51); // [0, 50]
        List<DailyJobs> input = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            DailyJobs job = new DailyJobs();
            long id = 1000L + i;
            job.setId(id);
            job.setTitle("job-" + id);
            job.setPublisherUid(MERCHANT_USER_INFO_ID);
            long status = rng.nextInt(4); // {0, 1, 2, 3}
            job.setStatus(status);
            input.add(job);
        }

        UserInfo merchant = new UserInfo();
        merchant.setId(MERCHANT_USER_INFO_ID);
        merchant.setUserId(MERCHANT_USER_ID);
        merchant.setUserType(USER_TYPE_MERCHANT);

        when(userInfoService.selectUserInfoByUserId(MERCHANT_USER_ID)).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsList(any(DailyJobs.class))).thenReturn(input);
        lenient().when(dailyJobsService.countPaidSignupOrders(any(Long.class), any(Integer.class)))
                .thenReturn(0);

        List<WxMerchantJobVo> output = service.listMerchantJobs(MERCHANT_USER_ID);

        // Post-fix invariant: status=3 jobs are filtered out
        assertNotNull(output, "service must return a non-null list");
        assertFalse(output.stream().anyMatch(j -> j.getStatus() != null && j.getStatus() == 3L),
                "post-fix: status=3 jobs must be filtered out of listMerchantJobs");

        // Returned size = input size minus status=3 jobs
        long expectedKept = input.stream()
                .filter(j -> j.getStatus() == null || j.getStatus() != 3L)
                .count();
        assertEquals((int) expectedKept, output.size(),
                "post-fix: returned size = input size minus status=3 jobs");

        // Relative order is preserved for kept jobs
        int idx = 0;
        for (DailyJobs in : input) {
            if (in.getStatus() == null || in.getStatus() != 3L) {
                assertTrue(idx < output.size(), "post-fix: kept job missing in output");
                assertEquals(in.getId(), output.get(idx).getId(),
                        "post-fix: relative order is preserved across the filter");
                idx++;
            }
        }
    }

    /**
     * Property 3 post-fix assertion (deterministic example).
     *
     * <p>Enabled in Task 6.2 after Task 5.2 landed the {@code status=3}
     * filter in {@link WxJobScheduleServiceImpl#listMerchantJobs(String)}.
     * Assertion: {@code listMerchantJobs} returns the input intersected with
     * {@code {status ≠ 3}}, preserving the relative order.
     *
     * <p>**Validates: Requirements 2.3**
     */
    @Test
    @DisplayName("Post-fix · listMerchantJobs filters status=3 (Property 3)")
    void postFix_listMerchantJobs_filtersStatus3() {
        List<DailyJobs> input = new ArrayList<>();
        long[] statusMix = new long[]{0L, 1L, 2L, 3L, 0L, 3L, 2L};
        for (int i = 0; i < statusMix.length; i++) {
            DailyJobs job = new DailyJobs();
            job.setId(2000L + i);
            job.setTitle("job-" + (2000L + i));
            job.setPublisherUid(MERCHANT_USER_INFO_ID);
            job.setStatus(statusMix[i]);
            input.add(job);
        }

        UserInfo merchant = new UserInfo();
        merchant.setId(MERCHANT_USER_INFO_ID);
        merchant.setUserId(MERCHANT_USER_ID);
        merchant.setUserType(USER_TYPE_MERCHANT);

        when(userInfoService.selectUserInfoByUserId(MERCHANT_USER_ID)).thenReturn(merchant);
        when(dailyJobsService.selectDailyJobsList(any(DailyJobs.class))).thenReturn(input);
        lenient().when(dailyJobsService.countPaidSignupOrders(any(Long.class), any(Integer.class)))
                .thenReturn(0);

        List<WxMerchantJobVo> output = service.listMerchantJobs(MERCHANT_USER_ID);

        // After Task 5.2: status=3 must be filtered out, all others preserved in order.
        assertFalse(output.stream().anyMatch(j -> j.getStatus() != null && j.getStatus() == 3L),
                "post-fix: status=3 jobs must be filtered out of listMerchantJobs");
        long expectedKept = input.stream()
                .filter(j -> j.getStatus() != null && j.getStatus() != 3L)
                .count();
        assertEquals((int) expectedKept, output.size(),
                "post-fix: returned size = input size minus status=3 jobs");

        int idx = 0;
        for (DailyJobs in : input) {
            if (in.getStatus() == null || in.getStatus() != 3L) {
                assertTrue(idx < output.size(), "post-fix: kept job missing in output");
                assertEquals(in.getId(), output.get(idx).getId(),
                        "post-fix: relative order is preserved across the filter");
                idx++;
            }
        }
    }

    // -----------------------------------------------------------------------
    // Property domain shrinker hint:
    //   On baseline failure the failing example is the random list captured
    //   in `input` for the seeded run (PROPERTY_SEED). To reproduce a single
    //   failing case, copy the input into a one-shot @Test using the same seed.
    // -----------------------------------------------------------------------
    @SuppressWarnings("unused")
    private static final long _UNUSED_KEEPSEED = PROPERTY_SEED;
}
