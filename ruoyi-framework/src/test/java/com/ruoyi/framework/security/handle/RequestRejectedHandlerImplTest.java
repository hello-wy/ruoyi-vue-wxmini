package com.ruoyi.framework.security.handle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.firewall.RequestRejectedException;

class RequestRejectedHandlerImplTest
{
    private final RequestRejectedHandlerImpl handler = new RequestRejectedHandlerImpl();

    @Test
    void returnsBadRequestWithoutExceptionDetails() throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/%2e%2e/admin");
        MockHttpServletResponse response = new MockHttpServletResponse();
        String exceptionMessage = "The request was rejected because the URL contained a potentially malicious String \"%2e\"";

        handler.handle(request, response, new RequestRejectedException(exceptionMessage));

        assertEquals(400, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());
        String body = response.getContentAsString();
        assertEquals(400, JSON.parseObject(body).getIntValue("code"));
        assertEquals("请求地址不合法", JSON.parseObject(body).getString("msg"));
        assertFalse(body.contains(exceptionMessage));
    }
}
