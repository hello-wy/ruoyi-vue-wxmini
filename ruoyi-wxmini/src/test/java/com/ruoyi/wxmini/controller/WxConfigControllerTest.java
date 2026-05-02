package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ISysConfigService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxConfigControllerTest {

    @Mock
    private ISysConfigService configService;

    @InjectMocks
    private WxConfigController controller;

    @Test
    void parttimeGroupQrcodeShouldReturn200WhenConfigured() {
        when(configService.selectConfigByKey("wxmini.parttime.group.qrcodeUrl")).thenReturn("https://cdn.example.com/qrcode.png");
        when(configService.selectConfigByKey("wxmini.parttime.group.description")).thenReturn("扫码进群");

        AjaxResult result = controller.parttimeGroupQrcode();

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void parttimeGroupQrcodeShouldReturn500WhenNotConfigured() {
        when(configService.selectConfigByKey("wxmini.parttime.group.qrcodeUrl")).thenReturn("");

        AjaxResult result = controller.parttimeGroupQrcode();

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("兼职群二维码暂未配置", result.get(AjaxResult.MSG_TAG));
    }
}
