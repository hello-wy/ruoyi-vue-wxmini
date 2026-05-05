package com.ruoyi.wxmini.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class WxMerchantJobVo {
    private Long id;
    private String title;
    private BigDecimal salaryDay;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workDate;
    private String workTime;
    private String location;
    private Long status;
    private Integer signupLimit;
    private Integer paidSignupCount;
    private Boolean payrollReminder;
    private String settlementStatus;
    private String settlementStatusLabel;
    private Boolean canSettle;
    private Boolean canCancel;
    private Boolean canResumeRecruiting;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public BigDecimal getSalaryDay() { return salaryDay; }
    public void setSalaryDay(BigDecimal salaryDay) { this.salaryDay = salaryDay; }
    public Date getWorkDate() { return workDate; }
    public void setWorkDate(Date workDate) { this.workDate = workDate; }
    public String getWorkTime() { return workTime; }
    public void setWorkTime(String workTime) { this.workTime = workTime; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Long getStatus() { return status; }
    public void setStatus(Long status) { this.status = status; }
    public Integer getSignupLimit() { return signupLimit; }
    public void setSignupLimit(Integer signupLimit) { this.signupLimit = signupLimit; }
    public Integer getPaidSignupCount() { return paidSignupCount; }
    public void setPaidSignupCount(Integer paidSignupCount) { this.paidSignupCount = paidSignupCount; }
    public Boolean getPayrollReminder() { return payrollReminder; }
    public void setPayrollReminder(Boolean payrollReminder) { this.payrollReminder = payrollReminder; }
    public String getSettlementStatus() { return settlementStatus; }
    public void setSettlementStatus(String settlementStatus) { this.settlementStatus = settlementStatus; }
    public String getSettlementStatusLabel() { return settlementStatusLabel; }
    public void setSettlementStatusLabel(String settlementStatusLabel) { this.settlementStatusLabel = settlementStatusLabel; }
    public Boolean getCanSettle() { return canSettle; }
    public void setCanSettle(Boolean canSettle) { this.canSettle = canSettle; }
    public Boolean getCanCancel() { return canCancel; }
    public void setCanCancel(Boolean canCancel) { this.canCancel = canCancel; }
    public Boolean getCanResumeRecruiting() { return canResumeRecruiting; }
    public void setCanResumeRecruiting(Boolean canResumeRecruiting) { this.canResumeRecruiting = canResumeRecruiting; }
}
