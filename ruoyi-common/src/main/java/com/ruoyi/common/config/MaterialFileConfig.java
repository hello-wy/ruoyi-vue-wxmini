package com.ruoyi.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 资料文件配置
 */
@Component
@ConfigurationProperties(prefix = "material.file")
public class MaterialFileConfig
{
    /** 资料文件存储目录，位于 ruoyi.profile 下 */
    private String storageDir = "files";

    /** 公开下载前缀，默认走 SpringBoot 签名下载接口 */
    private String publicPrefix = "/wxmini/growup/materials/files";

    /** 下载签名密钥 */
    private String signSecret = "zhiyujia-material-file";

    /** 下载签名有效期，单位秒 */
    private Long signExpireSeconds = 600L;

    public String getStorageDir()
    {
        return normalizeSegment(storageDir, "files");
    }

    public void setStorageDir(String storageDir)
    {
        this.storageDir = storageDir;
    }

    public String getPublicPrefix()
    {
        if (publicPrefix == null || publicPrefix.trim().isEmpty())
        {
            return "/wxmini/growup/materials/files";
        }
        String prefix = publicPrefix.trim();
        if (!prefix.startsWith("/"))
        {
            prefix = "/" + prefix;
        }
        while (prefix.endsWith("/") && prefix.length() > 1)
        {
            prefix = prefix.substring(0, prefix.length() - 1);
        }
        return prefix;
    }

    public void setPublicPrefix(String publicPrefix)
    {
        this.publicPrefix = publicPrefix;
    }

    public String getSignSecret()
    {
        return signSecret;
    }

    public void setSignSecret(String signSecret)
    {
        this.signSecret = signSecret;
    }

    public Long getSignExpireSeconds()
    {
        return signExpireSeconds;
    }

    public void setSignExpireSeconds(Long signExpireSeconds)
    {
        this.signExpireSeconds = signExpireSeconds;
    }

    private String normalizeSegment(String value, String defaultValue)
    {
        if (value == null || value.trim().isEmpty())
        {
            return defaultValue;
        }
        String segment = value.trim().replace('\\', '/');
        while (segment.startsWith("/"))
        {
            segment = segment.substring(1);
        }
        while (segment.endsWith("/") && segment.length() > 1)
        {
            segment = segment.substring(0, segment.length() - 1);
        }
        if (segment.contains("..") || segment.contains("/"))
        {
            return defaultValue;
        }
        return segment;
    }
}
