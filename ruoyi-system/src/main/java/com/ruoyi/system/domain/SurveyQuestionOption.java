package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SurveyQuestionOption extends BaseEntity {
    private Long id;
    private Long questionId;
    private String optionValue;
    private String optionLabel;
    private Integer allowTextInput;
    private Integer sortOrder;
    private Integer status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public String getOptionValue() { return optionValue; }
    public void setOptionValue(String optionValue) { this.optionValue = optionValue; }
    public String getOptionLabel() { return optionLabel; }
    public void setOptionLabel(String optionLabel) { this.optionLabel = optionLabel; }
    public Integer getAllowTextInput() { return allowTextInput; }
    public void setAllowTextInput(Integer allowTextInput) { this.allowTextInput = allowTextInput; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
