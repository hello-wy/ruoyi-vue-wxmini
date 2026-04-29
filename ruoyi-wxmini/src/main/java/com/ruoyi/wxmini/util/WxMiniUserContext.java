package com.ruoyi.wxmini.util;

/**
 * 微信小程序用户上下文处理器
 *
 * @author weijiayu
 * @date 2025/4/22 23:57
 */
public class WxMiniUserContext {

    /**
     * request attribute key（用于兜底传递 currentUserId）
     */
    public static final String REQUEST_ATTR_CURRENT_USER_ID = "wxmini.currentUserId";

    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();

    /**
     * 设置当前用户ID
     */
    public static void setCurrentUserId(String userId) {
        currentUser.set(userId);
    }

    /**
     * 获取当前用户ID
     */
    public static String getCurrentUserId() {
        return currentUser.get();
    }

    /**
     * 获取当前用户ID（推荐：controller 层可传入 request 兜底读取，避免 ThreadLocal 在极端情况下取不到）
     */
    public static String getCurrentUserId(javax.servlet.http.HttpServletRequest request) {
        String userId = getCurrentUserId();
        if (userId != null) {
            return userId;
        }
        if (request == null) {
            return null;
        }
        Object attr = request.getAttribute(REQUEST_ATTR_CURRENT_USER_ID);
        return attr == null ? null : attr.toString();
    }

    /**
     * 清除当前用户 ID
     */
    public static void clear() {
        currentUser.remove();
    }
}
