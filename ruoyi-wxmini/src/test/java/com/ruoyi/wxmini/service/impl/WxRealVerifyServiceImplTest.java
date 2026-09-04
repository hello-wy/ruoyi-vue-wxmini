package com.ruoyi.wxmini.service.impl;

import com.aliyun.cloudauth20190307.Client;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyResponse;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyResponseBody;
import com.aliyun.tea.TeaException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.wxmini.bo.WxRealVerifyRequestBo;
import com.ruoyi.wxmini.config.AliyunCloudauthProperties;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.vo.WxRealVerifyResultVo;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;

import java.net.SocketTimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WxRealVerifyServiceImplTest {

    private static final String USER_ID = "wx-user";
    private static final String REAL_NAME = "示例用户";
    private static final String VALID_ID_CARD = "11010519491231002X";
    private static final String INVALID_CHECK_CODE_ID_CARD = "110105194912310020";
    private final AliyunCloudauthProperties properties = new AliyunCloudauthProperties();

    @Test
    void verifyShouldReturnMatchedWhenBizCodeIsOne() throws Exception {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        when(userInfoService.updateRealnameInfo(USER_ID, REAL_NAME, VALID_ID_CARD)).thenReturn(1);
        WxRealVerifyServiceImpl service = createService(userInfoService, buildResponse("200", "1"));

        WxRealVerifyResultVo result = service.verify(USER_ID, request(REAL_NAME, VALID_ID_CARD.toLowerCase()));

        assertTrue(result.getMatched());
        assertEquals("", result.getReason());
        verify(userInfoService).updateRealnameInfo(USER_ID, REAL_NAME, VALID_ID_CARD);
    }

    @Test
    void verifyShouldReturnNotMatchedWhenBizCodeIsTwo() throws Exception {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        WxRealVerifyServiceImpl service = createService(userInfoService, buildResponse("200", "2"));

        WxRealVerifyResultVo result = service.verify(USER_ID, request(REAL_NAME, VALID_ID_CARD));

        assertFalse(result.getMatched());
        assertEquals("姓名或身份证信息不匹配，请重新填写", result.getReason());
        verify(userInfoService, never()).updateRealnameInfo(any(), any(), any());
    }

    @Test
    void verifyShouldExplainWhenRemoteResponseIsUnauthorized() throws Exception {
        WxRealVerifyServiceImpl service = createService(mock(IUserInfoService.class), buildResponse("401", null));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify(USER_ID, request(REAL_NAME, VALID_ID_CARD)));

        assertEquals("实名认证服务授权失败，请联系管理员", error.getMessage());
    }

    @Test
    void verifyShouldExplainWhenRemoteResponseIsUnavailable() throws Exception {
        WxRealVerifyServiceImpl service = createService(mock(IUserInfoService.class), buildResponse("500", null));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify(USER_ID, request(REAL_NAME, VALID_ID_CARD)));

        assertEquals("实名认证服务暂不可用，请稍后重试", error.getMessage());
    }

    @Test
    void verifyShouldExplainWhenRemoteRequestIsThrottled() throws Exception {
        Client client = mock(Client.class);
        when(client.id2MetaVerify(any())).thenThrow(teaException("Throttling.User", 429));
        WxRealVerifyServiceImpl service = spyService(mock(IUserInfoService.class), client);

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify(USER_ID, request(REAL_NAME, VALID_ID_CARD)));

        assertEquals("实名认证请求过于频繁，请稍后再试", error.getMessage());
    }

    @Test
    void verifyShouldExplainWhenRemoteRequestTimesOut() throws Exception {
        Client client = mock(Client.class);
        when(client.id2MetaVerify(any())).thenThrow(new SocketTimeoutException("timeout"));
        WxRealVerifyServiceImpl service = spyService(mock(IUserInfoService.class), client);

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify(USER_ID, request(REAL_NAME, VALID_ID_CARD)));

        assertEquals("实名认证服务响应超时，请稍后重试", error.getMessage());
    }

    @Test
    void verifyShouldThrowWhenUserInfoMissingAfterMatched() throws Exception {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        when(userInfoService.updateRealnameInfo(USER_ID, REAL_NAME, VALID_ID_CARD)).thenReturn(0);
        WxRealVerifyServiceImpl service = createService(userInfoService, buildResponse("200", "1"));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify(USER_ID, request(REAL_NAME, VALID_ID_CARD)));

        assertEquals("用户不存在", error.getMessage());
    }

    @Test
    void verifyShouldExplainWhenIdCardAlreadyBelongsToAnotherUser() throws Exception {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        when(userInfoService.updateRealnameInfo(USER_ID, REAL_NAME, VALID_ID_CARD))
                .thenThrow(new DuplicateKeyException("duplicate id_card"));
        WxRealVerifyServiceImpl service = createService(userInfoService, buildResponse("200", "1"));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify(USER_ID, request(REAL_NAME, VALID_ID_CARD)));

        assertEquals("该身份证号已被其他账号实名认证", error.getMessage());
    }

    @Test
    void verifyShouldExposeUnexpectedPersistenceFailure() throws Exception {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        when(userInfoService.updateRealnameInfo(USER_ID, REAL_NAME, VALID_ID_CARD))
                .thenThrow(new RuntimeException("unknown column"));
        WxRealVerifyServiceImpl service = createService(userInfoService, buildResponse("200", "1"));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify(USER_ID, request(REAL_NAME, VALID_ID_CARD)));

        assertEquals("实名认证信息保存出现未预期异常，请联系管理员", error.getMessage());
    }

    @Test
    void verifyShouldThrowConfigurationMessageWhenCredentialsMissing() {
        WxRealVerifyServiceImpl service = new WxRealVerifyServiceImpl(properties, mock(IUserInfoService.class));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify(USER_ID, request(REAL_NAME, VALID_ID_CARD)));

        assertEquals("实名认证服务未配置，请联系管理员", error.getMessage());
    }

    @Test
    void verifyShouldRejectMalformedIdCard() {
        WxRealVerifyServiceImpl service = new WxRealVerifyServiceImpl(properties, mock(IUserInfoService.class));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify(USER_ID, request(REAL_NAME, "123")));

        assertEquals("请填写正确的18位身份证号", error.getMessage());
    }

    @Test
    void verifyShouldRejectIdCardWithInvalidCheckCode() {
        WxRealVerifyServiceImpl service = new WxRealVerifyServiceImpl(properties, mock(IUserInfoService.class));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify(USER_ID, request(REAL_NAME, INVALID_CHECK_CODE_ID_CARD)));

        assertEquals("身份证号校验位不正确", error.getMessage());
    }

    @Test
    void verifyShouldRejectIdCardWithInvalidBirthDate() {
        WxRealVerifyServiceImpl service = new WxRealVerifyServiceImpl(properties, mock(IUserInfoService.class));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify(USER_ID, request(REAL_NAME, "11010519990230002X")));

        assertEquals("身份证号中的出生日期不正确", error.getMessage());
    }

    @Test
    void verifyShouldRejectUnknownRemoteBizCode() throws Exception {
        WxRealVerifyServiceImpl service = createService(mock(IUserInfoService.class), buildResponse("200", "unknown"));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify(USER_ID, request(REAL_NAME, VALID_ID_CARD)));

        assertEquals("实名认证结果状态异常，请稍后重试", error.getMessage());
    }

    private WxRealVerifyServiceImpl createService(IUserInfoService userInfoService, Id2MetaVerifyResponse response) throws Exception {
        Client client = mock(Client.class);
        when(client.id2MetaVerify(any())).thenReturn(response);
        return spyService(userInfoService, client);
    }

    private WxRealVerifyServiceImpl spyService(IUserInfoService userInfoService, Client client) throws Exception {
        WxRealVerifyServiceImpl service = org.mockito.Mockito.spy(new WxRealVerifyServiceImpl(properties, userInfoService));
        doReturn(client).when(service).buildClient();
        return service;
    }

    private WxRealVerifyRequestBo request(String realName, String idCard) {
        WxRealVerifyRequestBo request = new WxRealVerifyRequestBo();
        request.setRealName(realName);
        request.setIdCard(idCard);
        return request;
    }

    private Id2MetaVerifyResponse buildResponse(String code, String bizCode) {
        Id2MetaVerifyResponseBody body = new Id2MetaVerifyResponseBody();
        body.setCode(code);
        if (bizCode != null) {
            Id2MetaVerifyResponseBody.Id2MetaVerifyResponseBodyResultObject resultObject = new Id2MetaVerifyResponseBody.Id2MetaVerifyResponseBodyResultObject();
            resultObject.setBizCode(bizCode);
            body.setResultObject(resultObject);
        }
        Id2MetaVerifyResponse response = new Id2MetaVerifyResponse();
        response.setBody(body);
        return response;
    }

    private TeaException teaException(String code, int statusCode) {
        TeaException exception = new TeaException();
        exception.setCode(code);
        exception.setStatusCode(statusCode);
        return exception;
    }
}
