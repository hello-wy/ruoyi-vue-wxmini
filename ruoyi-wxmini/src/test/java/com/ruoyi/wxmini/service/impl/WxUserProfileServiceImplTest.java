package com.ruoyi.wxmini.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.MerchantUserTypeWhitelist;
import com.ruoyi.system.domain.vo.StudentDetailVo;
import com.ruoyi.system.service.IMerchantUserTypeWhitelistService;
import com.ruoyi.wxmini.bo.WxUserProfileUpdateBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.domain.WxUserProfile;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxUserProfileServiceImplTest {

    private static final String USER_ID = "user-123";

    @Mock
    private IUserInfoService userInfoService;

    @Mock
    private WxUserProfileMapper wxUserProfileMapper;

    @Mock
    private IMerchantUserTypeWhitelistService merchantUserTypeWhitelistService;

    @InjectMocks
    private WxUserProfileServiceImpl service;

    @Test
    void switchCurrentUserTypeShouldRejectMerchantWhenWhitelistMissing() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(USER_ID);
        userInfo.setUserType(0);
        userInfo.setIdCard("11010519900101123X");
        userInfo.setRealName("张三");
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(merchantUserTypeWhitelistService.selectEnabledMerchantUserTypeWhitelistByIdCard("11010519900101123X")).thenReturn(null);

        ServiceException error = assertThrows(ServiceException.class, () -> service.switchCurrentUserType(USER_ID, 2));

        assertEquals("当前账号暂未开通商家身份", error.getMessage());
    }

    @Test
    void switchCurrentUserTypeShouldRejectMerchantWhenRealNameMismatched() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(USER_ID);
        userInfo.setUserType(0);
        userInfo.setIdCard("11010519900101123X");
        userInfo.setRealName("张三");
        MerchantUserTypeWhitelist whitelist = new MerchantUserTypeWhitelist();
        whitelist.setIdCard("11010519900101123X");
        whitelist.setRealName("李四");
        whitelist.setStatus(1);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(merchantUserTypeWhitelistService.selectEnabledMerchantUserTypeWhitelistByIdCard("11010519900101123X")).thenReturn(whitelist);

        ServiceException error = assertThrows(ServiceException.class, () -> service.switchCurrentUserType(USER_ID, 2));

        assertEquals("当前账号暂未开通商家身份", error.getMessage());
    }

    @Test
    void switchCurrentUserTypeShouldAllowMerchantWhenWhitelistMatches() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(USER_ID);
        userInfo.setUserType(0);
        userInfo.setIdCard("11010519900101123X");
        userInfo.setRealName("张三");
        MerchantUserTypeWhitelist whitelist = new MerchantUserTypeWhitelist();
        whitelist.setIdCard("11010519900101123X");
        whitelist.setRealName("张三");
        whitelist.setStatus(1);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(userInfoService.updateUserInfo(userInfo)).thenReturn(1);
        when(merchantUserTypeWhitelistService.selectEnabledMerchantUserTypeWhitelistByIdCard("11010519900101123X")).thenReturn(whitelist);

        int rows = service.switchCurrentUserType(USER_ID, 2);

        assertEquals(1, rows);
        assertEquals(2, userInfo.getUserType());
        verify(userInfoService).updateUserInfo(userInfo);
    }

    @Test
    void submitMerchantApplicationShouldCreatePendingWhitelistRecord() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(USER_ID);
        userInfo.setRealName("张三");
        userInfo.setIdCard("11010519900101123X");
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(merchantUserTypeWhitelistService.selectMerchantUserTypeWhitelistByIdCard("11010519900101123X")).thenReturn(null);
        when(merchantUserTypeWhitelistService.insertMerchantUserTypeWhitelist(any(MerchantUserTypeWhitelist.class))).thenReturn(1);

        int rows = service.submitMerchantApplication(USER_ID, "/profile/merchant-license/user/a.jpg");

        assertEquals(1, rows);
        ArgumentCaptor<MerchantUserTypeWhitelist> captor = ArgumentCaptor.forClass(MerchantUserTypeWhitelist.class);
        verify(merchantUserTypeWhitelistService).insertMerchantUserTypeWhitelist(captor.capture());
        MerchantUserTypeWhitelist saved = captor.getValue();
        assertEquals("张三", saved.getRealName());
        assertEquals("11010519900101123X", saved.getIdCard());
        assertEquals(Integer.valueOf(0), saved.getStatus());
        assertEquals(USER_ID, saved.getApplyUserId());
        assertEquals("/profile/merchant-license/user/a.jpg", saved.getBusinessLicenseUrl());
    }

    @Test
    void submitMerchantApplicationShouldRejectBlankLicense() {
        ServiceException error = assertThrows(ServiceException.class, () -> service.submitMerchantApplication(USER_ID, " "));

        assertEquals("请先上传营业执照", error.getMessage());
    }

    @Test
    void submitMerchantApplicationShouldRejectMissingRealnameInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(USER_ID);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);

        ServiceException error = assertThrows(ServiceException.class, () -> {
            service.submitMerchantApplication(USER_ID, "/profile/merchant-license/user/a.jpg");
        });

        assertEquals("请先完成实名认证后再提交商家申请", error.getMessage());
    }

    @Test
    void submitMerchantApplicationShouldNotDowngradeApprovedRecord() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(USER_ID);
        userInfo.setRealName("张三");
        userInfo.setIdCard("11010519900101123X");
        MerchantUserTypeWhitelist existing = new MerchantUserTypeWhitelist();
        existing.setStatus(1);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(merchantUserTypeWhitelistService.selectMerchantUserTypeWhitelistByIdCard("11010519900101123X"))
                .thenReturn(existing);

        ServiceException error = assertThrows(ServiceException.class, () -> {
            service.submitMerchantApplication(USER_ID, "/profile/merchant-license/user/a.jpg");
        });

        assertEquals("商家身份已审核通过，无需重复提交", error.getMessage());
        verify(merchantUserTypeWhitelistService, never())
                .updateMerchantUserTypeWhitelistApplication(any(MerchantUserTypeWhitelist.class));
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
    void getCurrentUserProfileShouldHideMerchantWhenWhitelistUnavailable() {
        WxUserProfileVo profile = new WxUserProfileVo();
        profile.setUserInfoId(1L);
        profile.setUserName("张三");
        profile.setUserType(0);
        profile.setRealName("张三");
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(USER_ID);
        userInfo.setRealName("张三");
        userInfo.setIdCard("11010519900101123X");
        when(wxUserProfileMapper.selectProfileDetailByUserId(USER_ID)).thenReturn(profile);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(merchantUserTypeWhitelistService.selectEnabledMerchantUserTypeWhitelistByIdCard("11010519900101123X")).thenReturn(null);

        WxUserProfileVo result = service.getCurrentUserProfile(USER_ID);

        assertTrue(result.getCanSwitchUserType());
        assertEquals(Arrays.asList(0, 1, 3), result.getSwitchableUserTypes());
    }

    @Test
    void getCurrentUserProfileShouldExposeMerchantWhenWhitelistMatches() {
        WxUserProfileVo profile = new WxUserProfileVo();
        profile.setUserInfoId(1L);
        profile.setUserName("张三");
        profile.setUserType(1);
        profile.setRealName("张三");
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(USER_ID);
        userInfo.setRealName("张三");
        userInfo.setIdCard("11010519900101123X");
        MerchantUserTypeWhitelist whitelist = new MerchantUserTypeWhitelist();
        whitelist.setRealName("张三");
        whitelist.setIdCard("11010519900101123X");
        whitelist.setStatus(1);
        when(wxUserProfileMapper.selectProfileDetailByUserId(USER_ID)).thenReturn(profile);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(merchantUserTypeWhitelistService.selectEnabledMerchantUserTypeWhitelistByIdCard("11010519900101123X")).thenReturn(whitelist);

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
        assertEquals(null, userInfo.getUserName());
    }

    @Test
    void updateCurrentUserProfileShouldNotChangeRealNameAfterAuth() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserId(USER_ID);
        userInfo.setIsRealnameAuth(1);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(userInfoService.updateUserInfo(userInfo)).thenReturn(1);

        WxUserProfile existing = new WxUserProfile();
        existing.setUserInfoId(1L);
        existing.setRealName("旧姓名");
        when(wxUserProfileMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existing);
        when(wxUserProfileMapper.updateById(any(WxUserProfile.class))).thenReturn(1);

        WxUserProfileUpdateBo bo = new WxUserProfileUpdateBo();
        bo.setRealName("新姓名");

        int rows = service.updateCurrentUserProfile(USER_ID, bo);

        assertEquals(1, rows);
        assertEquals("旧姓名", existing.getRealName());
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
