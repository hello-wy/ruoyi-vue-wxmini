package com.ruoyi.wxmini.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class WxCourseNoteUpdateBo {
    @NotBlank(message = "笔记内容不能为空")
    @Size(max = 2000, message = "笔记内容不能超过2000字")
    private String content;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
