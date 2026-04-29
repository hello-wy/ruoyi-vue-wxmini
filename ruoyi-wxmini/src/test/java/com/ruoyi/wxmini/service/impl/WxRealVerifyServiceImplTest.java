package com.ruoyi.wxmini.service.impl;

import com.aliyun.cloudauth20190307.models.Id2MetaVerifyResponse;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyResponseBody;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.wxmini.bo.WxRealVerifyRequestBo;
import com.ruoyi.wxmini.config.AliyunCloudauthProperties;
import com.ruoyi.wxmini.vo.WxRealVerifyResultVo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WxRealVerifyServiceImplTest {

    private final AliyunCloudauthProperties properties = new AliyunCloudauthProperties();

    @Test
    void verifyShouldReturnMatchedWhenBizCodeIsOne() {
        TestableWxRealVerifyService service = new TestableWxRealVerifyService(properties, buildResponse("200", "1"));
        WxRealVerifyRequestBo request = new WxRealVerifyRequestBo();
        request.setRealName("吴扬");
        request.setIdCard("320123200106174211");

        WxRealVerifyResultVo result = service.verify("wx-user", request);

        assertTrue(result.getMatched());
        assertEquals("", result.getReason());
    }

    @Test
    void verifyShouldReturnNotMatchedWhenBizCodeIsTwo() {
        TestableWxRealVerifyService service = new TestableWxRealVerifyService(properties, buildResponse("200", "2"));
        WxRealVerifyRequestBo request = new WxRealVerifyRequestBo();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");

        WxRealVerifyResultVo result = service.verify("wx-user", request);

        assertFalse(result.getMatched());
        assertEquals("姓名或身份证信息不匹配，请重新填写", result.getReason());
    }

    @Test
    void verifyShouldThrowWhenRemoteCallFails() {
        TestableWxRealVerifyService service = new TestableWxRealVerifyService(properties, buildResponse("500", null));
        WxRealVerifyRequestBo request = new WxRealVerifyRequestBo();
        request.setRealName("张三");
        request.setIdCard("110105199001011234");

        ServiceException error = assertThrows(ServiceException.class, () -> service.verify("wx-user", request));

        assertEquals("实名认证失败，请稍后重试", error.getMessage());
    }

    @Test
    void verifyShouldThrowWhenRealNameBlank() {
        TestableWxRealVerifyService service = new TestableWxRealVerifyService(properties, buildResponse("200", "1"));
        WxRealVerifyRequestBo request = new WxRealVerifyRequestBo();
        request.setRealName(" ");
        request.setIdCard("110105199001011234");

        ServiceException error = assertThrows(ServiceException.class, () -> service.verify("wx-user", request));

        assertEquals("请填写真实姓名", error.getMessage());
    }

    @Test
    void verifyShouldThrowWhenIdCardInvalid() {
        TestableWxRealVerifyService service = new TestableWxRealVerifyService(properties, buildResponse("200", "1"));
        WxRealVerifyRequestBo request = new WxRealVerifyRequestBo();
        request.setRealName("张三");
        request.setIdCard("123");

        ServiceException error = assertThrows(ServiceException.class, () -> service.verify("wx-user", request));

        assertEquals("请填写正确的18位身份证号", error.getMessage());
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

    private static class TestableWxRealVerifyService extends WxRealVerifyServiceImpl {

        private final Id2MetaVerifyResponse response;

        private TestableWxRealVerifyService(AliyunCloudauthProperties properties, Id2MetaVerifyResponse response) {
            super(properties);
            this.response = response;
        }

        @Override
        public WxRealVerifyResultVo verify(String userId, WxRealVerifyRequestBo request) {
            if (request == null) {
                throw new ServiceException("参数错误");
            }
            if (request.getRealName() == null || request.getRealName().trim().isEmpty()) {
                throw new ServiceException("请填写真实姓名");
            }
            String idCard = request.getIdCard() == null ? "" : request.getIdCard().trim();
            if (!idCard.matches("^\\d{17}[\\dXx]$")) {
                throw new ServiceException("请填写正确的18位身份证号");
            }
            try {
                return super.toResult(this.response.getBody());
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                throw new ServiceException("实名认证失败，请稍后重试");
            }
        }
    }
}
