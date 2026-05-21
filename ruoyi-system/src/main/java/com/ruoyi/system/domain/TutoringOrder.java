package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class TutoringOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
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
    private String wechatOpenId;
    private String serviceTimesSnapshot;
    private Integer lessonCount;
    private BigDecimal hourlyPrice;
    private BigDecimal totalAmount;
    private BigDecimal commissionRate;
    private Integer status;
    private String wechatTransactionId;
    private String requestId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    private String parentName;
    private String tutorName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getWechatOpenId() { return wechatOpenId; }
    public void setWechatOpenId(String wechatOpenId) { this.wechatOpenId = wechatOpenId; }
    public String getServiceTimesSnapshot() { return serviceTimesSnapshot; }
    public void setServiceTimesSnapshot(String serviceTimesSnapshot) { this.serviceTimesSnapshot = serviceTimesSnapshot; }
    public Integer getLessonCount() { return lessonCount; }
    public void setLessonCount(Integer lessonCount) { this.lessonCount = lessonCount; }
    public BigDecimal getHourlyPrice() { return hourlyPrice; }
    public void setHourlyPrice(BigDecimal hourlyPrice) { this.hourlyPrice = hourlyPrice; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getCommissionRate() { return commissionRate; }
    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getWechatTransactionId() { return wechatTransactionId; }
    public void setWechatTransactionId(String wechatTransactionId) { this.wechatTransactionId = wechatTransactionId; }
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public Date getPayTime() { return payTime; }
    public void setPayTime(Date payTime) { this.payTime = payTime; }
    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }
    public String getTutorName() { return tutorName; }
    public void setTutorName(String tutorName) { this.tutorName = tutorName; }
}
