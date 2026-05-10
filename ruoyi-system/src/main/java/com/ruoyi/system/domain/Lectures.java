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
 * 课程活动/讲座对象 lectures
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
@TableName("lectures")
public class Lectures extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一编码 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 开讲时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Excel(name = "开讲时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
    private Date time;

    /** 会议结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Excel(name = "会议结束日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
    private Date endDate;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String name;

    /** 讲师名称 */
    @Excel(name = "讲师名称")
    private String speaker;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String location;

    /** 经纬度信息 (如: 118.80,32.05) */
    @Excel(name = "经纬度信息 (如: 118.80,32.05)")
    private String geo;

    /** 活动封面图 */
    @Excel(name = "活动封面图")
    private String cover;

    /** 封面资源目录ID */
    @Excel(name = "封面资源目录ID")
    private Long coverId;

    /** 活动详情 */
    @Excel(name = "活动详情")
    private String detail;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTime(Date time) 
    {
        this.time = time;
    }

    public Date getTime() 
    {
        return time;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setSpeaker(String speaker) 
    {
        this.speaker = speaker;
    }

    public String getSpeaker() 
    {
        return speaker;
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

    public void setDetail(String detail) 
    {
        this.detail = detail;
    }

    public String getDetail() 
    {
        return detail;
    }

    public void setCover(String cover)
    {
        this.cover = cover;
    }

    public String getCover()
    {
        return cover;
    }

    public void setCoverId(Long coverId)
    {
        this.coverId = coverId;
    }

    public Long getCoverId()
    {
        return coverId;
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
            .append("time", getTime())
            .append("endDate", getEndDate())
            .append("name", getName())
            .append("speaker", getSpeaker())
            .append("location", getLocation())
            .append("geo", getGeo())
            .append("cover", getCover())
            .append("coverId", getCoverId())
            .append("detail", getDetail())
            .append("createDate", getCreateDate())
            .append("updateDate", getUpdateDate())
            .toString();
    }
}
