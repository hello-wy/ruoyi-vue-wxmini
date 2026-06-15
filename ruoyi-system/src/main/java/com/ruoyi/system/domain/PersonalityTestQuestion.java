package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class PersonalityTestQuestion extends BaseEntity {
    private static final long serialVersionUID = 1L;

    public static final Integer STATUS_ENABLED = 1;

    private Long id;
    private Long testId;
    private Integer questionNo;
    private Integer dimensionNo;
    private String content;
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
