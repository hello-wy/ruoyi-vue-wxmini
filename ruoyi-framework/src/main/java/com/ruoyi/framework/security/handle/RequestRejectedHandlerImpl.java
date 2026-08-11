package com.ruoyi.framework.security.handle;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.security.web.firewall.RequestRejectedHandler;
import org.springframework.stereotype.Component;

@Component
public class RequestRejectedHandlerImpl implements RequestRejectedHandler
{
    private static final Logger log = LoggerFactory.getLogger(RequestRejectedHandlerImpl.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, RequestRejectedException exception)
            throws IOException
    {
        log.warn("SPRING_SECURITY_REQUEST_REJECTED method={}, remoteAddr={}", request.getMethod(), request.getRemoteAddr());
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(JSON.toJSONString(AjaxResult.error(HttpStatus.BAD_REQUEST, "请求地址不合法")));
    }
}
