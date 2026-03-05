package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 大学生/教员对象 tutors
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public class Tutors extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联user对应的id */
    private Long uid;

    /** 职称（如：教师、老师、大学生教员） */
    @Excel(name = "职称", readConverterExp = "如=：教师、老师、大学生教员")
    private String title;

    /** 证书图片url */
    @Excel(name = "证书图片url")
    private String certificates;

    /** 可授科目数组（使用索引0，1，2) */
    @Excel(name = "可授科目数组", readConverterExp = "可授科目数组（使用索引0，1，2)")
    private String subjects;

    /** 可授区域数组（地区索引id） */
    @Excel(name = "可授区域数组", readConverterExp = "地=区索引id")
    private String areas;

    /** 授课方式（网络辅导、线下） */
    @Excel(name = "授课方式", readConverterExp = "网=络辅导、线下")
    private String methods;

    /** 审核状态（待审核、已通过、已拒绝） */
    private String isCertified;

    /** 薪资要求（如：100元/小时） */
    private String salary;

    /** 经历/履历 */
    @Excel(name = "经历/履历")
    private String experience;

    /** 专业 */
    @Excel(name = "专业")
    private String major;

    /** 就读/毕业院校 */
    @Excel(name = "就读/毕业院校")
    private String school;

    /** 学历枚举：0-本科, 1-硕士, 2-博士 */
    @Excel(name = "学历枚举：0-本科, 1-硕士, 2-博士")
    private Long degree;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    public void setUid(Long uid) 
    {
        this.uid = uid;
    }

    public Long getUid() 
    {
        return uid;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setCertificates(String certificates) 
    {
        this.certificates = certificates;
    }

    public String getCertificates() 
    {
        return certificates;
    }

    public void setSubjects(String subjects) 
    {
        this.subjects = subjects;
    }

    public String getSubjects() 
    {
        return subjects;
    }

    public void setAreas(String areas) 
    {
        this.areas = areas;
    }

    public String getAreas() 
    {
        return areas;
    }

    public void setMethods(String methods) 
    {
        this.methods = methods;
    }

    public String getMethods() 
    {
        return methods;
    }

    public void setIsCertified(String isCertified) 
    {
        this.isCertified = isCertified;
    }

    public String getIsCertified() 
    {
        return isCertified;
    }

    public void setSalary(String salary) 
    {
        this.salary = salary;
    }

    public String getSalary() 
    {
        return salary;
    }

    public void setExperience(String experience) 
    {
        this.experience = experience;
    }

    public String getExperience() 
    {
        return experience;
    }

    public void setMajor(String major) 
    {
        this.major = major;
    }

    public String getMajor() 
    {
        return major;
    }

    public void setSchool(String school) 
    {
        this.school = school;
    }

    public String getSchool() 
    {
        return school;
    }

    public void setDegree(Long degree) 
    {
        this.degree = degree;
    }

    public Long getDegree() 
    {
        return degree;
    }

    public void setCreateDate(Date createDate) 
    {
        this.createDate = createDate;
    }

    public Date getCreateDate() 
    {
        return createDate;
    }

    public void setUpdateDate(Date updateDate) 
    {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate() 
    {
        return updateDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("uid", getUid())
            .append("title", getTitle())
            .append("certificates", getCertificates())
            .append("subjects", getSubjects())
            .append("areas", getAreas())
            .append("methods", getMethods())
            .append("isCertified", getIsCertified())
            .append("salary", getSalary())
            .append("experience", getExperience())
            .append("major", getMajor())
            .append("school", getSchool())
            .append("degree", getDegree())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .toString();
    }
}
