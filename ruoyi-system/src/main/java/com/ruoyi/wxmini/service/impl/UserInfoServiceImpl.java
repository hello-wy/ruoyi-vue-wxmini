package com.ruoyi.wxmini.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.mapper.UserInfoMapper;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户信息Service业务层处理。优先从缓存读取
 *
 * @author ruoyi
 * @date 2025-04-25
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserInfoMapper userInfoMapper;

    private static final String REDIS_KEY_WX_USER = "wx_user:";
    private static final Integer REALNAME_AUTH_VERIFIED = 1;

    /**
     * 查询用户信息列表
     *
     * @param userInfo 用户信息
     * @return 用户信息
     */
    @Override
    public List<UserInfo> selectUserInfoList(UserInfo userInfo) {
        return userInfoMapper.selectUserInfoList(userInfo);
    }

    @Override
    public UserInfo selectUserInfoById(Long id) {
        return userInfoMapper.selectUserInfoById(id);
    }

    /**
     * 新增用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    @Override
    public int insertUserInfo(UserInfo userInfo) {
        Date now = DateUtils.getNowDate();
        userInfo.setCreateTime(now);
        userInfo.setUpdateTime(now);
        int result = userInfoMapper.insertUserInfo(userInfo);
        redisCache.setCacheObject(REDIS_KEY_WX_USER + userInfo.getUserId(), JSON.toJSONString(userInfo));
        return result;
    }

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    @Override
    public int updateUserInfo(UserInfo userInfo) {
        if (userInfo.getId() == null && StringUtils.isNotBlank(userInfo.getUserId())) {
            UserInfo dbUser = userInfoMapper.selectUserInfoByUserId(userInfo.getUserId());
            if (dbUser != null) {
                userInfo.setId(dbUser.getId());
            }
        }
        userInfo.setUpdateTime(DateUtils.getNowDate());
        redisCache.setCacheObject(this.getWxUserCacheKey(userInfo.getUserId()), JSON.toJSONString(userInfo));
        return userInfoMapper.updateUserInfo(userInfo);
    }

    @Override
    public int updateRealnameInfo(String userId, String realName, String idCard) {
        UserInfo userInfo = userInfoMapper.selectUserInfoByUserId(userId);
        if (userInfo == null) {
            return 0;
        }
        userInfo.setRealName(realName);
        userInfo.setIdCard(idCard);
        userInfo.setIsRealnameAuth(REALNAME_AUTH_VERIFIED);
        return updateUserInfo(userInfo);
    }

    @Override
    public int updateAvatarUrlByUserId(String userId, String avatarUrl) {
        UserInfo userInfo = userInfoMapper.selectUserInfoByUserId(userId);
        if (userInfo == null) {
            return 0;
        }
        userInfo.setAvatarUrl(avatarUrl);
        return updateUserInfo(userInfo);
    }

    @Override
    public UserInfo selectUserInfoByOpenId(String openId) {
        return userInfoMapper.selectUserInfoByOpenId(openId);
    }

    @Override
    public UserInfo selectUserInfoByUserId(String userId) {
        Object t = redisCache.getCacheObject(this.getWxUserCacheKey(userId));
        if (t != null) {
            String cachedJson = t.toString();
            if (cachedJson.contains("\"isRealnameAuth\"")) {
                return JSON.parseObject(cachedJson, UserInfo.class);
            }
        }
        return reloadUserInfoToCache(userId);
    }

    private UserInfo reloadUserInfoToCache(String userId) {
        UserInfo userInfo = userInfoMapper.selectUserInfoByUserId(userId);
        if (userInfo == null) {
            return null;
        }
        redisCache.setCacheObject(this.getWxUserCacheKey(userId), JSON.toJSONString(userInfo));
        return userInfo;
    }

    @Override
    public UserInfo selectUserInfoByInviteCode(String inviteCode) {
        return userInfoMapper.selectUserInfoByInviteCode(inviteCode);
    }

    @Override
    public int updateBirthdayById(Long id, Date birthday) {
        int updated = userInfoMapper.updateBirthdayById(id, birthday);
        if (updated > 0) {
            UserInfo userInfo = userInfoMapper.selectUserInfoById(id);
            redisCache.setCacheObject(getWxUserCacheKey(userInfo.getUserId()), JSON.toJSONString(userInfo));
        }
        return updated;
    }

    @Override
    public void markStudent(String userId) {
        if (userInfoMapper.markStudentByUserId(userId) > 0) {
            reloadUserInfoToCache(userId);
        }
    }

    @Override
    public String getOrCreateInviteCode(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        UserInfo userInfo = selectUserInfoByUserId(userId);
        if (userInfo == null) {
            return null;
        }
        if (StringUtils.isNotBlank(userInfo.getInviteCode())) {
            return userInfo.getInviteCode();
        }

        // Generate unique invite code
        String inviteCode = null;
        int retries = 0;
        while (retries < 10) {
            String tempCode = generateRandomInviteCode();
            UserInfo existing = userInfoMapper.selectUserInfoByInviteCode(tempCode);
            if (existing == null) {
                inviteCode = tempCode;
                break;
            }
            retries++;
        }
        
        if (inviteCode == null) {
            // Fallback to substring of UUID if random fails repeatedly
            inviteCode = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        }

        userInfo.setInviteCode(inviteCode);
        updateUserInfo(userInfo);
        return inviteCode;
    }

    private String generateRandomInviteCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        java.util.Random rnd = new java.util.Random();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private String getWxUserCacheKey(String userId) {
        return REDIS_KEY_WX_USER + userId;
    }
}
