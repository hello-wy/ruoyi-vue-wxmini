package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class JobAttendanceAdminOrderVo {
    private String orderNo;
    private Long jobId;
    private String jobTitle;
    private Long userInfoId;
    private String displayName;
    private Integer attendanceStatus;
    private String attendanceStatusLabel;
    private String signImageUrl;
    private Integer auditStatus;
    private String auditStatusLabel;
    private String auditRemark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date signTime;

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public Long getUserInfoId() { return userInfoId; }
    public void setUserInfoId(Long userInfoId) { this.userInfoId = userInfoId; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public Integer getAttendanceStatus() { return attendanceStatus; }
    public void setAttendanceStatus(Integer attendanceStatus) { this.attendanceStatus = attendanceStatus; }
    public String getAttendanceStatusLabel() { return attendanceStatusLabel; }
    public void setAttendanceStatusLabel(String attendanceStatusLabel) { this.attendanceStatusLabel = attendanceStatusLabel; }
    public String getSignImageUrl() { return signImageUrl; }
    public void setSignImageUrl(String signImageUrl) { this.signImageUrl = signImageUrl; }
    public Integer getAuditStatus() { return auditStatus; }
    public void setAuditStatus(Integer auditStatus) { this.auditStatus = auditStatus; }
    public String getAuditStatusLabel() { return auditStatusLabel; }
    public void setAuditStatusLabel(String auditStatusLabel) { this.auditStatusLabel = auditStatusLabel; }
    public String getAuditRemark() { return auditRemark; }
    public void setAuditRemark(String auditRemark) { this.auditRemark = auditRemark; }
    public Date getSubmitTime() { return submitTime; }
    public void setSubmitTime(Date submitTime) { this.submitTime = submitTime; }
    public Date getAuditTime() { return auditTime; }
    public void setAuditTime(Date auditTime) { this.auditTime = auditTime; }
    public Date getSignTime() { return signTime; }
    public void setSignTime(Date signTime) { this.signTime = signTime; }
}
