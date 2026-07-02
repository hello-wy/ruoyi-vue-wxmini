package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 学员回访记录对象 student_follow_up_record
 */
public class StudentFollowUpRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 学员ID，对应 user_info.id */
    @Excel(name = "学员ID")
    private Long studentId;

    /** 回访时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "回访时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date followUpTime;

    /** 回访方式：0未指定，1电话，2微信，3线下，4其他 */
    @Excel(name = "回访方式")
    private Integer followUpMethod;

    /** 回访结果：0未记录，1已接通，2未接通，3需再次跟进，4已完成 */
    @Excel(name = "回访结果")
    private Integer followUpResult;

    /** 回访内容 */
    private String content;

    /** 回访表单扩展数据(JSON) */
    private String formData;

    /** 下次回访时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "下次回访时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date nextFollowUpTime;

    /** 回访人ID，对应 sys_user.user_id */
    private Long operatorId;

    /** 回访人姓名快照 */
    private String operatorName;

    /** 逻辑删除标识：0正常，1删除 */
    private Integer isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

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

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("studentId", getStudentId())
                .append("followUpTime", getFollowUpTime())
                .append("followUpMethod", getFollowUpMethod())
                .append("followUpResult", getFollowUpResult())
                .append("content", getContent())
                .append("formData", getFormData())
                .append("nextFollowUpTime", getNextFollowUpTime())
                .append("operatorId", getOperatorId())
                .append("operatorName", getOperatorName())
                .append("remark", getRemark())
                .append("isDeleted", getIsDeleted())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
