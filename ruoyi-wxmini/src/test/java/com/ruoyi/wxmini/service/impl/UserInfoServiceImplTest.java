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

    @Test
    void selectUserInfoByUserIdShouldRefreshCacheWhenCachedRealnameAuthIsMissing() {
        String staleCache = "{\"id\":1,\"userId\":\"123\",\"userName\":\"微信用户\"}";
        UserInfo dbUserInfo = new UserInfo();
        dbUserInfo.setId(1L);
        dbUserInfo.setUserId("123");
        dbUserInfo.setUserName("微信用户");
        dbUserInfo.setIsRealnameAuth(1);
        when(redisCache.getCacheObject("wx_user:123")).thenReturn(staleCache);
        when(userInfoMapper.selectUserInfoByUserId("123")).thenReturn(dbUserInfo);

        UserInfo result = service.selectUserInfoByUserId("123");

        assertEquals(Integer.valueOf(1), result.getIsRealnameAuth());
        verify(redisCache).setCacheObject(eq("wx_user:123"), contains("\"isRealnameAuth\":1"));
    }

    @Test
    void testFastjsonSerialization() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(12345L);
        userInfo.setUserId("test-user-id");
        userInfo.setUserName("test-user-name");
        userInfo.setIsRealnameAuth(1);

        String json = com.alibaba.fastjson2.JSON.toJSONString(userInfo);
        System.out.println("Serialized JSON: " + json);
        UserInfo parsed = com.alibaba.fastjson2.JSON.parseObject(json, UserInfo.class);
        System.out.println("Parsed ID: " + parsed.getId());
        assertEquals(12345L, parsed.getId());
        assertEquals("test-user-id", parsed.getUserId());
        assertEquals("test-user-name", parsed.getUserName());
        assertEquals(Integer.valueOf(1), parsed.getIsRealnameAuth());
    }

    @Test
    void testCacheHitPath() {
        String cachedJson = "{\"id\":999,\"userId\":\"123\",\"userName\":\"微信用户\",\"isRealnameAuth\":1}";
        when(redisCache.getCacheObject("wx_user:123")).thenReturn(cachedJson);

        UserInfo result = service.selectUserInfoByUserId("123");

        assertEquals(999L, result.getId());
        assertEquals("123", result.getUserId());
        assertEquals("微信用户", result.getUserName());
        assertEquals(Integer.valueOf(1), result.getIsRealnameAuth());
    }
}
