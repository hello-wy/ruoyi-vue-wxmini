package com.ruoyi.system.domain.vo;

import java.util.Date;

public class MiniUserVo {
    private Long id;
    private String userId;
    private String userName;
    private Integer userType;
    private String phone;
    private String avatarUrl;
    private String realName;
    private Integer gender;
    private Integer isRealnameAuth;
    private Integer isStudent;
    private Date createTime;
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Integer getUserType() { return userType; }
    public void setUserType(Integer userType) { this.userType = userType; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }
    public Integer getIsRealnameAuth() { return isRealnameAuth; }
    public void setIsRealnameAuth(Integer isRealnameAuth) { this.isRealnameAuth = isRealnameAuth; }
    public Integer getIsStudent() { return isStudent; }
    public void setIsStudent(Integer isStudent) { this.isStudent = isStudent; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
