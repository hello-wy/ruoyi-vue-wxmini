package com.ruoyi.system.domain;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

@TableName("tutors")
public class Tutors extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private Long id;

    @Excel(name = "关联 user_info.user_id")
    private String uid;

    @Excel(name = "身份", readConverterExp = "0=大学生教员,1=在职教师,2=其他")
    private Long identity;

    @Excel(name = "证书图片url")
    private String certificates;

    @Excel(name = "可授科目数组")
    private String subjects;

    @Excel(name = "可授区域数组")
    private String areas;

    @Excel(name = "授课方式")
    private Long methods;

    @Excel(name = "审核状态", readConverterExp = "0=待审核,1=通过,2=拒绝")
    private Long status;

    @Excel(name = "经历/履历")
    private String experience;

    @Excel(name = "专业")
    private String major;

    @Excel(name = "就读/毕业院校")
    private String school;

    @Excel(name = "学历")
    private Long degree;

    @Excel(name = "当前年级")
    private String currentGrade;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    @Excel(name = "实名认证真实姓名")
    private String realName;

    @Excel(name = "手机号")
    private String phone;

    @Excel(name = "身份证号码")
    private String idCard;

    @Excel(name = "个人评价")
    private String selfJudge;

    @Excel(name = "证书列表")
    private String certificateList;

    @Excel(name = "城市")
    private String city;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUid()
    {
        return uid;
    }

    public void setIdentity(Long identity)
    {
        this.identity = identity;
    }

    public Long getIdentity()
    {
        return identity;
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

    public void setMethods(Long methods)
    {
        this.methods = methods;
    }

    public Long getMethods()
    {
        return methods;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
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

    public void setCurrentGrade(String currentGrade)
    {
        this.currentGrade = currentGrade;
    }

    public String getCurrentGrade()
    {
        return currentGrade;
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

    public void setRealName(String realName)
    {
        this.realName = realName;
    }

    public String getRealName()
    {
        return realName;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setSelfJudge(String selfJudge)
    {
        this.selfJudge = selfJudge;
    }

    public String getSelfJudge()
    {
        return selfJudge;
    }

    public void setCertificateList(String certificateList)
    {
        this.certificateList = certificateList;
    }

    public String getCertificateList()
    {
        return certificateList;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getCity()
    {
        return city;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("uid", getUid())
            .append("identity", getIdentity())
            .append("certificates", getCertificates())
            .append("subjects", getSubjects())
            .append("areas", getAreas())
            .append("methods", getMethods())
            .append("status", getStatus())
            .append("experience", getExperience())
            .append("major", getMajor())
            .append("school", getSchool())
            .append("degree", getDegree())
            .append("currentGrade", getCurrentGrade())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .append("realName", getRealName())
            .append("phone", getPhone())
            .append("idCard", getIdCard())
            .append("selfJudge", getSelfJudge())
            .append("certificateList", getCertificateList())
            .append("city", getCity())
            .toString();
    }
}
