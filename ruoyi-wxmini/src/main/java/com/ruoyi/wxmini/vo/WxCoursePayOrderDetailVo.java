package com.ruoyi.wxmini.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class WxCoursePayOrderDetailVo {
    private String orderNo;
    private Long courseId;
    private String courseName;
    private String name;
    private String gender;
    private String phone;
    private String company;
    private String accommodation;
    private Long enrollmentId;
    private BigDecimal amount;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date signTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date refundTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getAccommodation() { return accommodation; }
    public void setAccommodation(String accommodation) { this.accommodation = accommodation; }
    public Long getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Date getPayTime() { return payTime; }
    public void setPayTime(Date payTime) { this.payTime = payTime; }
    public Date getSignTime() { return signTime; }
    public void setSignTime(Date signTime) { this.signTime = signTime; }
    public Date getRefundTime() { return refundTime; }
    public void setRefundTime(Date refundTime) { this.refundTime = refundTime; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
