package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.StudentStaffAssignment;
import com.ruoyi.system.domain.bo.StudentAccessScope;

public interface IStudentAccessService {

    StudentAccessScope resolveCurrentScope();

    boolean canAccessStudent(Long studentId);

    void checkStudentAccess(Long studentId);

    boolean canBind(StudentStaffAssignment assignment);

    boolean canBind(StudentStaffAssignment assignment, StudentAccessScope scope);

    boolean canClaim(StudentStaffAssignment assignment);

    boolean canClaim(StudentStaffAssignment assignment, StudentAccessScope scope);

    SysUser checkBindableTarget(Long targetUserId, StudentAccessScope scope);
}
