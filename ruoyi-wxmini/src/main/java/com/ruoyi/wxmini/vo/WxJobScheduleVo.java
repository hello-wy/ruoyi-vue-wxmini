package com.ruoyi.wxmini.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class WxJobScheduleVo {
    private Long jobId;
    private String orderNo;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workDate;
    private String workTime;
    private String location;
    private BigDecimal salaryDay;
    private Integer status;

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
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
}
