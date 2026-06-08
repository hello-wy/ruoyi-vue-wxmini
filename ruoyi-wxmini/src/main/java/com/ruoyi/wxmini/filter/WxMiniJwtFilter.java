package com.ruoyi.wxmini.filter;

import com.ruoyi.wxmini.config.WxMiniAnonymousUrlProvider;
import com.ruoyi.wxmini.service.IWxMiniJwtService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 微信小程序后台api接口校验过滤器
 *
 * @author weijiayu
 * @date 2025/4/22 23:48
 */
@Component
public class WxMiniJwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(WxMiniJwtFilter.class);

    @Resource
    private IWxMiniJwtService jwtService;

    @Resource
    private WxMiniAnonymousUrlProvider anonymousUrlProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String path = request.getRequestURI();
            if (!this.checkIsExcludeUri(request.getMethod(), path) && path.startsWith("/wxmini")) {
                String token = request.getHeader("Wx-Authorization");
                if (token == null || !token.startsWith("Bearer ")) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Missing or invalid token");
                    return;
                }

                token = token.substring(7);
                try {
                    if (!jwtService.verifyToken(token)) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid token");
                        return;
                    }
                    String userId = jwtService.parseUserId(token);
                    if (StringUtils.isEmpty(userId)) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("User ID not found in token");
                        return;
                    }
                    WxMiniUserContext.setCurrentUserId(userId);
                    request.setAttribute(WxMiniUserContext.REQUEST_ATTR_CURRENT_USER_ID, userId);
                } catch (Exception e) {
                    String message = e.getMessage();
                    log.error(message, e);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("JWT validation failed: " + message);
                    return;
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            WxMiniUserContext.clear();
        }
    }

    private boolean checkIsExcludeUri(String method, String path) {
        if (path.startsWith("/wxmini/login")
                || path.startsWith("/wxmini/portal")
                || path.startsWith("/wxmini/pay/notify")
                || path.startsWith("/wxmini/pay/salon/notify")
                || path.startsWith("/wxmini/pay/jobs/notify")
                || path.startsWith("/wxmini/pay/courses/notify")
                || path.startsWith("/wxmini/pay/growup/notify")
                || path.startsWith("/wxmini/pay/wallet/notify")) {
            return true;
        }

        return anonymousUrlProvider != null && anonymousUrlProvider.matches(method, path);
    }
}
