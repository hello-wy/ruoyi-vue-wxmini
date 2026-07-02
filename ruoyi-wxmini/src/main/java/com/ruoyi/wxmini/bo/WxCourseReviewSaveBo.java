package com.ruoyi.wxmini.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class WxCourseReviewSaveBo {
    @NotBlank(message = "orderNo不能为空")
    private String orderNo;

    @NotBlank(message = "评价内容不能为空")
    @Size(max = 1000, message = "评价内容不能超过1000字")
    private String content;

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
