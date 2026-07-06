package com.ruoyi.system.domain.vo;

import java.util.ArrayList;
import java.util.List;

public class SurveyQuestionVo {
    private Long questionId;
    private String questionNo;
    private String sectionTitle;
    private String questionType;
    private String content;
    private String inputPlaceholder;
    private Integer requiredFlag;
    private List<SurveyOptionVo> options = new ArrayList<>();
    private List<SurveyQuestionVo> children = new ArrayList<>();

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public String getQuestionNo() { return questionNo; }
    public void setQuestionNo(String questionNo) { this.questionNo = questionNo; }
    public String getSectionTitle() { return sectionTitle; }
    public void setSectionTitle(String sectionTitle) { this.sectionTitle = sectionTitle; }
    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getInputPlaceholder() { return inputPlaceholder; }
    public void setInputPlaceholder(String inputPlaceholder) { this.inputPlaceholder = inputPlaceholder; }
    public Integer getRequiredFlag() { return requiredFlag; }
    public void setRequiredFlag(Integer requiredFlag) { this.requiredFlag = requiredFlag; }
    public List<SurveyOptionVo> getOptions() { return options; }
    public void setOptions(List<SurveyOptionVo> options) { this.options = options; }
    public List<SurveyQuestionVo> getChildren() { return children; }
    public void setChildren(List<SurveyQuestionVo> children) { this.children = children; }
}
