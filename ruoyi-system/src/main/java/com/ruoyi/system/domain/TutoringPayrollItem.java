package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class TutoringPayrollItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String payrollNo;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long scheduleId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;
    private String orderNo;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long bindingId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentUserId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tutorId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tutorUserId;
    private BigDecimal grossAmount;
    private BigDecimal commissionRate;
    private BigDecimal commissionAmount;
    private BigDecimal netAmount;
    private Integer status;
    private String walletBizId;
    private String paidBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date paidTime;
    private String remark;
    private String tutorName;
    private String parentName;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date serviceDate;
    private String startTime;
    private String endTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPayrollNo() { return payrollNo; }
    public void setPayrollNo(String payrollNo) { this.payrollNo = payrollNo; }
    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getBindingId() { return bindingId; }
    public void setBindingId(Long bindingId) { this.bindingId = bindingId; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Long getParentUserId() { return parentUserId; }
    public void setParentUserId(Long parentUserId) { this.parentUserId = parentUserId; }
    public Long getTutorId() { return tutorId; }
    public void setTutorId(Long tutorId) { this.tutorId = tutorId; }
    public Long getTutorUserId() { return tutorUserId; }
    public void setTutorUserId(Long tutorUserId) { this.tutorUserId = tutorUserId; }
    public BigDecimal getGrossAmount() { return grossAmount; }
    public void setGrossAmount(BigDecimal grossAmount) { this.grossAmount = grossAmount; }
    public BigDecimal getCommissionRate() { return commissionRate; }
    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    public BigDecimal getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(BigDecimal commissionAmount) { this.commissionAmount = commissionAmount; }
    public BigDecimal getNetAmount() { return netAmount; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getWalletBizId() { return walletBizId; }
    public void setWalletBizId(String walletBizId) { this.walletBizId = walletBizId; }
    public String getPaidBy() { return paidBy; }
    public void setPaidBy(String paidBy) { this.paidBy = paidBy; }
    public Date getPaidTime() { return paidTime; }
    public void setPaidTime(Date paidTime) { this.paidTime = paidTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getTutorName() { return tutorName; }
    public void setTutorName(String tutorName) { this.tutorName = tutorName; }
    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }
    public Date getServiceDate() { return serviceDate; }
    public void setServiceDate(Date serviceDate) { this.serviceDate = serviceDate; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
