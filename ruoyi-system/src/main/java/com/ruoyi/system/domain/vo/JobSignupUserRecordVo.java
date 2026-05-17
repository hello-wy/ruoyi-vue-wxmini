package com.ruoyi.system.domain.vo;

import java.util.Date;

public class JobSignupUserRecordVo {
    private Long userInfoId;
    private String userId;
    private String displayName;
    private String phoneMasked;
    private String orderNo;
    private Integer attendanceStatus;
    private String attendanceStatusLabel;
    private String signImageUrl;
    private Integer auditStatus;
    private String auditRemark;
    private Date signTime;
    private Integer signedCount;

    public Long getUserInfoId() { return userInfoId; }
    public void setUserInfoId(Long userInfoId) { this.userInfoId = userInfoId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getPhoneMasked() { return phoneMasked; }
    public void setPhoneMasked(String phoneMasked) { this.phoneMasked = phoneMasked; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Integer getAttendanceStatus() { return attendanceStatus; }
    public void setAttendanceStatus(Integer attendanceStatus) { this.attendanceStatus = attendanceStatus; }
    public String getAttendanceStatusLabel() { return attendanceStatusLabel; }
    public void setAttendanceStatusLabel(String attendanceStatusLabel) { this.attendanceStatusLabel = attendanceStatusLabel; }
    public String getSignImageUrl() { return signImageUrl; }
    public void setSignImageUrl(String signImageUrl) { this.signImageUrl = signImageUrl; }
    public Integer getAuditStatus() { return auditStatus; }
    public void setAuditStatus(Integer auditStatus) { this.auditStatus = auditStatus; }
    public String getAuditRemark() { return auditRemark; }
    public void setAuditRemark(String auditRemark) { this.auditRemark = auditRemark; }
    public Date getSignTime() { return signTime; }
    public void setSignTime(Date signTime) { this.signTime = signTime; }
    public Integer getSignedCount() { return signedCount; }
    public void setSignedCount(Integer signedCount) { this.signedCount = signedCount; }
}
