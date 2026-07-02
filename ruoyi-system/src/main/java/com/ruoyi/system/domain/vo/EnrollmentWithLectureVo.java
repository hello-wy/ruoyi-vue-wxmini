package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 用户课程报名信息 VO（含讲座基本信息）
 */
public class EnrollmentWithLectureVo {

    /** 学籍记录ID */
    private Long id;

    /** 用户ID（user_info.id） */
    private Long uid;

    /** 学员显示名称 */
    private String studentName;

    /** 学员手机号 */
    private String phone;

    /** 讲座ID */
    private Long lectureId;

    /** 讲座名称 */
    private String lectureName;

    /** 开讲时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lectureTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endDate;

    /** 详细地址 */
    private String location;

    /** 总报名数/学籍数 */
    private Integer total;

    /** 剩余可用学籍数 */
    private Integer remain;

    /** 已使用学籍数 */
    private Integer usedCount;

    /** 已分享学籍数 */
    private Integer sharedCount;

    /** 可分享学籍数 */
    private Integer availableShareCount;

    /** 购买/创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getLectureId() {
        return lectureId;
    }

    public void setLectureId(Long lectureId) {
        this.lectureId = lectureId;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public Date getLectureTime() {
        return lectureTime;
    }

    public void setLectureTime(Date lectureTime) {
        this.lectureTime = lectureTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Integer getSharedCount() {
        return sharedCount;
    }

    public void setSharedCount(Integer sharedCount) {
        this.sharedCount = sharedCount;
    }

    public Integer getAvailableShareCount() {
        return availableShareCount;
    }

    public void setAvailableShareCount(Integer availableShareCount) {
        this.availableShareCount = availableShareCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
