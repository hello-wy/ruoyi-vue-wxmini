package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletWithdraw;
import com.ruoyi.system.mapper.WalletMapper;
import com.ruoyi.system.service.IWalletService;

/**
 * 用户钱包 Service 业务层
 *
 * @author ruoyi
 */
@Service
public class WalletServiceImpl implements IWalletService
{
    private static final BigDecimal MIN_WITHDRAW = new BigDecimal("1.00");

    @Autowired
    private WalletMapper walletMapper;

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
        // 扣减余额，增加冻结金额
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setFrozen(wallet.getFrozen().add(amount));
        walletMapper.updateWallet(wallet);

        // 创建提现申请记录
        WalletWithdraw withdraw = new WalletWithdraw();
        withdraw.setUid(uid);
        withdraw.setAmount(amount);
        withdraw.setStatus(0); // 审核中
        walletMapper.insertWithdraw(withdraw);

        return "提现申请已提交，预计1-3个工作日到账";
    }

    @Override
    public List<WalletWithdraw> getWithdrawRecords(Long uid)
    {
        return walletMapper.selectWithdrawListByUid(uid);
    }
}
