package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资料中心数据对象 lecture_material
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
public class LectureMaterial extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联课程ID */
    @Excel(name = "关联课程ID")
    private Long lectureId;

    /** 资料/PDF文件OSS链接 */
    @Excel(name = "资料/PDF文件OSS链接")
    private String url;

    /** 状态: 0-隐藏, 1-展示 (由后端接口业务调整) */
    @Excel(name = "状态: 0-隐藏, 1-展示 (由后端接口业务调整)")
    private Long status;

    /** 逻辑删除标识 */
    @Excel(name = "逻辑删除标识")
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
