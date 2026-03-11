package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 讲师风采对象 lecturer_profile
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
public class LecturerProfile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 讲师姓名 */
    @Excel(name = "讲师姓名")
    private String name;

    /** 讲师简介/职位介绍 */
    @Excel(name = "讲师简介/职位介绍")
    private String intro;

    /** 头像URL */
    @Excel(name = "头像URL")
    private String avatarUrl;

    /** 宣传海报URL */
    @Excel(name = "宣传海报URL")
    private String posterUrl;

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

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setIntro(String intro) 
    {
        this.intro = intro;
    }

    public String getIntro() 
    {
        return intro;
    }

    public void setAvatarUrl(String avatarUrl) 
    {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() 
    {
        return avatarUrl;
    }

    public void setPosterUrl(String posterUrl) 
    {
        this.posterUrl = posterUrl;
    }

    public String getPosterUrl() 
    {
        return posterUrl;
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
            .append("name", getName())
            .append("intro", getIntro())
            .append("avatarUrl", getAvatarUrl())
            .append("posterUrl", getPosterUrl())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isDeleted", getIsDeleted())
            .toString();
    }
}
