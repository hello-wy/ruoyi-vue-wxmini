package com.ruoyi.wxmini.bo;

import com.ruoyi.system.domain.bo.SurveyAnswerBo;

import java.util.List;

public class WxSurveySubmitBo {
    private List<SurveyAnswerBo> answers;

    public List<SurveyAnswerBo> getAnswers() { return answers; }
    public void setAnswers(List<SurveyAnswerBo> answers) { this.answers = answers; }
}
