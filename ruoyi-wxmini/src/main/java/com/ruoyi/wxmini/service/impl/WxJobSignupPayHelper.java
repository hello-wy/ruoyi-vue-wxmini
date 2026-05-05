package com.ruoyi.wxmini.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.wxmini.vo.WxJobSignupOrderDetailVo;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class WxJobSignupPayHelper {
    private static final String ORDER_DESC_PREFIX = "兼职押金：";
    private static final String CURRENCY_CNY = "CNY";
    private static final String TRADE_STATE_SUCCESS = "SUCCESS";
    private static final int CENTS_PER_YUAN = 100;

    public String buildOrderDesc(String jobTitle) {
        return ORDER_DESC_PREFIX + jobTitle;
    }

    public WxPayOrderQueryV3Request buildOrderQuery(String orderNo) {
        WxPayOrderQueryV3Request request = new WxPayOrderQueryV3Request();
        request.setOutTradeNo(orderNo);
        return request;
    }

    public boolean isPaid(WxPayOrderQueryV3Result result) {
        return result != null && TRADE_STATE_SUCCESS.equals(result.getTradeState());
    }

    public Date parseSuccessTime(String successTime) {
        if (successTime == null || successTime.isEmpty()) {
            return null;
        }
        return DateUtil.parse(successTime, DatePattern.UTC_WITH_XXX_OFFSET_PATTERN);
    }

    public WxPayRefundV3Request buildRefundRequest(JobSignupOrder order, String reason) {
        WxPayRefundV3Request request = new WxPayRefundV3Request();
        request.setOutTradeNo(order.getOrderNo());
        request.setOutRefundNo("REF" + order.getOrderNo());
        request.setReason(reason);
        request.setAmount(buildRefundAmount(order));
        return request;
    }

    public WxJobSignupOrderDetailVo toDetailVo(JobSignupOrder order, DailyJobs job) {
        WxJobSignupOrderDetailVo detailVo = new WxJobSignupOrderDetailVo();
        detailVo.setOrderNo(order.getOrderNo());
        detailVo.setJobId(order.getJobId());
        detailVo.setJobTitle(job == null ? null : job.getTitle());
        detailVo.setAmount(order.getAmount());
        detailVo.setStatus(order.getStatus());
        detailVo.setPayTime(order.getPayTime());
        detailVo.setRefundTime(order.getRefundTime());
        detailVo.setCreateTime(order.getCreateTime());
        return detailVo;
    }

    private WxPayRefundV3Request.Amount buildRefundAmount(JobSignupOrder order) {
        int amount = order.getAmount().multiply(new java.math.BigDecimal(CENTS_PER_YUAN)).intValue();
        WxPayRefundV3Request.Amount refundAmount = new WxPayRefundV3Request.Amount();
        refundAmount.setRefund(amount);
        refundAmount.setTotal(amount);
        refundAmount.setCurrency(CURRENCY_CNY);
        return refundAmount;
    }
}
