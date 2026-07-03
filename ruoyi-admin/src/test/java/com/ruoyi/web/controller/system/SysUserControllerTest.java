package com.ruoyi.web.controller.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;

@ExtendWith(MockitoExtension.class)
class SysUserControllerTest
{
    @Mock
    private ISysUserService userService;

    @Mock
    private ISysRoleService roleService;

    @Mock
    private ISysDeptService deptService;

    @Mock
    private ISysPostService postService;

    @InjectMocks
    private SysUserController controller;

    @BeforeEach
    void setUp()
    {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        ReflectionTestUtils.setField(controller, "validator", validator);
        SysUser loginUser = new SysUser();
        loginUser.setUserId(1L);
        loginUser.setUserName("admin");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                new LoginUser(1L, 100L, loginUser, Collections.emptySet()), null));
    }

    @AfterEach
    void tearDown()
    {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addShouldEncryptRawPasswordAndReturnSubmittedCredential()
    {
        SysUser user = buildUser();
        user.setRawPassword("Admin123");
        when(userService.checkUserNameUnique(any(SysUser.class))).thenReturn(true);
        when(userService.checkPhoneUnique(any(SysUser.class))).thenReturn(true);
        when(userService.insertUser(any(SysUser.class))).thenReturn(1);

        AjaxResult result = controller.add(user);

        ArgumentCaptor<SysUser> captor = ArgumentCaptor.forClass(SysUser.class);
        verify(userService).insertUser(captor.capture());
        SysUser savedUser = captor.getValue();
        assertEquals("13800000000", savedUser.getUserName());
        assertEquals("admin", savedUser.getCreateBy());
        assertNotEquals("Admin123", savedUser.getPassword());
        assertTrue(SecurityUtils.matchesPassword("Admin123", savedUser.getPassword()));
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        @SuppressWarnings("unchecked")
        Map<String, String> credentials = (Map<String, String>) result.get(AjaxResult.DATA_TAG);
        assertEquals("13800000000", credentials.get("phone"));
        assertEquals("Admin123", credentials.get("password"));
    }

    @Test
    void addShouldRejectMissingRawPassword()
    {
        SysUser user = buildUser();
        when(userService.checkUserNameUnique(any(SysUser.class))).thenReturn(true);
        when(userService.checkPhoneUnique(any(SysUser.class))).thenReturn(true);

        ServiceException exception = assertThrows(ServiceException.class, () -> controller.add(user));

        assertEquals("登录密码不能为空", exception.getMessage());
        verify(userService, never()).insertUser(any(SysUser.class));
    }

    @Test
    void addShouldRejectIllegalRawPassword()
    {
        SysUser user = buildUser();
        user.setRawPassword("Admin<123");
        when(userService.checkUserNameUnique(any(SysUser.class))).thenReturn(true);
        when(userService.checkPhoneUnique(any(SysUser.class))).thenReturn(true);

        ServiceException exception = assertThrows(ServiceException.class, () -> controller.add(user));

        assertEquals("密码不能包含非法字符：< > \" ' \\ |", exception.getMessage());
        verify(userService, never()).insertUser(any(SysUser.class));
    }

    @Test
    void resetPwdShouldEncryptNewPassword()
    {
        SysUser user = new SysUser();
        user.setUserId(2L);
        user.setPassword("NewPwd123");
        when(userService.resetPwd(any(SysUser.class))).thenReturn(1);

        AjaxResult result = controller.resetPwd(user);

        ArgumentCaptor<SysUser> captor = ArgumentCaptor.forClass(SysUser.class);
        verify(userService).resetPwd(captor.capture());
        SysUser savedUser = captor.getValue();
        assertEquals("admin", savedUser.getUpdateBy());
        assertNotEquals("NewPwd123", savedUser.getPassword());
        assertTrue(SecurityUtils.matchesPassword("NewPwd123", savedUser.getPassword()));
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void resetPwdShouldRejectShortPassword()
    {
        SysUser user = new SysUser();
        user.setUserId(2L);
        user.setPassword("1234");

        ServiceException exception = assertThrows(ServiceException.class, () -> controller.resetPwd(user));

        assertEquals("密码长度必须在5到20个字符之间", exception.getMessage());
        verify(userService, never()).resetPwd(any(SysUser.class));
    }

    private SysUser buildUser()
    {
        SysUser user = new SysUser();
        user.setPhonenumber("13800000000");
        user.setNickName("张三");
        user.setAdminLevel("employee");
        user.setStatus("0");
        return user;
    }
}
