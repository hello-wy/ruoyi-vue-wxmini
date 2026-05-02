package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

public class JobPayrollItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long batchId;
    private Long jobId;
    private String employeeUid;
    private Long employeeUserId;
    private BigDecimal hours;
    private BigDecimal hourlyRate;
    private BigDecimal amount;
    private Integer status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getEmployeeUid() { return employeeUid; }
    public void setEmployeeUid(String employeeUid) { this.employeeUid = employeeUid; }
    public Long getEmployeeUserId() { return employeeUserId; }
    public void setEmployeeUserId(Long employeeUserId) { this.employeeUserId = employeeUserId; }
    public BigDecimal getHours() { return hours; }
    public void setHours(BigDecimal hours) { this.hours = hours; }
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
