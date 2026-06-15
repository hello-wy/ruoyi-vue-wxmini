package com.ruoyi.system.domain.vo;

public class PersonalityTestEntryVo {
    private Long testId;
    private String title;
    private String description;
    private Integer totalQuestions;
    private Boolean hasInProgress;
    private Long attemptId;
    private Integer answeredCount;
    private Integer currentQuestionNo;

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Boolean getHasInProgress() {
        return hasInProgress;
    }

    public void setHasInProgress(Boolean hasInProgress) {
        this.hasInProgress = hasInProgress;
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Integer getAnsweredCount() {
        return answeredCount;
    }

    public void setAnsweredCount(Integer answeredCount) {
        this.answeredCount = answeredCount;
    }

    public Integer getCurrentQuestionNo() {
        return currentQuestionNo;
    }

    public void setCurrentQuestionNo(Integer currentQuestionNo) {
        this.currentQuestionNo = currentQuestionNo;
    }
}
