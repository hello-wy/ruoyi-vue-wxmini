package com.ruoyi.wxmini.vo;

public class WxSignupUserVo {
    private Long userInfoId;
    private String displayName;
    private String phoneMasked;
    private Boolean payrollPaid;
    private Integer payrollItemStatus;

    public Long getUserInfoId() { return userInfoId; }
    public void setUserInfoId(Long userInfoId) { this.userInfoId = userInfoId; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getPhoneMasked() { return phoneMasked; }
    public void setPhoneMasked(String phoneMasked) { this.phoneMasked = phoneMasked; }
    public Boolean getPayrollPaid() { return payrollPaid; }
    public void setPayrollPaid(Boolean payrollPaid) { this.payrollPaid = payrollPaid; }
    public Integer getPayrollItemStatus() { return payrollItemStatus; }
    public void setPayrollItemStatus(Integer payrollItemStatus) { this.payrollItemStatus = payrollItemStatus; }
}
