package com.ruoyi.wxmini.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.service.ICoursePayOrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxCoursePayServiceImplTest {

    @Mock private ICoursePayOrderService coursePayOrderService;
    @InjectMocks private WxCoursePayServiceImpl service;

    @Test
    void successfulCallbackShouldAlwaysDelegateToIdempotentPaidTransition() {
        CoursePayOrder order = new CoursePayOrder();
        order.setOrderNo("CRS1");
        order.setUserId("user-1");
        when(coursePayOrderService.selectCoursePayOrderByOrderNo("CRS1")).thenReturn(order);

        WxPayNotifyV3Result result = new WxPayNotifyV3Result();
        WxPayNotifyV3Result.DecryptNotifyResult notify = new WxPayNotifyV3Result.DecryptNotifyResult();
        notify.setOutTradeNo("CRS1");
        notify.setTransactionId("wx-1");
        result.setResult(notify);

        assertTrue(service.handleCoursePaidCallback(result, "request-1"));

        verify(coursePayOrderService).markPaid("CRS1", "wx-1", "request-1", null);
    }
}
