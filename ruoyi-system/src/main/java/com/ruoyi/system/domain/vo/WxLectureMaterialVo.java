package com.ruoyi.system.domain.vo;

import java.util.Date;

/**
 * 小程序资料中心列表项 VO
 */
public class WxLectureMaterialVo
{
    private Long id;

    private String name;

    private String originalName;

    private String fileType;

    private Long fileSize;

    private String fileSizeText;

    private String category;

    private String tag;

    private Long lectureId;

    private Date createTime;

    private String relativePath;

    private String downloadUrl;

    private String filePath;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getOriginalName()
    {
        return originalName;
    }

    public void setOriginalName(String originalName)
    {
        this.originalName = originalName;
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

    public String getFileSizeText()
    {
        return fileSizeText;
    }

    public void setFileSizeText(String fileSizeText)
    {
        this.fileSizeText = fileSizeText;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    public Long getLectureId()
    {
        return lectureId;
    }

    public void setLectureId(Long lectureId)
    {
        this.lectureId = lectureId;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getRelativePath()
    {
        return relativePath;
    }

    public void setRelativePath(String relativePath)
    {
        this.relativePath = relativePath;
    }

    public String getDownloadUrl()
    {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl)
    {
        this.downloadUrl = downloadUrl;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }
}
