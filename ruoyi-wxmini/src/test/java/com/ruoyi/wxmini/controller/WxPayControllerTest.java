package com.ruoyi.wxmini.controller;

import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxPayControllerTest {

    @Mock
    private WxPayService wxPayService;

    @InjectMocks
    private WxPayController controller;

    @Test
    void queryOrderShouldReturn200WhenServiceReturnsNull() throws Exception {
        when(wxPayService.queryOrderV3(any())).thenReturn(null);
        AjaxResult result = controller.queryOrder("order-1");
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void createOrderShouldReturnNon200WhenServiceThrowsException() throws Exception {
        when(wxPayService.getConfig()).thenThrow(new RuntimeException("boom"));
        AjaxResult result = controller.createOrder();
        assertEquals(false, Integer.valueOf(200).equals(result.get(AjaxResult.CODE_TAG)));
    }

    @Test
    void payNotifyShouldReturnFailXmlWhenParsingFails() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getInputStream()).thenReturn(new SimpleServletInputStream("{}"));
        when(wxPayService.parseOrderNotifyV3Result(anyString(), any())).thenThrow(new RuntimeException("boom"));
        String result = controller.payNotify(request, response);
        assertEquals("<xml><return_code><![CDATA[FAIL]]></return_code></xml>", result);
    }

    private static class SimpleServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream inputStream;

        private SimpleServletInputStream(String body) {
            this.inputStream = new ByteArrayInputStream(body.getBytes());
        }

        @Override
        public int read() {
            return inputStream.read();
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(javax.servlet.ReadListener readListener) {
        }
    }
}
