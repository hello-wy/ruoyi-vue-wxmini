package com.ruoyi.wxmini.service.impl;

import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.domain.SalonPayOrder;
import com.ruoyi.system.service.ICoursePayOrderService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.ISalonPayOrderService;
import com.ruoyi.wxmini.enums.JobSignupOrderStatusEnum;
import com.ruoyi.wxmini.service.IWxRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class WxRefundServiceImpl implements IWxRefundService {

    @Autowired
    private IJobSignupOrderService jobSignupOrderService;
    @Autowired
    private ISalonPayOrderService salonPayOrderService;
    @Autowired
    private ICoursePayOrderService coursePayOrderService;
    @Resource
    private WxPayService wxPayService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundJobOrder(String orderNo, String reason) {
        JobSignupOrder order = jobSignupOrderService.selectJobSignupOrderByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (JobSignupOrderStatusEnum.PAID.getCode() != order.getStatus()) {
            throw new RuntimeException("订单状态不允许退款");
        }
        order.setStatus(JobSignupOrderStatusEnum.REFUNDING.getCode());
        jobSignupOrderService.updateJobSignupOrder(order);

        String refundNo = "REF" + orderNo;
        try {
            WxPayRefundV3Request request = buildRefundRequest(orderNo, refundNo, reason, order.getAmount());
            wxPayService.refundV3(request);
        } catch (Exception e) {
            throw new RuntimeException("微信退款接口调用失败: " + e.getMessage(), e);
        }

        order.setRefundNo(refundNo);
        order.setRefundTime(DateUtils.getNowDate());
        order.setStatus(JobSignupOrderStatusEnum.REFUNDED.getCode());
        jobSignupOrderService.updateJobSignupOrder(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundSalonOrder(String orderNo, String reason) {
        SalonPayOrder order = salonPayOrderService.selectSalonPayOrderByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!"PAID".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许退款");
        }
        order.setStatus("REFUNDING");
        salonPayOrderService.updateSalonPayOrder(order);

        String refundNo = "REF" + orderNo;
        try {
            WxPayRefundV3Request request = buildRefundRequest(orderNo, refundNo, reason, order.getAmount());
            wxPayService.refundV3(request);
        } catch (Exception e) {
            throw new RuntimeException("微信退款接口调用失败: " + e.getMessage(), e);
        }

        order.setRefundNo(refundNo);
        order.setRefundTime(DateUtils.getNowDate());
        order.setStatus("REFUNDED");
        salonPayOrderService.updateSalonPayOrder(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundCourseOrder(String orderNo, String reason) {
        CoursePayOrder order = coursePayOrderService.selectCoursePayOrderByOrderNo(orderNo);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        if (!CoursePayOrder.STATUS_SIGNED.equals(order.getStatus())) {
            throw new ServiceException("只有已签到课程订单可退款");
        }
        String refundNo = "REF" + orderNo;
        try {
            WxPayRefundV3Request request = buildRefundRequest(orderNo, refundNo, reason, order.getAmount());
            wxPayService.refundV3(request);
        } catch (Exception e) {
            throw new ServiceException("微信退款接口调用失败: " + e.getMessage());
        }
        coursePayOrderService.markRefunded(orderNo, refundNo, DateUtils.getNowDate());
    }

    private WxPayRefundV3Request buildRefundRequest(String orderNo, String refundNo, String reason, BigDecimal amount) {
        WxPayRefundV3Request request = new WxPayRefundV3Request();
        request.setOutTradeNo(orderNo);
        request.setOutRefundNo(refundNo);
        request.setReason(reason);
        WxPayRefundV3Request.Amount amt = new WxPayRefundV3Request.Amount();
        int amountInFen = amount.multiply(new BigDecimal("100")).intValue();
        amt.setRefund(amountInFen);
        amt.setTotal(amountInFen);
        amt.setCurrency("CNY");
        request.setAmount(amt);
        return request;
    }
}
