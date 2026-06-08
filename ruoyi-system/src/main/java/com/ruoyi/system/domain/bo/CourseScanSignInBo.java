package com.ruoyi.system.domain.bo;

import javax.validation.constraints.NotBlank;

public class CourseScanSignInBo {
    @NotBlank(message = "userId不能为空")
    private String userId;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
