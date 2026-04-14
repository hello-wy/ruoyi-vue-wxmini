package com.ruoyi.wxmini.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxPortalControllerTest {

    @Mock
    private WxMaService wxMaService;
    @Mock
    private WxMaMessageRouter wxMaMessageRouter;
    @Mock
    private WxMaConfig wxMaConfig;

    @InjectMocks
    private WxPortalController controller;

    @Test
    void authGetShouldThrowWhenRequestParamMissing() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> controller.authGet("app-1", null, "1", "2", "echo"));

        assertEquals("请求参数非法，请核实!", exception.getMessage());
    }

    @Test
    void authGetShouldReturnEchoWhenSignatureValid() {
        when(wxMaService.switchover("app-1")).thenReturn(true);
        when(wxMaService.checkSignature("1", "2", "sig")).thenReturn(true);

        String result = controller.authGet("app-1", "sig", "1", "2", "echo");

        assertEquals("echo", result);
    }

    @Test
    void postShouldThrowWhenEncryptTypeUnknown() {
        when(wxMaService.switchover("app-1")).thenReturn(true);
        when(wxMaService.getWxMaConfig()).thenReturn(wxMaConfig);
        when(wxMaConfig.getMsgDataFormat()).thenReturn(WxMaConstants.MsgDataFormat.JSON);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> controller.post("app-1", "{}", null, "unknown", null, "1", "2"));

        assertEquals("不可识别的加密类型：unknown", exception.getMessage());
    }
}
