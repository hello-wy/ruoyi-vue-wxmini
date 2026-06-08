package com.ruoyi.wxmini.service;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.ruoyi.wxmini.bo.WxGrowupCourseEnrollBo;
import com.ruoyi.wxmini.vo.WxPayParamVo;

public interface IWxGrowupPayService {
    WxPayParamVo createCourseOrder(String userId, Long courseId, WxGrowupCourseEnrollBo bo) throws Exception;

    boolean handleCoursePaidCallback(WxPayNotifyV3Result result, String requestId);
}
