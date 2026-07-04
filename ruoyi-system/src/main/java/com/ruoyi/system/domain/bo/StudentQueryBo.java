package com.ruoyi.system.domain.bo;

public class StudentQueryBo {

    private String realName;
    private String phone;
    /** 绑定状态：bound 已绑定，unbound 未绑定 */
    private String assignmentStatus;
    /** 绑定员工用户ID */
    private Long boundUserId;
    /** 服务端生成的访问范围，不接收前端传值 */
    private StudentAccessScope accessScope;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAssignmentStatus() {
        return assignmentStatus;
    }

    public void setAssignmentStatus(String assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
    }

    public Long getBoundUserId() {
        return boundUserId;
    }

    public void setBoundUserId(Long boundUserId) {
        this.boundUserId = boundUserId;
    }

    public StudentAccessScope getAccessScope() {
        return accessScope;
    }

    public void setAccessScope(StudentAccessScope accessScope) {
        this.accessScope = accessScope;
    }
}
