package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.vo.PersonalityTestQuestionVo;
import com.ruoyi.system.domain.vo.PersonalityTestResultVo;
import com.ruoyi.system.service.IPersonalityTestService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxPersonalityTestControllerTest {
    private static final String WX_USER_ID = "wx-user-1";
    private static final Long USER_INFO_ID = 21L;

    @Mock
    private IPersonalityTestService personalityTestService;

    @Mock
    private IUserInfoService userInfoService;

    @InjectMocks
    private WxPersonalityTestController controller;

    @AfterEach
    void clearContext() {
        WxMiniUserContext.clear();
    }

    @Test
    void questionShouldLoadOwnedAttemptQuestionByNo() {
        WxMiniUserContext.setCurrentUserId(WX_USER_ID);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(USER_INFO_ID);
        userInfo.setUserId(WX_USER_ID);
        PersonalityTestQuestionVo question = new PersonalityTestQuestionVo();
        question.setQuestionNo(37);
        when(userInfoService.selectUserInfoByUserId(WX_USER_ID)).thenReturn(userInfo);
        when(personalityTestService.getQuestion(12L, USER_INFO_ID, 37)).thenReturn(question);

        AjaxResult result = controller.question(12L, 37);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals(question, result.get(AjaxResult.DATA_TAG));
        verify(personalityTestService).getQuestion(12L, USER_INFO_ID, 37);
    }

    @Test
    void resultShouldForwardAttemptResultPayload() {
        WxMiniUserContext.setCurrentUserId(WX_USER_ID);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(USER_INFO_ID);
        userInfo.setUserId(WX_USER_ID);
        PersonalityTestResultVo resultVo = new PersonalityTestResultVo();
        resultVo.setAttemptId(12L);
        when(userInfoService.selectUserInfoByUserId(WX_USER_ID)).thenReturn(userInfo);
        when(personalityTestService.getResult(12L, USER_INFO_ID)).thenReturn(resultVo);

        AjaxResult result = controller.result(12L);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals(resultVo, result.get(AjaxResult.DATA_TAG));
        verify(personalityTestService).getResult(12L, USER_INFO_ID);
    }
}
