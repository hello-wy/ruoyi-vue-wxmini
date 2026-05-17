package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.vo.JobAttendanceAdminOrderVo;
import com.ruoyi.system.service.IJobAttendanceAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobAttendanceAdminControllerTest {

    @Mock
    private IJobAttendanceAdminService jobAttendanceAdminService;

    @InjectMocks
    private JobAttendanceAdminController controller;

    @Test
    void orderShouldReturn200() {
        JobAttendanceAdminOrderVo detail = new JobAttendanceAdminOrderVo();
        detail.setOrderNo("order-1");
        when(jobAttendanceAdminService.getOrderAttendance("order-1")).thenReturn(detail);

        AjaxResult result = controller.getOrder("order-1");

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void signInShouldReturn200() {
        JobAttendanceAdminOrderVo detail = new JobAttendanceAdminOrderVo();
        detail.setOrderNo("order-1");
        when(jobAttendanceAdminService.confirmAttendance("order-1", null)).thenReturn(detail);

        AjaxResult result = controller.signIn("order-1");

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }
}
