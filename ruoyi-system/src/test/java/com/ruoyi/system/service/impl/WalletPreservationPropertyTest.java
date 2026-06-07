package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletTransaction;
import com.ruoyi.system.domain.WalletWithdraw;
import com.ruoyi.system.mapper.WalletMapper;
import com.ruoyi.system.service.IWalletTransferGateway;
import com.ruoyi.system.service.dto.WalletTransferQueryResult;
import com.ruoyi.system.service.dto.WithdrawResult;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Preservation Property Tests — 非提现流程行为不变
 *
 * <p><b>Validates: Requirements 3.1, 3.2, 3.3, 3.4, 3.5, 3.6</b></p>
 *
 * <p>These tests capture the baseline behavior of the UNFIXED code that MUST be preserved
 * after the fix is implemented. They are expected to PASS on the current code.</p>
 *
 * <p>Property 2: For any input where the bug condition does NOT hold (queries, creditPayroll,
 * other callbacks, already-terminal records), the fixed code SHALL produce exactly the same
 * behavior as the original code.</p>
 *
 * <p>Uses {@link RepeatedTest} with random generation as a lightweight property-based testing
 * approach (the project does not currently depend on jqwik).</p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Preservation Property: 非提现流程行为不变")
class WalletPreservationPropertyTest {

    @Mock
    private WalletMapper walletMapper;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private IWalletTransferGateway walletTransferGateway;

    @InjectMocks
    private WalletServiceImpl service;

    private final Random random = new Random(42); // Fixed seed for reproducibility

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "transferMinAmount", new BigDecimal("1.00"));
        ReflectionTestUtils.setField(service, "transferMaxAmount", new BigDecimal("5000.00"));
        ReflectionTestUtils.setField(service, "transferNotifyUrl", "https://zhiyujia.xyz/api/wxmini/pay/wallet/notify");
        ReflectionTestUtils.setField(service, "transferSceneId", "1005");
        ReflectionTestUtils.setField(service, "transferBatchName", "钱包提现");
    }

    // ========================================================================
    // Property 2a: creditPayroll correctly updates balance, totalEarned, wallet_transaction
    // Validates: Requirement 3.3
    // ========================================================================

    /**
     * Property: For any valid amount > 0, creditPayroll SHALL:
     * 1. Increase balance by exactly amount
     * 2. Increase totalEarned by exactly amount
     * 3. Insert exactly one WalletTransaction with direction=1 (INCOME), bizType="PAYROLL"
     * 4. Transaction.balanceAfter = original balance + amount
     */
    @RepeatedTest(50)
    @DisplayName("creditPayroll: 随机正金额 → balance += amount, totalEarned += amount, 写入一条 INCOME 流水")
    void creditPayrollPreservesBalanceAndEarnedInvariant() {
        // Generate random valid payroll amount: [0.01, 9999.99]
        BigDecimal amount = generateRandomAmount(0.01, 9999.99);
        BigDecimal initialBalance = generateRandomAmount(0.00, 50000.00);
        BigDecimal initialTotalEarned = generateRandomAmount(0.00, 100000.00);
        Long uid = (long) (random.nextInt(10000) + 1);
        String bizId = "PAY-" + random.nextInt(100000);
        String remark = "工资入账-" + random.nextInt(100);

        UserWallet wallet = new UserWallet();
        wallet.setUid(uid);
        wallet.setBalance(initialBalance);
        wallet.setFrozen(BigDecimal.ZERO);
        wallet.setTotalEarned(initialTotalEarned);
        wallet.setTotalWithdrawn(BigDecimal.ZERO);
        when(walletMapper.selectWalletByUidForUpdate(uid)).thenReturn(wallet);

        // Act
        service.creditPayroll(uid, amount, bizId, remark);

        // Assert: balance increased by exactly amount
        BigDecimal expectedBalance = initialBalance.add(amount);
        assertEquals(0, expectedBalance.compareTo(wallet.getBalance()),
                "balance should increase by exactly amount. Initial=" + initialBalance + ", amount=" + amount);

        // Assert: totalEarned increased by exactly amount
        BigDecimal expectedTotalEarned = initialTotalEarned.add(amount);
        assertEquals(0, expectedTotalEarned.compareTo(wallet.getTotalEarned()),
                "totalEarned should increase by exactly amount");

        // Assert: wallet updated
        verify(walletMapper).updateWallet(wallet);

        // Assert: exactly one transaction inserted
        ArgumentCaptor<WalletTransaction> txCaptor = ArgumentCaptor.forClass(WalletTransaction.class);
        verify(walletMapper).insertTransaction(txCaptor.capture());
        WalletTransaction tx = txCaptor.getValue();
        assertEquals(uid, tx.getUid());
        assertEquals("PAYROLL", tx.getBizType());
        assertEquals(bizId, tx.getBizId());
        assertEquals(Integer.valueOf(1), tx.getDirection()); // INCOME
        assertEquals(0, amount.compareTo(tx.getAmount()));
        assertEquals(0, expectedBalance.compareTo(tx.getBalanceAfter()));
        assertEquals(remark, tx.getRemark());
    }

    /**
     * Property: creditPayroll throws ServiceException for amount <= 0 or null
     */
    @RepeatedTest(20)
    @DisplayName("creditPayroll: 无效金额(<=0 或 null) → 抛出 ServiceException")
    void creditPayrollRejectsInvalidAmount() {
        Long uid = (long) (random.nextInt(10000) + 1);

        // Generate invalid amounts: negative, zero, or null
        BigDecimal invalidAmount;
        int choice = random.nextInt(3);
        if (choice == 0) {
            invalidAmount = null;
        } else if (choice == 1) {
            invalidAmount = BigDecimal.ZERO;
        } else {
            invalidAmount = generateRandomAmount(-9999.99, -0.01);
        }

        BigDecimal finalInvalidAmount = invalidAmount;
        assertThrows(ServiceException.class,
                () -> service.creditPayroll(uid, finalInvalidAmount, "BIZ-1", "test"),
                "creditPayroll should throw ServiceException for amount=" + invalidAmount);

        // Should NOT update wallet or insert transaction
        verify(walletMapper, never()).updateWallet(any());
        verify(walletMapper, never()).insertTransaction(any());
    }

    // ========================================================================
    // Property 2b: Amount validation rejects invalid amounts
    // Validates: Requirement 3.2
    // ========================================================================

    /**
     * Property: applyWithdraw rejects amount < MIN_WITHDRAW (1.00)
     * Returns specific error message, does NOT create withdraw record
     */
    @RepeatedTest(30)
    @DisplayName("金额校验: amount < 1.00 → 返回'提现金额不能低于1元', 不写入 withdraw")
    void amountBelowMinimumIsRejected() {
        // Generate random amount in [0.01, 0.99]
        BigDecimal amount = generateRandomAmount(0.01, 0.99);

        WithdrawResult result = service.applyWithdraw("wx-user-1", 1L, amount);

        assertFalse(result.isSuccess(), "Amount " + amount + " should be rejected as below minimum");
        assertEquals("AMOUNT_OUT_OF_LIMIT", result.getFailType());
        assertTrue(result.getUserMessage().contains("提现金额不能低于"),
                "Amount " + amount + " should be rejected as below minimum");
        verify(walletMapper, never()).insertWithdraw(any());
    }

    /**
     * Property: applyWithdraw rejects null amount
     */
    @Test
    @DisplayName("金额校验: amount=null → 返回'提现金额不能低于1元'")
    void nullAmountIsRejected() {
        WithdrawResult result = service.applyWithdraw("wx-user-1", 1L, null);

        assertFalse(result.isSuccess());
        assertEquals("AMOUNT_OUT_OF_LIMIT", result.getFailType());
        assertTrue(result.getUserMessage().contains("提现金额不能低于"));
        verify(walletMapper, never()).insertWithdraw(any());
    }

    /**
     * Property: applyWithdraw rejects amount with precision > 2 decimal places
     */
    @RepeatedTest(30)
    @DisplayName("金额校验: 精度>2位小数 → 返回'提现金额最多保留两位小数', 不写入 withdraw")
    void amountWithExcessPrecisionIsRejected() {
        // Generate amount with 3+ decimal places, >= 1.00
        int intPart = random.nextInt(100) + 1;
        int decimalDigits = random.nextInt(3) + 3; // 3, 4, or 5 decimal places
        StringBuilder sb = new StringBuilder();
        sb.append(intPart).append(".");
        for (int i = 0; i < decimalDigits; i++) {
            sb.append(random.nextInt(9) + 1); // non-zero digits to ensure scale is preserved
        }
        BigDecimal amount = new BigDecimal(sb.toString());

        // Need to set up userInfoService since amount validation passes first two checks
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setIsRealnameAuth(1);
        userInfo.setRealName("张三");
        userInfo.setIdCard("110101199001010011");
        userInfo.setOpenId("openid-1");
        // Note: amount.scale() > 2 check happens BEFORE userInfo lookup in the code
        // Actually looking at the code: null/min check → scale check → userInfo lookup
        // So we don't need to mock userInfoService for this test

        WithdrawResult result = service.applyWithdraw("wx-user-1", 1L, amount);

        assertFalse(result.isSuccess(),
                "Amount " + amount + " (scale=" + amount.scale() + ") should be rejected for excess precision");
        assertEquals("AMOUNT_OUT_OF_LIMIT", result.getFailType());
        assertEquals("提现金额最多保留两位小数", result.getUserMessage());
        verify(walletMapper, never()).insertWithdraw(any());
    }

    /**
     * Property: applyWithdraw rejects amount > balance
     */
    @RepeatedTest(30)
    @DisplayName("金额校验: amount > balance → 返回'可用余额不足', 不写入 withdraw")
    void amountExceedingBalanceIsRejected() {
        // Generate balance and amount where amount > balance
        BigDecimal balance = generateRandomAmount(1.00, 100.00);
        BigDecimal amount = balance.add(generateRandomAmount(0.01, 500.00));
        // Ensure amount has scale <= 2
        amount = amount.setScale(2, RoundingMode.DOWN);
        Long uid = (long) (random.nextInt(10000) + 1);

        UserInfo userInfo = new UserInfo();
        userInfo.setId(uid);
        userInfo.setIsRealnameAuth(1);
        userInfo.setRealName("张三");
        userInfo.setIdCard("110101199001010011");
        userInfo.setOpenId("openid-1");
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(userInfo);

        UserWallet wallet = new UserWallet();
        wallet.setUid(uid);
        wallet.setBalance(balance);
        wallet.setFrozen(BigDecimal.ZERO);
        wallet.setTotalEarned(balance);
        wallet.setTotalWithdrawn(BigDecimal.ZERO);
        when(walletMapper.selectWalletByUid(uid)).thenReturn(wallet);

        WithdrawResult result = service.applyWithdraw("wx-user-1", uid, amount);

        assertFalse(result.isSuccess(),
                "Amount " + amount + " exceeding balance " + balance + " should be rejected");
        assertEquals("BALANCE_INSUFFICIENT", result.getFailType());
        verify(walletMapper, never()).insertWithdraw(any());
    }

    // ========================================================================
    // Property 2c: syncWithdrawStatusByOutBatchNo idempotency — balance deducted only once
    // Validates: Requirement 3.5
    // ========================================================================

    /**
     * Property: Calling syncWithdrawStatusByOutBatchNo twice with the same outBatchNo
     * that resolves to SUCCESS → balance deducted only once, only one transaction inserted
     */
    @Test
    @DisplayName("幂等性: syncWithdrawStatusByOutBatchNo 同一 outBatchNo 调用两次 → 余额仅扣一次")
    void syncWithdrawStatusIdempotent_balanceDeductedOnlyOnce() throws Exception {
        String outBatchNo = "WD10001234567890";
        Long uid = 10L;
        BigDecimal amount = new BigDecimal("50.00");
        BigDecimal initialBalance = new BigDecimal("200.00");

        // Track state changes across calls
        AtomicReference<Integer> withdrawStatus = new AtomicReference<>(0); // starts PROCESSING
        AtomicReference<BigDecimal> walletBalance = new AtomicReference<>(initialBalance);
        AtomicReference<BigDecimal> walletFrozen = new AtomicReference<>(amount);
        AtomicReference<BigDecimal> walletWithdrawn = new AtomicReference<>(BigDecimal.ZERO);

        WalletWithdraw withdraw = new WalletWithdraw();
        withdraw.setId(1L);
        withdraw.setUid(uid);
        withdraw.setAmount(amount);
        withdraw.setStatus(0);
        withdraw.setOutBatchNo(outBatchNo);
        withdraw.setOutDetailNo("WDD10001234567890");

        when(walletMapper.selectWithdrawByOutBatchNo(outBatchNo)).thenAnswer(inv -> {
            WalletWithdraw w = new WalletWithdraw();
            w.setId(1L);
            w.setUid(uid);
            w.setAmount(amount);
            w.setStatus(withdrawStatus.get());
            w.setOutBatchNo(outBatchNo);
            w.setOutDetailNo("WDD10001234567890");
            return w;
        });

        when(walletMapper.selectWithdrawById(1L)).thenAnswer(inv -> {
            WalletWithdraw w = new WalletWithdraw();
            w.setId(1L);
            w.setUid(uid);
            w.setAmount(amount);
            w.setStatus(withdrawStatus.get());
            w.setOutBatchNo(outBatchNo);
            w.setOutDetailNo("WDD10001234567890");
            return w;
        });

        UserWallet wallet = new UserWallet();
        wallet.setUid(uid);
        wallet.setBalance(initialBalance);
        wallet.setFrozen(amount);
        wallet.setTotalEarned(new BigDecimal("500.00"));
        wallet.setTotalWithdrawn(BigDecimal.ZERO);
        when(walletMapper.selectWalletByUidForUpdate(uid)).thenAnswer(inv -> {
            UserWallet w = new UserWallet();
            w.setUid(uid);
            w.setBalance(walletBalance.get());
            w.setFrozen(walletFrozen.get());
            w.setTotalEarned(new BigDecimal("500.00"));
            w.setTotalWithdrawn(walletWithdrawn.get());
            return w;
        });

        WalletTransaction existingWithdrawTransaction = new WalletTransaction();
        existingWithdrawTransaction.setUid(uid);
        existingWithdrawTransaction.setBizType("WITHDRAW");
        existingWithdrawTransaction.setBizId("1");
        existingWithdrawTransaction.setDirection(2);
        existingWithdrawTransaction.setAmount(amount);
        existingWithdrawTransaction.setBalanceAfter(initialBalance);
        when(walletMapper.selectTransactionByBiz("WITHDRAW", "1")).thenReturn(existingWithdrawTransaction);

        WalletTransferQueryResult detail = new WalletTransferQueryResult();
        detail.setBatchId("wx-batch-1");
        detail.setDetailId("wx-detail-1");
        detail.setDetailStatus("SUCCESS");
        when(walletTransferGateway.queryTransfer(outBatchNo, "WDD10001234567890")).thenReturn(detail);

        when(walletMapper.updateWithdrawStatus(any())).thenAnswer(inv -> {
            WalletWithdraw w = inv.getArgument(0);
            withdrawStatus.set(w.getStatus());
            return 1;
        });
        when(walletMapper.updateWallet(any())).thenAnswer(inv -> {
            UserWallet w = inv.getArgument(0);
            walletBalance.set(w.getBalance());
            walletFrozen.set(w.getFrozen());
            walletWithdrawn.set(w.getTotalWithdrawn());
            return 1;
        });
        // First call — should complete frozen withdraw
        boolean result1 = service.syncWithdrawStatusByOutBatchNo(outBatchNo);
        assertTrue(result1, "First sync should succeed");

        // Second call — should NOT deduct again (withdraw is now SUCCESS, skipped)
        boolean result2 = service.syncWithdrawStatusByOutBatchNo(outBatchNo);
        assertTrue(result2, "Second sync should return true (already terminal)");

        assertEquals(0, initialBalance.compareTo(walletBalance.get()),
                "Balance should not be deducted again after the amount has already been frozen");
        assertEquals(0, BigDecimal.ZERO.compareTo(walletFrozen.get()),
                "Frozen amount should be released after successful withdraw");
        assertEquals(0, amount.compareTo(walletWithdrawn.get()),
                "Successful withdraw should increase totalWithdrawn exactly once");

        verify(walletMapper, never()).insertTransaction(any());
    }

    // ========================================================================
    // Property 2d: Already terminal withdraw cannot be reverted to PROCESSING
    // Validates: Requirement 3.6
    // ========================================================================

    /**
     * Property: markWithdrawSuccess on an already SUCCESS withdraw → no state change
     */
    @Test
    @DisplayName("终态不可回退: status=1(SUCCESS) 的提现单调用 markWithdrawSuccess → 无变化")
    void alreadySuccessfulWithdrawCannotBeMarkedSuccessAgain() throws Exception {
        String outBatchNo = "WD10001234567890";
        Long uid = 10L;

        // Withdraw already in SUCCESS state
        WalletWithdraw withdraw = new WalletWithdraw();
        withdraw.setId(1L);
        withdraw.setUid(uid);
        withdraw.setAmount(new BigDecimal("50.00"));
        withdraw.setStatus(1); // SUCCESS
        withdraw.setOutBatchNo(outBatchNo);
        withdraw.setOutDetailNo("WDD10001234567890");

        when(walletMapper.selectWithdrawByOutBatchNo(outBatchNo)).thenReturn(withdraw);

        // Call sync — should return true immediately (already terminal)
        boolean result = service.syncWithdrawStatusByOutBatchNo(outBatchNo);
        assertTrue(result, "Already terminal withdraw should return true");

        // Should NOT attempt to query transfer or update wallet
        verify(walletTransferGateway, never()).queryTransfer(any(), any());
        verify(walletMapper, never()).updateWallet(any());
        verify(walletMapper, never()).insertTransaction(any());
        verify(walletMapper, never()).updateWithdrawStatus(any());
    }

    /**
     * Property: markWithdrawSuccess on an already FAILED withdraw → no state change
     */
    @Test
    @DisplayName("终态不可回退: status=2(FAILED) 的提现单调用 markWithdrawSuccess → 无变化")
    void alreadyFailedWithdrawCannotBeMarkedSuccess() throws Exception {
        String outBatchNo = "WD10001234567890";
        Long uid = 10L;

        // Withdraw already in FAILED state
        WalletWithdraw withdraw = new WalletWithdraw();
        withdraw.setId(1L);
        withdraw.setUid(uid);
        withdraw.setAmount(new BigDecimal("50.00"));
        withdraw.setStatus(2); // FAILED
        withdraw.setOutBatchNo(outBatchNo);
        withdraw.setOutDetailNo("WDD10001234567890");

        when(walletMapper.selectWithdrawByOutBatchNo(outBatchNo)).thenReturn(withdraw);

        // Call sync — should return true immediately (already terminal)
        boolean result = service.syncWithdrawStatusByOutBatchNo(outBatchNo);
        assertTrue(result, "Already terminal (FAILED) withdraw should return true");

        // Should NOT attempt to query transfer or update wallet
        verify(walletTransferGateway, never()).queryTransfer(any(), any());
        verify(walletMapper, never()).updateWallet(any());
        verify(walletMapper, never()).insertTransaction(any());
        verify(walletMapper, never()).updateWithdrawStatus(any());
    }

    /**
     * Property: markWithdrawFailed on an already SUCCESS withdraw → no state change
     * (Tests the internal markWithdrawFailed idempotency via syncWithdrawStatusByOutBatchNo
     * when query returns FAIL but withdraw is already SUCCESS)
     */
    @Test
    @DisplayName("终态不可回退: status=1(SUCCESS) 的提现单收到 FAIL 查询结果 → 不回退为失败")
    void alreadySuccessWithdrawCannotBeRevertedToFailed() throws Exception {
        String outBatchNo = "WD10001234567890";
        Long uid = 10L;

        // Withdraw already in SUCCESS state
        WalletWithdraw successWithdraw = new WalletWithdraw();
        successWithdraw.setId(1L);
        successWithdraw.setUid(uid);
        successWithdraw.setAmount(new BigDecimal("50.00"));
        successWithdraw.setStatus(1); // SUCCESS — already terminal
        successWithdraw.setOutBatchNo(outBatchNo);
        successWithdraw.setOutDetailNo("WDD10001234567890");

        when(walletMapper.selectWithdrawByOutBatchNo(outBatchNo)).thenReturn(successWithdraw);

        // syncWithdrawStatusByOutBatchNo checks status first — if not PROCESSING, returns true
        boolean result = service.syncWithdrawStatusByOutBatchNo(outBatchNo);
        assertTrue(result);

        // Verify no state changes occurred
        verify(walletMapper, never()).updateWithdrawStatus(any());
    }

    /**
     * Property: markWithdrawFailed on an already FAILED withdraw → no state change
     * (Direct test via a PROCESSING withdraw that queries as FAIL, then calling again)
     */
    @Test
    @DisplayName("终态不可回退: status=2(FAILED) 的提现单再次收到 FAIL → 不重复更新")
    void alreadyFailedWithdrawCannotBeMarkedFailedAgain() throws Exception {
        String outBatchNo = "WD10001234567890";
        Long uid = 10L;

        AtomicReference<Integer> withdrawStatus = new AtomicReference<>(0); // starts PROCESSING

        when(walletMapper.selectWithdrawByOutBatchNo(outBatchNo)).thenAnswer(inv -> {
            WalletWithdraw w = new WalletWithdraw();
            w.setId(1L);
            w.setUid(uid);
            w.setAmount(new BigDecimal("50.00"));
            w.setStatus(withdrawStatus.get());
            w.setOutBatchNo(outBatchNo);
            w.setOutDetailNo("WDD10001234567890");
            return w;
        });

        when(walletMapper.selectWithdrawById(1L)).thenAnswer(inv -> {
            WalletWithdraw w = new WalletWithdraw();
            w.setId(1L);
            w.setUid(uid);
            w.setAmount(new BigDecimal("50.00"));
            w.setStatus(withdrawStatus.get());
            w.setOutBatchNo(outBatchNo);
            w.setOutDetailNo("WDD10001234567890");
            return w;
        });

        WalletTransferQueryResult failDetail = new WalletTransferQueryResult();
        failDetail.setBatchId("wx-batch-1");
        failDetail.setDetailId("wx-detail-1");
        failDetail.setDetailStatus("FAIL");
        failDetail.setFailReason("REALNAME_CHECK_FAIL");
        when(walletTransferGateway.queryTransfer(outBatchNo, "WDD10001234567890")).thenReturn(failDetail);

        when(walletMapper.updateWithdrawStatus(any())).thenAnswer(inv -> {
            WalletWithdraw w = inv.getArgument(0);
            withdrawStatus.set(w.getStatus());
            return 1;
        });

        // First call — marks as FAILED
        boolean result1 = service.syncWithdrawStatusByOutBatchNo(outBatchNo);
        assertTrue(result1);
        assertEquals(Integer.valueOf(2), withdrawStatus.get(), "Should be marked FAILED after first call");

        // Reset mock invocation counts
        clearInvocations(walletMapper);

        // Second call — withdraw is now FAILED, should return true without changes
        boolean result2 = service.syncWithdrawStatusByOutBatchNo(outBatchNo);
        assertTrue(result2, "Second call on FAILED withdraw should return true");

        // Should NOT update status again
        verify(walletMapper, never()).updateWithdrawStatus(any());
        verify(walletMapper, never()).updateWallet(any());
    }

    // ========================================================================
    // Property 2e: syncWithdrawStatusByOutBatchNo with blank/null outBatchNo → returns false
    // Validates: Requirement 3.4 (callback path robustness)
    // ========================================================================

    @Test
    @DisplayName("syncWithdrawStatusByOutBatchNo: null outBatchNo → 返回 false")
    void syncWithNullOutBatchNoReturnsFalse() {
        boolean result = service.syncWithdrawStatusByOutBatchNo(null);
        assertFalse(result);
        verify(walletMapper, never()).selectWithdrawByOutBatchNo(any());
    }

    @Test
    @DisplayName("syncWithdrawStatusByOutBatchNo: 空字符串 outBatchNo → 返回 false")
    void syncWithBlankOutBatchNoReturnsFalse() {
        boolean result = service.syncWithdrawStatusByOutBatchNo("");
        assertFalse(result);
        verify(walletMapper, never()).selectWithdrawByOutBatchNo(any());
    }

    @Test
    @DisplayName("syncWithdrawStatusByOutBatchNo: 不存在的 outBatchNo → 返回 false")
    void syncWithNonExistentOutBatchNoReturnsFalse() {
        when(walletMapper.selectWithdrawByOutBatchNo("NONEXISTENT")).thenReturn(null);

        boolean result = service.syncWithdrawStatusByOutBatchNo("NONEXISTENT");
        assertFalse(result);
    }

    // ========================================================================
    // Helpers
    // ========================================================================

    /**
     * Generate a random BigDecimal amount with 2 decimal places in [min, max]
     */
    private BigDecimal generateRandomAmount(double min, double max) {
        double value = min + (max - min) * random.nextDouble();
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
