package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class PersonalityTestAnswer extends BaseEntity {
    private static final long serialVersionUID = 1L;

    public static final Integer ANSWER_NO = 0;
    public static final Integer ANSWER_YES = 1;
    public static final Integer ANSWER_UNSURE = 2;

    private Long id;
    private Long attemptId;
    private Long testId;
    private Long userInfoId;
    private Long questionId;
    private Integer questionNo;
    private Integer answerValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
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

    public Integer getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(Integer answerValue) {
        this.answerValue = answerValue;
    }
}
