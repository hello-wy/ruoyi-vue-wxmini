package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class CourseReview extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String orderNo;
    private String userId;
    private Long courseId;
    private String content;
    private String reviewerName;
    private String reviewerAvatarUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }
    public String getReviewerAvatarUrl() { return reviewerAvatarUrl; }
    public void setReviewerAvatarUrl(String reviewerAvatarUrl) { this.reviewerAvatarUrl = reviewerAvatarUrl; }
}
