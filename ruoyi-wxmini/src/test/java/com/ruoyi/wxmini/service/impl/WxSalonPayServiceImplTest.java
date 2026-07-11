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
import com.ruoyi.wxmini.vo.WxSalonPayOrderDetailVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
    void should_list_my_orders_with_salon_id_and_create_time() {
        SalonPayOrder firstOrder = new SalonPayOrder();
        firstOrder.setOrderNo("order-1");
        firstOrder.setSalonId(1L);
        firstOrder.setTitle("组局思维");
        firstOrder.setAmount(new BigDecimal("128.00"));
        firstOrder.setStatus("PAID");
        firstOrder.setPayTime(new Date());
        firstOrder.setCreateTime(new Date());

        SalonPayOrder secondOrder = new SalonPayOrder();
        secondOrder.setOrderNo("order-2");
        secondOrder.setSalonId(2L);
        secondOrder.setTitle("沟通表达");
        secondOrder.setAmount(new BigDecimal("88.00"));
        secondOrder.setStatus("PENDING");
        secondOrder.setCreateTime(new Date());

        when(salonPayOrderService.selectMySalonOrders("user-1")).thenReturn(Arrays.asList(firstOrder, secondOrder));

        List<WxSalonPayOrderDetailVo> orders = service.listMyOrders("user-1");

        assertEquals(2, orders.size());
        assertEquals("order-1", orders.get(0).getOrderNo());
        assertEquals(1L, orders.get(0).getSalonId());
        assertEquals("组局思维", orders.get(0).getTitle());
        assertNotNull(orders.get(0).getCreateTime());
    }

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
    void should_compensate_pending_order_when_query_confirms_paid() throws Exception {
        SalonPayOrder order = new SalonPayOrder();
        order.setId(1L);
        order.setOrderNo("order-1");
        order.setUserId("user-1");
        order.setSalonId(1L);
        order.setAmount(new BigDecimal("128.00"));
        order.setStatus("PENDING");
        SalonInfo salonInfo = new SalonInfo();
        salonInfo.setTitle("组局思维");
        when(salonPayOrderService.selectSalonPayOrderByOrderNo("order-1")).thenReturn(order);
        when(salonPayOrderService.updateSalonPayOrder(order)).thenReturn(1);
        when(salonInfoService.selectSalonInfoById(1L)).thenReturn(salonInfo);

        WxPayOrderQueryV3Result queryResult = new WxPayOrderQueryV3Result();
        queryResult.setTradeState("SUCCESS");
        queryResult.setSuccessTime("2026-04-15T08:30:00+08:00");
        when(wxPayService.queryOrderV3(any(WxPayOrderQueryV3Request.class))).thenReturn(queryResult);

        WxSalonPayOrderDetailVo detail = service.querySalonOrder("user-1", "order-1");

        assertEquals("PAID", detail.getStatus());
        assertNotNull(detail.getPayTime());
        verify(userInfoService).markStudent("user-1");
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
