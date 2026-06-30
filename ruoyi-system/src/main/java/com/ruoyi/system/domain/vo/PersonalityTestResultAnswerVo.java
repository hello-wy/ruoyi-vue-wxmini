package com.ruoyi.system.domain.vo;

public class PersonalityTestResultAnswerVo {
    private Long questionId;
    private Integer questionNo;
    private Integer dimensionNo;
    private Integer answerValue;
    private String answerLabel;

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

    public Integer getDimensionNo() {
        return dimensionNo;
    }

    public void setDimensionNo(Integer dimensionNo) {
        this.dimensionNo = dimensionNo;
    }

    public Integer getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(Integer answerValue) {
        this.answerValue = answerValue;
    }

    public String getAnswerLabel() {
        return answerLabel;
    }

    public void setAnswerLabel(String answerLabel) {
        this.answerLabel = answerLabel;
    }
}
