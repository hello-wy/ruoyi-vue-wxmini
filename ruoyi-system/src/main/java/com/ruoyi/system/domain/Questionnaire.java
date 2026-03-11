package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 问卷调查配置对象 questionnaire
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
public class Questionnaire extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID (雪花算法) */
    private Long id;

    /** 关联课程ID */
    @Excel(name = "关联课程ID")
    private Long lectureId;

    /** 问卷星链接URL */
    @Excel(name = "问卷星链接URL")
    private String url;

    /** 状态: 0-失效, 1-有效 (配合Java定时任务控制开课7天内有效) */
    @Excel(name = "状态: 0-失效, 1-有效 (配合Java定时任务控制开课7天内有效)")
    private Long status;

    /** 逻辑删除标识: 0-未删除, 1-已删除 */
    @Excel(name = "逻辑删除标识: 0-未删除, 1-已删除")
    private Long isDeleted;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setLectureId(Long lectureId) 
    {
        this.lectureId = lectureId;
    }

    public Long getLectureId() 
    {
        return lectureId;
    }

    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setIsDeleted(Long isDeleted) 
    {
        this.isDeleted = isDeleted;
    }

    public Long getIsDeleted() 
    {
        return isDeleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("lectureId", getLectureId())
            .append("url", getUrl())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isDeleted", getIsDeleted())
            .toString();
    }
}
