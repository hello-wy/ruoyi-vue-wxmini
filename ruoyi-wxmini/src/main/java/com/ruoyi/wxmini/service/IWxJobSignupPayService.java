package com.ruoyi.wxmini.service;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.ruoyi.wxmini.bo.WxJobSignupCreateOrderBo;
import com.ruoyi.wxmini.vo.WxJobSignupOrderDetailVo;
import com.ruoyi.wxmini.vo.WxPayParamVo;

import java.util.List;

public interface IWxJobSignupPayService {
    List<WxJobSignupOrderDetailVo> listMyOrders(String userId);

    WxPayParamVo createJobOrder(String userId, WxJobSignupCreateOrderBo bo) throws Exception;

    WxJobSignupOrderDetailVo queryJobOrder(String userId, String orderNo);

    boolean handleJobPaidCallback(WxPayNotifyV3Result result, String requestId);
}
