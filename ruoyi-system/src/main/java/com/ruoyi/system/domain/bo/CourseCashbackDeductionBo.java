package com.ruoyi.system.domain.bo;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CourseCashbackDeductionBo {
    @NotNull(message = "员工ID不能为空") private Long employeeUserId;
    @NotNull(message = "扣减金额不能为空") @DecimalMin(value = "0.01", message = "扣减金额必须大于0") private BigDecimal amount;
    @NotBlank(message = "扣减原因不能为空") private String reason;
    public Long getEmployeeUserId() { return employeeUserId; } public void setEmployeeUserId(Long employeeUserId) { this.employeeUserId = employeeUserId; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getReason() { return reason; } public void setReason(String reason) { this.reason = reason; }
}
