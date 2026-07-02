package com.ruoyi.system.domain.vo;

public class StudentListVo {

    private Long id;
    private String userId;
    private String displayName;
    private String realName;
    private String phone;
    private Integer userType;
    private String userTypeLabel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserTypeLabel() {
        return userTypeLabel;
    }

    public void setUserTypeLabel(String userTypeLabel) {
        this.userTypeLabel = userTypeLabel;
    }
}
