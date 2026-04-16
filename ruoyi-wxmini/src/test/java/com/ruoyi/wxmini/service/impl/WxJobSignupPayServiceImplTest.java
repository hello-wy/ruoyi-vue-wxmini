package com.ruoyi.wxmini.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.wxmini.bo.WxJobSignupCreateOrderBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.enums.JobSignupOrderStatusEnum;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.vo.WxJobSignupOrderDetailVo;
import com.ruoyi.wxmini.vo.WxPayParamVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxJobSignupPayServiceImplTest {

    @Mock
    private IJobSignupOrderService jobSignupOrderService;
    @Mock
    private IDailyJobsService dailyJobsService;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private WxPayService wxPayService;

    @InjectMocks
    private WxJobSignupPayServiceImpl service;

    @Test
    void should_return_my_order_detail() {
        JobSignupOrder order = new JobSignupOrder();
        order.setOrderNo("order-1");
        order.setUserId("user-1");
        order.setJobId(1L);
        order.setAmount(new BigDecimal("50.00"));
        order.setStatus(JobSignupOrderStatusEnum.PAID.getCode());
        order.setPayTime(new Date());
        DailyJobs job = new DailyJobs();
        job.setId(1L);
        job.setTitle("日结助教");
        when(jobSignupOrderService.selectJobSignupOrderByOrderNo("order-1")).thenReturn(order);
        when(dailyJobsService.selectDailyJobsById(1L)).thenReturn(job);

        WxJobSignupOrderDetailVo detail = service.queryJobOrder("user-1", "order-1");

        assertEquals("日结助教", detail.getJobTitle());
        assertEquals(JobSignupOrderStatusEnum.PAID.getCode(), detail.getStatus());
    }

    @Test
    void should_reject_create_when_paid_order_exists() {
        DailyJobs job = new DailyJobs();
        job.setId(1L);
        job.setTitle("日结助教");
        job.setStatus(0L);
        when(dailyJobsService.selectDailyJobsById(1L)).thenReturn(job);
        when(jobSignupOrderService.selectLatestPaidOrder("user-1", 1L)).thenReturn(new JobSignupOrder());

        WxJobSignupCreateOrderBo bo = new WxJobSignupCreateOrderBo();
        bo.setJobId(1L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.createJobOrder("user-1", bo));
        assertEquals("当前岗位已报名", ex.getMessage());
    }

    @Test
    void should_cancel_old_pending_order_before_create() throws Exception {
        DailyJobs job = new DailyJobs();
        job.setId(1L);
        job.setTitle("日结助教");
        job.setStatus(0L);
        when(dailyJobsService.selectDailyJobsById(1L)).thenReturn(job);
        when(jobSignupOrderService.selectLatestPaidOrder("user-1", 1L)).thenReturn(null);
        JobSignupOrder pendingOrder = new JobSignupOrder();
        pendingOrder.setOrderNo("pending-1");
        pendingOrder.setStatus(JobSignupOrderStatusEnum.PENDING.getCode());
        when(jobSignupOrderService.selectLatestPendingOrder("user-1", 1L)).thenReturn(pendingOrder);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("user-1");
        userInfo.setOpenId("openid-1");
        when(userInfoService.selectUserInfoByUserId("user-1")).thenReturn(userInfo);
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId("wx-appid");
        wxPayConfig.setMchId("mch-id");
        when(wxPayService.getConfig()).thenReturn(wxPayConfig);
        WxPayUnifiedOrderV3Result.JsapiResult jsapiResult = new WxPayUnifiedOrderV3Result.JsapiResult();
        when(wxPayService.createOrderV3(any(), any())).thenReturn(jsapiResult);
        when(jobSignupOrderService.insertJobSignupOrder(any(JobSignupOrder.class))).thenReturn(1);

        WxJobSignupCreateOrderBo bo = new WxJobSignupCreateOrderBo();
        bo.setJobId(1L);

        WxPayParamVo result = service.createJobOrder("user-1", bo);

        assertTrue(result.getOrderNo() != null && !result.getOrderNo().isEmpty());
        assertEquals(JobSignupOrderStatusEnum.CANCELED.getCode(), pendingOrder.getStatus());
        verify(wxPayService).closeOrderV3("pending-1");
        verify(jobSignupOrderService).updateJobSignupOrder(pendingOrder);
        verify(jobSignupOrderService).insertJobSignupOrder(any(JobSignupOrder.class));
    }

    @Test
    void should_mark_order_paid_when_capacity_available() {
        JobSignupOrder order = new JobSignupOrder();
        order.setId(1L);
        order.setOrderNo("order-1");
        order.setUserId("user-1");
        order.setJobId(1L);
        order.setAmount(new BigDecimal("50.00"));
        order.setStatus(JobSignupOrderStatusEnum.PENDING.getCode());
        when(jobSignupOrderService.selectJobSignupOrderByOrderNo("order-1")).thenReturn(order);
        DailyJobs job = new DailyJobs();
        job.setId(1L);
        job.setStatus(0L);
        job.setSignupLimit(2);
        when(dailyJobsService.selectDailyJobsById(1L)).thenReturn(job);
        when(dailyJobsService.selectDailyJobsByIdForUpdate(1L)).thenReturn(job);
        when(dailyJobsService.countPaidSignupOrders(1L, JobSignupOrderStatusEnum.PAID.getCode())).thenReturn(0);
        when(jobSignupOrderService.updateJobSignupOrder(order)).thenReturn(1);

        WxPayNotifyV3Result notifyResult = new WxPayNotifyV3Result();
        WxPayNotifyV3Result.DecryptNotifyResult decrypt = new WxPayNotifyV3Result.DecryptNotifyResult();
        decrypt.setOutTradeNo("order-1");
        decrypt.setTransactionId("wx-1");
        decrypt.setSuccessTime("2026-04-16T10:00:00+08:00");
        notifyResult.setResult(decrypt);

        assertTrue(service.handleJobPaidCallback(notifyResult, "req-1"));
        assertEquals(JobSignupOrderStatusEnum.PAID.getCode(), order.getStatus());
        verify(dailyJobsService, never()).updateDailyJobs(any(DailyJobs.class));
    }

    @Test
    void should_refund_order_when_capacity_full() throws Exception {
        JobSignupOrder order = new JobSignupOrder();
        order.setId(1L);
        order.setOrderNo("order-1");
        order.setUserId("user-1");
        order.setJobId(1L);
        order.setAmount(new BigDecimal("50.00"));
        order.setStatus(JobSignupOrderStatusEnum.PENDING.getCode());
        when(jobSignupOrderService.selectJobSignupOrderByOrderNo("order-1")).thenReturn(order);
        DailyJobs job = new DailyJobs();
        job.setId(1L);
        job.setStatus(0L);
        job.setSignupLimit(1);
        when(dailyJobsService.selectDailyJobsById(1L)).thenReturn(job);
        when(dailyJobsService.selectDailyJobsByIdForUpdate(1L)).thenReturn(job);
        when(dailyJobsService.countPaidSignupOrders(1L, JobSignupOrderStatusEnum.PAID.getCode())).thenReturn(1);
        when(jobSignupOrderService.updateJobSignupOrder(order)).thenReturn(1);

        WxPayNotifyV3Result notifyResult = new WxPayNotifyV3Result();
        WxPayNotifyV3Result.DecryptNotifyResult decrypt = new WxPayNotifyV3Result.DecryptNotifyResult();
        decrypt.setOutTradeNo("order-1");
        decrypt.setTransactionId("wx-1");
        decrypt.setSuccessTime("2026-04-16T10:00:00+08:00");
        notifyResult.setResult(decrypt);

        assertTrue(service.handleJobPaidCallback(notifyResult, "req-1"));
        assertEquals(JobSignupOrderStatusEnum.REFUNDED.getCode(), order.getStatus());
        assertEquals("REForder-1", order.getRefundNo());
        verify(wxPayService).refundV3(any());
    }
}
