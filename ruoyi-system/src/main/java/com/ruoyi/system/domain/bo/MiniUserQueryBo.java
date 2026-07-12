package com.ruoyi.system.domain.bo;

public class MiniUserQueryBo {
    private String keyword;
    private String userName;
    private Integer userType;
    private String phone;
    private Integer isStudent;

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Integer getUserType() { return userType; }
    public void setUserType(Integer userType) { this.userType = userType; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Integer getIsStudent() { return isStudent; }
    public void setIsStudent(Integer isStudent) { this.isStudent = isStudent; }
}
