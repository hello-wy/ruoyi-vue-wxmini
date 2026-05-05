package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.wxmini.bo.WxMerchantJobStatusUpdateBo;
import com.ruoyi.wxmini.service.IWxJobScheduleService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import com.ruoyi.wxmini.vo.WxJobScheduleVo;
import com.ruoyi.wxmini.vo.WxMerchantJobVo;
import com.ruoyi.wxmini.vo.WxSignupUserVo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxJobScheduleControllerTest {

    @Mock
    private IWxJobScheduleService wxJobScheduleService;

    @InjectMocks
    private WxJobScheduleController controller;

    @AfterEach
    void clearContext() {
        WxMiniUserContext.clear();
    }

    @Test
    void mySchedulesShouldReturn200() {
        WxMiniUserContext.setCurrentUserId("student-1");
        when(wxJobScheduleService.listMySchedules("student-1")).thenReturn(Collections.singletonList(new WxJobScheduleVo()));

        AjaxResult result = controller.mySchedules();

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void signupUsersShouldReturn200() {
        WxMiniUserContext.setCurrentUserId("merchant-1");
        when(wxJobScheduleService.listSignupUsers("merchant-1", 1L, "张")).thenReturn(Collections.singletonList(new WxSignupUserVo()));

        AjaxResult result = controller.signupUsers(1L, "张");

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void myPublishedJobsShouldReturn200() {
        WxMiniUserContext.setCurrentUserId("merchant-1");
        when(wxJobScheduleService.listMerchantJobs("merchant-1")).thenReturn(Collections.singletonList(new WxMerchantJobVo()));

        AjaxResult result = controller.myPublishedJobs();

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void updateJobStatusShouldReturn200() {
        WxMiniUserContext.setCurrentUserId("merchant-1");
        WxMerchantJobStatusUpdateBo bo = new WxMerchantJobStatusUpdateBo();
        bo.setStatus(3L);

        AjaxResult result = controller.updateJobStatus(1L, bo);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        verify(wxJobScheduleService).updateMerchantJobStatus("merchant-1", 1L, 3L);
    }
}
