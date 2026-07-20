package com.ruoyi.system.service;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletTransaction;
import com.ruoyi.system.domain.WalletWithdraw;
import com.ruoyi.system.service.dto.WithdrawResult;

public interface IWalletService
{
    UserWallet getOrCreateWallet(Long uid);

    Long resolveCurrentUserUid(String userId);

    WithdrawResult applyWithdraw(String userId, Long uid, BigDecimal amount);

    boolean syncWithdrawStatusByOutBatchNo(String outBatchNo);

    List<WalletWithdraw> getWithdrawRecords(Long uid);

    List<WalletTransaction> getTransactions(Long uid);

    void creditPayroll(Long uid, BigDecimal amount, String bizId, String remark);

    void creditReferralReward(Long uid, BigDecimal amount, String bizId);
}
