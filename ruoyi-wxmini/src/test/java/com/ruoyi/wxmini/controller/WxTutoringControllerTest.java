package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.service.IParentsService;
import com.ruoyi.system.service.ITutorsService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxTutoringControllerTest {

    private static final String USER_ID = "123";

    @Mock
    private ITutorsService tutorsService;
    @Mock
    private IParentsService parentsService;
    @Mock
    private IUserInfoService userInfoService;

    @InjectMocks
    private WxTutoringController controller;

    @AfterEach
    void clearContext() {
        WxMiniUserContext.clear();
    }

    @Test
    void myTutorShouldReturn200WhenTutorMissing() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(tutorsService.selectTutorsByUid(123L)).thenReturn(null);
        AjaxResult result = controller.myTutor();
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void myTutorShouldReturn200WhenTutorFound() {
        Tutors tutor = new Tutors();
        tutor.setUid(123L);
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(tutorsService.selectTutorsByUid(123L)).thenReturn(tutor);
        AjaxResult result = controller.myTutor();
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void addParentsShouldReturn200AfterCall() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(parentsService.insertParents(any(Parents.class))).thenReturn(0);
        AjaxResult result = controller.addParents(new Parents());
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void myParentsShouldReturn200WhenUserExists() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        Parents parent = new Parents();
        parent.setWechatUid(123L);
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(parentsService.selectParentsByWechatUid(123L)).thenReturn(Collections.singletonList(parent));
        AjaxResult result = controller.myParents();
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }
}
