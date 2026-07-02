package com.ruoyi.wxmini.service;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.ruoyi.wxmini.bo.WxCoursePayCreateOrderBo;
import com.ruoyi.wxmini.vo.WxCoursePayOrderDetailVo;
import com.ruoyi.wxmini.vo.WxPayParamVo;

import java.util.List;

public interface IWxCoursePayService {
    List<WxCoursePayOrderDetailVo> listMyOrders(String userId);

    WxPayParamVo createCourseOrder(String userId, WxCoursePayCreateOrderBo bo) throws Exception;

    WxCoursePayOrderDetailVo queryCourseOrder(String userId, String orderNo);

    WxCoursePayOrderDetailVo queryPaidCourseOrder(String userId, Long courseId);

    WxCoursePayOrderDetailVo cancelCourseOrder(String userId, String orderNo);

    boolean handleCoursePaidCallback(WxPayNotifyV3Result result, String requestId);
}
