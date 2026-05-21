package com.ruoyi.wxmini.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.ruoyi.system.domain.TutoringBinding;
import com.ruoyi.system.domain.TutoringOrder;
import com.ruoyi.system.mapper.TutoringBindingMapper;
import com.ruoyi.system.mapper.TutoringOrderMapper;
import com.ruoyi.system.mapper.TutoringScheduleMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxTutoringPayServiceImplTest {

    @Mock
    private TutoringOrderMapper tutoringOrderMapper;
    @Mock
    private TutoringBindingMapper tutoringBindingMapper;
    @Mock
    private TutoringScheduleMapper tutoringScheduleMapper;

    @InjectMocks
    private WxTutoringPayServiceImpl service;

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
}
