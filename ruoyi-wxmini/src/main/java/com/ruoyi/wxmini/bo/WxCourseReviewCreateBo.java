package com.ruoyi.wxmini.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class WxCourseReviewCreateBo {
    @NotBlank(message = "评价内容不能为空")
    @Size(max = 1000, message = "评价内容不能超过1000字")
    private String content;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
