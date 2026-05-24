package com.ruoyi.wxmini.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WxPayTransferConfigValidatorTest {

    @Test
    void shouldRejectPublicKeyPathWithoutPublicKeyId() {
        WxPayProperties properties = validProperties();
        properties.setPublicKeyPath("classpath:cert/pub_key.pem");

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> new WxPayTransferConfigValidator(properties).validate());

        assertTrue(error.getMessage().contains("wx.pay.publicKeyId"));
    }

    private WxPayProperties validProperties() {
        WxPayProperties properties = new WxPayProperties();
        properties.setAppId("wx-test");
        properties.setMchId("mch-test");
        properties.setPrivateKeyPath("classpath:cert/apiclient_key.pem");
        properties.setCertSerialNo("serial-test");
        properties.setApiV3Key("api-v3-key-test");
        properties.getTransfer().setNotifyUrl("https://example.com/notify");
        properties.getTransfer().setSceneId("1005");
        return properties;
    }
}
