package com.ruoyi.framework.config;

import com.ruoyi.common.utils.ServletUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 服务相关配置
 *
 * @author ruoyi
 */
@Component
public class ServerConfig
{
    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     *
     * @return 服务地址
     */
    public String getUrl()
    {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request)
    {
        String scheme = firstHeader(request, "X-Forwarded-Proto");
        String host = firstHeader(request, "X-Forwarded-Host");
        String port = firstHeader(request, "X-Forwarded-Port");
        String contextPath = request.getServletContext().getContextPath();

        if (isBlank(scheme))
        {
            scheme = request.getScheme();
        }

        if (isBlank(host))
        {
            host = request.getServerName();
        }

        if (isBlank(port))
        {
            port = String.valueOf(request.getServerPort());
        }

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(host);
        if (shouldAppendPort(scheme, port))
        {
            url.append(":").append(port);
        }
        url.append(contextPath);
        return url.toString();
    }

    private static String firstHeader(HttpServletRequest request, String name)
    {
        String value = request.getHeader(name);
        if (value == null)
        {
            return null;
        }
        int commaIndex = value.indexOf(',');
        return (commaIndex >= 0 ? value.substring(0, commaIndex) : value).trim();
    }

    private static boolean shouldAppendPort(String scheme, String port)
    {
        if (isBlank(port))
        {
            return false;
        }
        if ("http".equalsIgnoreCase(scheme) && "80".equals(port))
        {
            return false;
        }
        if ("https".equalsIgnoreCase(scheme) && "443".equals(port))
        {
            return false;
        }
        return true;
    }

    private static boolean isBlank(String value)
    {
        return value == null || value.trim().isEmpty();
    }
}
