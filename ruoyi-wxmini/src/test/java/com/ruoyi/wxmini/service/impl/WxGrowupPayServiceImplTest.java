package com.ruoyi.wxmini.service.impl;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.domain.TradeOrder;
import com.ruoyi.system.service.ILecturesService;
import com.ruoyi.system.service.IStudentEnrollmentService;
import com.ruoyi.system.service.ITradeOrderService;
import com.ruoyi.wxmini.bo.WxGrowupCourseEnrollBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.vo.WxGrowupCourseOrderVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxGrowupPayServiceImplTest {

    @Mock
    private ILecturesService lecturesService;
    @Mock
    private IStudentEnrollmentService studentEnrollmentService;
    @Mock
    private ITradeOrderService tradeOrderService;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private WxPayService wxPayService;

    @InjectMocks
    private WxGrowupPayServiceImpl service;

    @Test
    void createCourseOrderShouldUseCourseDepositAndDecreaseCourseEnrollment() throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(10L);
        userInfo.setOpenId("openid-1");
        Lectures course = new Lectures();
        course.setId(99L);
        course.setName("幸福解码");
        course.setDeposit(new BigDecimal("30.00"));
        WxGrowupCourseEnrollBo bo = new WxGrowupCourseEnrollBo();
        bo.setName("张三");
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(userInfo);
        when(lecturesService.selectLecturesById(99L)).thenReturn(course);
        when(wxPayService.getConfig()).thenReturn(buildWxPayConfig());
        when(wxPayService.createOrderV3(any(), any())).thenReturn(new WxPayUnifiedOrderV3Result.JsapiResult());
        when(tradeOrderService.insertTradeOrder(any(TradeOrder.class))).thenReturn(1);

        service.createCourseOrder("wx-user-1", 99L, bo);

        verify(studentEnrollmentService).decreaseRemain(10L, 99L, 1);
        verify(tradeOrderService).insertTradeOrder(any(TradeOrder.class));
    }

    @Test
    void buildOrderParamShouldConvertDepositToCents() {
        WxGrowupCourseOrderVo payVo = new WxGrowupCourseOrderVo();
        payVo.setCourseId(99L);
        payVo.setCourseName("幸福解码");
        payVo.setDeposit(new BigDecimal("30.00"));
        payVo.setOpenId("openid-1");

        assertEquals(3000, service.buildOrderParam("wx-user-1", payVo, new HashMap<>()).getAmount());
    }

    @Test
    void buildOrderParamShouldUseWechatAllowedOutTradeNo() {
        WxGrowupCourseOrderVo payVo = new WxGrowupCourseOrderVo();
        payVo.setCourseId(99L);
        payVo.setCourseName("幸福解码");
        payVo.setDeposit(new BigDecimal("30.00"));
        payVo.setOpenId("openid-1");

        String orderNo = service.buildOrderParam("wx-user-1", payVo, new HashMap<>()).getOrderNo();

        assertTrue(orderNo.length() <= 32, "微信商户订单号长度不能超过32位");
        assertTrue(orderNo.matches("GRW\\d{27}"));
    }

    @Test
    void createCourseOrderShouldExposeMissingDepositConfiguration() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(10L);
        userInfo.setOpenId("openid-1");
        Lectures course = new Lectures();
        course.setId(99L);
        course.setName("幸福解码");
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(userInfo);
        when(lecturesService.selectLecturesById(99L)).thenReturn(course);

        ServiceException error = assertThrows(ServiceException.class, () ->
                service.createCourseOrder("wx-user-1", 99L, new WxGrowupCourseEnrollBo()));

        assertTrue(error.getMessage().contains("课程押金未配置"));
    }

    private WxPayConfig buildWxPayConfig() {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId("wx-appid");
        wxPayConfig.setMchId("mch-id");
        return wxPayConfig;
    }
}
