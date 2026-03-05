package com.ruoyi.wxmini.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.face-auth")
public class AliyunFaceAuthProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private Long sceneId;
    private String endpoint = "cloudauth.aliyuncs.com";
    private String returnUrl;
}
