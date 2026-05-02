package com.ruoyi.wxmini.bo;

import javax.validation.constraints.NotNull;
import java.util.List;

public class WxJobPayrollCreateOrderBo {
    @NotNull(message = "jobId不能为空")
    private Long jobId;

    @NotNull(message = "employees不能为空")
    private List<WxPayrollEmployeeBo> employees;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public List<WxPayrollEmployeeBo> getEmployees() {
        return employees;
    }

    public void setEmployees(List<WxPayrollEmployeeBo> employees) {
        this.employees = employees;
    }
}
