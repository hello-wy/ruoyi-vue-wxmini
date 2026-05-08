package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletTransaction;
import com.ruoyi.system.domain.WalletWithdraw;
import com.ruoyi.system.mapper.WalletMapper;
import com.ruoyi.system.service.IWalletService;
import com.ruoyi.system.service.IWalletTransferGateway;
import com.ruoyi.system.service.dto.WalletTransferCreateRequest;
import com.ruoyi.system.service.dto.WalletTransferCreateResult;
import com.ruoyi.system.service.dto.WalletTransferQueryResult;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletServiceImpl implements IWalletService
{
    private static final BigDecimal MIN_WITHDRAW = new BigDecimal("1.00");
    private static final Integer DIRECTION_INCOME = 1;
    private static final Integer DIRECTION_EXPENSE = 2;
    private static final Integer WITHDRAW_STATUS_PROCESSING = 0;
    private static final Integer WITHDRAW_STATUS_SUCCESS = 1;
    private static final Integer WITHDRAW_STATUS_FAILED = 2;
    private static final Integer REALNAME_VERIFIED = 1;
    private static final String WITHDRAW_BIZ_TYPE = "WITHDRAW";
    private static final String WITHDRAW_REMARK = "微信提现";
    private static final String TRANSFER_NOTIFY_URL = "https://zhiyujia.xyz/api/wxmini/pay/wallet/notify";
    private static final String TRANSFER_SCENE_ID = "1005";
    private static final String DETAIL_STATUS_SUCCESS = "SUCCESS";
    private static final String DETAIL_STATUS_FAILED = "FAIL";

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IWalletTransferGateway walletTransferGateway;

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
    public String applyWithdraw(String userId, Long uid, BigDecimal amount)
    {
        if (amount == null || amount.compareTo(MIN_WITHDRAW) < 0)
        {
            return "提现金额不能低于1元";
        }
        if (amount.scale() > 2)
        {
            return "提现金额最多保留两位小数";
        }
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null)
        {
            return "用户不存在";
        }
        if (!REALNAME_VERIFIED.equals(userInfo.getIsRealnameAuth()) || StringUtils.isAnyBlank(userInfo.getRealName(), userInfo.getIdCard()))
        {
            return "请先完成实名认证后再提现";
        }
        if (StringUtils.isBlank(userInfo.getOpenId()))
        {
            return "未获取到微信账户信息，请重新登录后重试";
        }

        UserWallet wallet = getOrCreateWallet(uid);
        if (wallet.getBalance().compareTo(amount) < 0)
        {
            return "可用余额不足";
        }

        String outBatchNo = buildOutBatchNo(uid);
        String outDetailNo = buildOutDetailNo(uid);
        WalletWithdraw withdraw = new WalletWithdraw();
        withdraw.setUid(uid);
        withdraw.setAmount(amount);
        withdraw.setStatus(WITHDRAW_STATUS_PROCESSING);
        withdraw.setRemark("微信提现处理中");
        withdraw.setOutBatchNo(outBatchNo);
        withdraw.setOutDetailNo(outDetailNo);
        walletMapper.insertWithdraw(withdraw);

        try
        {
            WalletTransferCreateResult result = walletTransferGateway.createTransfer(buildTransferRequest(userInfo, amount, outBatchNo, outDetailNo));
            withdraw.setWxTransferNo(result.getBatchId());
            walletMapper.updateWithdrawStatus(withdraw);

            return syncWithdrawStatus(withdraw.getId());
        }
        catch (Exception e)
        {
            markWithdrawFailed(withdraw.getId(), e.getMessage(), null, null, null);
            return StringUtils.defaultIfBlank(e.getMessage(), "微信提现发起失败，请稍后重试");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncWithdrawStatusByOutBatchNo(String outBatchNo)
    {
        if (StringUtils.isBlank(outBatchNo))
        {
            return false;
        }
        WalletWithdraw withdraw = walletMapper.selectWithdrawByOutBatchNo(outBatchNo);
        if (withdraw == null)
        {
            return false;
        }
        if (!WITHDRAW_STATUS_PROCESSING.equals(withdraw.getStatus()))
        {
            return true;
        }
        try
        {
            return updateWithdrawByDetailQuery(withdraw);
        }
        catch (Exception e)
        {
            return false;
        }
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

    private WalletTransferCreateRequest buildTransferRequest(UserInfo userInfo, BigDecimal amount, String outBatchNo, String outDetailNo)
    {
        WalletTransferCreateRequest request = new WalletTransferCreateRequest();
        request.setOpenId(userInfo.getOpenId());
        request.setRealName(userInfo.getRealName());
        request.setAmount(amount);
        request.setOutBatchNo(outBatchNo);
        request.setOutDetailNo(outDetailNo);
        request.setBatchName("钱包提现");
        request.setBatchRemark(WITHDRAW_REMARK);
        request.setTransferRemark(WITHDRAW_REMARK);
        request.setNotifyUrl(TRANSFER_NOTIFY_URL);
        request.setTransferSceneId(TRANSFER_SCENE_ID);
        return request;
    }

    private String syncWithdrawStatus(Long withdrawId)
    {
        WalletWithdraw withdraw = walletMapper.selectWithdrawById(withdrawId);
        if (withdraw == null)
        {
            return "微信提现已发起，请稍后刷新查看结果";
        }
        try
        {
            boolean updated = updateWithdrawByDetailQuery(withdraw);
            WalletWithdraw latest = walletMapper.selectWithdrawById(withdrawId);
            if (!updated || latest == null)
            {
                return "微信提现已发起，请稍后刷新查看结果";
            }
            if (WITHDRAW_STATUS_SUCCESS.equals(latest.getStatus()))
            {
                return "微信提现成功";
            }
            if (WITHDRAW_STATUS_FAILED.equals(latest.getStatus()))
            {
                return StringUtils.defaultIfBlank(latest.getRemark(), "微信提现失败，请稍后重试");
            }
            return "微信提现已发起，请稍后刷新查看结果";
        }
        catch (Exception e)
        {
            return "微信提现已发起，请稍后刷新查看结果";
        }
    }

    @Transactional(rollbackFor = Exception.class)
    protected boolean updateWithdrawByDetailQuery(WalletWithdraw withdraw) throws Exception
    {
        WalletTransferQueryResult detail = walletTransferGateway.queryTransfer(withdraw.getOutBatchNo(), withdraw.getOutDetailNo());
        if (detail == null || StringUtils.isBlank(detail.getDetailStatus()))
        {
            return false;
        }
        if (DETAIL_STATUS_SUCCESS.equals(detail.getDetailStatus()))
        {
            markWithdrawSuccess(withdraw, detail.getBatchId(), detail.getDetailId());
            return true;
        }
        if (DETAIL_STATUS_FAILED.equals(detail.getDetailStatus()))
        {
            markWithdrawFailed(withdraw.getId(), detail.getFailReason(), detail.getBatchId(), detail.getOutBatchNo(), detail.getDetailId());
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    protected void markWithdrawSuccess(WalletWithdraw withdraw, String batchId, String detailId)
    {
        WalletWithdraw latest = walletMapper.selectWithdrawById(withdraw.getId());
        if (latest == null || WITHDRAW_STATUS_SUCCESS.equals(latest.getStatus()))
        {
            return;
        }
        if (WITHDRAW_STATUS_FAILED.equals(latest.getStatus()))
        {
            return;
        }
        UserWallet wallet = walletMapper.selectWalletByUidForUpdate(latest.getUid());
        if (wallet == null)
        {
            throw new ServiceException("钱包不存在");
        }
        if (wallet.getBalance().compareTo(latest.getAmount()) < 0)
        {
            throw new ServiceException("钱包余额不足，无法完成提现扣款");
        }
        wallet.setBalance(wallet.getBalance().subtract(latest.getAmount()));
        wallet.setTotalWithdrawn(wallet.getTotalWithdrawn().add(latest.getAmount()));
        walletMapper.updateWallet(wallet);

        latest.setStatus(WITHDRAW_STATUS_SUCCESS);
        latest.setRemark("微信提现成功");
        latest.setWxTransferNo(batchId);
        latest.setWxDetailNo(detailId);
        walletMapper.updateWithdrawStatus(latest);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setId(SnowflakeIdWorker.nextIdDefault());
        transaction.setUid(latest.getUid());
        transaction.setBizType(WITHDRAW_BIZ_TYPE);
        transaction.setBizId(String.valueOf(latest.getId()));
        transaction.setDirection(DIRECTION_EXPENSE);
        transaction.setAmount(latest.getAmount());
        transaction.setBalanceAfter(wallet.getBalance());
        transaction.setRemark(WITHDRAW_REMARK);
        transaction.setCreateTime(DateUtils.getNowDate());
        walletMapper.insertTransaction(transaction);
    }

    @Transactional(rollbackFor = Exception.class)
    protected void markWithdrawFailed(Long withdrawId, String reason, String batchId, String outBatchNo, String detailId)
    {
        WalletWithdraw withdraw = walletMapper.selectWithdrawById(withdrawId);
        if (withdraw == null || WITHDRAW_STATUS_FAILED.equals(withdraw.getStatus()) || WITHDRAW_STATUS_SUCCESS.equals(withdraw.getStatus()))
        {
            return;
        }
        withdraw.setStatus(WITHDRAW_STATUS_FAILED);
        withdraw.setRemark(StringUtils.defaultIfBlank(reason, "微信提现失败，请稍后重试"));
        if (StringUtils.isNotBlank(batchId))
        {
            withdraw.setWxTransferNo(batchId);
        }
        if (StringUtils.isNotBlank(outBatchNo))
        {
            withdraw.setOutBatchNo(outBatchNo);
        }
        if (StringUtils.isNotBlank(detailId))
        {
            withdraw.setWxDetailNo(detailId);
        }
        walletMapper.updateWithdrawStatus(withdraw);
    }

    private String buildOutBatchNo(Long uid)
    {
        return trimToMaxLength("WD" + uid + SnowflakeIdWorker.nextIdDefault(), 32);
    }

    private String buildOutDetailNo(Long uid)
    {
        return trimToMaxLength("WDD" + uid + SnowflakeIdWorker.nextIdDefault(), 32);
    }

    private String trimToMaxLength(String value, int maxLength)
    {
        if (value == null || value.length() <= maxLength)
        {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
