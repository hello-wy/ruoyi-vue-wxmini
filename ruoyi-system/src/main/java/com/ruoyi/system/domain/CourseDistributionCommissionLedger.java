package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;

public class CourseDistributionCommissionLedger {
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_REVERSED = "REVERSED";
    private Long id;
    private String orderNo;
    private Long courseId;
    private String buyerUserId;
    private String beneficiaryUserId;
    private Integer commissionLevel;
    private BigDecimal paidAmount;
    private BigDecimal commissionRatio;
    private BigDecimal commissionAmount;
    private String status;
    private Date createTime;
    private Date reverseTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getBuyerUserId() { return buyerUserId; }
    public void setBuyerUserId(String buyerUserId) { this.buyerUserId = buyerUserId; }
    public String getBeneficiaryUserId() { return beneficiaryUserId; }
    public void setBeneficiaryUserId(String beneficiaryUserId) { this.beneficiaryUserId = beneficiaryUserId; }
    public Integer getCommissionLevel() { return commissionLevel; }
    public void setCommissionLevel(Integer commissionLevel) { this.commissionLevel = commissionLevel; }
    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
    public BigDecimal getCommissionRatio() { return commissionRatio; }
    public void setCommissionRatio(BigDecimal commissionRatio) { this.commissionRatio = commissionRatio; }
    public BigDecimal getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(BigDecimal commissionAmount) { this.commissionAmount = commissionAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getReverseTime() { return reverseTime; }
    public void setReverseTime(Date reverseTime) { this.reverseTime = reverseTime; }
}
