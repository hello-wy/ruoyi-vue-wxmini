package com.ruoyi.system.domain.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurveyCourseGroupVo {
    private Long courseId;
    private String courseName;
    private Date courseTime;
    private List<SurveyAssignmentVo> surveys = new ArrayList<>();

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public Date getCourseTime() { return courseTime; }
    public void setCourseTime(Date courseTime) { this.courseTime = courseTime; }
    public List<SurveyAssignmentVo> getSurveys() { return surveys; }
    public void setSurveys(List<SurveyAssignmentVo> surveys) { this.surveys = surveys; }
}
