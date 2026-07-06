package com.ruoyi.system.domain.vo;

public class SurveyAnswerVo {
    private Long questionId;
    private String questionNo;
    private String sectionTitle;
    private String questionType;
    private String questionContent;
    private String answerValue;
    private String answerLabel;
    private String answerText;

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public String getQuestionNo() { return questionNo; }
    public void setQuestionNo(String questionNo) { this.questionNo = questionNo; }
    public String getSectionTitle() { return sectionTitle; }
    public void setSectionTitle(String sectionTitle) { this.sectionTitle = sectionTitle; }
    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }
    public String getQuestionContent() { return questionContent; }
    public void setQuestionContent(String questionContent) { this.questionContent = questionContent; }
    public String getAnswerValue() { return answerValue; }
    public void setAnswerValue(String answerValue) { this.answerValue = answerValue; }
    public String getAnswerLabel() { return answerLabel; }
    public void setAnswerLabel(String answerLabel) { this.answerLabel = answerLabel; }
    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
}
