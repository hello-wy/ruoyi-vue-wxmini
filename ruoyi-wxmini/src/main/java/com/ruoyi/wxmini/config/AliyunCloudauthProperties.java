package com.ruoyi.wxmini.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "aliyun.cloudauth")
public class AliyunCloudauthProperties {

    private String accessKeyId;

    private String accessKeySecret;

    private String regionId = "cn-hangzhou";

    private String endpoint = "cloudauth.aliyuncs.com";
}
