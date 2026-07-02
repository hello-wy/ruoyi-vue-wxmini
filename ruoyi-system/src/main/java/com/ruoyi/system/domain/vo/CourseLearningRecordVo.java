package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class CourseLearningRecordVo {
    private Long orderId;
    private String orderNo;
    private String userId;
    private Long courseId;
    private String courseName;
    private String courseLocation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date courseTime;
    private BigDecimal amount;
    private Integer status;
    private Integer recordType;
    private Integer signStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date signTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refundTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

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
    public String getCourseLocation() { return courseLocation; }
    public void setCourseLocation(String courseLocation) { this.courseLocation = courseLocation; }
    public Date getCourseTime() { return courseTime; }
    public void setCourseTime(Date courseTime) { this.courseTime = courseTime; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getRecordType() { return recordType; }
    public void setRecordType(Integer recordType) { this.recordType = recordType; }
    public Integer getSignStatus() { return signStatus; }
    public void setSignStatus(Integer signStatus) { this.signStatus = signStatus; }
    public Date getPayTime() { return payTime; }
    public void setPayTime(Date payTime) { this.payTime = payTime; }
    public Date getSignTime() { return signTime; }
    public void setSignTime(Date signTime) { this.signTime = signTime; }
    public Date getRefundTime() { return refundTime; }
    public void setRefundTime(Date refundTime) { this.refundTime = refundTime; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
