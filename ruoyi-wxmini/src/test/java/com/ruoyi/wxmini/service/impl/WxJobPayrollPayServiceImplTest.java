package com.ruoyi.wxmini.service.impl;

import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.wxmini.bo.WxPayCreateOrderParam;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.vo.WxJobPayrollOrderDetailVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxJobPayrollPayServiceImplTest {

    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private WxPayService wxPayService;

    @InjectMocks
    private WxJobPayrollPayServiceImpl service;

    @Test
    void should_build_payroll_order_no_within_wechat_limit() {
        UserInfo merchant = new UserInfo();
        merchant.setId(1L);
        merchant.setUserId("merchant-1");
        merchant.setOpenId("openid-1");
        when(userInfoService.selectUserInfoByUserId("merchant-1")).thenReturn(merchant);

        WxJobPayrollOrderDetailVo payVo = new WxJobPayrollOrderDetailVo();
        payVo.setJobId(10L);
        payVo.setJobTitle("兼职工资：助教");
        payVo.setTotalAmount(new BigDecimal("200.00"));

        WxPayCreateOrderParam orderParam = service.buildOrderParam("merchant-1", payVo, new HashMap<>());

        assertTrue(orderParam.getOrderNo().length() <= 32, "微信商户订单号长度不能超过32位");
        assertTrue(orderParam.getOrderNo().matches("PR\\d{27}"));
    }
}
