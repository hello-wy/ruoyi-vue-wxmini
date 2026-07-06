package com.ruoyi.system.domain.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurveyAssignmentDetailVo {
    private Long assignmentId;
    private Long submissionId;
    private Integer status;
    private String statusLabel;
    private Date submittedAt;
    private SurveyCourseVo course;
    private SurveyUserVo user;
    private SurveyFormVo form;
    private List<SurveyAnswerVo> answers = new ArrayList<>();

    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }
    public Long getSubmissionId() { return submissionId; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }
    public Date getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Date submittedAt) { this.submittedAt = submittedAt; }
    public SurveyCourseVo getCourse() { return course; }
    public void setCourse(SurveyCourseVo course) { this.course = course; }
    public SurveyUserVo getUser() { return user; }
    public void setUser(SurveyUserVo user) { this.user = user; }
    public SurveyFormVo getForm() { return form; }
    public void setForm(SurveyFormVo form) { this.form = form; }
    public List<SurveyAnswerVo> getAnswers() { return answers; }
    public void setAnswers(List<SurveyAnswerVo> answers) { this.answers = answers; }
}
