package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class PersonalityTest extends BaseEntity {
    private static final long serialVersionUID = 1L;

    public static final Integer STATUS_ENABLED = 1;

    private Long id;
    private String code;
    private String title;
    private String description;
    private Integer totalQuestions;
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
