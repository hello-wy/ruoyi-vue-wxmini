package com.ruoyi.wxmini.service.impl;

import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.domain.TradeOrder;
import com.ruoyi.system.service.ILecturesService;
import com.ruoyi.system.service.IStudentEnrollmentService;
import com.ruoyi.system.service.ITradeOrderService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxGrowupPayServiceImplTest {

    @Mock private ILecturesService lecturesService;
    @Mock private IStudentEnrollmentService studentEnrollmentService;
    @Mock private ITradeOrderService tradeOrderService;
    @Mock private IUserInfoService userInfoService;
    @InjectMocks private WxGrowupPayServiceImpl service;

    @Test
    void paidQueryCompensationShouldMarkStudentIdempotently() {
        TradeOrder order = new TradeOrder();
        order.setOrderNo("GRW1");
        order.setUserId(7L);
        order.setLectureId(3L);
        order.setPayStatus(1L);
        order.setPayTime(new Date());
        UserInfo user = new UserInfo();
        user.setUserId("user-7");
        when(tradeOrderService.selectTradeOrderList(org.mockito.ArgumentMatchers.any(TradeOrder.class)))
                .thenReturn(Collections.singletonList(order));
        when(userInfoService.selectUserInfoById(7L)).thenReturn(user);

        assertTrue(service.updOrderWithPaySuccess("GRW1"));

        verify(userInfoService).markStudent("user-7");
    }
}
