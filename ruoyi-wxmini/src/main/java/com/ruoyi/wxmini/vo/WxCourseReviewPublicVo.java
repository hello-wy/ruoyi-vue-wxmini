package com.ruoyi.wxmini.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class WxCourseReviewPublicVo {
    private Long id;
    private Long courseId;
    private String content;
    private String reviewerName;
    private String reviewerAvatarUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }
    public String getReviewerAvatarUrl() { return reviewerAvatarUrl; }
    public void setReviewerAvatarUrl(String reviewerAvatarUrl) { this.reviewerAvatarUrl = reviewerAvatarUrl; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
