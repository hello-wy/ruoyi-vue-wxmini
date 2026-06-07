package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletTransaction;
import com.ruoyi.system.domain.WalletWithdraw;
import com.ruoyi.system.mapper.WalletMapper;
import com.ruoyi.system.service.IWalletTransferGateway;
import com.ruoyi.system.service.dto.WalletTransferCreateResult;
import com.ruoyi.system.service.dto.WalletTransferQueryResult;
import com.ruoyi.system.service.dto.WithdrawResult;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletWithdrawProcessingTest {
    @Mock
    private WalletMapper walletMapper;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private IWalletTransferGateway walletTransferGateway;

    @InjectMocks
    private WalletServiceImpl service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "transferMinAmount", new BigDecimal("1.00"));
        ReflectionTestUtils.setField(service, "transferMaxAmount", new BigDecimal("2000.00"));
        ReflectionTestUtils.setField(service, "transferBatchName", "钱包提现");
        ReflectionTestUtils.setField(service, "transferUserRecvPerception", "劳务报酬");
    }

    @Test
    void applyWithdrawShouldFreezeBalanceWhenTransferIsProcessing() throws Exception {
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(verifiedUser());
        UserWallet wallet = walletWithState("100.00", "0.00", "0.00");
        when(walletMapper.selectWalletByUid(10L)).thenReturn(wallet);
        when(walletMapper.selectWalletByUidForUpdate(10L)).thenReturn(wallet);
        when(walletTransferGateway.createTransfer(any())).thenReturn(processingCreateResult());
        when(walletTransferGateway.queryTransfer(any(), any())).thenReturn(processingQueryResult());
        when(walletMapper.insertWithdraw(any())).thenAnswer(invocation -> {
            WalletWithdraw withdraw = invocation.getArgument(0);
            withdraw.setId(1L);
            return 1;
        });

        WithdrawResult result = service.applyWithdraw("wx-user-1", 10L, new BigDecimal("10.00"));

        assertTrue(result.isSuccess());
        assertEquals(Integer.valueOf(0), result.getStatus());
        assertEquals(new BigDecimal("90.00"), wallet.getBalance());
        assertEquals(new BigDecimal("10.00"), wallet.getFrozen());
        assertEquals(new BigDecimal("0.00"), wallet.getTotalWithdrawn());
        verify(walletMapper).updateWallet(wallet);
        verify(walletMapper).insertTransaction(any(WalletTransaction.class));
    }

    @Test
    void getWithdrawRecordsShouldSyncPendingRecordsBeforeReturning() throws Exception {
        WalletWithdraw pending = withdraw(1L, 0);
        WalletWithdraw success = withdraw(1L, 1);
        when(walletMapper.selectWithdrawListByUid(10L))
                .thenReturn(Collections.singletonList(pending))
                .thenReturn(Collections.singletonList(success));
        when(walletMapper.selectWithdrawById(1L)).thenReturn(pending);
        UserWallet wallet = walletWithState("90.00", "10.00", "0.00");
        when(walletMapper.selectWalletByUidForUpdate(10L)).thenReturn(wallet);
        when(walletMapper.selectTransactionByBiz("WITHDRAW", "1")).thenReturn(withdrawTransaction());
        when(walletTransferGateway.queryTransfer("WD1001", "WDD1001")).thenReturn(successQueryResult());

        List<WalletWithdraw> records = service.getWithdrawRecords(10L);

        assertEquals(1, records.size());
        assertEquals(Integer.valueOf(1), records.get(0).getStatus());
        assertEquals(new BigDecimal("90.00"), wallet.getBalance());
        assertEquals(new BigDecimal("0.00"), wallet.getFrozen());
        assertEquals(new BigDecimal("10.00"), wallet.getTotalWithdrawn());
        verify(walletTransferGateway).queryTransfer("WD1001", "WDD1001");
        verify(walletMapper, atLeastOnce()).updateWithdrawStatus(any(WalletWithdraw.class));
    }

    @Test
    void getWithdrawRecordsShouldReleaseFrozenAmountWhenTransferFails() throws Exception {
        WalletWithdraw pending = withdraw(1L, 0);
        WalletWithdraw failed = withdraw(1L, 2);
        when(walletMapper.selectWithdrawListByUid(10L))
                .thenReturn(Collections.singletonList(pending))
                .thenReturn(Collections.singletonList(failed));
        when(walletMapper.selectWithdrawById(1L)).thenReturn(pending);
        UserWallet wallet = walletWithState("90.00", "10.00", "0.00");
        when(walletMapper.selectWalletByUidForUpdate(10L)).thenReturn(wallet);
        when(walletMapper.selectTransactionByBiz("WITHDRAW", "1")).thenReturn(withdrawTransaction());
        when(walletTransferGateway.queryTransfer("WD1001", "WDD1001")).thenReturn(failedQueryResult());

        List<WalletWithdraw> records = service.getWithdrawRecords(10L);

        assertEquals(1, records.size());
        assertEquals(Integer.valueOf(2), records.get(0).getStatus());
        assertEquals(new BigDecimal("100.00"), wallet.getBalance());
        assertEquals(new BigDecimal("0.00"), wallet.getFrozen());
        assertEquals(new BigDecimal("0.00"), wallet.getTotalWithdrawn());
    }

    private UserInfo verifiedUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(10L);
        userInfo.setIsRealnameAuth(1);
        userInfo.setRealName("张三");
        userInfo.setIdCard("110101199001010011");
        userInfo.setOpenId("openid-1");
        return userInfo;
    }

    private UserWallet walletWithState(String balance, String frozen, String totalWithdrawn) {
        UserWallet wallet = new UserWallet();
        wallet.setUid(10L);
        wallet.setBalance(new BigDecimal(balance));
        wallet.setFrozen(new BigDecimal(frozen));
        wallet.setTotalEarned(new BigDecimal("100.00"));
        wallet.setTotalWithdrawn(new BigDecimal(totalWithdrawn));
        return wallet;
    }

    private WalletTransferCreateResult processingCreateResult() {
        WalletTransferCreateResult result = new WalletTransferCreateResult();
        result.setBatchId("wx-batch-1");
        result.setState("WAIT_USER_CONFIRM");
        result.setPackageInfo("package-1");
        return result;
    }

    private WalletTransferQueryResult processingQueryResult() {
        WalletTransferQueryResult result = new WalletTransferQueryResult();
        result.setDetailStatus("WAIT_USER_CONFIRM");
        return result;
    }

    private WalletTransferQueryResult successQueryResult() {
        WalletTransferQueryResult result = new WalletTransferQueryResult();
        result.setBatchId("wx-batch-1");
        result.setDetailId("wx-detail-1");
        result.setDetailStatus("SUCCESS");
        return result;
    }

    private WalletTransferQueryResult failedQueryResult() {
        WalletTransferQueryResult result = new WalletTransferQueryResult();
        result.setBatchId("wx-batch-1");
        result.setDetailId("wx-detail-1");
        result.setDetailStatus("FAIL");
        result.setFailReason("SYSTEMERROR");
        return result;
    }

    private WalletTransaction withdrawTransaction() {
        WalletTransaction transaction = new WalletTransaction();
        transaction.setUid(10L);
        transaction.setBizType("WITHDRAW");
        transaction.setBizId("1");
        transaction.setDirection(2);
        transaction.setAmount(new BigDecimal("10.00"));
        transaction.setBalanceAfter(new BigDecimal("90.00"));
        return transaction;
    }

    private WalletWithdraw withdraw(Long id, Integer status) {
        WalletWithdraw withdraw = new WalletWithdraw();
        withdraw.setId(id);
        withdraw.setUid(10L);
        withdraw.setAmount(new BigDecimal("10.00"));
        withdraw.setStatus(status);
        withdraw.setOutBatchNo("WD1001");
        withdraw.setOutDetailNo("WDD1001");
        return withdraw;
    }
}
