package com.ruoyi.wxmini.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class WxCourseNoteSaveBo {
    @NotNull(message = "courseId不能为空")
    private Long courseId;

    @NotBlank(message = "笔记内容不能为空")
    @Size(max = 2000, message = "笔记内容不能超过2000字")
    private String content;

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
