package com.ruoyi.wxmini.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信支付配置
 *
 * @author weijiayu
 */
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {

    private String appId;

    private String mchId;

    private String mchKey;

    private String subAppId;

    private String subMchId;

    private String keyPath;

    private String serviceId;

    private String certSerialNo;

    private String apiV3Key;

    private String payScoreNotifyUrl;

    private String privateKeyPath;

    private String privateCertPath;

    private String publicKeyPath;

    private String publicKeyId;

    private boolean useSandboxEnv;
}
