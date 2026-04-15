package com.ruoyi.wxmini.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.wxmini.bo.WxSalonPayCreateOrderBo;
import com.ruoyi.wxmini.service.IWxSalonPayService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import com.ruoyi.wxmini.vo.WxSalonPayOrderDetailVo;
import com.ruoyi.wxmini.vo.WxPayParamVo;
import org.junit.jupiter.api.AfterEach;
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
    @Mock
    private IWxSalonPayService wxSalonPayService;

    @InjectMocks
    private WxPayController controller;

    @AfterEach
    void clearContext() {
        WxMiniUserContext.clear();
    }

    @Test
    void querySalonOrderShouldReturn200WhenServiceReturnsData() {
        WxMiniUserContext.setCurrentUserId("user-1");
        WxSalonPayOrderDetailVo detailVo = new WxSalonPayOrderDetailVo();
        detailVo.setOrderNo("order-1");
        when(wxSalonPayService.querySalonOrder("user-1", "order-1")).thenReturn(detailVo);
        AjaxResult result = controller.querySalonOrder("order-1");
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void createSalonOrderShouldReturn200WhenServiceReturnsData() throws Exception {
        WxMiniUserContext.setCurrentUserId("user-1");
        when(wxSalonPayService.createSalonOrder(anyString(), any())).thenReturn(new WxPayParamVo());
        AjaxResult result = controller.createSalonOrder(new WxSalonPayCreateOrderBo());
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
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
