package com.ruoyi.wxmini.vo;

public class WxSignupUserVo {
    private Long userInfoId;
    private String displayName;
    private String phoneMasked;

    public Long getUserInfoId() { return userInfoId; }
    public void setUserInfoId(Long userInfoId) { this.userInfoId = userInfoId; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getPhoneMasked() { return phoneMasked; }
    public void setPhoneMasked(String phoneMasked) { this.phoneMasked = phoneMasked; }
}
