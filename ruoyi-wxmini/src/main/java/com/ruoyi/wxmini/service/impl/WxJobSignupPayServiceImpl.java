package com.ruoyi.wxmini.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.domain.ParttimeSignupWhitelist;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.IParttimeSignupWhitelistService;
import com.ruoyi.wxmini.bo.WxJobSignupCreateOrderBo;
import com.ruoyi.wxmini.bo.WxPayCreateOrderParam;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.enums.JobSignupOrderStatusEnum;
import com.ruoyi.wxmini.service.AbsWxPayBaseService;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IWxJobSignupPayService;
import com.ruoyi.wxmini.vo.WxJobSignupOrderDetailVo;
import com.ruoyi.wxmini.vo.WxPayParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WxJobSignupPayServiceImpl extends AbsWxPayBaseService<WxJobSignupOrderDetailVo> implements IWxJobSignupPayService {
    private static final String ORDER_PREFIX = "JOB";
    private static final BigDecimal JOB_SIGNUP_AMOUNT = new BigDecimal("50.00");
    private static final Long JOB_STATUS_OPEN = 0L;
    private static final Long JOB_STATUS_FULL = 1L;

    @Autowired
    private IJobSignupOrderService jobSignupOrderService;
    @Autowired
    private IDailyJobsService dailyJobsService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IParttimeSignupWhitelistService parttimeSignupWhitelistService;
    @Resource
    private WxPayService wxPayService;
    @Resource
    private WxJobSignupPayHelper payHelper;

    @Override
    public List<WxJobSignupOrderDetailVo> listMyOrders(String userId) {
        List<JobSignupOrder> orders = jobSignupOrderService.selectMyJobSignupOrders(userId);
        return orders.stream().map(this::toDetailVo).collect(Collectors.toList());
    }

    @Override
    public WxPayParamVo createJobOrder(String userId, WxJobSignupCreateOrderBo bo) throws Exception {
        DailyJobs job = dailyJobsService.selectDailyJobsById(bo.getJobId());
        if (job == null) {
            throw new RuntimeException("岗位不存在");
        }
        if (!JOB_STATUS_OPEN.equals(job.getStatus())) {
            throw new RuntimeException("当前岗位不可报名");
        }
        JobSignupOrder paidOrder = jobSignupOrderService.selectLatestPaidOrder(userId, bo.getJobId());
        if (paidOrder != null) {
            throw new RuntimeException("当前岗位已报名");
        }
        JobSignupOrder pendingOrder = jobSignupOrderService.selectLatestPendingOrder(userId, bo.getJobId());
        if (pendingOrder != null) {
            wxPayService.closeOrderV3(pendingOrder.getOrderNo());
            pendingOrder.setStatus(JobSignupOrderStatusEnum.CANCELED.getCode());
            jobSignupOrderService.updateJobSignupOrder(pendingOrder);
        }
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null || userInfo.getOpenId() == null || userInfo.getOpenId().isEmpty()) {
            throw new RuntimeException("当前用户缺少openId");
        }
        validateSignupWhitelist(userInfo);
        WxJobSignupOrderDetailVo payVo = new WxJobSignupOrderDetailVo();
        payVo.setJobId(job.getId());
        payVo.setJobTitle(job.getTitle());
        payVo.setAmount(JOB_SIGNUP_AMOUNT);
        payVo.setStatus(JobSignupOrderStatusEnum.PENDING.getCode());
        payVo.setOrderNo(UUID.fastUUID().toString());
        return this.createOrder(userId, payVo);
    }

    @Override
    public WxJobSignupOrderDetailVo queryJobOrder(String userId, String orderNo) {
        JobSignupOrder order = loadOwnedJobOrder(userId, orderNo);
        order = compensatePendingOrder(order);
        return toDetailVo(order);
    }

    @Override
    public boolean handleJobPaidCallback(WxPayNotifyV3Result result, String requestId) {
        if (result == null || result.getResult() == null) {
            return false;
        }
        String orderNo = result.getResult().getOutTradeNo();
        JobSignupOrder order = jobSignupOrderService.selectJobSignupOrderByOrderNo(orderNo);
        if (order == null) {
            return false;
        }
        if (JobSignupOrderStatusEnum.PAID.getCode() == order.getStatus()) {
            return true;
        }
        Date successTime = payHelper.parseSuccessTime(result.getResult().getSuccessTime());
        if (successTime == null) {
            successTime = DateUtils.getNowDate();
        }
        return finalizePaidOrder(order, result.getResult().getTransactionId(), requestId, successTime);
    }

    @Override
    public String getResourceId(WxJobSignupOrderDetailVo payVo) {
        return "job:" + payVo.getJobId() + ":" + UUID.fastUUID();
    }

    @Override
    public Boolean checkBeforeCreatOrder(String userId, WxJobSignupOrderDetailVo payVo) {
        return payVo.getJobId() != null && payVo.getAmount() != null;
    }

    @Override
    public Boolean checkUserOrderIsMatch(String userId, String orderNo) {
        JobSignupOrder order = jobSignupOrderService.selectJobSignupOrderByOrderNo(orderNo);
        return order != null && userId.equals(order.getUserId());
    }

    @Override
    public WxPayCreateOrderParam buildOrderParam(String userId, WxJobSignupOrderDetailVo payVo, HashMap<String, Object> contextMap) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null || userInfo.getOpenId() == null || userInfo.getOpenId().isEmpty()) {
            throw new RuntimeException("当前用户缺少openId");
        }
        WxPayCreateOrderParam orderParam = new WxPayCreateOrderParam();
        orderParam.setOrderNo(ORDER_PREFIX + DateUtils.dateTimeNow("yyyyMMddHHmmss") + System.currentTimeMillis());
        orderParam.setOrderDesc(payHelper.buildOrderDesc(payVo.getJobTitle()));
        orderParam.setAmount(payVo.getAmount().multiply(new BigDecimal("100")).intValue());
        orderParam.setOpenId(userInfo.getOpenId());
        orderParam.setTimeExpire(DateUtil.format(DateUtil.offsetMinute(new Date(), 5), DatePattern.UTC_WITH_XXX_OFFSET_PATTERN));
        contextMap.put("jobId", payVo.getJobId());
        contextMap.put("userId", userId);
        contextMap.put("jobTitle", payVo.getJobTitle());
        return orderParam;
    }

    @Override
    public WxJobSignupOrderDetailVo buildPayVoWithReCreatOrder(String userId, String orderNo) {
        JobSignupOrder order = jobSignupOrderService.selectJobSignupOrderByOrderNo(orderNo);
        if (order == null) {
            return null;
        }
        DailyJobs job = dailyJobsService.selectDailyJobsById(order.getJobId());
        if (job == null) {
            return null;
        }
        WxJobSignupOrderDetailVo payVo = new WxJobSignupOrderDetailVo();
        payVo.setJobId(order.getJobId());
        payVo.setJobTitle(job.getTitle());
        payVo.setAmount(order.getAmount());
        return payVo;
    }

    @Override
    public Boolean saveOrderInfo(String orderNo, WxJobSignupOrderDetailVo payVo, WxPayCreateOrderParam orderParam, HashMap<String, Object> contextMap) {
        JobSignupOrder order = new JobSignupOrder();
        order.setId(SnowflakeIdWorker.nextIdDefault());
        order.setOrderNo(orderNo);
        order.setUserId((String) contextMap.get("userId"));
        order.setJobId((Long) contextMap.get("jobId"));
        order.setAmount(payVo.getAmount());
        order.setStatus(JobSignupOrderStatusEnum.PENDING.getCode());
        return jobSignupOrderService.insertJobSignupOrder(order) > 0;
    }

    @Override
    public Boolean updOrderWithPaySuccess(String orderNo) {
        JobSignupOrder order = jobSignupOrderService.selectJobSignupOrderByOrderNo(orderNo);
        if (order == null) {
            return false;
        }
        Date payTime = resolvePaidTime(orderNo);
        return finalizePaidOrder(order, order.getWechatTransactionId(), order.getRequestId(), payTime);
    }

    @Override
    public Boolean closeOrder(String orderNo) {
        JobSignupOrder order = jobSignupOrderService.selectJobSignupOrderByOrderNo(orderNo);
        if (order == null) {
            return false;
        }
        order.setStatus(JobSignupOrderStatusEnum.CANCELED.getCode());
        return jobSignupOrderService.updateJobSignupOrder(order) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    protected boolean finalizePaidOrder(JobSignupOrder order, String transactionId, String requestId, Date payTime) {
        if (order == null) {
            return false;
        }
        if (JobSignupOrderStatusEnum.PAID.getCode() == order.getStatus()) {
            return true;
        }
        DailyJobs lockedJob = dailyJobsService.selectDailyJobsByIdForUpdate(order.getJobId());
        if (lockedJob == null || !JOB_STATUS_OPEN.equals(lockedJob.getStatus())) {
            return refundPaidOrder(order, transactionId, requestId, payTime, "岗位不可报名");
        }
        int paidCount = dailyJobsService.countPaidSignupOrders(order.getJobId(), JobSignupOrderStatusEnum.PAID.getCode());
        int signupLimit = lockedJob.getSignupLimit() == null || lockedJob.getSignupLimit() <= 0 ? 1 : lockedJob.getSignupLimit();
        if (paidCount >= signupLimit) {
            return refundPaidOrder(order, transactionId, requestId, payTime, "报名名额已满");
        }
        order.setStatus(JobSignupOrderStatusEnum.PAID.getCode());
        order.setWechatTransactionId(transactionId);
        order.setRequestId(requestId);
        order.setPayTime(payTime == null ? DateUtils.getNowDate() : payTime);
        boolean updated = jobSignupOrderService.updateJobSignupOrder(order) > 0;
        if (!updated) {
            return false;
        }
        if (paidCount + 1 >= signupLimit) {
            lockedJob.setStatus(JOB_STATUS_FULL);
            dailyJobsService.updateDailyJobs(lockedJob);
        }
        return true;
    }

    private boolean refundPaidOrder(JobSignupOrder order, String transactionId, String requestId, Date payTime, String reason) {
        order.setStatus(JobSignupOrderStatusEnum.REFUNDING.getCode());
        order.setWechatTransactionId(transactionId);
        order.setRequestId(requestId);
        order.setPayTime(payTime == null ? DateUtils.getNowDate() : payTime);
        jobSignupOrderService.updateJobSignupOrder(order);
        try {
            wxPayService.refundV3(payHelper.buildRefundRequest(order, reason));
        } catch (Exception e) {
            throw new RuntimeException("微信退款失败", e);
        }
        order.setRefundNo("REF" + order.getOrderNo());
        order.setRefundTime(DateUtils.getNowDate());
        order.setStatus(JobSignupOrderStatusEnum.REFUNDED.getCode());
        return jobSignupOrderService.updateJobSignupOrder(order) > 0;
    }

    private void validateSignupWhitelist(UserInfo userInfo) {
        if (userInfo == null || userInfo.getRealName() == null || userInfo.getRealName().trim().isEmpty()
                || userInfo.getIdCard() == null || userInfo.getIdCard().trim().isEmpty()) {
            throw new RuntimeException("请你联系管理员开通权限进行报名");
        }
        ParttimeSignupWhitelist whitelist = parttimeSignupWhitelistService
                .selectEnabledParttimeSignupWhitelistByIdCard(userInfo.getIdCard().trim().toUpperCase());
        if (whitelist == null || whitelist.getRealName() == null
                || !userInfo.getRealName().trim().equals(whitelist.getRealName().trim())) {
            throw new RuntimeException("请你联系管理员开通权限进行报名");
        }
    }

    private JobSignupOrder loadOwnedJobOrder(String userId, String orderNo) {
        JobSignupOrder order = jobSignupOrderService.selectJobSignupOrderByOrderNo(orderNo);
        if (order == null || !userId.equals(order.getUserId())) {
            throw new RuntimeException("订单不存在");
        }
        return order;
    }

    private JobSignupOrder compensatePendingOrder(JobSignupOrder order) {
        if (order == null || order.getStatus() != JobSignupOrderStatusEnum.PENDING.getCode()) {
            return order;
        }
        try {
            WxPayOrderQueryV3Result result = wxPayService.queryOrderV3(payHelper.buildOrderQuery(order.getOrderNo()));
            if (!payHelper.isPaid(result)) {
                return order;
            }
            Date payTime = payHelper.parseSuccessTime(result.getSuccessTime());
            finalizePaidOrder(order, result.getTransactionId(), null, payTime);
        } catch (Exception e) {
            throw new RuntimeException("同步支付状态失败", e);
        }
        return jobSignupOrderService.selectJobSignupOrderByOrderNo(order.getOrderNo());
    }

    private WxJobSignupOrderDetailVo toDetailVo(JobSignupOrder order) {
        DailyJobs job = dailyJobsService.selectDailyJobsById(order.getJobId());
        return payHelper.toDetailVo(order, job);
    }

    @Override
    protected String getNotifyUrl(WxJobSignupOrderDetailVo payVo) {
        return "https://zhiyujia.xyz/api/wxmini/pay/jobs/notify";
    }

    private Date resolvePaidTime(String orderNo) {
        try {
            WxPayOrderQueryV3Result result = wxPayService.queryOrderV3(payHelper.buildOrderQuery(orderNo));
            if (payHelper.isPaid(result)) {
                Date successTime = payHelper.parseSuccessTime(result.getSuccessTime());
                if (successTime != null) {
                    return successTime;
                }
            }
        } catch (Exception ignored) {
        }
        return DateUtils.getNowDate();
    }

}
