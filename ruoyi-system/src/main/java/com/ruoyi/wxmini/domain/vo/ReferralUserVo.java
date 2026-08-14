package com.ruoyi.wxmini.domain.vo;

import com.ruoyi.wxmini.domain.UserInfo;

import java.io.Serializable;

/**
 * 邀请关系查询中的小程序用户摘要。
 */
public class ReferralUserVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String userName;
    private String realName;
    private String phone;
    private String avatarUrl;

    public ReferralUserVo() {
    }

    public ReferralUserVo(UserInfo user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.realName = user.getRealName();
        this.phone = user.getPhone();
        this.avatarUrl = user.getAvatarUrl();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}
