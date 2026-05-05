package com.ruoyi.wxmini.service;

/**
 * 微信退款服务（admin 端调用）
 */
public interface IWxRefundService {

    /**
     * 退款兼职定金订单
     *
     * @param orderNo 订单号
     * @param reason  退款原因
     */
    void refundJobOrder(String orderNo, String reason);

    /**
     * 退款沙龙订单
     *
     * @param orderNo 订单号
     * @param reason  退款原因
     */
    void refundSalonOrder(String orderNo, String reason);
}
