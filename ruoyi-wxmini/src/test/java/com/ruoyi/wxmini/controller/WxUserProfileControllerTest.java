package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.wxmini.bo.WxUserProfileUpdateBo;
import com.ruoyi.wxmini.domain.vo.WxUserProfileVo;
import com.ruoyi.wxmini.service.IWxUserProfileService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxUserProfileControllerTest {

    private static final String USER_ID = "user-123";

    @Mock
    private IWxUserProfileService wxUserProfileService;

    @InjectMocks
    private WxUserProfileController controller;

    @AfterEach
    void clearContext() {
        WxMiniUserContext.clear();
    }

    @Test
    void detailShouldReturnNon200WhenNotLoggedIn() {
        AjaxResult result = controller.detail();
        assertEquals(false, Integer.valueOf(200).equals(result.get(AjaxResult.CODE_TAG)));
    }

    @Test
    void detailShouldReturn200WhenUserExists() {
        WxUserProfileVo profile = new WxUserProfileVo();
        profile.setUserInfoId(1L);
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(wxUserProfileService.getCurrentUserProfile(USER_ID)).thenReturn(profile);
        AjaxResult result = controller.detail();
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void updateShouldReturnNon200WhenSaveFailed() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(wxUserProfileService.updateCurrentUserProfile(eq(USER_ID), any(WxUserProfileUpdateBo.class))).thenReturn(0);
        AjaxResult result = controller.update(new WxUserProfileUpdateBo());
        assertEquals(false, Integer.valueOf(200).equals(result.get(AjaxResult.CODE_TAG)));
    }
}
