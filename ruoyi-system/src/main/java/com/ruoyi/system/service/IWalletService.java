package com.ruoyi.system.service;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletTransaction;
import com.ruoyi.system.domain.WalletWithdraw;

public interface IWalletService
{
    UserWallet getOrCreateWallet(Long uid);

    Long resolveCurrentUserUid(String userId);

    String applyWithdraw(Long uid, BigDecimal amount);

    List<WalletWithdraw> getWithdrawRecords(Long uid);

    List<WalletTransaction> getTransactions(Long uid);

    void creditPayroll(Long uid, BigDecimal amount, String bizId, String remark);
}
