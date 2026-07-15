package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.system.domain.bo.StudentAccessScope;
import com.ruoyi.system.mapper.StudentStaffAssignmentMapper;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentAccessServiceImplTest {

    @Mock
    private ISysUserService sysUserService;

    @Mock
    private ISysDeptService sysDeptService;

    @Mock
    private StudentStaffAssignmentMapper studentStaffAssignmentMapper;

    @Mock
    private SysDeptMapper sysDeptMapper;

    @InjectMocks
    private StudentAccessServiceImpl studentAccessService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void adminRoleShouldHaveFullStudentAccessForNonDefaultUserId() {
        setLoginUser(22L, "manager", Constants.SUPER_ADMIN);

        StudentAccessScope scope = studentAccessService.resolveCurrentScope();

        assertTrue(scope.isFullAccess());
        assertEquals(Collections.emptyList(), scope.getVisibleDeptIds());
        assertEquals(Collections.emptyList(), scope.getVisibleUserIds());
        verify(sysDeptService, never()).selectDeptList(any(SysDept.class));
        verify(sysUserService, never()).selectUserList(any(SysUser.class));
        verify(sysDeptMapper, never()).selectChildrenDeptById(any(Long.class));
    }

    @Test
    void ordinaryRoleShouldKeepExistingRestrictedScope() {
        setLoginUser(22L, "manager", "city_manager");
        SysDept visibleDept = new SysDept();
        visibleDept.setDeptId(101L);
        SysUser visibleEmployee = new SysUser();
        visibleEmployee.setUserId(33L);
        when(sysDeptService.selectDeptList(any(SysDept.class)))
                .thenReturn(Collections.singletonList(visibleDept));
        when(sysUserService.selectUserList(any(SysUser.class)))
                .thenReturn(Collections.singletonList(visibleEmployee));

        StudentAccessScope scope = studentAccessService.resolveCurrentScope();

        assertFalse(scope.isFullAccess());
        assertEquals(Collections.singletonList(101L), scope.getVisibleDeptIds());
        assertEquals(Collections.singletonList(33L), scope.getVisibleUserIds());
    }

    private void setLoginUser(Long userId, String adminLevel, String roleKey) {
        SysRole role = new SysRole();
        role.setRoleKey(roleKey);
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setDeptId(100L);
        user.setAdminLevel(adminLevel);
        user.setRoles(Collections.singletonList(role));
        LoginUser loginUser = new LoginUser(userId, user.getDeptId(), user, Collections.emptySet());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(loginUser, null));
    }
}
