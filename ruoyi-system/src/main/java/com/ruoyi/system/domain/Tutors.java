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

/**
 * 大学生/教员对象 tutors
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
@TableName("tutors")
public class Tutors extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 教员表主键ID */
    @TableId(type = IdType.INPUT)
    private Long id;

    /** 关联user表的主键ID */
    @Excel(name = "关联user表的主键ID")
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
    private Long methods;

    /** 审核状态（待审核、已通过、已拒绝） */
    @Excel(name = "审核状态", readConverterExp = "0 待=审核、1 已通过、2 已拒绝")
    private Long isCertified;

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

    /** 实名认证真实姓名 */
    @Excel(name = "实名认证真实姓名")
    private String realName;

    /** 身份证号码 */
    @Excel(name = "身份证号码")
    private String idCard;

    /** 个人评价 */
    @Excel(name = "个人评价")
    private String selfJudge;

    /** 证书 */
    @Excel(name = "证书")
    private String certificate;

    /** 生活区域 */
    @Excel(name = "生活区域")
    private String live;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String work;

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

    public void setMethods(Long methods) 
    {
        this.methods = methods;
    }

    public Long getMethods() 
    {
        return methods;
    }

    public void setIsCertified(Long isCertified) 
    {
        this.isCertified = isCertified;
    }

    public Long getIsCertified() 
    {
        return isCertified;
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

    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
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

    public void setCertificate(String certificate) 
    {
        this.certificate = certificate;
    }

    public String getCertificate() 
    {
        return certificate;
    }

    public void setLive(String live) 
    {
        this.live = live;
    }

    public String getLive() 
    {
        return live;
    }

    public void setWork(String work) 
    {
        this.work = work;
    }

    public String getWork() 
    {
        return work;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("uid", getUid())
            .append("title", getTitle())
            .append("certificates", getCertificates())
            .append("subjects", getSubjects())
            .append("areas", getAreas())
            .append("methods", getMethods())
            .append("isCertified", getIsCertified())
            .append("experience", getExperience())
            .append("major", getMajor())
            .append("school", getSchool())
            .append("degree", getDegree())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .append("realName", getRealName())
            .append("idCard", getIdCard())
            .append("selfJudge", getSelfJudge())
            .append("certificate", getCertificate())
            .append("live", getLive())
            .append("work", getWork())
            .toString();
    }
}
