package com.ruoyi.wxmini.domain.vo;

import java.util.List;

public class WxUserProfileVo {

    private Long userInfoId;

    private String displayName;

    private String userName;

    private String phone;

    private String realName;

    private String nickName;

    private Integer gender;

    private String companyName;

    private String companyAddress;

    private String companyPosition;

    private String industry;

    private String workYears;

    private String personalIntro;

    private Boolean verified;

    private Integer userType;

    private String userTypeLabel;

    private Boolean canSwitchUserType;

    private String primaryAction;

    private List<Integer> switchableUserTypes;

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPosition() {
        return companyPosition;
    }

    public void setCompanyPosition(String companyPosition) {
        this.companyPosition = companyPosition;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getWorkYears() {
        return workYears;
    }

    public void setWorkYears(String workYears) {
        this.workYears = workYears;
    }

    public String getPersonalIntro() {
        return personalIntro;
    }

    public void setPersonalIntro(String personalIntro) {
        this.personalIntro = personalIntro;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
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

    public Boolean getCanSwitchUserType() {
        return canSwitchUserType;
    }

    public void setCanSwitchUserType(Boolean canSwitchUserType) {
        this.canSwitchUserType = canSwitchUserType;
    }

    public String getPrimaryAction() {
        return primaryAction;
    }

    public void setPrimaryAction(String primaryAction) {
        this.primaryAction = primaryAction;
    }

    public List<Integer> getSwitchableUserTypes() {
        return switchableUserTypes;
    }

    public void setSwitchableUserTypes(List<Integer> switchableUserTypes) {
        this.switchableUserTypes = switchableUserTypes;
    }
}
