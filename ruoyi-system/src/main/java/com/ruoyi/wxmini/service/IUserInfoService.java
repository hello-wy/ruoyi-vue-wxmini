package com.ruoyi.wxmini.service;

import com.ruoyi.wxmini.domain.UserInfo;

import java.util.List;

/**
 * 用户信息Service接口
 *
 * @author ruoyi
 * @date 2025-04-25
 */
public interface IUserInfoService {

    /**
     * 查询用户信息列表
     *
     * @param userInfo 用户信息
     * @return 用户信息集合
     */
    public List<UserInfo> selectUserInfoList(UserInfo userInfo);

    UserInfo selectUserInfoById(Long id);

    /**
     * 新增用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    public int insertUserInfo(UserInfo userInfo);

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    public int updateUserInfo(UserInfo userInfo);

    int updateRealnameInfo(String userId, String realName, String idCard);

    int updateAvatarUrlByUserId(String userId, String avatarUrl);

    UserInfo selectUserInfoByOpenId(String openId);

    UserInfo selectUserInfoByUserId(String userId);

    UserInfo selectUserInfoByInviteCode(String inviteCode);

    String getOrCreateInviteCode(String userId);

    void markStudent(String userId);
}
