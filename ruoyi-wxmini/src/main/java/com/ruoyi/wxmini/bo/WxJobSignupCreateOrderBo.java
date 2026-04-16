package com.ruoyi.wxmini.bo;

import javax.validation.constraints.NotNull;

public class WxJobSignupCreateOrderBo {
    @NotNull(message = "jobId不能为空")
    private Long jobId;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}
