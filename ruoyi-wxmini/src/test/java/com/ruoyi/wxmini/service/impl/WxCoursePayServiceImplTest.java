package com.ruoyi.wxmini.service.impl;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.service.ICoursePayOrderService;
import com.ruoyi.system.service.ILecturesService;
import com.ruoyi.system.service.IStudentEnrollmentService;
import com.ruoyi.wxmini.bo.WxCoursePayCreateOrderBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxCoursePayServiceImplTest {

    @Mock
    private ILecturesService lecturesService;
    @Mock
    private ICoursePayOrderService coursePayOrderService;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private IStudentEnrollmentService studentEnrollmentService;
    @Mock
    private WxPayService wxPayService;

    @InjectMocks
    private WxCoursePayServiceImpl service;

    @Test
    void createCourseOrderShouldBlockWhenRequiredEnrollmentIsMissing() {
        when(lecturesService.selectLecturesById(99L)).thenReturn(requiredEnrollmentCourse());
        when(coursePayOrderService.selectLatestPaidOrder("wx-user-1", 99L)).thenReturn(null);
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(openIdUser());
        doThrow(new ServiceException("未找到对应的学籍记录"))
                .when(studentEnrollmentService).assertCourseEnrollmentAvailable(10L, 99L);

        ServiceException error = assertThrows(ServiceException.class, () ->
                service.createCourseOrder("wx-user-1", createBo()));

        assertEquals("未找到对应的学籍记录", error.getMessage());
    }

    @Test
    void createCourseOrderShouldCheckEnrollmentWhenCourseRequiresIt() throws Exception {
        when(lecturesService.selectLecturesById(99L)).thenReturn(requiredEnrollmentCourse());
        when(coursePayOrderService.selectLatestPaidOrder("wx-user-1", 99L)).thenReturn(null);
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(openIdUser());
        when(wxPayService.getConfig()).thenReturn(buildWxPayConfig());
        when(wxPayService.createOrderV3(any(), any())).thenReturn(new WxPayUnifiedOrderV3Result.JsapiResult());
        when(coursePayOrderService.insertCoursePayOrder(any(CoursePayOrder.class))).thenReturn(1);

        service.createCourseOrder("wx-user-1", createBo());

        verify(studentEnrollmentService).assertCourseEnrollmentAvailable(10L, 99L);
    }

    @Test
    void createCourseOrderShouldSkipEnrollmentCheckWhenCourseDoesNotRequireIt() throws Exception {
        Lectures course = requiredEnrollmentCourse();
        course.setRequiresEnrollment(false);
        when(lecturesService.selectLecturesById(99L)).thenReturn(course);
        when(coursePayOrderService.selectLatestPaidOrder("wx-user-1", 99L)).thenReturn(null);
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(openIdUser());
        when(wxPayService.getConfig()).thenReturn(buildWxPayConfig());
        when(wxPayService.createOrderV3(any(), any())).thenReturn(new WxPayUnifiedOrderV3Result.JsapiResult());
        when(coursePayOrderService.insertCoursePayOrder(any(CoursePayOrder.class))).thenReturn(1);

        service.createCourseOrder("wx-user-1", createBo());

        verify(studentEnrollmentService, never()).assertCourseEnrollmentAvailable(any(), any());
    }

    @Test
    void createCourseOrderShouldSendWechatAllowedOutTradeNo() throws Exception {
        when(lecturesService.selectLecturesById(99L)).thenReturn(requiredEnrollmentCourse());
        when(coursePayOrderService.selectLatestPaidOrder("wx-user-1", 99L)).thenReturn(null);
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(openIdUser());
        when(wxPayService.getConfig()).thenReturn(buildWxPayConfig());
        when(wxPayService.createOrderV3(any(), any())).thenReturn(new WxPayUnifiedOrderV3Result.JsapiResult());
        when(coursePayOrderService.insertCoursePayOrder(any(CoursePayOrder.class))).thenReturn(1);

        service.createCourseOrder("wx-user-1", createBo());

        org.mockito.ArgumentCaptor<WxPayUnifiedOrderV3Request> captor =
                forClass(WxPayUnifiedOrderV3Request.class);
        verify(wxPayService).createOrderV3(any(), captor.capture());
        String outTradeNo = captor.getValue().getOutTradeNo();
        assertTrue(outTradeNo.length() <= 32, "微信商户订单号长度不能超过32位");
        assertTrue(outTradeNo.matches("CRS\\d{27}"));
    }

    private WxCoursePayCreateOrderBo createBo() {
        WxCoursePayCreateOrderBo bo = new WxCoursePayCreateOrderBo();
        bo.setCourseId(99L);
        bo.setName("张三");
        bo.setPhone("13800000000");
        return bo;
    }

    private Lectures requiredEnrollmentCourse() {
        Lectures course = new Lectures();
        course.setId(99L);
        course.setName("幸福解码");
        course.setRegistrationFee(new BigDecimal("100.00"));
        course.setRequiresEnrollment(true);
        return course;
    }

    private UserInfo openIdUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(10L);
        userInfo.setOpenId("openid-1");
        return userInfo;
    }

    private WxPayConfig buildWxPayConfig() {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId("wx-appid");
        wxPayConfig.setMchId("mch-id");
        return wxPayConfig;
    }
}
