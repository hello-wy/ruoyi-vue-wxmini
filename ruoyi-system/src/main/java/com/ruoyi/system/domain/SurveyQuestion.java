package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SurveyQuestion extends BaseEntity {
    private Long id;
    private Long formId;
    private Long parentQuestionId;
    private String questionNo;
    private String sectionTitle;
    private String questionType;
    private String content;
    private String inputPlaceholder;
    private Integer requiredFlag;
    private Integer sortOrder;
    private Integer status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFormId() { return formId; }
    public void setFormId(Long formId) { this.formId = formId; }
    public Long getParentQuestionId() { return parentQuestionId; }
    public void setParentQuestionId(Long parentQuestionId) { this.parentQuestionId = parentQuestionId; }
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
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
