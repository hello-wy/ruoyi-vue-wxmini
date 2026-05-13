package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 兼职退款列表 VO
 */
public class JobRefundOrderVo {
    private Long orderId;
    private String orderNo;
    private String userId;
    private Long jobId;
    private String jobTitle;
    private String userName;
    private BigDecimal amount;
    private Integer status;
    private Boolean signedIn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refundTime;
    private Integer auditStatus;
    private String auditStatusLabel;
    private String signImageUrl;
    private Boolean canRefund;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Boolean getSignedIn() { return signedIn; }
    public void setSignedIn(Boolean signedIn) { this.signedIn = signedIn; }
    public Date getPayTime() { return payTime; }
    public void setPayTime(Date payTime) { this.payTime = payTime; }
    public Date getRefundTime() { return refundTime; }
    public void setRefundTime(Date refundTime) { this.refundTime = refundTime; }
    public Integer getAuditStatus() { return auditStatus; }
    public void setAuditStatus(Integer auditStatus) { this.auditStatus = auditStatus; }
    public String getAuditStatusLabel() { return auditStatusLabel; }
    public void setAuditStatusLabel(String auditStatusLabel) { this.auditStatusLabel = auditStatusLabel; }
    public String getSignImageUrl() { return signImageUrl; }
    public void setSignImageUrl(String signImageUrl) { this.signImageUrl = signImageUrl; }
    public Boolean getCanRefund() { return canRefund; }
    public void setCanRefund(Boolean canRefund) { this.canRefund = canRefund; }
}
