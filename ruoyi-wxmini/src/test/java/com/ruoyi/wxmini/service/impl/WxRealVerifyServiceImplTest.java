package com.ruoyi.wxmini.service.impl;

import com.aliyun.cloudauth20190307.Client;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyResponse;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyResponseBody;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.wxmini.bo.WxRealVerifyRequestBo;
import com.ruoyi.wxmini.config.AliyunCloudauthProperties;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.vo.WxRealVerifyResultVo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WxRealVerifyServiceImplTest {

    private final AliyunCloudauthProperties properties = new AliyunCloudauthProperties();

    @Test
    void verifyShouldReturnMatchedWhenBizCodeIsOne() throws Exception {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        when(userInfoService.updateRealnameInfo("wx-user", "吴扬", "320123200106174211")).thenReturn(1);
        WxRealVerifyServiceImpl service = createService(userInfoService, buildResponse("200", "1"));
        WxRealVerifyRequestBo request = new WxRealVerifyRequestBo();
        request.setRealName("吴扬");
        request.setIdCard("320123200106174211");

        WxRealVerifyResultVo result = service.verify("wx-user", request);

        assertTrue(result.getMatched());
        assertEquals("", result.getReason());
        verify(userInfoService).updateRealnameInfo("wx-user", "吴扬", "320123200106174211");
    }

    @Test
    void verifyShouldReturnNotMatchedWhenBizCodeIsTwo() throws Exception {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        WxRealVerifyServiceImpl service = createService(userInfoService, buildResponse("200", "2"));
        WxRealVerifyRequestBo request = new WxRealVerifyRequestBo();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");

        WxRealVerifyResultVo result = service.verify("wx-user", request);

        assertFalse(result.getMatched());
        assertEquals("姓名或身份证信息不匹配，请重新填写", result.getReason());
        verify(userInfoService, never()).updateRealnameInfo(any(), any(), any());
    }

    @Test
    void verifyShouldThrowWhenRemoteCallFails() throws Exception {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        WxRealVerifyServiceImpl service = createService(userInfoService, buildResponse("500", null));
        WxRealVerifyRequestBo request = new WxRealVerifyRequestBo();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");

        ServiceException error = assertThrows(ServiceException.class, () -> service.verify("wx-user", request));

        assertEquals("实名认证失败，请稍后重试", error.getMessage());
    }

    @Test
    void verifyShouldThrowWhenUserInfoMissingAfterMatched() throws Exception {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        when(userInfoService.updateRealnameInfo("wx-user", "吴扬", "320123200106174211")).thenReturn(0);
        WxRealVerifyServiceImpl service = createService(userInfoService, buildResponse("200", "1"));
        WxRealVerifyRequestBo request = new WxRealVerifyRequestBo();
        request.setRealName("吴扬");
        request.setIdCard("320123200106174211");

        ServiceException error = assertThrows(ServiceException.class, () -> service.verify("wx-user", request));

        assertEquals("用户不存在", error.getMessage());
        verify(userInfoService).updateRealnameInfo("wx-user", "吴扬", "320123200106174211");
    }

    @Test
    void verifyShouldThrowConfigurationMessageWhenCredentialsMissing() {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        WxRealVerifyServiceImpl service = new WxRealVerifyServiceImpl(properties, userInfoService);

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify("wx-user", request("张三", "110105199001011234")));

        assertEquals("实名认证服务未配置，请联系管理员", error.getMessage());
    }

    @Test
    void verifyShouldThrowGenericMessageWhenRemoteClientThrows() throws Exception {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        Client client = mock(Client.class);
        when(client.id2MetaVerify(any())).thenThrow(new RuntimeException("remote unavailable"));
        WxRealVerifyServiceImpl service = spyService(userInfoService, client);

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify("wx-user", request("张三", "110105199001011234")));

        assertEquals("实名认证失败，请稍后重试", error.getMessage());
    }

    @Test
    void verifyShouldThrowPersistenceMessageWhenSavingVerifiedUserFails() throws Exception {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        when(userInfoService.updateRealnameInfo("wx-user", "张三", "11010520050110010X"))
                .thenThrow(new RuntimeException("unknown column"));
        WxRealVerifyServiceImpl service = createService(userInfoService, buildResponse("200", "1"));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.verify("wx-user", request("张三", "11010520050110010x")));

        assertEquals("实名认证信息保存失败，请稍后重试", error.getMessage());
    }

    @Test
    void verifyShouldThrowWhenRealNameBlank() {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        WxRealVerifyServiceImpl service = new WxRealVerifyServiceImpl(properties, userInfoService);
        WxRealVerifyRequestBo request = new WxRealVerifyRequestBo();
        request.setRealName(" ");
        request.setIdCard("110105199001011234");

        ServiceException error = assertThrows(ServiceException.class, () -> service.verify("wx-user", request));

        assertEquals("请填写真实姓名", error.getMessage());
    }

    @Test
    void verifyShouldThrowWhenIdCardInvalid() {
        IUserInfoService userInfoService = mock(IUserInfoService.class);
        WxRealVerifyServiceImpl service = new WxRealVerifyServiceImpl(properties, userInfoService);
        WxRealVerifyRequestBo request = new WxRealVerifyRequestBo();
        request.setRealName("张三");
        request.setIdCard("123");

        ServiceException error = assertThrows(ServiceException.class, () -> service.verify("wx-user", request));

        assertEquals("请填写正确的18位身份证号", error.getMessage());
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
}
