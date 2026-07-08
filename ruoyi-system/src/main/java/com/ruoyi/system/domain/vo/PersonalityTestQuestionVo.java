package com.ruoyi.system.domain.vo;

import java.util.List;

public class PersonalityTestQuestionVo {
    private Long attemptId;
    private Long testId;
    private Long questionId;
    private Integer questionNo;
    private Integer totalQuestions;
    private String content;
    private Integer answerValue;
    private Integer selectedOptionId;
    private Integer answerOptionId;
    private List<PersonalityTestOptionVo> options;

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Integer getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(Integer questionNo) {
        this.questionNo = questionNo;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(Integer answerValue) {
        this.answerValue = answerValue;
    }

    public Integer getSelectedOptionId() {
        return selectedOptionId;
    }

    public void setSelectedOptionId(Integer selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
    }

    public Integer getAnswerOptionId() {
        return answerOptionId;
    }

    public void setAnswerOptionId(Integer answerOptionId) {
        this.answerOptionId = answerOptionId;
    }

    public List<PersonalityTestOptionVo> getOptions() {
        return options;
    }

    public void setOptions(List<PersonalityTestOptionVo> options) {
        this.options = options;
    }
}
