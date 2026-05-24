package com.ruoyi.wxmini.service;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.ruoyi.system.domain.TutoringBinding;
import com.ruoyi.system.domain.TutoringOrder;
import com.ruoyi.system.domain.TutoringSchedule;
import com.ruoyi.wxmini.bo.WxTutoringCreateOrderBo;
import com.ruoyi.wxmini.vo.WxPayParamVo;

import java.util.List;

public interface IWxMiniTutoringService {
    List<TutoringBinding> listAvailableBindings(String wxUserId);

    WxPayParamVo createOrder(String wxUserId, WxTutoringCreateOrderBo bo) throws Exception;

    WxPayParamVo payPendingOrder(String wxUserId, String orderNo) throws Exception;

    List<TutoringOrder> listMyOrders(String wxUserId);

    TutoringOrder getOrderDetail(String wxUserId, String orderNo);

    List<TutoringSchedule> listMySchedules(String wxUserId);

    void finishSchedule(String wxUserId, Long scheduleId, String remark);

    void confirmSchedule(String wxUserId, Long scheduleId, String remark);

    boolean handleTutoringPaidCallback(WxPayNotifyV3Result result, String requestId);
}
