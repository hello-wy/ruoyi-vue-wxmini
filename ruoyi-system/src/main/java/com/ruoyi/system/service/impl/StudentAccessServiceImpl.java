package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.StudentStaffAssignment;
import com.ruoyi.system.domain.bo.StudentAccessScope;
import com.ruoyi.system.mapper.StudentStaffAssignmentMapper;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.service.IStudentAccessService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentAccessServiceImpl implements IStudentAccessService {

    public static final String ADMIN_LEVEL_EMPLOYEE = "employee";
    public static final String ADMIN_LEVEL_NATIONAL_MANAGER = "national_general_manager";
    private static final String STUDENT_BIND_PERMISSION = "system:student:bind";
    private static final String ACCESS_DENIED_MESSAGE = "无权限访问学员数据";

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysDeptService sysDeptService;

    @Resource
    private StudentStaffAssignmentMapper studentStaffAssignmentMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public StudentAccessScope resolveCurrentScope() {
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if (currentUser == null || currentUser.getUserId() == null) {
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }

        StudentAccessScope scope = new StudentAccessScope();
        scope.setCurrentUserId(currentUser.getUserId());
        scope.setCurrentDeptId(currentUser.getDeptId());
        scope.setAdminLevel(currentUser.getAdminLevel());
        scope.setBindPermission(SecurityUtils.hasPermi(STUDENT_BIND_PERMISSION));

        boolean fullAccess = SysUser.isAdmin(currentUser.getUserId())
                || ADMIN_LEVEL_NATIONAL_MANAGER.equals(currentUser.getAdminLevel());
        scope.setFullAccess(fullAccess);
        scope.setEmployeeAccess(ADMIN_LEVEL_EMPLOYEE.equals(currentUser.getAdminLevel()));

        if (fullAccess) {
            scope.setVisibleDeptIds(Collections.emptyList());
            scope.setVisibleUserIds(Collections.emptyList());
            return scope;
        }

        if (scope.isEmployeeAccess()) {
            scope.setVisibleUserIds(Collections.singletonList(currentUser.getUserId()));
            scope.setVisibleDeptIds(resolveOwnDeptAndChildren(currentUser.getDeptId()));
            return scope;
        }

        scope.setVisibleDeptIds(resolveDataScopeDeptIds());
        scope.setVisibleUserIds(resolveDataScopeEmployeeUserIds());
        return scope;
    }

    @Override
    public boolean canAccessStudent(Long studentId) {
        if (studentId == null) {
            return false;
        }
        StudentAccessScope scope = resolveCurrentScope();
        return studentStaffAssignmentMapper.countAccessibleStudent(studentId, scope) > 0;
    }

    @Override
    public void checkStudentAccess(Long studentId) {
        if (!canAccessStudent(studentId)) {
            throw new ServiceException(ACCESS_DENIED_MESSAGE, HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public boolean canBind(StudentStaffAssignment assignment) {
        StudentAccessScope scope = resolveCurrentScope();
        return canBind(assignment, scope);
    }

    @Override
    public boolean canClaim(StudentStaffAssignment assignment) {
        StudentAccessScope scope = resolveCurrentScope();
        return canClaim(assignment, scope);
    }

    @Override
    public SysUser checkBindableTarget(Long targetUserId, StudentAccessScope scope) {
        if (targetUserId == null) {
            throw new ServiceException("请选择绑定员工");
        }
        SysUser target = sysUserService.selectUserById(targetUserId);
        if (target == null || !UserConstants.NORMAL.equals(target.getDelFlag())) {
            throw new ServiceException("绑定员工不存在");
        }
        if (!UserConstants.NORMAL.equals(target.getStatus())) {
            throw new ServiceException("绑定员工已停用");
        }
        if (!ADMIN_LEVEL_EMPLOYEE.equals(target.getAdminLevel())) {
            throw new ServiceException("只能绑定员工级别的系统用户");
        }
        if (target.getDeptId() == null || target.getDeptId() == 0L) {
            throw new ServiceException("绑定员工未配置所属部门，无法绑定学员");
        }
        if (!scope.isFullAccess() && !safeContains(scope.getVisibleUserIds(), targetUserId)) {
            throw new ServiceException("无权限绑定该员工", HttpStatus.FORBIDDEN);
        }
        return target;
    }

    public boolean canBind(StudentStaffAssignment assignment, StudentAccessScope scope) {
        if (assignment == null || !scope.isBindPermission()) {
            return false;
        }
        if (scope.isFullAccess()) {
            return true;
        }
        if (assignment.getId() == null) {
            return false;
        }
        if (scope.isEmployeeAccess()) {
            return canClaim(assignment, scope);
        }
        if (assignment.getEmployeeUserId() != null) {
            return safeContains(scope.getVisibleUserIds(), assignment.getEmployeeUserId());
        }
        return safeContains(scope.getVisibleDeptIds(), assignment.getOwnerDeptId());
    }

    public boolean canClaim(StudentStaffAssignment assignment, StudentAccessScope scope) {
        if (assignment == null || !scope.isBindPermission() || !scope.isEmployeeAccess()) {
            return false;
        }
        return assignment.getEmployeeUserId() == null
                && safeContains(scope.getVisibleDeptIds(), assignment.getOwnerDeptId());
    }

    private List<Long> resolveDataScopeDeptIds() {
        List<SysDept> depts = sysDeptService.selectDeptList(new SysDept());
        return depts.stream()
                .map(SysDept::getDeptId)
                .filter(id -> id != null && id != 0L)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Long> resolveDataScopeEmployeeUserIds() {
        SysUser query = new SysUser();
        query.setAdminLevel(ADMIN_LEVEL_EMPLOYEE);
        query.setStatus(UserConstants.NORMAL);
        List<SysUser> users = sysUserService.selectUserList(query);
        return users.stream()
                .map(SysUser::getUserId)
                .filter(id -> id != null && id != 0L)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Long> resolveOwnDeptAndChildren(Long deptId) {
        if (deptId == null || deptId == 0L) {
            return Collections.emptyList();
        }
        Set<Long> deptIds = new LinkedHashSet<>();
        deptIds.add(deptId);
        List<SysDept> children = sysDeptMapper.selectChildrenDeptById(deptId);
        for (SysDept dept : children) {
            if (dept == null || dept.getDeptId() == null) {
                continue;
            }
            deptIds.add(dept.getDeptId());
        }
        return new ArrayList<>(deptIds);
    }

    private boolean safeContains(List<Long> values, Long value) {
        return value != null && values != null && values.contains(value);
    }
}
