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

    /** 资料展示名 */
    @Excel(name = "资料展示名")
    private String name;

    /** 服务器扁平存储文件名 */
    @Excel(name = "服务器扁平存储文件名")
    private String uuid;

    /** 上传原始文件名 */
    @Excel(name = "上传原始文件名")
    private String originalName;

    /** 服务器相对路径 */
    @Excel(name = "服务器相对路径")
    private String relativePath;

    /** 文件类型 */
    @Excel(name = "文件类型")
    private String fileType;

    /** 文件大小，单位字节 */
    @Excel(name = "文件大小")
    private Long fileSize;

    /** 分类标签 */
    @Excel(name = "分类标签")
    private String tag;

    /** 资料/PDF文件OSS链接，兼容旧字段 */
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getOriginalName()
    {
        return originalName;
    }

    public void setOriginalName(String originalName)
    {
        this.originalName = originalName;
    }

    public String getRelativePath()
    {
        return relativePath;
    }

    public void setRelativePath(String relativePath)
    {
        this.relativePath = relativePath;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public Long getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
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
            .append("name", getName())
            .append("uuid", getUuid())
            .append("originalName", getOriginalName())
            .append("relativePath", getRelativePath())
            .append("fileType", getFileType())
            .append("fileSize", getFileSize())
            .append("tag", getTag())
            .append("url", getUrl())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isDeleted", getIsDeleted())
            .toString();
    }
}
