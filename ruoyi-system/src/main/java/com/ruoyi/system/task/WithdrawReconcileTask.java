package com.ruoyi.system.task;

import com.ruoyi.system.domain.WalletWithdraw;
import com.ruoyi.system.enums.WithdrawFailType;
import com.ruoyi.system.mapper.WalletMapper;
import com.ruoyi.system.service.IWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 提现对账定时任务
 * 每5分钟扫描处理中的提现记录，主动查询微信侧状态并更新
 * 超过24小时仍未到终态的标记为超时失败
 */
@Component
public class WithdrawReconcileTask
{
    private static final Logger log = LoggerFactory.getLogger(WithdrawReconcileTask.class);

    /** 24小时超时阈值（毫秒） */
    private static final long TIMEOUT_MILLIS = 24 * 60 * 60 * 1000L;

    private static final Integer WITHDRAW_STATUS_FAILED = 2;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private IWalletService walletService;

    /**
     * 每5分钟执行一次对账
     * 扫描 status=0 且 create_time 早于3分钟前的提现记录
     */
    @Scheduled(fixedRate = 300000)
    public void reconcile()
    {
        List<WalletWithdraw> pendingList = walletMapper.selectPendingWithdrawsForReconcile();
        if (pendingList == null || pendingList.isEmpty())
        {
            return;
        }

        int scanned = pendingList.size();
        int updated = 0;
        int timedOut = 0;

        for (WalletWithdraw withdraw : pendingList)
        {
            try
            {
                if (isTimedOut(withdraw))
                {
                    markReconcileTimeout(withdraw);
                    timedOut++;
                    updated++;
                }
                else
                {
                    boolean synced = walletService.syncWithdrawStatusByOutBatchNo(withdraw.getOutBatchNo());
                    if (synced)
                    {
                        updated++;
                    }
                }
            }
            catch (Exception e)
            {
                log.warn("[WithdrawReconcile] Failed to reconcile withdrawId={}, outBatchNo={}: {}",
                        withdraw.getId(), withdraw.getOutBatchNo(), e.getMessage());
            }
        }

        log.info("Withdraw reconcile: scanned={}, updated={}, timedOut={}", scanned, updated, timedOut);
    }

    /**
     * 判断提现记录是否超过24小时仍为PROCESSING
     */
    private boolean isTimedOut(WalletWithdraw withdraw)
    {
        Date applyTime = withdraw.getApplyTime();
        if (applyTime == null)
        {
            return false;
        }
        long elapsed = System.currentTimeMillis() - applyTime.getTime();
        return elapsed > TIMEOUT_MILLIS;
    }

    /**
     * 标记超时失败
     */
    private void markReconcileTimeout(WalletWithdraw withdraw)
    {
        withdraw.setStatus(WITHDRAW_STATUS_FAILED);
        withdraw.setFailType(WithdrawFailType.RECONCILE_TIMEOUT.name());
        withdraw.setUserMessage(WithdrawFailType.RECONCILE_TIMEOUT.getUserMessage());
        withdraw.setRemark("提现超时未完成（超过24小时），系统自动标记失败");
        walletMapper.updateWithdrawStatus(withdraw);

        log.info("[WithdrawReconcile] Marked timeout: withdrawId={}, uid={}, outBatchNo={}",
                withdraw.getId(), withdraw.getUid(), withdraw.getOutBatchNo());
    }
}
