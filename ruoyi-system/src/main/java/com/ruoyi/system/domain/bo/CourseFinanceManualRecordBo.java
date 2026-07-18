package com.ruoyi.system.domain.bo;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CourseFinanceManualRecordBo {
    @NotBlank(message = "记录类型不能为空")
    private String recordType;
    @NotNull(message = "课程不能为空")
    private Long courseId;
    @NotBlank(message = "微信用户不能为空")
    private String wxminiUserId;
    @NotNull(message = "员工不能为空")
    private Long employeeUserId;
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private BigDecimal amount;
    @NotBlank(message = "原因不能为空")
    private String reason;

    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getWxminiUserId() { return wxminiUserId; }
    public void setWxminiUserId(String wxminiUserId) { this.wxminiUserId = wxminiUserId; }
    public Long getEmployeeUserId() { return employeeUserId; }
    public void setEmployeeUserId(Long employeeUserId) { this.employeeUserId = employeeUserId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
