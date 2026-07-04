package com.ruoyi.system.domain.bo;

import java.util.List;

/**
 * 后台学员访问范围内部对象，仅由服务端根据当前登录用户生成。
 */
public class StudentAccessScope {

    private boolean fullAccess;
    private boolean employeeAccess;
    private boolean bindPermission;
    private Long currentUserId;
    private Long currentDeptId;
    private String adminLevel;
    private List<Long> visibleDeptIds;
    private List<Long> visibleUserIds;

    public boolean isFullAccess() {
        return fullAccess;
    }

    public void setFullAccess(boolean fullAccess) {
        this.fullAccess = fullAccess;
    }

    public boolean isEmployeeAccess() {
        return employeeAccess;
    }

    public void setEmployeeAccess(boolean employeeAccess) {
        this.employeeAccess = employeeAccess;
    }

    public boolean isBindPermission() {
        return bindPermission;
    }

    public void setBindPermission(boolean bindPermission) {
        this.bindPermission = bindPermission;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public Long getCurrentDeptId() {
        return currentDeptId;
    }

    public void setCurrentDeptId(Long currentDeptId) {
        this.currentDeptId = currentDeptId;
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    public List<Long> getVisibleDeptIds() {
        return visibleDeptIds;
    }

    public void setVisibleDeptIds(List<Long> visibleDeptIds) {
        this.visibleDeptIds = visibleDeptIds;
    }

    public List<Long> getVisibleUserIds() {
        return visibleUserIds;
    }

    public void setVisibleUserIds(List<Long> visibleUserIds) {
        this.visibleUserIds = visibleUserIds;
    }
}
