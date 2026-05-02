package com.ruoyi.wxmini.bo;

import javax.validation.constraints.NotNull;

public class WxPayrollEmployeeBo {
    @NotNull(message = "employeeUserId不能为空")
    private Long employeeUserId;

    @NotNull(message = "hours不能为空")
    private String hours;

    @NotNull(message = "hourlyRate不能为空")
    private String hourlyRate;

    public Long getEmployeeUserId() {
        return employeeUserId;
    }

    public void setEmployeeUserId(Long employeeUserId) {
        this.employeeUserId = employeeUserId;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
