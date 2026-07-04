package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 学员员工绑定视图对象。
 */
public class StudentStaffAssignmentVo {

    private Long assignmentId;
    private Long studentId;
    private Long ownerDeptId;
    private String ownerDeptName;
    private Long boundUserId;
    private String boundUserName;
    private String boundUserNickName;
    private String boundUserPhone;
    private String boundUserAdminLevel;
    private String boundDeptName;
    private Long bindBy;
    private String bindByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date bindTime;

    private Boolean bound;
    private Boolean canBind;
    private Boolean canClaim;

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
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

    public String getOwnerDeptName() {
        return ownerDeptName;
    }

    public void setOwnerDeptName(String ownerDeptName) {
        this.ownerDeptName = ownerDeptName;
    }

    public Long getBoundUserId() {
        return boundUserId;
    }

    public void setBoundUserId(Long boundUserId) {
        this.boundUserId = boundUserId;
    }

    public String getBoundUserName() {
        return boundUserName;
    }

    public void setBoundUserName(String boundUserName) {
        this.boundUserName = boundUserName;
    }

    public String getBoundUserNickName() {
        return boundUserNickName;
    }

    public void setBoundUserNickName(String boundUserNickName) {
        this.boundUserNickName = boundUserNickName;
    }

    public String getBoundUserPhone() {
        return boundUserPhone;
    }

    public void setBoundUserPhone(String boundUserPhone) {
        this.boundUserPhone = boundUserPhone;
    }

    public String getBoundUserAdminLevel() {
        return boundUserAdminLevel;
    }

    public void setBoundUserAdminLevel(String boundUserAdminLevel) {
        this.boundUserAdminLevel = boundUserAdminLevel;
    }

    public String getBoundDeptName() {
        return boundDeptName;
    }

    public void setBoundDeptName(String boundDeptName) {
        this.boundDeptName = boundDeptName;
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

    public Boolean getBound() {
        return bound;
    }

    public void setBound(Boolean bound) {
        this.bound = bound;
    }

    public Boolean getCanBind() {
        return canBind;
    }

    public void setCanBind(Boolean canBind) {
        this.canBind = canBind;
    }

    public Boolean getCanClaim() {
        return canClaim;
    }

    public void setCanClaim(Boolean canClaim) {
        this.canClaim = canClaim;
    }
}
