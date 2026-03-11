package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 兼职日结工作对象 daily_jobs
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
public class DailyJobs extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日结工作主键ID */
    private Long id;

    /** 工作标题（如：初中数学日结兼职） */
    @Excel(name = "工作标题", readConverterExp = "如=：初中数学日结兼职")
    private String title;

    /** 分类：0-家教, 1-助教, 2-派发, 3-其他 */
    @Excel(name = "分类：0-家教, 1-助教, 2-派发, 3-其他")
    private Long category;

    /** 日结薪水（元/日） */
    @Excel(name = "日结薪水", readConverterExp = "元=/日")
    private BigDecimal salaryDay;

    /** 工作具体日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "工作具体日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date workDate;

    /** 具体时间段（如：14:00-16:00） */
    @Excel(name = "具体时间段", readConverterExp = "如=：14:00-16:00")
    private String workTime;

    /** 工作详细地址 */
    @Excel(name = "工作详细地址")
    private String location;

    /** 区域区号（如：320115） */
    @Excel(name = "区域区号", readConverterExp = "如=：320115")
    private String districtId;

    /** 联系人姓名 */
    @Excel(name = "联系人姓名")
    private String contacts;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 工作具体要求内容 */
    @Excel(name = "工作具体要求内容")
    private String description;

    /** 状态：0-招募中, 1-已满员, 2-已结束 */
    @Excel(name = "状态：0-招募中, 1-已满员, 2-已结束")
    private Long status;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date updateDate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setCategory(Long category) 
    {
        this.category = category;
    }

    public Long getCategory() 
    {
        return category;
    }

    public void setSalaryDay(BigDecimal salaryDay) 
    {
        this.salaryDay = salaryDay;
    }

    public BigDecimal getSalaryDay() 
    {
        return salaryDay;
    }

    public void setWorkDate(Date workDate) 
    {
        this.workDate = workDate;
    }

    public Date getWorkDate() 
    {
        return workDate;
    }

    public void setWorkTime(String workTime) 
    {
        this.workTime = workTime;
    }

    public String getWorkTime() 
    {
        return workTime;
    }

    public void setLocation(String location) 
    {
        this.location = location;
    }

    public String getLocation() 
    {
        return location;
    }

    public void setDistrictId(String districtId) 
    {
        this.districtId = districtId;
    }

    public String getDistrictId() 
    {
        return districtId;
    }

    public void setContacts(String contacts) 
    {
        this.contacts = contacts;
    }

    public String getContacts() 
    {
        return contacts;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
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
            .append("id", getId())
            .append("title", getTitle())
            .append("category", getCategory())
            .append("salaryDay", getSalaryDay())
            .append("workDate", getWorkDate())
            .append("workTime", getWorkTime())
            .append("location", getLocation())
            .append("districtId", getDistrictId())
            .append("contacts", getContacts())
            .append("phone", getPhone())
            .append("description", getDescription())
            .append("status", getStatus())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .toString();
    }
}
