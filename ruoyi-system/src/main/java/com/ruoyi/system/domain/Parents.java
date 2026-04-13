package com.ruoyi.system.domain;

import java.time.LocalTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 家教订单对象 parents
 * 
 * @author ruoyi
 * @date 2026-03-04
 */
@TableName("parents")
public class Parents extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 教员表主键ID */
    @TableId(type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 微信用户ID */
    @Excel(name = "微信用户ID")
    private Long wechatUid;

    /** 平台用户ID */
    @Excel(name = "平台用户ID")
    private Long systemUid;

    /** 地理位置文本（如：XX小区） */
    @Excel(name = "地理位置文本", readConverterExp = "如=：XX小区")
    private String location;

    /** 经纬度位置（如：118.82,32.04） */
    @Excel(name = "经纬度位置", readConverterExp = "如=：118.82,32.04")
    private String geo;

    /** 区域（如：玄武区） */
    @Excel(name = "区域", readConverterExp = "如=：玄武区")
    private String region;

    /** 家教单的名称 */
    @Excel(name = "家教单的名称")
    private String name;

    /** 年级（如：一年级，初一） */
    @Excel(name = "年级", readConverterExp = "如=：一年级，初一")
    private String grade;

    /** 科目 */
    @Excel(name = "科目")
    private String subject;

    /** 辅导方式（网络辅导、线下） */
    @Excel(name = "辅导方式", readConverterExp = "网=络辅导、线下")
    private Long methods;

    /** 家长家教需求文本 */
    @Excel(name = "家长家教需求文本")
    private String requirements;

    /** 家长孩子情况简介 */
    @Excel(name = "家长孩子情况简介")
    private String brief;

    /** 每周几到几 */
    @Excel(name = "每周几到几")
    private String dayOfWeek;

    @JsonFormat(pattern = "HH:mm")
    @Excel(name = "开始时间", width = 30, dateFormat = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @Excel(name = "结束时间", width = 30, dateFormat = "HH:mm")
    private LocalTime endTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    /** 请家教订单状态 */
    @Excel(name = "请家教订单状态",readConverterExp = "0 正常 1 取消")
    private Long status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setWechatUid(Long wechatUid)
    {
        this.wechatUid = wechatUid;
    }

    public Long getWechatUid()
    {
        return wechatUid;
    }

    public void setSystemUid(Long systemUid)
    {
        this.systemUid = systemUid;
    }

    public Long getSystemUid()
    {
        return systemUid;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setGeo(String geo) 
    {
        this.geo = geo;
    }

    public String getGeo() 
    {
        return geo;
    }

    public void setRegion(String region) 
    {
        this.region = region;
    }

    public String getRegion() 
    {
        return region;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setGrade(String grade) 
    {
        this.grade = grade;
    }

    public String getGrade() 
    {
        return grade;
    }

    public void setSubject(String subject) 
    {
        this.subject = subject;
    }

    public String getSubject() 
    {
        return subject;
    }

    public void setMethods(Long methods) 
    {
        this.methods = methods;
    }

    public Long getMethods() 
    {
        return methods;
    }

    public void setRequirements(String requirements) 
    {
        this.requirements = requirements;
    }

    public String getRequirements() 
    {
        return requirements;
    }

    public void setBrief(String brief) 
    {
        this.brief = brief;
    }

    public String getBrief() 
    {
        return brief;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("wechatUid", getWechatUid())
            .append("systemUid", getSystemUid())
            .append("location", getLocation())
            .append("geo", getGeo())
            .append("region", getRegion())
            .append("name", getName())
            .append("grade", getGrade())
            .append("subject", getSubject())
            .append("methods", getMethods())
            .append("requirements", getRequirements())
            .append("brief", getBrief())
            .append("dayOfWeek", getDayOfWeek())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .append("status", getStatus())
            .toString();
    }
}
