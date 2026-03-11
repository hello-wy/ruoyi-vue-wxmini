package com.ruoyi.system.service;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletWithdraw;

/**
 * 用户钱包 Service 接口
 *
 * @author ruoyi
 */
public interface IWalletService
{
    /**
     * 获取用户钱包（不存在则自动初始化）
     *
     * @param uid 用户ID
     * @return 钱包信息
     */
    UserWallet getOrCreateWallet(Long uid);

    /**
     * 申请提现到微信钱包
     *
     * @param uid    用户ID
     * @param amount 提现金额
     * @return 操作结果消息
     */
    String applyWithdraw(Long uid, BigDecimal amount);

    /**
     * 查询提现记录列表
     *
     * @param uid 用户ID
     * @return 提现记录
     */
    List<WalletWithdraw> getWithdrawRecords(Long uid);
}
