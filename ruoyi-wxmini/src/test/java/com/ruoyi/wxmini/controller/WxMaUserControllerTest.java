package com.ruoyi.wxmini.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.wxmini.bo.WxPhoneCodeRequest;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import com.ruoyi.wxmini.vo.WxPhoneInfoVO;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxMaUserControllerTest {

    private static final String APP_ID = "wx-test-app";
    private static final String PHONE_CODE = "phone-code";
    private static final String PHONE = "13800138000";
    private static final String USER_ID = "user-123";

    @Mock
    private WxMaService wxMaService;
    @Mock
    private WxMaUserService wxMaUserService;
    @Mock
    private IUserInfoService userInfoService;

    @InjectMocks
    private WxMaUserController controller;

    @AfterEach
    void clearContext() {
        WxMiniUserContext.clear();
    }

    @Test
    void phoneShouldPersistRealtimePhoneForCurrentUser() throws WxErrorException {
        WxPhoneCodeRequest request = new WxPhoneCodeRequest();
        request.setAppid(APP_ID);
        request.setPhoneCode(PHONE_CODE);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserId(USER_ID);
        userInfo.setPhone(null);
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(wxMaService.switchover(APP_ID)).thenReturn(true);
        when(wxMaService.getUserService()).thenReturn(wxMaUserService);
        when(wxMaUserService.getPhoneNumber(PHONE_CODE)).thenReturn(buildPhoneInfo());
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);

        AjaxResult result = controller.phone(request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        WxPhoneInfoVO data = (WxPhoneInfoVO) result.get(AjaxResult.DATA_TAG);
        assertEquals(PHONE, data.getPhone());

        ArgumentCaptor<UserInfo> userCaptor = ArgumentCaptor.forClass(UserInfo.class);
        verify(userInfoService).updateUserInfo(userCaptor.capture());
        assertEquals(PHONE, userCaptor.getValue().getPhone());
    }

    @Test
    void phoneShouldFailWhenCurrentUserDoesNotExist() {
        WxPhoneCodeRequest request = new WxPhoneCodeRequest();
        request.setAppid(APP_ID);
        request.setPhoneCode(PHONE_CODE);
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(wxMaService.switchover(APP_ID)).thenReturn(true);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(null);

        AjaxResult result = controller.phone(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("user not found", result.get(AjaxResult.MSG_TAG));
        assertNull(result.get(AjaxResult.DATA_TAG));
    }

    private WxMaPhoneNumberInfo buildPhoneInfo() {
        WxMaPhoneNumberInfo phoneNumberInfo = new WxMaPhoneNumberInfo();
        phoneNumberInfo.setPhoneNumber(PHONE);
        assertTrue(phoneNumberInfo.getPhoneNumber().length() > 0);
        return phoneNumberInfo;
    }
}
