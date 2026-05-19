package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletTransaction;
import com.ruoyi.system.domain.WalletWithdraw;
import org.apache.ibatis.annotations.Param;

public interface WalletMapper
{
    UserWallet selectWalletByUid(Long uid);

    UserWallet selectWalletByUidForUpdate(Long uid);

    int insertWallet(UserWallet wallet);

    int updateWallet(UserWallet wallet);

    int insertWithdraw(WalletWithdraw withdraw);

    List<WalletWithdraw> selectWithdrawListByUid(@Param("uid") Long uid);

    WalletWithdraw selectWithdrawById(Long id);

    WalletWithdraw selectWithdrawByOutBatchNo(@Param("outBatchNo") String outBatchNo);

    int updateWithdrawStatus(WalletWithdraw withdraw);

    int insertTransaction(WalletTransaction transaction);

    List<WalletTransaction> selectTransactionListByUid(@Param("uid") Long uid);

    int selectPendingWithdrawCountByUid(@Param("uid") Long uid);

    /**
     * 查询待对账的提现记录：status=0 且 create_time 早于3分钟前
     */
    List<WalletWithdraw> selectPendingWithdrawsForReconcile();
}
