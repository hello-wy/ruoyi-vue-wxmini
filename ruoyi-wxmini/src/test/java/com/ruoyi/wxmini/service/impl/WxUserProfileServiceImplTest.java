package com.ruoyi.wxmini.service.impl;

import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.domain.vo.WxUserProfileVo;
import com.ruoyi.wxmini.mapper.WxUserProfileMapper;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WxUserProfileServiceImplTest {

    private static final String USER_ID = "user-123";

    @Mock
    private IUserInfoService userInfoService;

    @Mock
    private WxUserProfileMapper wxUserProfileMapper;

    @InjectMocks
    private WxUserProfileServiceImpl service;

    @Test
    void switchCurrentUserTypeShouldAllowMerchantType() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(USER_ID);
        userInfo.setUserType(0);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(userInfoService.updateUserInfo(userInfo)).thenReturn(1);

        int rows = service.switchCurrentUserType(USER_ID, 2);

        assertEquals(1, rows);
        assertEquals(2, userInfo.getUserType());
        verify(userInfoService).updateUserInfo(userInfo);
    }

    @Test
    void getCurrentUserProfileShouldExposeMerchantAsSwitchableType() {
        WxUserProfileVo profile = new WxUserProfileVo();
        profile.setUserInfoId(1L);
        profile.setUserName("张三");
        profile.setUserType(0);
        when(wxUserProfileMapper.selectProfileDetailByUserId(USER_ID)).thenReturn(profile);

        WxUserProfileVo result = service.getCurrentUserProfile(USER_ID);

        assertTrue(result.getCanSwitchUserType());
        assertEquals(Arrays.asList(0, 1, 2), result.getSwitchableUserTypes());
    }
}
