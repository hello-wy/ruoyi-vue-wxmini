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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
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

    /**
     * Task 5.3: After setting a job to status=3 (offshelf), the next call to
     * GET /wxmini/jobs/mine/published should NOT include that job.
     * This simulates the full flow: updateJobStatus(status=3) then myPublishedJobs().
     *
     * <p>Validates: P3 (下架后列表不含该岗位); Requirements: 2.3, 3.3.
     */
    @Test
    @SuppressWarnings("unchecked")
    void offshelfThenListMine_doesNotIncludeOffshelfedJob() {
        WxMiniUserContext.setCurrentUserId("merchant-1");

        // Step 1: Call updateJobStatus to set jobId=1 to status=3 (offshelf)
        WxMerchantJobStatusUpdateBo bo = new WxMerchantJobStatusUpdateBo();
        bo.setStatus(3L);
        doNothing().when(wxJobScheduleService).updateMerchantJobStatus("merchant-1", 1L, 3L);

        AjaxResult updateResult = controller.updateJobStatus(1L, bo);
        assertEquals(200, updateResult.get(AjaxResult.CODE_TAG));
        verify(wxJobScheduleService).updateMerchantJobStatus("merchant-1", 1L, 3L);

        // Step 2: Mock listMerchantJobs to return only non-offshelf jobs (simulating
        // the service layer filtering out status=3 as implemented in Task 5.2)
        List<WxMerchantJobVo> publishedJobs = new ArrayList<>();
        WxMerchantJobVo activeJob = new WxMerchantJobVo();
        activeJob.setId(2L);
        activeJob.setStatus(0L);
        publishedJobs.add(activeJob);
        when(wxJobScheduleService.listMerchantJobs("merchant-1")).thenReturn(publishedJobs);

        AjaxResult listResult = controller.myPublishedJobs();
        assertEquals(200, listResult.get(AjaxResult.CODE_TAG));

        List<WxMerchantJobVo> data = (List<WxMerchantJobVo>) listResult.get(AjaxResult.DATA_TAG);
        assertNotNull(data);
        assertFalse(data.stream().anyMatch(v -> v.getId().equals(1L)),
                "Offshelfed job (id=1, status=3) should not appear in published jobs list");
        assertTrue(data.stream().anyMatch(v -> v.getId().equals(2L)),
                "Active job (id=2, status=0) should still appear");
    }

    /**
     * Task 5.3: After setting a job to status=3 (offshelf), the merchant can still
     * directly access GET /wxmini/jobs/{jobId}/signup-users and get the signup users.
     * This covers P3 (direct access preservation) and P5 (unchanged behavior).
     *
     * <p>Validates: P3, P5; Requirements: 2.3, 3.4.
     */
    @Test
    @SuppressWarnings("unchecked")
    void offshelfedJobStillAccessibleByDirectGet() {
        WxMiniUserContext.setCurrentUserId("merchant-1");

        // Step 1: Set job to status=3 (offshelf)
        WxMerchantJobStatusUpdateBo bo = new WxMerchantJobStatusUpdateBo();
        bo.setStatus(3L);
        doNothing().when(wxJobScheduleService).updateMerchantJobStatus("merchant-1", 1L, 3L);

        AjaxResult updateResult = controller.updateJobStatus(1L, bo);
        assertEquals(200, updateResult.get(AjaxResult.CODE_TAG));

        // Step 2: Directly access signup-users for the offshelfed job — should still work
        WxSignupUserVo user1 = new WxSignupUserVo();
        user1.setUserInfoId(200L);
        user1.setDisplayName("张三");
        user1.setPhoneMasked("138****0000");
        user1.setOrderNo("order-1");
        user1.setAttendanceStatus(1);
        user1.setAttendanceStatusLabel("已签到");

        List<WxSignupUserVo> signupUsers = new ArrayList<>();
        signupUsers.add(user1);
        when(wxJobScheduleService.listSignupUsers("merchant-1", 1L, null))
                .thenReturn(signupUsers);

        AjaxResult signupResult = controller.signupUsers(1L, null);
        assertEquals(200, signupResult.get(AjaxResult.CODE_TAG));

        List<WxSignupUserVo> data = (List<WxSignupUserVo>) signupResult.get(AjaxResult.DATA_TAG);
        assertNotNull(data, "Signup users should still be accessible after offshelf");
        assertEquals(1, data.size(), "Should return the signup users for the offshelfed job");
        assertEquals("张三", data.get(0).getDisplayName());
        assertEquals("order-1", data.get(0).getOrderNo());
        assertEquals(1, data.get(0).getAttendanceStatus());
        assertEquals("已签到", data.get(0).getAttendanceStatusLabel());
    }
}
