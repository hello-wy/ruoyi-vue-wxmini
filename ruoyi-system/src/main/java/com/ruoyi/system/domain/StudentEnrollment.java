package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学籍信息对象 student_enrollment
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
public class StudentEnrollment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 用户ID (关联小程序学员) */
    @Excel(name = "用户ID (关联小程序学员)")
    private Long uid;

    /** 关联课程ID */
    @Excel(name = "关联课程ID")
    private Long lectureId;

    /** 总学籍数/报名数 */
    @Excel(name = "总学籍数/报名数")
    private Integer total;

    /** 剩余可用学籍数 (支持线下核销或赠送扣减) */
    @Excel(name = "剩余可用学籍数 (支持线下核销或赠送扣减)")
    private Integer remain;

    /** 逻辑删除标识 */
    @Excel(name = "逻辑删除标识")
    private Long isDeleted;

    /** 学员显示名称 */
    @TableField(exist = false)
    private String studentName;

    /** 学员手机号 */
    @TableField(exist = false)
    private String phone;

    /** 课程名称 */
    @TableField(exist = false)
    private String lectureName;

    /** 课程时间 */
    @TableField(exist = false)
    private java.util.Date lectureTime;

    /** 课程地点 */
    @TableField(exist = false)
    private String location;

    /** 已分享学籍数 */
    @TableField(exist = false)
    private Integer sharedCount;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUid(Long uid) 
    {
        this.uid = uid;
    }

    public Long getUid() 
    {
        return uid;
    }

    public void setLectureId(Long lectureId) 
    {
        this.lectureId = lectureId;
    }

    public Long getLectureId() 
    {
        return lectureId;
    }

    public void setTotal(Integer total)
    {
        this.total = total;
    }

    public Integer getTotal()
    {
        return total;
    }

    public void setRemain(Integer remain)
    {
        this.remain = remain;
    }

    public Integer getRemain()
    {
        return remain;
    }

    public void setIsDeleted(Long isDeleted) 
    {
        this.isDeleted = isDeleted;
    }

    public Long getIsDeleted()
    {
        return isDeleted;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getLectureName()
    {
        return lectureName;
    }

    public void setLectureName(String lectureName)
    {
        this.lectureName = lectureName;
    }

    public java.util.Date getLectureTime()
    {
        return lectureTime;
    }

    public void setLectureTime(java.util.Date lectureTime)
    {
        this.lectureTime = lectureTime;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public Integer getSharedCount()
    {
        return sharedCount;
    }

    public void setSharedCount(Integer sharedCount)
    {
        this.sharedCount = sharedCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("uid", getUid())
            .append("lectureId", getLectureId())
            .append("total", getTotal())
            .append("remain", getRemain())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isDeleted", getIsDeleted())
            .append("studentName", getStudentName())
            .append("phone", getPhone())
            .append("lectureName", getLectureName())
            .append("lectureTime", getLectureTime())
            .append("location", getLocation())
            .append("sharedCount", getSharedCount())
            .toString();
    }
}
