package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletTransaction;
import com.ruoyi.system.domain.WalletWithdraw;
import com.ruoyi.system.mapper.WalletMapper;
import com.ruoyi.system.service.IWalletTransferGateway;
import com.ruoyi.system.service.dto.WalletTransferCreateResult;
import com.ruoyi.system.service.dto.WalletTransferQueryResult;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletMapper walletMapper;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private IWalletTransferGateway walletTransferGateway;

    @InjectMocks
    private WalletServiceImpl service;

    @Test
    void resolveCurrentUserUidShouldReturnUserInfoId() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(8L);
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(userInfo);

        Long uid = service.resolveCurrentUserUid("wx-user-1");

        assertEquals(Long.valueOf(8L), uid);
    }

    @Test
    void shouldRejectWithdrawWhenUserNotVerified() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(10L);
        userInfo.setIsRealnameAuth(0);
        userInfo.setOpenId("openid-1");
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(userInfo);

        String result = service.applyWithdraw("wx-user-1", 10L, new BigDecimal("10.00"));

        assertEquals("请先完成实名认证后再提现", result);
        verify(walletMapper, never()).insertWithdraw(any());
    }

    @Test
    void shouldRejectWithdrawWhenOpenIdMissing() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(10L);
        userInfo.setIsRealnameAuth(1);
        userInfo.setRealName("张三");
        userInfo.setIdCard("110101199001010011");
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(userInfo);

        String result = service.applyWithdraw("wx-user-1", 10L, new BigDecimal("10.00"));

        assertEquals("未获取到微信账户信息，请重新登录后重试", result);
        verify(walletMapper, never()).insertWithdraw(any());
    }

    @Test
    void shouldCreateWechatTransferAndReturnSuccess() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(10L);
        userInfo.setIsRealnameAuth(1);
        userInfo.setRealName("张三");
        userInfo.setIdCard("110101199001010011");
        userInfo.setOpenId("openid-1");
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(userInfo);

        UserWallet wallet = new UserWallet();
        wallet.setUid(10L);
        wallet.setBalance(new BigDecimal("100.00"));
        wallet.setFrozen(BigDecimal.ZERO);
        wallet.setTotalEarned(new BigDecimal("100.00"));
        wallet.setTotalWithdrawn(BigDecimal.ZERO);
        when(walletMapper.selectWalletByUid(10L)).thenReturn(wallet);
        when(walletMapper.selectWalletByUidForUpdate(10L)).thenReturn(wallet);

        WalletTransferCreateResult createResult = new WalletTransferCreateResult();
        createResult.setBatchId("wx-batch-1");
        when(walletTransferGateway.createTransfer(any())).thenReturn(createResult);

        WalletTransferQueryResult detail = new WalletTransferQueryResult();
        detail.setBatchId("wx-batch-1");
        detail.setDetailId("wx-detail-1");
        detail.setDetailStatus("SUCCESS");
        when(walletTransferGateway.queryTransfer(any(), any())).thenReturn(detail);

        WalletWithdraw persistedWithdraw = new WalletWithdraw();
        persistedWithdraw.setId(1L);
        persistedWithdraw.setUid(10L);
        persistedWithdraw.setAmount(new BigDecimal("10.00"));
        persistedWithdraw.setStatus(0);

        ArgumentCaptor<WalletWithdraw> insertCaptor = ArgumentCaptor.forClass(WalletWithdraw.class);
        when(walletMapper.insertWithdraw(any())).thenAnswer(invocation -> {
            WalletWithdraw withdraw = invocation.getArgument(0);
            withdraw.setId(1L);
            persistedWithdraw.setId(withdraw.getId());
            persistedWithdraw.setUid(withdraw.getUid());
            persistedWithdraw.setAmount(withdraw.getAmount());
            persistedWithdraw.setStatus(withdraw.getStatus());
            persistedWithdraw.setRemark(withdraw.getRemark());
            persistedWithdraw.setOutBatchNo(withdraw.getOutBatchNo());
            persistedWithdraw.setOutDetailNo(withdraw.getOutDetailNo());
            persistedWithdraw.setWxTransferNo(withdraw.getWxTransferNo());
            persistedWithdraw.setWxDetailNo(withdraw.getWxDetailNo());
            return 1;
        });
        when(walletMapper.selectWithdrawById(1L)).thenAnswer(invocation -> {
            WalletWithdraw withdraw = new WalletWithdraw();
            withdraw.setId(persistedWithdraw.getId());
            withdraw.setUid(persistedWithdraw.getUid());
            withdraw.setAmount(persistedWithdraw.getAmount());
            withdraw.setStatus(persistedWithdraw.getStatus());
            withdraw.setRemark(persistedWithdraw.getRemark());
            withdraw.setOutBatchNo(persistedWithdraw.getOutBatchNo());
            withdraw.setOutDetailNo(persistedWithdraw.getOutDetailNo());
            withdraw.setWxTransferNo(persistedWithdraw.getWxTransferNo());
            withdraw.setWxDetailNo(persistedWithdraw.getWxDetailNo());
            return withdraw;
        });
        when(walletMapper.updateWithdrawStatus(any())).thenAnswer(invocation -> {
            WalletWithdraw withdraw = invocation.getArgument(0);
            persistedWithdraw.setStatus(withdraw.getStatus());
            persistedWithdraw.setRemark(withdraw.getRemark());
            persistedWithdraw.setOutBatchNo(withdraw.getOutBatchNo());
            persistedWithdraw.setOutDetailNo(withdraw.getOutDetailNo());
            persistedWithdraw.setWxTransferNo(withdraw.getWxTransferNo());
            persistedWithdraw.setWxDetailNo(withdraw.getWxDetailNo());
            return 1;
        });

        String result = service.applyWithdraw("wx-user-1", 10L, new BigDecimal("10.00"));

        assertEquals("微信提现成功", result);
        verify(walletMapper).insertWithdraw(insertCaptor.capture());
        verify(walletMapper, atLeastOnce()).updateWithdrawStatus(any());
        assertTrue(insertCaptor.getValue().getOutBatchNo().startsWith("WD"));
        verify(walletMapper).updateWallet(any(UserWallet.class));
        verify(walletMapper).insertTransaction(any(WalletTransaction.class));
    }

    @Test
    void creditPayrollShouldIncreaseBalanceAndInsertTransaction() {
        UserWallet wallet = new UserWallet();
        wallet.setUid(8L);
        wallet.setBalance(new BigDecimal("10.00"));
        wallet.setFrozen(BigDecimal.ZERO);
        wallet.setTotalEarned(new BigDecimal("30.00"));
        wallet.setTotalWithdrawn(new BigDecimal("5.00"));
        when(walletMapper.selectWalletByUidForUpdate(8L)).thenReturn(wallet);

        service.creditPayroll(8L, new BigDecimal("20.00"), "PAY-1", "兼职工资入账");

        assertEquals(new BigDecimal("30.00"), wallet.getBalance());
        assertEquals(new BigDecimal("50.00"), wallet.getTotalEarned());
        verify(walletMapper).updateWallet(wallet);
        verify(walletMapper).insertTransaction(any(WalletTransaction.class));
    }
}
