package com.ruoyi.wxmini.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SalonInfo;
import com.ruoyi.system.domain.SalonPayOrder;
import com.ruoyi.system.service.ISalonInfoService;
import com.ruoyi.system.service.ISalonPayOrderService;
import com.ruoyi.wxmini.bo.WxPayCreateOrderParam;
import com.ruoyi.wxmini.bo.WxSalonPayCreateOrderBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.AbsWxPayBaseService;
import com.ruoyi.wxmini.service.IWxSalonPayService;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.vo.WxPayParamVo;
import com.ruoyi.wxmini.vo.WxSalonPayOrderDetailVo;
import com.ruoyi.wxmini.vo.WxSalonPayOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Service
public class WxSalonPayServiceImpl extends AbsWxPayBaseService<WxSalonPayOrderVo> implements IWxSalonPayService {
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_PAID = "PAID";
    private static final String STATUS_CANCELED = "CANCELED";
    private static final String ORDER_PREFIX = "SALON";

    @Autowired
    private ISalonInfoService salonInfoService;
    @Autowired
    private ISalonPayOrderService salonPayOrderService;
    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public WxPayParamVo createSalonOrder(String userId, WxSalonPayCreateOrderBo bo) throws Exception {
        SalonInfo salonInfo = salonInfoService.selectSalonInfoById(bo.getSalonId());
        if (salonInfo == null || salonInfo.getStatus() == null || salonInfo.getStatus() != 1L) {
            throw new RuntimeException("沙龙不存在或不可支付");
        }
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null || userInfo.getOpenId() == null || userInfo.getOpenId().isEmpty()) {
            throw new RuntimeException("当前用户缺少openId");
        }
        WxSalonPayOrderVo payVo = new WxSalonPayOrderVo();
        payVo.setUserId(userId);
        payVo.setSalonId(bo.getSalonId());
        payVo.setAmount(salonInfo.getCurrentPrice());
        payVo.setTitle(salonInfo.getTitle());
        payVo.setOpenId(userInfo.getOpenId());
        return this.createOrder(userId, payVo);
    }

    @Override
    public WxSalonPayOrderDetailVo querySalonOrder(String userId, String orderNo) {
        SalonPayOrder order = salonPayOrderService.selectSalonPayOrderByOrderNo(orderNo);
        if (order == null || !userId.equals(order.getUserId())) {
            throw new RuntimeException("订单不存在");
        }
        SalonInfo salonInfo = salonInfoService.selectSalonInfoById(order.getSalonId());
        WxSalonPayOrderDetailVo detailVo = new WxSalonPayOrderDetailVo();
        detailVo.setOrderNo(order.getOrderNo());
        detailVo.setAmount(order.getAmount());
        detailVo.setPayTime(order.getPayTime());
        detailVo.setStatus(order.getStatus());
        detailVo.setTitle(salonInfo == null ? null : salonInfo.getTitle());
        return detailVo;
    }

    @Override
    public boolean handleSalonPaidCallback(WxPayNotifyV3Result result, String requestId) {
        if (result == null || result.getResult() == null) {
            return false;
        }
        String orderNo = result.getResult().getOutTradeNo();
        SalonPayOrder order = salonPayOrderService.selectSalonPayOrderByOrderNo(orderNo);
        if (order == null) {
            return false;
        }
        if (STATUS_PAID.equals(order.getStatus())) {
            return true;
        }
        order.setStatus(STATUS_PAID);
        order.setWechatTransactionId(result.getResult().getTransactionId());
        order.setRequestId(requestId);
        order.setPayTime(DateUtil.parse(result.getResult().getSuccessTime(), DatePattern.UTC_WITH_XXX_OFFSET_PATTERN));
        return salonPayOrderService.updateSalonPayOrder(order) > 0;
    }

    @Override
    public String getResourceId(WxSalonPayOrderVo payVo) {
        return "salon:" + payVo.getUserId() + ":" + payVo.getSalonId() + ":" + UUID.fastUUID();
    }

    @Override
    public Boolean checkBeforeCreatOrder(String userId, WxSalonPayOrderVo payVo) {
        return payVo.getSalonId() != null && payVo.getAmount() != null && payVo.getOpenId() != null;
    }

    @Override
    public Boolean checkUserOrderIsMatch(String userId, String orderNo) {
        SalonPayOrder order = salonPayOrderService.selectSalonPayOrderByOrderNo(orderNo);
        return order != null && userId.equals(order.getUserId());
    }

    @Override
    public WxPayCreateOrderParam buildOrderParam(String userId, WxSalonPayOrderVo payVo, HashMap<String, Object> contextMap) {
        WxPayCreateOrderParam orderParam = new WxPayCreateOrderParam();
        orderParam.setOrderNo(ORDER_PREFIX + DateUtils.dateTimeNow("yyyyMMddHHmmss") + System.currentTimeMillis());
        orderParam.setOrderDesc(payVo.getTitle());
        orderParam.setAmount(payVo.getAmount().multiply(new BigDecimal("100")).intValue());
        orderParam.setOpenId(payVo.getOpenId());
        orderParam.setTimeExpire(DateUtil.format(DateUtil.offsetMinute(new Date(), 5), DatePattern.UTC_WITH_XXX_OFFSET_PATTERN));
        contextMap.put("title", payVo.getTitle());
        return orderParam;
    }

    @Override
    public WxSalonPayOrderVo buildPayVoWithReCreatOrder(String userId, String orderNo) {
        SalonPayOrder order = salonPayOrderService.selectSalonPayOrderByOrderNo(orderNo);
        if (order == null) {
            return null;
        }
        SalonInfo salonInfo = salonInfoService.selectSalonInfoById(order.getSalonId());
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (salonInfo == null || userInfo == null) {
            return null;
        }
        WxSalonPayOrderVo payVo = new WxSalonPayOrderVo();
        payVo.setUserId(userId);
        payVo.setSalonId(order.getSalonId());
        payVo.setAmount(order.getAmount());
        payVo.setTitle(salonInfo.getTitle());
        payVo.setOpenId(userInfo.getOpenId());
        return payVo;
    }

    @Override
    public Boolean saveOrderInfo(String orderNo, WxSalonPayOrderVo payVo, WxPayCreateOrderParam orderParam, HashMap<String, Object> contextMap) {
        SalonPayOrder order = new SalonPayOrder();
        order.setOrderNo(orderNo);
        order.setUserId(payVo.getUserId());
        order.setSalonId(payVo.getSalonId());
        order.setAmount(payVo.getAmount());
        order.setStatus(STATUS_PENDING);
        return salonPayOrderService.insertSalonPayOrder(order) > 0;
    }

    @Override
    public Boolean updOrderWithPaySuccess(String orderNo) {
        SalonPayOrder order = salonPayOrderService.selectSalonPayOrderByOrderNo(orderNo);
        if (order == null || STATUS_PAID.equals(order.getStatus())) {
            return order != null;
        }
        order.setStatus(STATUS_PAID);
        order.setPayTime(DateUtils.getNowDate());
        return salonPayOrderService.updateSalonPayOrder(order) > 0;
    }

    @Override
    public Boolean closeOrder(String orderNo) {
        SalonPayOrder order = salonPayOrderService.selectSalonPayOrderByOrderNo(orderNo);
        if (order == null) {
            return false;
        }
        order.setStatus(STATUS_CANCELED);
        return salonPayOrderService.updateSalonPayOrder(order) > 0;
    }
}
