package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户课程笔记。
 */
public class CourseNote extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String userId;
    private Long courseId;
    private String courseName;
    private String content;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
