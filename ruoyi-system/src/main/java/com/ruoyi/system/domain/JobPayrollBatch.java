package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class JobPayrollBatch extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String orderNo;
    private Long jobId;
    private Long merchantUid;
    private BigDecimal totalAmount;
    private Integer status;
    private String wechatTransactionId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    private String requestId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public Long getMerchantUid() { return merchantUid; }
    public void setMerchantUid(Long merchantUid) { this.merchantUid = merchantUid; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getWechatTransactionId() { return wechatTransactionId; }
    public void setWechatTransactionId(String wechatTransactionId) { this.wechatTransactionId = wechatTransactionId; }
    public Date getPayTime() { return payTime; }
    public void setPayTime(Date payTime) { this.payTime = payTime; }
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
}
