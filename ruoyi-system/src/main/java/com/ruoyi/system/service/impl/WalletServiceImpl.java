package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletTransaction;
import com.ruoyi.system.domain.WalletWithdraw;
import com.ruoyi.system.mapper.WalletMapper;
import com.ruoyi.system.service.IWalletService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletServiceImpl implements IWalletService
{
    private static final BigDecimal MIN_WITHDRAW = new BigDecimal("1.00");
    private static final Integer DIRECTION_INCOME = 1;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public UserWallet getOrCreateWallet(Long uid)
    {
        UserWallet wallet = walletMapper.selectWalletByUid(uid);
        if (wallet == null)
        {
            wallet = new UserWallet();
            wallet.setUid(uid);
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setFrozen(BigDecimal.ZERO);
            wallet.setTotalEarned(BigDecimal.ZERO);
            wallet.setTotalWithdrawn(BigDecimal.ZERO);
            walletMapper.insertWallet(wallet);
        }
        return wallet;
    }

    @Override
    public Long resolveCurrentUserUid(String userId)
    {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null || userInfo.getId() == null)
        {
            throw new ServiceException("用户不存在");
        }
        return userInfo.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String applyWithdraw(Long uid, BigDecimal amount)
    {
        if (amount == null || amount.compareTo(MIN_WITHDRAW) < 0)
        {
            return "提现金额不能低于1元";
        }
        UserWallet wallet = getOrCreateWallet(uid);
        if (wallet.getBalance().compareTo(amount) < 0)
        {
            return "可用余额不足";
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setFrozen(wallet.getFrozen().add(amount));
        walletMapper.updateWallet(wallet);

        WalletWithdraw withdraw = new WalletWithdraw();
        withdraw.setUid(uid);
        withdraw.setAmount(amount);
        withdraw.setStatus(0);
        walletMapper.insertWithdraw(withdraw);

        return "提现申请已提交，预计1-3个工作日到账";
    }

    @Override
    public List<WalletWithdraw> getWithdrawRecords(Long uid)
    {
        return walletMapper.selectWithdrawListByUid(uid);
    }

    @Override
    public List<WalletTransaction> getTransactions(Long uid)
    {
        return walletMapper.selectTransactionListByUid(uid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void creditPayroll(Long uid, BigDecimal amount, String bizId, String remark)
    {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new ServiceException("工资金额必须大于0");
        }
        UserWallet wallet = walletMapper.selectWalletByUidForUpdate(uid);
        if (wallet == null)
        {
            getOrCreateWallet(uid);
            wallet = walletMapper.selectWalletByUidForUpdate(uid);
        }
        if (wallet == null)
        {
            throw new ServiceException("钱包初始化失败");
        }
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setTotalEarned(wallet.getTotalEarned().add(amount));
        walletMapper.updateWallet(wallet);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setId(SnowflakeIdWorker.nextIdDefault());
        transaction.setUid(uid);
        transaction.setBizType("PAYROLL");
        transaction.setBizId(bizId);
        transaction.setDirection(DIRECTION_INCOME);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(wallet.getBalance());
        transaction.setRemark(remark);
        transaction.setCreateTime(DateUtils.getNowDate());
        walletMapper.insertTransaction(transaction);
    }
}
