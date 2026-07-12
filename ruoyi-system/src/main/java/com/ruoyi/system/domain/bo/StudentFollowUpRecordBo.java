package com.ruoyi.system.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class StudentFollowUpRecordBo {

    @NotNull(message = "回访时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date followUpTime;

    @Min(value = 0, message = "回访方式不正确")
    @Max(value = 4, message = "回访方式不正确")
    private Integer followUpMethod;

    @Min(value = 0, message = "回访结果不正确")
    @Max(value = 4, message = "回访结果不正确")
    private Integer followUpResult;

    @NotBlank(message = "回访标题不能为空")
    @Size(max = 200, message = "回访标题不能超过200字")
    private String title;

    @NotBlank(message = "回访内容不能为空")
    @Size(max = 4000, message = "回访内容不能超过4000字")
    private String content;

    @Size(max = 8000, message = "回访表单数据不能超过8000字")
    private String formData;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date nextFollowUpTime;

    @Size(max = 500, message = "备注不能超过500字")
    private String remark;

    public Date getFollowUpTime() {
        return followUpTime;
    }

    public void setFollowUpTime(Date followUpTime) {
        this.followUpTime = followUpTime;
    }

    public Integer getFollowUpMethod() {
        return followUpMethod;
    }

    public void setFollowUpMethod(Integer followUpMethod) {
        this.followUpMethod = followUpMethod;
    }

    public Integer getFollowUpResult() {
        return followUpResult;
    }

    public void setFollowUpResult(Integer followUpResult) {
        this.followUpResult = followUpResult;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    public Date getNextFollowUpTime() {
        return nextFollowUpTime;
    }

    public void setNextFollowUpTime(Date nextFollowUpTime) {
        this.nextFollowUpTime = nextFollowUpTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
