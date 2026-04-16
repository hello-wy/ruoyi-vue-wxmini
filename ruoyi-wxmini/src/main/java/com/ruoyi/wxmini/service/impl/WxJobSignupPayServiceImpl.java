package com.ruoyi.wxmini.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobSignupOrderService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    @Resource
    private WxPayService wxPayService;

    @Override
    public List<WxJobSignupOrderDetailVo> listMyOrders(String userId) {
        List<JobSignupOrder> orders = jobSignupOrderService.selectMyJobSignupOrders(userId);
        List<WxJobSignupOrderDetailVo> result = new ArrayList<>();
        for (JobSignupOrder order : orders) {
            result.add(toDetailVo(order));
        }
        return result;
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
            try {
                wxPayService.closeOrderV3(pendingOrder.getOrderNo());
            } catch (Exception ignored) {
            }
            pendingOrder.setStatus(JobSignupOrderStatusEnum.CANCELED.getCode());
            jobSignupOrderService.updateJobSignupOrder(pendingOrder);
        }
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null || userInfo.getOpenId() == null || userInfo.getOpenId().isEmpty()) {
            throw new RuntimeException("当前用户缺少openId");
        }
        WxJobSignupOrderDetailVo payVo = new WxJobSignupOrderDetailVo();
        payVo.setJobId(job.getId());
        payVo.setJobTitle(job.getTitle());
        payVo.setAmount(JOB_SIGNUP_AMOUNT);
        payVo.setStatus(JobSignupOrderStatusEnum.PENDING.getCode());
        HashMap<String, Object> tmp = new HashMap<>();
        tmp.put("userId", userId);
        tmp.put("openId", userInfo.getOpenId());
        payVo.setOrderNo(UUID.fastUUID().toString());
        return this.createOrder(userId, payVo);
    }

    @Override
    public WxJobSignupOrderDetailVo queryJobOrder(String userId, String orderNo) {
        JobSignupOrder order = jobSignupOrderService.selectJobSignupOrderByOrderNo(orderNo);
        if (order == null || !userId.equals(order.getUserId())) {
            throw new RuntimeException("订单不存在");
        }
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
        Date successTime = parseSuccessTime(result.getResult().getSuccessTime());
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
        orderParam.setOrderDesc(payVo.getJobTitle());
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
        DailyJobs job = dailyJobsService.selectDailyJobsById(order.getJobId());
        DailyJobs lockedJob = dailyJobsService.selectDailyJobsByIdForUpdate(order.getJobId());
        if (job == null || lockedJob == null || !JOB_STATUS_OPEN.equals(lockedJob.getStatus())) {
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
            WxPayRefundV3Request request = new WxPayRefundV3Request();
            request.setOutTradeNo(order.getOrderNo());
            request.setOutRefundNo("REF" + order.getOrderNo());
            request.setReason(reason);
            WxPayRefundV3Request.Amount amount = new WxPayRefundV3Request.Amount();
            amount.setRefund(order.getAmount().multiply(new BigDecimal("100")).intValue());
            amount.setTotal(order.getAmount().multiply(new BigDecimal("100")).intValue());
            amount.setCurrency("CNY");
            request.setAmount(amount);
            wxPayService.refundV3(request);
        } catch (Exception ignored) {
        }
        order.setRefundNo("REF" + order.getOrderNo());
        order.setRefundTime(DateUtils.getNowDate());
        order.setStatus(JobSignupOrderStatusEnum.REFUNDED.getCode());
        return jobSignupOrderService.updateJobSignupOrder(order) > 0;
    }

    private WxJobSignupOrderDetailVo toDetailVo(JobSignupOrder order) {
        DailyJobs job = dailyJobsService.selectDailyJobsById(order.getJobId());
        WxJobSignupOrderDetailVo detailVo = new WxJobSignupOrderDetailVo();
        detailVo.setOrderNo(order.getOrderNo());
        detailVo.setJobId(order.getJobId());
        detailVo.setJobTitle(job == null ? null : job.getTitle());
        detailVo.setAmount(order.getAmount());
        detailVo.setStatus(order.getStatus());
        detailVo.setPayTime(order.getPayTime());
        detailVo.setRefundTime(order.getRefundTime());
        return detailVo;
    }

    @Override
    protected String getNotifyUrl(WxJobSignupOrderDetailVo payVo) {
        return "https://zhiyujia.xyz/api/wxmini/pay/jobs/notify";
    }

    private Date resolvePaidTime(String orderNo) {
        try {
            WxPayOrderQueryV3Request request = new WxPayOrderQueryV3Request();
            request.setOutTradeNo(orderNo);
            WxPayOrderQueryV3Result result = wxPayService.queryOrderV3(request);
            if (result != null && "SUCCESS".equals(result.getTradeState())) {
                Date successTime = parseSuccessTime(result.getSuccessTime());
                if (successTime != null) {
                    return successTime;
                }
            }
        } catch (Exception ignored) {
        }
        return DateUtils.getNowDate();
    }

    private Date parseSuccessTime(String successTime) {
        if (successTime == null || successTime.isEmpty()) {
            return null;
        }
        return DateUtil.parse(successTime, DatePattern.UTC_WITH_XXX_OFFSET_PATTERN);
    }
}
