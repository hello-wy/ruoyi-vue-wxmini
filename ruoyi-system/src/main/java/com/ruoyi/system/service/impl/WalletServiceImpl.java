package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletTransaction;
import com.ruoyi.system.domain.WalletWithdraw;
import com.ruoyi.system.enums.WithdrawFailType;
import com.ruoyi.system.mapper.WalletMapper;
import com.ruoyi.system.service.IWalletService;
import com.ruoyi.system.service.IWalletTransferGateway;
import com.ruoyi.system.service.dto.WalletTransferCreateRequest;
import com.ruoyi.system.service.dto.WalletTransferCreateResult;
import com.ruoyi.system.service.dto.WalletTransferQueryResult;
import com.ruoyi.system.service.dto.WithdrawResult;
import com.ruoyi.system.util.WithdrawFailReasonMapper;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletServiceImpl implements IWalletService
{
    private static final Logger log = LoggerFactory.getLogger(WalletServiceImpl.class);

    private static final Integer DIRECTION_INCOME = 1;
    private static final Integer DIRECTION_EXPENSE = 2;
    private static final Integer WITHDRAW_STATUS_PROCESSING = 0;
    private static final Integer WITHDRAW_STATUS_SUCCESS = 1;
    private static final Integer WITHDRAW_STATUS_FAILED = 2;
    private static final Integer REALNAME_VERIFIED = 1;
    private static final String WITHDRAW_BIZ_TYPE = "WITHDRAW";
    private static final String WITHDRAW_REMARK = "微信提现";
    private static final String DETAIL_STATUS_SUCCESS = "SUCCESS";
    private static final String DETAIL_STATUS_FAILED = "FAIL";
    private static final String DETAIL_STATUS_FAILED_NEW = "FAILED";
    private static final String TRANSFER_STATE_WAIT_USER_CONFIRM = "WAIT_USER_CONFIRM";

    @Value("${wx.pay.appId:}")
    private String wxPayAppId;

    @Value("${wx.pay.mchId:}")
    private String wxPayMchId;

    @Value("${wx.pay.transfer.notify-url:}")
    private String transferNotifyUrl;

    @Value("${wx.pay.transfer.scene-id:1005}")
    private String transferSceneId;

    @Value("${wx.pay.transfer.min-amount:1.00}")
    private BigDecimal transferMinAmount;

    @Value("${wx.pay.transfer.max-amount:2000.00}")
    private BigDecimal transferMaxAmount;

    @Value("${wx.pay.transfer.batch-name:钱包提现}")
    private String transferBatchName;

    @Value("${wx.pay.transfer.user-recv-perception:钱包提现}")
    private String transferUserRecvPerception;

    @Value("${wx.pay.transfer.scene-report-job-type:其他}")
    private String transferSceneReportJobType;

    @Value("${wx.pay.transfer.scene-report-reward-desc:微信提现}")
    private String transferSceneReportRewardDesc;

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
    public WithdrawResult applyWithdraw(String userId, Long uid, BigDecimal amount)
    {
        // 金额校验
        if (amount == null || amount.compareTo(transferMinAmount) < 0)
        {
            return WithdrawResult.fail(WithdrawFailType.AMOUNT_OUT_OF_LIMIT,
                    "提现金额不能低于" + transferMinAmount.stripTrailingZeros().toPlainString() + "元");
        }
        if (amount.compareTo(transferMaxAmount) > 0)
        {
            return WithdrawResult.fail(WithdrawFailType.AMOUNT_OUT_OF_LIMIT,
                    "提现金额不能高于" + transferMaxAmount.stripTrailingZeros().toPlainString() + "元");
        }
        if (amount.scale() > 2)
        {
            return WithdrawResult.fail(WithdrawFailType.AMOUNT_OUT_OF_LIMIT, "提现金额最多保留两位小数");
        }

        // 用户信息校验
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null)
        {
            return WithdrawResult.fail(WithdrawFailType.UNKNOWN, "用户不存在");
        }
        if (!REALNAME_VERIFIED.equals(userInfo.getIsRealnameAuth())
                || StringUtils.isAnyBlank(userInfo.getRealName(), userInfo.getIdCard()))
        {
            return WithdrawResult.fail(WithdrawFailType.USER_NOT_REALNAME);
        }
        if (StringUtils.isBlank(userInfo.getOpenId()))
        {
            return WithdrawResult.fail(WithdrawFailType.OPENID_MISSING);
        }

        // 余额校验
        UserWallet wallet = getOrCreateWallet(uid);
        if (wallet.getBalance().compareTo(amount) < 0)
        {
            return WithdrawResult.fail(WithdrawFailType.BALANCE_INSUFFICIENT);
        }

        // 互斥检查：同用户处理中提现
        int pendingCount = walletMapper.selectPendingWithdrawCountByUid(uid);
        if (pendingCount > 0)
        {
            return WithdrawResult.fail(WithdrawFailType.PENDING_WITHDRAW_EXISTS);
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
            WalletTransferCreateResult result = walletTransferGateway.createTransfer(
                    buildTransferRequest(userInfo, amount, outBatchNo, outDetailNo));
            withdraw.setWxTransferNo(result.getBatchId());
            walletMapper.updateWithdrawStatus(withdraw);

            return syncWithdrawStatusForResult(withdraw, result);
        }
        catch (Exception e)
        {
            String errCode = extractErrCode(e);
            String errMsg = e.getMessage();
            WithdrawFailType failType = WithdrawFailReasonMapper.resolve(errCode, errMsg);

            log.error("[Withdraw] uid={}, outBatchNo={}, failType={}, errMsg={}",
                    uid, outBatchNo, failType, errMsg, e);

            withdraw.setStatus(WITHDRAW_STATUS_FAILED);
            withdraw.setFailType(failType.name());
            withdraw.setUserMessage(failType.getUserMessage());
            withdraw.setRemark(StringUtils.defaultIfBlank(errMsg, "微信提现发起失败"));
            walletMapper.updateWithdrawStatus(withdraw);

            return WithdrawResult.fail(failType);
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

    /**
     * SDK 调用成功后同步查询状态，返回结构化 WithdrawResult
     */
    private WithdrawResult syncWithdrawStatusForResult(WalletWithdraw withdraw, WalletTransferCreateResult createResult)
    {
        try
        {
            boolean updated = updateWithdrawByDetailQuery(withdraw);
            if (!updated)
            {
                return buildProcessingResult(withdraw, createResult);
            }
            WalletWithdraw latest = walletMapper.selectWithdrawById(withdraw.getId());
            if (latest == null)
            {
                return WithdrawResult.processing(withdraw.getId(), withdraw.getOutBatchNo());
            }
            if (WITHDRAW_STATUS_SUCCESS.equals(latest.getStatus()))
            {
                return WithdrawResult.success(latest.getId(), latest.getOutBatchNo(), "微信提现成功");
            }
            if (WITHDRAW_STATUS_FAILED.equals(latest.getStatus()))
            {
                WithdrawFailType failType = WithdrawFailType.UNKNOWN;
                if (StringUtils.isNotBlank(latest.getFailType()))
                {
                    try
                    {
                        failType = WithdrawFailType.valueOf(latest.getFailType());
                    }
                    catch (IllegalArgumentException ignored)
                    {
                    }
                }
                return WithdrawResult.fail(failType,
                        StringUtils.defaultIfBlank(latest.getUserMessage(), failType.getUserMessage()));
            }
            return buildProcessingResult(withdraw, createResult);
        }
        catch (Exception e)
        {
            return buildProcessingResult(withdraw, createResult);
        }
    }

    private WithdrawResult buildProcessingResult(WalletWithdraw withdraw, WalletTransferCreateResult createResult)
    {
        if (createResult == null || !TRANSFER_STATE_WAIT_USER_CONFIRM.equals(createResult.getState())
                || StringUtils.isBlank(createResult.getPackageInfo()))
        {
            return WithdrawResult.processing(withdraw.getId(), withdraw.getOutBatchNo());
        }
        return WithdrawResult.processingWithPackage(withdraw.getId(), withdraw.getOutBatchNo(),
                createResult.getPackageInfo(), wxPayAppId, wxPayMchId);
    }

    private WalletTransferCreateRequest buildTransferRequest(UserInfo userInfo, BigDecimal amount, String outBatchNo, String outDetailNo)
    {
        WalletTransferCreateRequest request = new WalletTransferCreateRequest();
        request.setOpenId(userInfo.getOpenId());
        request.setRealName(userInfo.getRealName());
        request.setAmount(amount);
        request.setOutBatchNo(outBatchNo);
        request.setOutDetailNo(outDetailNo);
        request.setBatchName(transferBatchName);
        request.setBatchRemark(WITHDRAW_REMARK);
        request.setTransferRemark(WITHDRAW_REMARK);
        request.setNotifyUrl(transferNotifyUrl);
        request.setTransferSceneId(transferSceneId);
        request.setUserRecvPerception(transferUserRecvPerception);
        request.addTransferSceneReportInfo("岗位类型", transferSceneReportJobType);
        request.addTransferSceneReportInfo("报酬说明", transferSceneReportRewardDesc);
        return request;
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
        if (DETAIL_STATUS_FAILED.equals(detail.getDetailStatus())
                || DETAIL_STATUS_FAILED_NEW.equals(detail.getDetailStatus()))
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

        WithdrawFailType failType = WithdrawFailReasonMapper.fromFailReason(reason);

        withdraw.setStatus(WITHDRAW_STATUS_FAILED);
        withdraw.setRemark(StringUtils.defaultIfBlank(reason, "微信提现失败，请稍后重试"));
        withdraw.setFailType(failType.name());
        withdraw.setUserMessage(failType.getUserMessage());
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

        log.error("[Withdraw] markFailed uid={}, outBatchNo={}, failType={}, reason={}",
                withdraw.getUid(), withdraw.getOutBatchNo(), failType, reason);
    }

    /**
     * 从异常中提取微信支付错误码
     * WxPayException 有 getErrCode() 方法
     */
    private String extractErrCode(Exception e)
    {
        // 使用反射避免 ruoyi-system 对 weixin-java-pay 的编译依赖
        try
        {
            if (e.getClass().getName().contains("WxPayException"))
            {
                java.lang.reflect.Method getErrCode = e.getClass().getMethod("getErrCode");
                Object errCode = getErrCode.invoke(e);
                return errCode != null ? errCode.toString() : null;
            }
        }
        catch (Exception ignored)
        {
        }
        return null;
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
