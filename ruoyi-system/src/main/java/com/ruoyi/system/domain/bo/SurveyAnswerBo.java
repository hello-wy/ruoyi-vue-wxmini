package com.ruoyi.system.domain.bo;

public class SurveyAnswerBo {
    private Long questionId;
    private Object answerValue;
    private String answerText;

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public Object getAnswerValue() { return answerValue; }
    public void setAnswerValue(Object answerValue) { this.answerValue = answerValue; }
    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
}
