package com.ruoyi.wxmini.vo;

import java.math.BigDecimal;

public class WxJobPayrollItemVo {
    private Long employeeUserId;
    private String employeeName;
    private String phoneMasked;
    private BigDecimal hours;
    private BigDecimal hourlyRate;
    private BigDecimal amount;
    private Integer status;

    public Long getEmployeeUserId() { return employeeUserId; }
    public void setEmployeeUserId(Long employeeUserId) { this.employeeUserId = employeeUserId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getPhoneMasked() { return phoneMasked; }
    public void setPhoneMasked(String phoneMasked) { this.phoneMasked = phoneMasked; }
    public BigDecimal getHours() { return hours; }
    public void setHours(BigDecimal hours) { this.hours = hours; }
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
