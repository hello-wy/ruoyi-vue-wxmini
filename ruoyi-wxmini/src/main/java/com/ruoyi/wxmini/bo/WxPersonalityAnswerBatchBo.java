package com.ruoyi.wxmini.bo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class WxPersonalityAnswerBatchBo {
    @Valid
    @NotNull(message = "答案列表不能为空")
    @Size(min = 1, message = "答案列表不能为空")
    private List<WxPersonalityAnswerBo> answers;

    public List<WxPersonalityAnswerBo> getAnswers() {
        return answers;
    }

    public void setAnswers(List<WxPersonalityAnswerBo> answers) {
        this.answers = answers;
    }
}
