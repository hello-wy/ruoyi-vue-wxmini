package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 沙龙活动信息主对象 salon_info
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
public class SalonInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 沙龙ID，主键 */
    private Long id;

    /** 沙龙主标题，如：组局思维 */
    @Excel(name = "沙龙主标题，如：组局思维")
    private String title;

    /** 副标题或标签，如：沙龙/社群/KOL */
    @Excel(name = "副标题或标签，如：沙龙/社群/KOL")
    private String subtitle;

    /** 封面图的URL */
    @Excel(name = "封面图的URL")
    private String coverImg;

    /** 详情页内容（富文本HTML或JSON） */
    @Excel(name = "详情页内容", readConverterExp = "富=文本HTML或JSON")
    private String description;

    /** 原价/划线价，如：768.00 */
    @Excel(name = "原价/划线价，如：768.00")
    private BigDecimal originalPrice;

    /** 实际售卖价，如：128.00 */
    @Excel(name = "实际售卖价，如：128.00")
    private BigDecimal currentPrice;

    /** 沙龙举办/开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "沙龙举办/开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 已售数量（用于页面展示） */
    @Excel(name = "已售数量", readConverterExp = "用=于页面展示")
    private Long salesVolume;

    /** 状态：0-下架草稿，1-上架售卖中 */
    @Excel(name = "状态：0-下架草稿，1-上架售卖中")
    private Long status;

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

    public void setSubtitle(String subtitle) 
    {
        this.subtitle = subtitle;
    }

    public String getSubtitle() 
    {
        return subtitle;
    }

    public void setCoverImg(String coverImg) 
    {
        this.coverImg = coverImg;
    }

    public String getCoverImg() 
    {
        return coverImg;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setOriginalPrice(BigDecimal originalPrice) 
    {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getOriginalPrice() 
    {
        return originalPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) 
    {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getCurrentPrice() 
    {
        return currentPrice;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setSalesVolume(Long salesVolume) 
    {
        this.salesVolume = salesVolume;
    }

    public Long getSalesVolume() 
    {
        return salesVolume;
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
            .append("title", getTitle())
            .append("subtitle", getSubtitle())
            .append("coverImg", getCoverImg())
            .append("description", getDescription())
            .append("originalPrice", getOriginalPrice())
            .append("currentPrice", getCurrentPrice())
            .append("startTime", getStartTime())
            .append("salesVolume", getSalesVolume())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
