package com.ruoyi.wxmini.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.system.domain.SalonInfo;
import com.ruoyi.system.domain.SalonPayOrder;
import com.ruoyi.system.service.ISalonInfoService;
import com.ruoyi.system.service.ISalonPayOrderService;
import com.ruoyi.wxmini.bo.WxSalonPayCreateOrderBo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.vo.WxPayParamVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxSalonPayServiceImplTest {

    @Mock
    private ISalonInfoService salonInfoService;
    @Mock
    private ISalonPayOrderService salonPayOrderService;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private WxPayService wxPayService;

    @InjectMocks
    private WxSalonPayServiceImpl service;

    @Test
    void should_return_paid_order_detail_for_current_user() {
        SalonPayOrder order = new SalonPayOrder();
        order.setOrderNo("order-1");
        order.setUserId("user-1");
        order.setSalonId(1L);
        order.setAmount(new BigDecimal("128.00"));
        order.setPayTime(new Date());
        order.setStatus("PAID");
        SalonInfo salonInfo = new SalonInfo();
        salonInfo.setTitle("组局思维");
        when(salonPayOrderService.selectSalonPayOrderByOrderNo("order-1")).thenReturn(order);
        when(salonInfoService.selectSalonInfoById(1L)).thenReturn(salonInfo);

        assertEquals("组局思维", service.querySalonOrder("user-1", "order-1").getTitle());
    }

    @Test
    void should_update_paid_order_idempotently_on_success_callback() {
        SalonPayOrder order = new SalonPayOrder();
        order.setId(1L);
        order.setOrderNo("order-1");
        order.setStatus("CANCELED");
        when(salonPayOrderService.selectSalonPayOrderByOrderNo("order-1")).thenReturn(order);
        when(salonPayOrderService.updateSalonPayOrder(order)).thenReturn(1);

        WxPayNotifyV3Result result = new WxPayNotifyV3Result();
        WxPayNotifyV3Result.DecryptNotifyResult notifyResult = new WxPayNotifyV3Result.DecryptNotifyResult();
        notifyResult.setOutTradeNo("order-1");
        notifyResult.setTransactionId("wx-1");
        notifyResult.setSuccessTime("2026-04-14T12:30:00+08:00");
        result.setResult(notifyResult);

        assertTrue(service.handleSalonPaidCallback(result, "req-1"));
        order.setStatus("PAID");
        order.setPayTime(new Date());
        assertTrue(service.handleSalonPaidCallback(result, "req-1"));
    }

    @Test
    void should_fill_pay_time_when_query_result_confirms_paid() throws Exception {
        SalonPayOrder order = new SalonPayOrder();
        order.setId(1L);
        order.setOrderNo("order-1");
        order.setStatus("PENDING");
        when(salonPayOrderService.selectSalonPayOrderByOrderNo("order-1")).thenReturn(order);
        when(salonPayOrderService.updateSalonPayOrder(order)).thenReturn(1);

        WxPayOrderQueryV3Result queryResult = new WxPayOrderQueryV3Result();
        queryResult.setTradeState("SUCCESS");
        queryResult.setSuccessTime("2026-04-15T08:30:00+08:00");
        when(wxPayService.queryOrderV3(org.mockito.ArgumentMatchers.any(WxPayOrderQueryV3Request.class))).thenReturn(queryResult);

        assertTrue(service.updOrderWithPaySuccess("order-1"));
        assertEquals("PAID", order.getStatus());
        assertNotNull(order.getPayTime());
    }

    @Test
    void should_validate_create_order_inputs() {
        WxSalonPayOrderBoStub payVo = new WxSalonPayOrderBoStub();
        payVo.setSalonId(1L);
        payVo.setAmount(new BigDecimal("128.00"));
        payVo.setOpenId("openid");
        assertTrue(service.checkBeforeCreatOrder("user-1", payVo));
    }

    private static class WxSalonPayOrderBoStub extends com.ruoyi.wxmini.vo.WxSalonPayOrderVo {
    }
}
