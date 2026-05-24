package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class Parents extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Excel(name = "微信用户ID")
    private String wechatUid;

    @Excel(name = "平台用户ID")
    private Long systemUid;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long babyId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long addressId;

    @Excel(name = "地理位置文本", readConverterExp = "如=：XX小区")
    private String location;

    @Excel(name = "经纬度位置", readConverterExp = "如=：118.82,32.04")
    private String geo;

    @Excel(name = "区域", readConverterExp = "如=：玄武区")
    private String region;

    @Excel(name = "家教单的名称")
    private String name;

    @Excel(name = "联系手机号")
    private String phone;

    @Excel(name = "年级", readConverterExp = "如=：一年级，初一")
    private String grade;

    @Excel(name = "科目")
    private String subject;

    @Excel(name = "辅导方式", readConverterExp = "网=络辅导、线下")
    private Long methods;

    @Excel(name = "服务需求项目")
    private String demandItems;

    @Excel(name = "陪伴官性别要求", readConverterExp = "0=不限,1=男,2=女")
    private Integer genderRequirement;

    @Excel(name = "陪伴官时薪预算")
    private BigDecimal hourlyBudget;

    @Excel(name = "家长家教需求文本")
    private String requirements;

    @Excel(name = "家长孩子情况简介")
    private String brief;

    @Excel(name = "每周几到几")
    private String dayOfWeek;

    @Excel(name = "服务日期")
    private String serviceDates;

    @Excel(name = "服务时段")
    private String serviceTimes;

    @JsonFormat(pattern = "HH:mm")
    @Excel(name = "开始时间", width = 30, dateFormat = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @Excel(name = "结束时间", width = 30, dateFormat = "HH:mm")
    private LocalTime endTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    @Excel(name = "请家教订单状态", readConverterExp = "0 正常 1 取消")
    private Long status;

    @TableField(exist = false)
    private Boolean bound;

    @TableField(exist = false)
    private String parentName;

    @TableField(exist = false)
    private String parentPhone;

    @TableField(exist = false)
    private String tutorName;

    @TableField(exist = false)
    private String studentPhone;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setWechatUid(String wechatUid) {
        this.wechatUid = wechatUid;
    }

    public String getWechatUid() {
        return wechatUid;
    }

    public void setSystemUid(Long systemUid) {
        this.systemUid = systemUid;
    }

    public Long getSystemUid() {
        return systemUid;
    }

    public void setBabyId(Long babyId) {
        this.babyId = babyId;
    }

    public Long getBabyId() {
        return babyId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public String getGeo() {
        return geo;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setMethods(Long methods) {
        this.methods = methods;
    }

    public Long getMethods() {
        return methods;
    }

    public String getDemandItems() {
        return demandItems;
    }

    public void setDemandItems(String demandItems) {
        this.demandItems = demandItems;
    }

    public Integer getGenderRequirement() {
        return genderRequirement;
    }

    public void setGenderRequirement(Integer genderRequirement) {
        this.genderRequirement = genderRequirement;
    }

    public BigDecimal getHourlyBudget() {
        return hourlyBudget;
    }

    public void setHourlyBudget(BigDecimal hourlyBudget) {
        this.hourlyBudget = hourlyBudget;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getBrief() {
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

    public String getServiceDates() {
        return serviceDates;
    }

    public void setServiceDates(String serviceDates) {
        this.serviceDates = serviceDates;
    }

    public String getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(String serviceTimes) {
        this.serviceTimes = serviceTimes;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStatus() {
        return status;
    }

    public Boolean getBound() {
        return bound;
    }

    public void setBound(Boolean bound) {
        this.bound = bound;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("wechatUid", getWechatUid())
                .append("systemUid", getSystemUid())
                .append("babyId", getBabyId())
                .append("addressId", getAddressId())
                .append("location", getLocation())
                .append("geo", getGeo())
                .append("region", getRegion())
                .append("name", getName())
                .append("phone", getPhone())
                .append("grade", getGrade())
                .append("subject", getSubject())
                .append("methods", getMethods())
                .append("demandItems", getDemandItems())
                .append("genderRequirement", getGenderRequirement())
                .append("hourlyBudget", getHourlyBudget())
                .append("requirements", getRequirements())
                .append("brief", getBrief())
                .append("dayOfWeek", getDayOfWeek())
                .append("serviceDates", getServiceDates())
                .append("serviceTimes", getServiceTimes())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
                .append("createDate", getCreateDate())
                .append("updateDate", getUpdateDate())
                .append("status", getStatus())
                .append("bound", getBound())
                .append("parentName", getParentName())
                .append("parentPhone", getParentPhone())
                .append("tutorName", getTutorName())
                .append("studentPhone", getStudentPhone())
                .toString();
    }
}
