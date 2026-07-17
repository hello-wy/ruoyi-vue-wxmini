package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;

/** Immutable course cashback ledger entry. */
public class CourseCashbackLedger extends BaseEntity {
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_REVERSED = "REVERSED";
    public static final String STATUS_UNATTRIBUTED = "UNATTRIBUTED";
    private Long id;
    private Long studentId;
    private Long employeeUserId;
    private Long ownerDeptId;
    private Long courseId;
    private String orderNo;
    private BigDecimal paidAmount;
    private BigDecimal courseTotal;
    private BigDecimal outstandingAmount;
    private BigDecimal cashbackRatio;
    private BigDecimal grossCashback;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") private Date createTime;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; } public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getEmployeeUserId() { return employeeUserId; } public void setEmployeeUserId(Long employeeUserId) { this.employeeUserId = employeeUserId; }
    public Long getOwnerDeptId() { return ownerDeptId; } public void setOwnerDeptId(Long ownerDeptId) { this.ownerDeptId = ownerDeptId; }
    public Long getCourseId() { return courseId; } public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getOrderNo() { return orderNo; } public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public BigDecimal getPaidAmount() { return paidAmount; } public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
    public BigDecimal getCourseTotal() { return courseTotal; } public void setCourseTotal(BigDecimal courseTotal) { this.courseTotal = courseTotal; }
    public BigDecimal getOutstandingAmount() { return outstandingAmount; } public void setOutstandingAmount(BigDecimal outstandingAmount) { this.outstandingAmount = outstandingAmount; }
    public BigDecimal getCashbackRatio() { return cashbackRatio; } public void setCashbackRatio(BigDecimal cashbackRatio) { this.cashbackRatio = cashbackRatio; }
    public BigDecimal getGrossCashback() { return grossCashback; } public void setGrossCashback(BigDecimal grossCashback) { this.grossCashback = grossCashback; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public Date getCreateTime() { return createTime; } public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
