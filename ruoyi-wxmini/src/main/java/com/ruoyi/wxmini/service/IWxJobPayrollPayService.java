package com.ruoyi.wxmini.service;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.ruoyi.wxmini.bo.WxJobPayrollCreateOrderBo;
import com.ruoyi.wxmini.vo.WxJobPayrollOrderDetailVo;
import com.ruoyi.wxmini.vo.WxPayParamVo;

public interface IWxJobPayrollPayService {
    WxPayParamVo createPayrollOrder(String userId, WxJobPayrollCreateOrderBo bo) throws Exception;

    WxJobPayrollOrderDetailVo queryPayrollOrder(String userId, String orderNo);

    boolean handlePayrollPaidCallback(WxPayNotifyV3Result result, String requestId);
}
