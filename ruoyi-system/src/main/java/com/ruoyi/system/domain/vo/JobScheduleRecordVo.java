package com.ruoyi.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

public class JobScheduleRecordVo {
    private String orderNo;
    private Long jobId;
    private String title;
    private Date workDate;
    private String workTime;
    private String location;
    private BigDecimal salaryDay;
    private Integer status;
    private Integer attendanceStatus;
    private String attendanceStatusLabel;
    private Date signTime;
    private String signImageUrl;
    private Integer auditStatus;
    private String auditStatusLabel;
    private String auditRemark;
    private Boolean canUploadSignImage;

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Date getWorkDate() { return workDate; }
    public void setWorkDate(Date workDate) { this.workDate = workDate; }
    public String getWorkTime() { return workTime; }
    public void setWorkTime(String workTime) { this.workTime = workTime; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public BigDecimal getSalaryDay() { return salaryDay; }
    public void setSalaryDay(BigDecimal salaryDay) { this.salaryDay = salaryDay; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getAttendanceStatus() { return attendanceStatus; }
    public void setAttendanceStatus(Integer attendanceStatus) { this.attendanceStatus = attendanceStatus; }
    public String getAttendanceStatusLabel() { return attendanceStatusLabel; }
    public void setAttendanceStatusLabel(String attendanceStatusLabel) { this.attendanceStatusLabel = attendanceStatusLabel; }
    public Date getSignTime() { return signTime; }
    public void setSignTime(Date signTime) { this.signTime = signTime; }
    public String getSignImageUrl() { return signImageUrl; }
    public void setSignImageUrl(String signImageUrl) { this.signImageUrl = signImageUrl; }
    public Integer getAuditStatus() { return auditStatus; }
    public void setAuditStatus(Integer auditStatus) { this.auditStatus = auditStatus; }
    public String getAuditStatusLabel() { return auditStatusLabel; }
    public void setAuditStatusLabel(String auditStatusLabel) { this.auditStatusLabel = auditStatusLabel; }
    public String getAuditRemark() { return auditRemark; }
    public void setAuditRemark(String auditRemark) { this.auditRemark = auditRemark; }
    public Boolean getCanUploadSignImage() { return canUploadSignImage; }
    public void setCanUploadSignImage(Boolean canUploadSignImage) { this.canUploadSignImage = canUploadSignImage; }
}
