package com.ruoyi.system.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

public class CourseFinanceManualRecordVo {
    private Long id;
    private String recordType;
    private Long courseId;
    private String courseName;
    private BigDecimal coursePrice;
    private String wxminiUserId;
    private String wxminiUserName;
    private String wxminiUserPhone;
    private Long employeeUserId;
    private String employeeName;
    private String employeePhone;
    private BigDecimal amount;
    private String reason;
    private Date occurredAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public BigDecimal getCoursePrice() { return coursePrice; }
    public void setCoursePrice(BigDecimal coursePrice) { this.coursePrice = coursePrice; }
    public String getWxminiUserId() { return wxminiUserId; }
    public void setWxminiUserId(String wxminiUserId) { this.wxminiUserId = wxminiUserId; }
    public String getWxminiUserName() { return wxminiUserName; }
    public void setWxminiUserName(String wxminiUserName) { this.wxminiUserName = wxminiUserName; }
    public String getWxminiUserPhone() { return wxminiUserPhone; }
    public void setWxminiUserPhone(String wxminiUserPhone) { this.wxminiUserPhone = wxminiUserPhone; }
    public Long getEmployeeUserId() { return employeeUserId; }
    public void setEmployeeUserId(Long employeeUserId) { this.employeeUserId = employeeUserId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getEmployeePhone() { return employeePhone; }
    public void setEmployeePhone(String employeePhone) { this.employeePhone = employeePhone; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Date getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Date occurredAt) { this.occurredAt = occurredAt; }
}
