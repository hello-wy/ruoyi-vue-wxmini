package com.ruoyi.system.domain.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurveyFormVo {
    private Long formId;
    private String code;
    private String title;
    private String description;
    private String closingText;
    private Integer totalQuestions;
    private Integer status;
    private Integer sentCount;
    private Integer answeredCount;
    private Long assignmentId;
    private SurveyCourseVo course;
    private Date createTime;
    private List<SurveyQuestionVo> questions = new ArrayList<>();

    public Long getFormId() { return formId; }
    public void setFormId(Long formId) { this.formId = formId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getClosingText() { return closingText; }
    public void setClosingText(String closingText) { this.closingText = closingText; }
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getSentCount() { return sentCount; }
    public void setSentCount(Integer sentCount) { this.sentCount = sentCount; }
    public Integer getAnsweredCount() { return answeredCount; }
    public void setAnsweredCount(Integer answeredCount) { this.answeredCount = answeredCount; }
    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }
    public SurveyCourseVo getCourse() { return course; }
    public void setCourse(SurveyCourseVo course) { this.course = course; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public List<SurveyQuestionVo> getQuestions() { return questions; }
    public void setQuestions(List<SurveyQuestionVo> questions) { this.questions = questions; }
}
