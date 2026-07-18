package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;

public class CourseFinanceManualRecord {
    private Long id;
    private String recordType;
    private Long courseId;
    private String wxminiUserId;
    private Long employeeUserId;
    private BigDecimal amount;
    private String reason;
    private Long operatorUserId;
    private Long cashbackDeductionId;
    private Date occurredAt;
    private Date createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getWxminiUserId() { return wxminiUserId; }
    public void setWxminiUserId(String wxminiUserId) { this.wxminiUserId = wxminiUserId; }
    public Long getEmployeeUserId() { return employeeUserId; }
    public void setEmployeeUserId(Long employeeUserId) { this.employeeUserId = employeeUserId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Long getOperatorUserId() { return operatorUserId; }
    public void setOperatorUserId(Long operatorUserId) { this.operatorUserId = operatorUserId; }
    public Long getCashbackDeductionId() { return cashbackDeductionId; }
    public void setCashbackDeductionId(Long cashbackDeductionId) { this.cashbackDeductionId = cashbackDeductionId; }
    public Date getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Date occurredAt) { this.occurredAt = occurredAt; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
