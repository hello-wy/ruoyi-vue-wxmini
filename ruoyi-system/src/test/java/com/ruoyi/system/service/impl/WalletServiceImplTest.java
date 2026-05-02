package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletTransaction;
import com.ruoyi.system.mapper.WalletMapper;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletMapper walletMapper;

    @Mock
    private IUserInfoService userInfoService;

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
    void creditPayrollShouldIncreaseBalanceAndInsertTransaction() {
        UserWallet wallet = new UserWallet();
        wallet.setUid(8L);
        wallet.setBalance(new BigDecimal("10.00"));
        wallet.setFrozen(BigDecimal.ZERO);
        wallet.setTotalEarned(new BigDecimal("30.00"));
        wallet.setTotalWithdrawn(BigDecimal.ZERO);
        when(walletMapper.selectWalletByUidForUpdate(8L)).thenReturn(wallet);

        service.creditPayroll(8L, new BigDecimal("50.00"), "BATCH-1", "工资入账");

        assertEquals(new BigDecimal("60.00"), wallet.getBalance());
        assertEquals(new BigDecimal("80.00"), wallet.getTotalEarned());
        verify(walletMapper).updateWallet(wallet);
        verify(walletMapper).insertTransaction(any(WalletTransaction.class));
    }
}
