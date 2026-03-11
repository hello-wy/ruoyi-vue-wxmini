package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletWithdraw;
import org.apache.ibatis.annotations.Param;

/**
 * 用户钱包 Mapper 接口
 *
 * @author ruoyi
 */
public interface WalletMapper
{
    /** 根据用户ID查询钱包，不存在返回null */
    UserWallet selectWalletByUid(Long uid);

    /** 初始化用户钱包（首次访问自动创建） */
    int insertWallet(UserWallet wallet);

    /** 更新钱包余额 */
    int updateWallet(UserWallet wallet);

    // ---- 提现记录 ----

    /** 新增提现申请 */
    int insertWithdraw(WalletWithdraw withdraw);

    /** 查询提现记录列表（按用户ID） */
    List<WalletWithdraw> selectWithdrawListByUid(@Param("uid") Long uid);

    /** 查询提现记录详情 */
    WalletWithdraw selectWithdrawById(Long id);

    /** 更新提现记录状态 */
    int updateWithdrawStatus(WalletWithdraw withdraw);
}
