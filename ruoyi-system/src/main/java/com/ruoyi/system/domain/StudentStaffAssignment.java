package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 学员员工绑定对象 student_staff_assignment
 */
public class StudentStaffAssignment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long studentId;
    private Long ownerDeptId;
    private Long employeeUserId;
    private Long bindBy;
    private String bindByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bindTime;

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

    public Long getOwnerDeptId() {
        return ownerDeptId;
    }

    public void setOwnerDeptId(Long ownerDeptId) {
        this.ownerDeptId = ownerDeptId;
    }

    public Long getEmployeeUserId() {
        return employeeUserId;
    }

    public void setEmployeeUserId(Long employeeUserId) {
        this.employeeUserId = employeeUserId;
    }

    public Long getBindBy() {
        return bindBy;
    }

    public void setBindBy(Long bindBy) {
        this.bindBy = bindBy;
    }

    public String getBindByName() {
        return bindByName;
    }

    public void setBindByName(String bindByName) {
        this.bindByName = bindByName;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }
}
