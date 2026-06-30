package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class PersonalityTestResultVo {
    private Long attemptId;
    private Integer status;
    private Boolean completed;
    private Integer answeredCount;
    private Integer totalQuestions;
    private List<PersonalityTestResultAnswerVo> answers;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date completedAt;

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Integer getAnsweredCount() {
        return answeredCount;
    }

    public void setAnsweredCount(Integer answeredCount) {
        this.answeredCount = answeredCount;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public List<PersonalityTestResultAnswerVo> getAnswers() {
        return answers;
    }

    public void setAnswers(List<PersonalityTestResultAnswerVo> answers) {
        this.answers = answers;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }
}
