package com.ruoyi.wxmini.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.domain.TutoringBinding;
import com.ruoyi.system.domain.TutoringOrder;
import com.ruoyi.system.domain.TutoringSchedule;
import com.ruoyi.system.mapper.ParentsMapper;
import com.ruoyi.system.mapper.TutoringBindingMapper;
import com.ruoyi.system.mapper.TutoringOrderMapper;
import com.ruoyi.system.mapper.TutoringScheduleMapper;
import com.ruoyi.wxmini.bo.WxTutoringCreateOrderBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.vo.WxPayParamVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class WxTutoringPayServiceImplTest {

    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private ParentsMapper parentsMapper;
    @Mock
    private TutoringOrderMapper tutoringOrderMapper;
    @Mock
    private TutoringBindingMapper tutoringBindingMapper;
    @Mock
    private TutoringScheduleMapper tutoringScheduleMapper;
    @Mock
    private WxPayService wxPayService;

    @InjectMocks
    private WxTutoringPayServiceImpl service;

    @Test
    void should_create_new_order_for_binding_with_paid_history() throws Exception {
        UserInfo currentUser = buildParentUser();
        TutoringBinding binding = buildBinding(1);
        Parents parent = buildParent();
        WxTutoringCreateOrderBo bo = new WxTutoringCreateOrderBo();
        bo.setBindingId(21L);
        WxPayUnifiedOrderV3Result.JsapiResult jsapiResult = new WxPayUnifiedOrderV3Result.JsapiResult();

        setBasePayService();
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(currentUser);
        when(tutoringBindingMapper.selectById(21L)).thenReturn(binding);
        when(tutoringOrderMapper.selectLatestByBindingId(21L)).thenReturn(buildPaidOrder());
        when(parentsMapper.selectParentsById(31L)).thenReturn(parent);
        when(wxPayService.getConfig()).thenReturn(buildWxPayConfig());
        when(wxPayService.createOrderV3(any(), any())).thenReturn(jsapiResult);
        when(tutoringOrderMapper.insertTutoringOrder(any(TutoringOrder.class))).thenReturn(1);

        WxPayParamVo result = service.createOrder("wx-user-1", bo);

        assertNotNull(result);
        assertSame(jsapiResult, result.getPayParam());
        verify(wxPayService, never()).closeOrderV3("TORDER-PAID");
    }

    @Test
    void should_calculate_order_amount_from_selected_dates_hours_and_unit_price() throws Exception {
        UserInfo currentUser = buildParentUser();
        TutoringBinding binding = buildBinding(0);
        Parents parent = buildParent();
        WxTutoringCreateOrderBo bo = new WxTutoringCreateOrderBo();
        bo.setBindingId(21L);
        bo.setServiceTimes("[{\"serviceDate\":\"2026-05-20\",\"startTime\":\"18:00\",\"endTime\":\"20:30\"},{\"serviceDate\":\"2026-05-21\",\"startTime\":\"18:00\",\"endTime\":\"20:30\"},{\"serviceDate\":\"2026-05-22\",\"startTime\":\"09:00\",\"endTime\":\"10:00\"}]");
        WxPayUnifiedOrderV3Result.JsapiResult jsapiResult = new WxPayUnifiedOrderV3Result.JsapiResult();
        ArgumentCaptor<TutoringOrder> orderCaptor = ArgumentCaptor.forClass(TutoringOrder.class);

        setBasePayService();
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(currentUser);
        when(tutoringBindingMapper.selectById(21L)).thenReturn(binding);
        when(tutoringOrderMapper.selectLatestByBindingId(21L)).thenReturn(null);
        when(parentsMapper.selectParentsById(31L)).thenReturn(parent);
        when(wxPayService.getConfig()).thenReturn(buildWxPayConfig());
        when(wxPayService.createOrderV3(any(), any())).thenReturn(jsapiResult);
        when(tutoringOrderMapper.insertTutoringOrder(any(TutoringOrder.class))).thenReturn(1);

        service.createOrder("wx-user-1", bo);

        verify(tutoringOrderMapper).insertTutoringOrder(orderCaptor.capture());
        TutoringOrder order = orderCaptor.getValue();
        assertEquals(3, order.getLessonCount());
        assertEquals(new BigDecimal("720.00"), order.getTotalAmount());
    }

    @Test
    void should_not_close_other_parent_bindings_after_payment_success() {
        TutoringOrder order = buildPendingOrder();
        TutoringBinding binding = buildBinding(0);
        when(tutoringOrderMapper.selectByOrderNoForUpdate("TORDER-1")).thenReturn(order);
        when(tutoringOrderMapper.updateTutoringOrder(any(TutoringOrder.class))).thenReturn(1);
        when(tutoringBindingMapper.selectByIdForUpdate(21L)).thenReturn(binding);
        when(tutoringBindingMapper.updateTutoringBinding(any(TutoringBinding.class))).thenReturn(1);
        when(tutoringScheduleMapper.countByOrderId(11L)).thenReturn(1);

        assertTrue(service.updOrderWithPaySuccess("TORDER-1"));

        verify(tutoringBindingMapper, never()).closeBindingsByParentId(any(), any(), any());
    }

    @Test
    void should_pay_existing_pending_admin_order_without_creating_new_business_order() throws Exception {
        UserInfo currentUser = new UserInfo();
        currentUser.setId(41L);
        currentUser.setUserId("wx-user-1");
        currentUser.setOpenId("openid-1");

        TutoringOrder order = new TutoringOrder();
        order.setId(11L);
        order.setOrderNo("TORDER-1");
        order.setBindingId(21L);
        order.setParentUserId(41L);
        order.setTotalAmount(new BigDecimal("420.00"));
        order.setStatus(0);

        WxPayUnifiedOrderV3Result.JsapiResult jsapiResult = new WxPayUnifiedOrderV3Result.JsapiResult();
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(currentUser);
        when(tutoringOrderMapper.selectByOrderNo("TORDER-1")).thenReturn(order);
        when(wxPayService.getConfig()).thenReturn(buildWxPayConfig());
        when(wxPayService.createOrderV3(any(), any())).thenReturn(jsapiResult);
        when(tutoringOrderMapper.updateTutoringOrder(any(TutoringOrder.class))).thenReturn(1);

        WxPayParamVo result = service.payPendingOrder("wx-user-1", "TORDER-1");

        assertEquals("TORDER-1", result.getOrderNo());
        assertSame(jsapiResult, result.getPayParam());
        verify(tutoringOrderMapper, never()).insertTutoringOrder(any(TutoringOrder.class));
        verify(tutoringOrderMapper).updateTutoringOrder(any(TutoringOrder.class));
    }

    @Test
    void should_generate_schedules_only_once_when_payment_callback_repeats() {
        TutoringOrder order = new TutoringOrder();
        order.setId(11L);
        order.setBindingId(21L);
        order.setOrderNo("TORDER-1");
        order.setParentId(31L);
        order.setParentUserId(41L);
        order.setTutorId(51L);
        order.setTutorUserId(61L);
        order.setHourlyPrice(new BigDecimal("120.00"));
        order.setCommissionRate(new BigDecimal("10.00"));
        order.setServiceTimesSnapshot("[{\"serviceDate\":\"2026-05-20\",\"startTime\":\"18:00\",\"endTime\":\"20:00\"}]");
        order.setStatus(0);

        TutoringBinding binding = new TutoringBinding();
        binding.setId(21L);
        binding.setStatus(0);

        when(tutoringOrderMapper.selectByOrderNoForUpdate("TORDER-1")).thenReturn(order);
        when(tutoringOrderMapper.updateTutoringOrder(any(TutoringOrder.class))).thenReturn(1);
        when(tutoringBindingMapper.selectByIdForUpdate(21L)).thenReturn(binding);
        when(tutoringBindingMapper.updateTutoringBinding(any(TutoringBinding.class))).thenReturn(1);
        when(tutoringScheduleMapper.countByOrderId(11L)).thenReturn(0, 1);
        when(tutoringScheduleMapper.batchInsertTutoringSchedules(any())).thenReturn(1);

        WxPayNotifyV3Result notifyResult = new WxPayNotifyV3Result();
        WxPayNotifyV3Result.DecryptNotifyResult decrypt = new WxPayNotifyV3Result.DecryptNotifyResult();
        decrypt.setOutTradeNo("TORDER-1");
        decrypt.setTransactionId("wx-tutoring-1");
        decrypt.setSuccessTime("2026-05-19T10:00:00+08:00");
        notifyResult.setResult(decrypt);

        assertTrue(service.handleTutoringPaidCallback(notifyResult, "req-1"));
        assertTrue(service.handleTutoringPaidCallback(notifyResult, "req-1"));
        verify(tutoringScheduleMapper, times(1)).batchInsertTutoringSchedules(any());
    }

    @Test
    void should_reject_repeated_parent_confirmation() {
        UserInfo currentUser = buildParentUser();
        TutoringSchedule schedule = buildWaitingParentConfirmSchedule();
        schedule.setConfirmTime(new Date());

        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(currentUser);
        when(tutoringScheduleMapper.selectByIdForUpdate(101L)).thenReturn(schedule);

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.confirmSchedule("wx-user-1", 101L, "再次确认"));

        assertEquals("家长已提交确认，等待管理员审核", error.getMessage());
        verify(tutoringScheduleMapper, never()).updateTutoringSchedule(any());
    }

    private UserInfo buildParentUser() {
        UserInfo currentUser = new UserInfo();
        currentUser.setId(41L);
        currentUser.setUserId("wx-user-1");
        currentUser.setOpenId("openid-1");
        return currentUser;
    }

    private TutoringBinding buildBinding(int status) {
        TutoringBinding binding = new TutoringBinding();
        binding.setId(21L);
        binding.setParentId(31L);
        binding.setParentUserId(41L);
        binding.setTutorId(51L);
        binding.setTutorUserId(61L);
        binding.setStatus(status);
        binding.setServiceTimesSnapshot("[{\"serviceDate\":\"2026-05-20\",\"startTime\":\"18:00\",\"endTime\":\"20:00\"}]");
        return binding;
    }

    private Parents buildParent() {
        Parents parent = new Parents();
        parent.setId(31L);
        parent.setSubject("数学");
        parent.setHourlyBudget(new BigDecimal("120.00"));
        return parent;
    }

    private WxPayConfig buildWxPayConfig() {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId("wx-app-id");
        wxPayConfig.setMchId("mch-id");
        return wxPayConfig;
    }

    private void setBasePayService() {
        ReflectionTestUtils.setField(
                service,
                com.ruoyi.wxmini.service.AbsWxPayBaseService.class,
                "wxPayService",
                wxPayService,
                WxPayService.class
        );
    }

    private TutoringOrder buildPendingOrder() {
        TutoringOrder order = new TutoringOrder();
        order.setId(11L);
        order.setBindingId(21L);
        order.setOrderNo("TORDER-1");
        order.setParentId(31L);
        order.setParentUserId(41L);
        order.setTutorId(51L);
        order.setTutorUserId(61L);
        order.setHourlyPrice(new BigDecimal("120.00"));
        order.setCommissionRate(new BigDecimal("10.00"));
        order.setServiceTimesSnapshot("[{\"serviceDate\":\"2026-05-20\",\"startTime\":\"18:00\",\"endTime\":\"20:00\"}]");
        order.setStatus(0);
        return order;
    }

    private TutoringOrder buildPaidOrder() {
        TutoringOrder order = buildPendingOrder();
        order.setOrderNo("TORDER-PAID");
        order.setStatus(1);
        return order;
    }

    private TutoringSchedule buildWaitingParentConfirmSchedule() {
        TutoringSchedule schedule = new TutoringSchedule();
        schedule.setId(101L);
        schedule.setParentUserId(41L);
        schedule.setTutorUserId(61L);
        schedule.setStatus(1);
        schedule.setFinishTime(new Date());
        return schedule;
    }

}
