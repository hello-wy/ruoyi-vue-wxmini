package com.ruoyi.wxmini.service;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.ruoyi.wxmini.bo.WxSalonPayCreateOrderBo;
import com.ruoyi.wxmini.vo.WxPayParamVo;
import com.ruoyi.wxmini.vo.WxSalonPayOrderDetailVo;

import java.util.List;

public interface IWxSalonPayService {
    List<WxSalonPayOrderDetailVo> listMyOrders(String userId);

    WxPayParamVo createSalonOrder(String userId, WxSalonPayCreateOrderBo bo) throws Exception;

    WxSalonPayOrderDetailVo querySalonOrder(String userId, String orderNo);

    boolean handleSalonPaidCallback(WxPayNotifyV3Result result, String requestId);
}
