package com.ruoyi.system.domain.vo;

public class JobSignupUserRecordVo {
    private Long userInfoId;
    private String userId;
    private String displayName;
    private String phoneMasked;

    public Long getUserInfoId() { return userInfoId; }
    public void setUserInfoId(Long userInfoId) { this.userInfoId = userInfoId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getPhoneMasked() { return phoneMasked; }
    public void setPhoneMasked(String phoneMasked) { this.phoneMasked = phoneMasked; }
}
