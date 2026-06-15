package com.ruoyi.system.domain.vo;

public class PersonalityTestAnswerResultVo {
    private Boolean completed;
    private Long attemptId;
    private Integer answeredCount;
    private PersonalityTestQuestionVo nextQuestion;

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Integer getAnsweredCount() {
        return answeredCount;
    }

    public void setAnsweredCount(Integer answeredCount) {
        this.answeredCount = answeredCount;
    }

    public PersonalityTestQuestionVo getNextQuestion() {
        return nextQuestion;
    }

    public void setNextQuestion(PersonalityTestQuestionVo nextQuestion) {
        this.nextQuestion = nextQuestion;
    }
}
