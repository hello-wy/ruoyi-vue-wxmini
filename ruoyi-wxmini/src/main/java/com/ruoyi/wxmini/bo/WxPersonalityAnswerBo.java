package com.ruoyi.wxmini.bo;

import javax.validation.constraints.NotNull;

public class WxPersonalityAnswerBo {
    @NotNull(message = "题目不能为空")
    private Long questionId;

    @NotNull(message = "答案不能为空")
    private Integer answerValue;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Integer getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(Integer answerValue) {
        this.answerValue = answerValue;
    }
}
