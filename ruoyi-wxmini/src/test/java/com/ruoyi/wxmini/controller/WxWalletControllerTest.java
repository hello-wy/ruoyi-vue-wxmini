package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletTransaction;
import com.ruoyi.system.domain.WalletWithdraw;
import com.ruoyi.system.service.IWalletService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxWalletControllerTest {

    @Mock
    private IWalletService walletService;

    @InjectMocks
    private WxWalletController controller;

    @AfterEach
    void clearContext() {
        WxMiniUserContext.clear();
    }

    @Test
    void infoShouldReturn200() {
        UserWallet wallet = new UserWallet();
        wallet.setUid(10L);
        wallet.setBalance(new BigDecimal("88.50"));
        WxMiniUserContext.setCurrentUserId("wx-user-1");
        when(walletService.getOrCreateWallet(10L)).thenReturn(wallet);
        when(walletService.resolveCurrentUserUid("wx-user-1")).thenReturn(10L);

        AjaxResult result = controller.info();
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void withdrawShouldReturnNon200WhenAmountMissing() {
        WxMiniUserContext.setCurrentUserId("wx-user-1");
        when(walletService.resolveCurrentUserUid("wx-user-1")).thenReturn(10L);
        AjaxResult result = controller.withdraw(new HashMap<>());
        assertEquals(false, Integer.valueOf(200).equals(result.get(AjaxResult.CODE_TAG)));
    }

    @Test
    void withdrawShouldReturnNon200WhenServiceRejects() {
        Map<String, Object> body = new HashMap<>();
        body.put("amount", "50.00");
        WxMiniUserContext.setCurrentUserId("wx-user-1");
        when(walletService.resolveCurrentUserUid("wx-user-1")).thenReturn(10L);
        when(walletService.applyWithdraw(10L, new BigDecimal("50.00"))).thenReturn("余额不足");

        AjaxResult result = controller.withdraw(body);
        assertEquals(false, Integer.valueOf(200).equals(result.get(AjaxResult.CODE_TAG)));
    }

    @Test
    void withdrawShouldReturn200WhenServiceAccepts() {
        Map<String, Object> body = new HashMap<>();
        body.put("amount", "50.00");
        WxMiniUserContext.setCurrentUserId("wx-user-1");
        when(walletService.resolveCurrentUserUid("wx-user-1")).thenReturn(10L);
        when(walletService.applyWithdraw(10L, new BigDecimal("50.00"))).thenReturn("提现申请已提交，请等待审核");

        AjaxResult result = controller.withdraw(body);
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void withdrawRecordsShouldReturn200() {
        WalletWithdraw record = new WalletWithdraw();
        record.setUid(10L);
        WxMiniUserContext.setCurrentUserId("wx-user-1");
        when(walletService.resolveCurrentUserUid("wx-user-1")).thenReturn(10L);
        when(walletService.getWithdrawRecords(10L)).thenReturn(Collections.singletonList(record));

        AjaxResult result = controller.withdrawRecords();
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void transactionsShouldReturn200() {
        WalletTransaction transaction = new WalletTransaction();
        transaction.setUid(10L);
        transaction.setBizType("PAYROLL");
        WxMiniUserContext.setCurrentUserId("wx-user-1");
        when(walletService.resolveCurrentUserUid("wx-user-1")).thenReturn(10L);
        when(walletService.getTransactions(10L)).thenReturn(Collections.singletonList(transaction));

        AjaxResult result = controller.transactions();
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }
}
