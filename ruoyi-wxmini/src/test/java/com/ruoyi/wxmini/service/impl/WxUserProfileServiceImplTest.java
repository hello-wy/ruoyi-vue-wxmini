package com.ruoyi.wxmini.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.system.domain.vo.StudentDetailVo;
import com.ruoyi.wxmini.bo.WxUserProfileUpdateBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.domain.WxUserProfile;
import com.ruoyi.wxmini.domain.vo.WxUserProfileVo;
import com.ruoyi.wxmini.mapper.WxUserProfileMapper;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void switchCurrentUserTypeShouldAllowAuntType() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(USER_ID);
        userInfo.setUserType(0);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(userInfoService.updateUserInfo(userInfo)).thenReturn(1);

        int rows = service.switchCurrentUserType(USER_ID, 3);

        assertEquals(1, rows);
        assertEquals(3, userInfo.getUserType());
        verify(userInfoService).updateUserInfo(userInfo);
    }

    @Test
    void getCurrentUserProfileShouldExposeAuntAsSwitchableType() {
        WxUserProfileVo profile = new WxUserProfileVo();
        profile.setUserInfoId(1L);
        profile.setUserName("张三");
        profile.setUserType(0);
        when(wxUserProfileMapper.selectProfileDetailByUserId(USER_ID)).thenReturn(profile);

        WxUserProfileVo result = service.getCurrentUserProfile(USER_ID);

        assertTrue(result.getCanSwitchUserType());
        assertEquals(Arrays.asList(0, 1, 2, 3), result.getSwitchableUserTypes());
    }

    @Test
    void getCurrentUserProfileShouldKeepRealnameAuthFlag() {
        WxUserProfileVo profile = new WxUserProfileVo();
        profile.setUserInfoId(1L);
        profile.setUserName("张三");
        profile.setUserType(1);
        profile.setIsRealnameAuth(1);
        when(wxUserProfileMapper.selectProfileDetailByUserId(USER_ID)).thenReturn(profile);

        WxUserProfileVo result = service.getCurrentUserProfile(USER_ID);

        assertEquals(Integer.valueOf(1), result.getIsRealnameAuth());
    }

    @Test
    void getCurrentUserProfileShouldReturnAuntPrimaryActionAndLabel() {
        WxUserProfileVo profile = new WxUserProfileVo();
        profile.setUserInfoId(1L);
        profile.setUserName("李阿姨");
        profile.setUserType(3);
        profile.setAge(48);
        when(wxUserProfileMapper.selectProfileDetailByUserId(USER_ID)).thenReturn(profile);

        WxUserProfileVo result = service.getCurrentUserProfile(USER_ID);

        assertEquals("阿姨", result.getUserTypeLabel());
        assertEquals("/pages/mine/info/index", result.getPrimaryAction());
        assertEquals(Integer.valueOf(48), result.getAge());
    }

    @Test
    void updateCurrentUserProfileShouldPersistParttimeFields() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserId(USER_ID);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(userInfoService.updateUserInfo(userInfo)).thenReturn(1);

        WxUserProfile existing = new WxUserProfile();
        existing.setUserInfoId(1L);
        when(wxUserProfileMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existing);
        when(wxUserProfileMapper.updateById(any(WxUserProfile.class))).thenReturn(1);

        WxUserProfileUpdateBo bo = new WxUserProfileUpdateBo();
        bo.setRealName("张三");
        bo.setAge(20);
        bo.setPersonalIntro("可兼职周末活动");
        bo.setAvailableTime("周末白天");
        bo.setWorkExperience("做过地推和助教");

        int rows = service.updateCurrentUserProfile(USER_ID, bo);

        assertEquals(1, rows);
    }

    @Test
    void getAdminStudentDetailShouldKeepAuntAgeData() {
        StudentDetailVo detail = new StudentDetailVo();
        detail.setUserType(3);
        detail.setAge(48);
        when(wxUserProfileMapper.selectAdminStudentDetailById(9L)).thenReturn(detail);

        StudentDetailVo result = wxUserProfileMapper.selectAdminStudentDetailById(9L);

        assertEquals(Integer.valueOf(48), result.getAge());
    }
}
