package com.ruoyi.wxmini.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 微信支付配置
 *
 * @author weijiayu
 */
@Configuration
@EnableConfigurationProperties({WxPayProperties.class, AliyunCloudauthProperties.class})
public class WxPayConfiguration {

    private final WxPayProperties properties;

    @Autowired
    public WxPayConfiguration(WxPayProperties properties) {
        this.properties = properties;
    }

    @Bean
    @Primary
    public WxPayService wxPayService() {
        WxPayConfig config = new WxPayConfig();
        config.setAppId(StringUtils.trimToNull(properties.getAppId()));
        config.setMchId(StringUtils.trimToNull(properties.getMchId()));
        config.setMchKey(StringUtils.trimToNull(properties.getMchKey()));
        config.setSubAppId(StringUtils.trimToNull(properties.getSubAppId()));
        config.setSubMchId(StringUtils.trimToNull(properties.getSubMchId()));
        config.setKeyPath(StringUtils.trimToNull(properties.getKeyPath()));
        config.setUseSandboxEnv(properties.isUseSandboxEnv());
        config.setServiceId(StringUtils.trimToNull(properties.getServiceId()));
        config.setPayScoreNotifyUrl(StringUtils.trimToNull(properties.getPayScoreNotifyUrl()));
        config.setPrivateKeyPath(StringUtils.trimToNull(properties.getPrivateKeyPath()));
        config.setPrivateCertPath(StringUtils.trimToNull(properties.getPrivateCertPath()));
        config.setCertSerialNo(StringUtils.trimToNull(properties.getCertSerialNo()));
        config.setApiV3Key(StringUtils.trimToNull(properties.getApiV3Key()));
        config.setPublicKeyPath(StringUtils.trimToNull(properties.getPublicKeyPath()));
        config.setPublicKeyId(StringUtils.trimToNull(properties.getPublicKeyId()));

        WxPayService service = new WxPayServiceImpl();
        service.setConfig(config);
        return service;
    }
}
