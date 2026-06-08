package com.ruoyi.wxmini.bo;

import javax.validation.constraints.NotNull;

public class WxCoursePayCreateOrderBo {
    @NotNull(message = "courseId不能为空")
    private Long courseId;
    private String name;
    private String gender;
    private String phone;
    private String company;
    private String accommodation;
    private Long enrollmentId;

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
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
}
