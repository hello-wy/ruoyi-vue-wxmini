package com.ruoyi.wxmini.service.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.mapper.UserInfoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceImplTest {

    @Mock
    private RedisCache redisCache;

    @Mock
    private UserInfoMapper userInfoMapper;

    @InjectMocks
    private UserInfoServiceImpl service;

    @Test
    void updateRealnameInfoShouldPersistNameIdCardAndAuthFlagByUserId() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserId("123");
        userInfo.setUserName("微信用户");
        when(userInfoMapper.selectUserInfoByUserId("123")).thenReturn(userInfo);
        when(userInfoMapper.updateUserInfo(userInfo)).thenReturn(1);

        int rows = service.updateRealnameInfo("123", "张三", "110105199001011234");

        assertEquals(1, rows);
        assertEquals("张三", userInfo.getRealName());
        assertEquals("110105199001011234", userInfo.getIdCard());
        assertEquals(Integer.valueOf(1), userInfo.getIsRealnameAuth());
        verify(userInfoMapper).updateUserInfo(userInfo);
    }

    @Test
    void updateAvatarUrlByUserIdShouldPersistAvatarAndRefreshCache() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserId("123");
        userInfo.setUserName("微信用户");
        when(userInfoMapper.selectUserInfoByUserId("123")).thenReturn(userInfo);
        when(userInfoMapper.updateUserInfo(userInfo)).thenReturn(1);

        int rows = service.updateAvatarUrlByUserId("123", "/profile/avatar/123.png");

        assertEquals(1, rows);
        assertEquals("/profile/avatar/123.png", userInfo.getAvatarUrl());
        verify(userInfoMapper).updateUserInfo(userInfo);
        verify(redisCache).setCacheObject(eq("wx_user:123"), contains("/profile/avatar/123.png"));
    }

    @Test
    void updateAvatarUrlByUserIdShouldReturnZeroWhenUserDoesNotExist() {
        when(userInfoMapper.selectUserInfoByUserId("123")).thenReturn(null);

        int rows = service.updateAvatarUrlByUserId("123", "/profile/avatar/123.png");

        assertEquals(0, rows);
    }
}
