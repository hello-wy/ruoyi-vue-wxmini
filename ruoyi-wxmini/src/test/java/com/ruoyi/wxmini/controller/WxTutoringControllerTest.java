package com.ruoyi.wxmini.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.service.IParentsService;
import com.ruoyi.system.service.ITutorsService;
import com.ruoyi.wxmini.domain.BabyInfo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IBabyInfoService;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxTutoringControllerTest {

    private static final String USER_ID = "123";
    private static final String WECHAT_USER_ID = "6723e06a-5f1d-4479-9b59-b8df275dea40";

    @Mock
    private ITutorsService tutorsService;
    @Mock
    private IParentsService parentsService;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private IBabyInfoService babyInfoService;

    @InjectMocks
    private WxTutoringController controller;

    @AfterEach
    void clearContext() {
        WxMiniUserContext.clear();
    }

    @Test
    void listTutorsShouldBeAnonymous() throws Exception {
        Method method = WxTutoringController.class.getMethod(
                "listTutors",
                long.class,
                long.class,
                String.class,
                String.class,
                Long.class,
                Long.class
        );

        assertNotNull(method.getAnnotation(Anonymous.class));
    }

    @Test
    void myTutorShouldReturn200WhenTutorMissing() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(tutorsService.selectTutorsByUid(USER_ID)).thenReturn(null);
        AjaxResult result = controller.myTutor();
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void myTutorShouldReturn200WhenTutorFound() {
        Tutors tutor = new Tutors();
        tutor.setUid(USER_ID);
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(tutorsService.selectTutorsByUid(USER_ID)).thenReturn(tutor);
        AjaxResult result = controller.myTutor();
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void applyTutorShouldPersistRealnameToUserInfoInsteadOfTutors() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        Tutors request = new Tutors();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");
        request.setSchool("南大");
        request.setIdentity(1L);
        when(tutorsService.selectTutorsByUid(USER_ID)).thenReturn(null);
        when(userInfoService.updateRealnameInfo(USER_ID, "张三", "110105199001011234")).thenReturn(1);
        when(tutorsService.insertTutors(any(Tutors.class))).thenAnswer(invocation -> {
            Tutors tutor = invocation.getArgument(0);
            tutor.setId(1001L);
            return 1;
        });

        AjaxResult result = controller.applyTutor(request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals("申请成功，请等待审核", result.get(AjaxResult.MSG_TAG));
        assertEquals(1001L, result.get(AjaxResult.DATA_TAG));
        verify(userInfoService).updateRealnameInfo(USER_ID, "张三", "110105199001011234");
        ArgumentCaptor<Tutors> captor = ArgumentCaptor.forClass(Tutors.class);
        verify(tutorsService).insertTutors(captor.capture());
        Tutors saved = captor.getValue();
        assertEquals(USER_ID, saved.getUid());
        assertEquals(0L, saved.getStatus());
        assertEquals(1001L, saved.getId());
        assertNull(saved.getRealName());
        assertNull(saved.getIdCard());
        assertEquals("南大", saved.getSchool());
    }

    @Test
    void applyTutorShouldRejectUniversityTutorWithoutCurrentGrade() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        Tutors request = new Tutors();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");
        request.setIdentity(0L);
        request.setSchool("南京大学");
        request.setMajor("数学");
        request.setDegree(1L);

        AjaxResult result = controller.applyTutor(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("请选择当前年级", result.get(AjaxResult.MSG_TAG));
        verify(tutorsService, never()).insertTutors(any(Tutors.class));
    }

    @Test
    void applyTutorShouldClearCurrentGradeForNonUniversityTutor() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        Tutors request = new Tutors();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");
        request.setIdentity(1L);
        request.setCurrentGrade("大三");
        when(tutorsService.selectTutorsByUid(USER_ID)).thenReturn(null);
        when(userInfoService.updateRealnameInfo(USER_ID, "张三", "110105199001011234")).thenReturn(1);
        when(tutorsService.insertTutors(any(Tutors.class))).thenReturn(1);

        controller.applyTutor(request);

        ArgumentCaptor<Tutors> captor = ArgumentCaptor.forClass(Tutors.class);
        verify(tutorsService).insertTutors(captor.capture());
        assertNull(captor.getValue().getCurrentGrade());
    }

    @Test
    void applyTutorShouldReturnErrorWhenUserInfoMissing() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        Tutors request = new Tutors();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");
        when(tutorsService.selectTutorsByUid(USER_ID)).thenReturn(null);
        when(userInfoService.updateRealnameInfo(USER_ID, "张三", "110105199001011234")).thenReturn(0);

        AjaxResult result = controller.applyTutor(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("用户不存在", result.get(AjaxResult.MSG_TAG));
        verify(tutorsService, never()).insertTutors(any(Tutors.class));
    }

    @Test
    void updateMyTutorShouldResetStatusToPendingReview() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        Tutors existing = new Tutors();
        existing.setId(1001L);
        existing.setUid(USER_ID);
        Tutors request = new Tutors();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");
        request.setSchool("南京大学");
        request.setIdentity(1L);
        when(tutorsService.selectTutorsByUid(USER_ID)).thenReturn(existing);
        when(userInfoService.updateRealnameInfo(USER_ID, "张三", "110105199001011234")).thenReturn(1);
        when(tutorsService.updateTutors(any(Tutors.class))).thenReturn(1);

        AjaxResult result = controller.updateMyTutor(request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals("资料已更新，请等待审核", result.get(AjaxResult.MSG_TAG));
        ArgumentCaptor<Tutors> captor = ArgumentCaptor.forClass(Tutors.class);
        verify(tutorsService).updateTutors(captor.capture());
        Tutors saved = captor.getValue();
        assertEquals(1001L, saved.getId());
        assertEquals(USER_ID, saved.getUid());
        assertEquals(0L, saved.getStatus());
        assertNull(saved.getRealName());
        assertNull(saved.getIdCard());
        assertEquals("南京大学", saved.getSchool());
    }

    @Test
    void updateMyTutorShouldRejectUniversityTutorWithoutCurrentGrade() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        Tutors existing = new Tutors();
        existing.setId(1001L);
        existing.setUid(USER_ID);
        Tutors request = new Tutors();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");
        request.setIdentity(0L);

        AjaxResult result = controller.updateMyTutor(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("请选择当前年级", result.get(AjaxResult.MSG_TAG));
        verify(tutorsService, never()).updateTutors(any(Tutors.class));
    }

    @Test
    void addParentsShouldReturnErrorWhenBabyIdMissing() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserType(0);
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);

        AjaxResult result = controller.addParents(new Parents());

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("请选择服务萌娃", result.get(AjaxResult.MSG_TAG));
        verify(parentsService, never()).insertParents(any(Parents.class));
    }

    @Test
    void addParentsShouldReturnErrorWhenBabyDoesNotBelongToCurrentUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserType(0);
        Parents request = new Parents();
        request.setBabyId(99L);
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);
        when(babyInfoService.selectBabyInfoByIdAndUserId(99L, WECHAT_USER_ID)).thenReturn(null);

        AjaxResult result = controller.addParents(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("萌娃信息不存在或无权使用", result.get(AjaxResult.MSG_TAG));
        verify(parentsService, never()).insertParents(any(Parents.class));
    }

    @Test
    void addParentsShouldReturn200ForWechatUserIdString() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUserType(0);
        BabyInfo babyInfo = new BabyInfo();
        babyInfo.setId(7L);
        Parents request = new Parents();
        request.setBabyId(7L);
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);
        when(babyInfoService.selectBabyInfoByIdAndUserId(7L, WECHAT_USER_ID)).thenReturn(babyInfo);
        when(parentsService.insertParents(any(Parents.class))).thenReturn(0);

        AjaxResult result = controller.addParents(request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        ArgumentCaptor<Parents> captor = ArgumentCaptor.forClass(Parents.class);
        verify(parentsService).insertParents(captor.capture());
        assertEquals(WECHAT_USER_ID, captor.getValue().getWechatUid());
        assertEquals(7L, captor.getValue().getBabyId());
    }

    @Test
    void myParentsShouldReturn200WhenWechatUserIdIsString() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        WxMiniUserContext.setCurrentUserId(WECHAT_USER_ID);
        when(userInfoService.selectUserInfoByUserId(WECHAT_USER_ID)).thenReturn(userInfo);
        when(parentsService.selectParentsByWechatUid(WECHAT_USER_ID)).thenReturn(Collections.singletonList(new Parents()));

        AjaxResult result = controller.myParents();

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        verify(parentsService).selectParentsByWechatUid(WECHAT_USER_ID);
    }
}
