package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.bo.CourseScanSignInBo;
import com.ruoyi.system.domain.vo.CourseScanSignInVo;
import com.ruoyi.system.service.ICoursePayOrderService;
import com.ruoyi.wxmini.service.IWxRefundService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseAttendanceControllerTest {
    private static final String ORDER_NO = "CRS1";
    private static final String USER_ID = "user-1";
    private static final Long COURSE_ID = 1L;
    private static final String REFUND_REASON = "管理员扫码签到后自动退款";

    @Mock
    private ICoursePayOrderService coursePayOrderService;
    @Mock
    private IWxRefundService wxRefundService;
    @InjectMocks
    private CourseAttendanceController controller;

    @Test
    void scanSignInShouldRefundTheSignedOrderAndReturnRefundedStatus() {
        CourseScanSignInVo signInResult = new CourseScanSignInVo();
        signInResult.setOrderNo(ORDER_NO);
        CoursePayOrder refundedOrder = new CoursePayOrder();
        Date refundTime = new Date();
        refundedOrder.setStatus(CoursePayOrder.STATUS_REFUNDED);
        refundedOrder.setRefundTime(refundTime);
        CourseScanSignInBo request = new CourseScanSignInBo();
        request.setUserId(USER_ID);
        when(coursePayOrderService.scanSignInVo(COURSE_ID, USER_ID)).thenReturn(signInResult);
        when(coursePayOrderService.selectCoursePayOrderByOrderNo(ORDER_NO)).thenReturn(refundedOrder);

        AjaxResult result = controller.scanSignIn(COURSE_ID, request);

        InOrder calls = inOrder(coursePayOrderService, wxRefundService);
        calls.verify(coursePayOrderService).scanSignInVo(COURSE_ID, USER_ID);
        calls.verify(wxRefundService).refundCourseOrder(ORDER_NO, REFUND_REASON);
        calls.verify(coursePayOrderService).selectCoursePayOrderByOrderNo(ORDER_NO);
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals("签到成功，已原路退款", result.get(AjaxResult.MSG_TAG));
        assertEquals(signInResult, result.get(AjaxResult.DATA_TAG));
        assertEquals(CoursePayOrder.STATUS_REFUNDED, signInResult.getStatus());
        assertEquals(refundTime, signInResult.getRefundTime());
    }
}
