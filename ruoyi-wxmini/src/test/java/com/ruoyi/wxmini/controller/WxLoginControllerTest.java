package com.ruoyi.wxmini.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.wxmini.bo.WxUserInfo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IWxMiniJwtService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentCaptor.forClass;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxLoginControllerTest {

    private static final String APP_ID = "wx-test-app";
    private static final String LOGIN_CODE = "login-code";
    private static final String OPEN_ID = "openid-123";
    private static final String UNION_ID = "unionid-456";
    private static final String SESSION_KEY = "session-key";
    private static final String PHONE = "13800138000";
    private static final String PHONE_CODE = "phone-code";

    @Mock
    private WxMaService wxMaService;
    @Mock
    private WxMaUserService wxMaUserService;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private IWxMiniJwtService jwtService;

    @InjectMocks
    private WxLoginController controller;

    @Test
    void loginShouldReturnDocumentedUserFieldsForExistingUser() throws WxErrorException {
        UserInfo userInfo = buildUserInfo();
        when(wxMaService.switchover(APP_ID)).thenReturn(true);
        when(wxMaService.getUserService()).thenReturn(wxMaUserService);
        when(wxMaUserService.getSessionInfo(LOGIN_CODE)).thenReturn(buildSession());
        when(userInfoService.selectUserInfoByOpenId(OPEN_ID)).thenReturn(userInfo);
        when(jwtService.createToken(userInfo.getUserId())).thenReturn("jwt-token");

        AjaxResult result = controller.login(APP_ID, LOGIN_CODE, null, null);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        WxUserInfo data = (WxUserInfo) result.get(AjaxResult.DATA_TAG);
        assertEquals("jwt-token", data.getApiToken());
        assertEquals(SESSION_KEY, data.getSessionKey());
        assertEquals(OPEN_ID, data.getOpenId());
        assertEquals("user-123", data.getUserId());
        assertEquals("测试用户", data.getUserName());
        assertEquals(1, data.getUserType());
        assertEquals(PHONE, data.getPhone());
        assertEquals("https://img.example/avatar.png", data.getAvatarUrl());
        assertNull(result.get("unionId"));
        verify(userInfoService, never()).insertUserInfo(any(UserInfo.class));
    }

    @Test
    void loginShouldRequirePhoneCodeWhenWechatUserDoesNotExist() throws WxErrorException {
        when(wxMaService.switchover(APP_ID)).thenReturn(true);
        when(wxMaService.getUserService()).thenReturn(wxMaUserService);
        when(wxMaUserService.getSessionInfo(LOGIN_CODE)).thenReturn(buildSession());
        when(userInfoService.selectUserInfoByOpenId(OPEN_ID)).thenReturn(null);

        AjaxResult result = controller.login(APP_ID, LOGIN_CODE, null, null);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        WxUserInfo data = (WxUserInfo) result.get(AjaxResult.DATA_TAG);
        assertTrue(data.isNeedPhoneCode());
        assertEquals(SESSION_KEY, data.getSessionKey());
        assertEquals(OPEN_ID, data.getOpenId());
        assertNull(data.getApiToken());
        verify(userInfoService, never()).insertUserInfo(any(UserInfo.class));
    }

    @Test
    void loginShouldCreateUserWithPhoneWhenWechatUserDoesNotExist() throws WxErrorException {
        when(wxMaService.switchover(APP_ID)).thenReturn(true);
        when(wxMaService.getUserService()).thenReturn(wxMaUserService);
        when(wxMaUserService.getSessionInfo(LOGIN_CODE)).thenReturn(buildSession());
        when(wxMaUserService.getPhoneNumber(PHONE_CODE)).thenReturn(buildPhoneInfo());
        when(userInfoService.selectUserInfoByOpenId(OPEN_ID)).thenReturn(null);
        when(jwtService.createToken(any())).thenReturn("jwt-token");

        AjaxResult result = controller.login(APP_ID, LOGIN_CODE, PHONE_CODE, null);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        WxUserInfo data = (WxUserInfo) result.get(AjaxResult.DATA_TAG);
        assertEquals("jwt-token", data.getApiToken());
        assertEquals(PHONE, data.getPhone());
        assertEquals(OPEN_ID, data.getOpenId());
        assertEquals(SESSION_KEY, data.getSessionKey());

        ArgumentCaptor<UserInfo> userCaptor = forClass(UserInfo.class);
        verify(userInfoService).insertUserInfo(userCaptor.capture());
        assertEquals(OPEN_ID, userCaptor.getValue().getOpenId());
        assertEquals(UNION_ID, userCaptor.getValue().getUnionId());
        assertEquals(PHONE, userCaptor.getValue().getPhone());
    }

    private WxMaJscode2SessionResult buildSession() {
        WxMaJscode2SessionResult session = new WxMaJscode2SessionResult();
        session.setOpenid(OPEN_ID);
        session.setUnionid(UNION_ID);
        session.setSessionKey(SESSION_KEY);
        return session;
    }

    private UserInfo buildUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserId("user-123");
        userInfo.setOpenId(OPEN_ID);
        userInfo.setUnionId(UNION_ID);
        userInfo.setUserName("测试用户");
        userInfo.setUserType(1);
        userInfo.setPhone(PHONE);
        userInfo.setAvatarUrl("https://img.example/avatar.png");
        return userInfo;
    }

    private WxMaPhoneNumberInfo buildPhoneInfo() {
        WxMaPhoneNumberInfo phoneNumberInfo = new WxMaPhoneNumberInfo();
        phoneNumberInfo.setPhoneNumber(PHONE);
        return phoneNumberInfo;
    }
}
