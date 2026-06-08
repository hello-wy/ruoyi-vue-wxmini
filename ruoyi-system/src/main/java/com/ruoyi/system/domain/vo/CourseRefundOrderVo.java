package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class CourseRefundOrderVo {
    private Long orderId;
    private String orderNo;
    private String userId;
    private Long courseId;
    private String courseName;
    private String userName;
    private BigDecimal amount;
    private Integer status;
    private Boolean signedIn;
    private Boolean canRefund;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date signTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refundTime;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Boolean getSignedIn() { return signedIn; }
    public void setSignedIn(Boolean signedIn) { this.signedIn = signedIn; }
    public Boolean getCanRefund() { return canRefund; }
    public void setCanRefund(Boolean canRefund) { this.canRefund = canRefund; }
    public Date getSignTime() { return signTime; }
    public void setSignTime(Date signTime) { this.signTime = signTime; }
    public Date getRefundTime() { return refundTime; }
    public void setRefundTime(Date refundTime) { this.refundTime = refundTime; }
}
