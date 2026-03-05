package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.wxmini.bo.FaceAuthInitBO;
import com.ruoyi.wxmini.service.IFaceAuthService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 实人认证（人脸识别）接口
 * 接入文档：https://help.aliyun.com/zh/id-verification/financial-grade-id-verification/server-side-integration-2
 *
 * 接口需要小程序端携带 Wx-Authorization: Bearer {token} 请求头
 *
 * @author ruoyi
 */
@Slf4j
@RestController
@RequestMapping("/wxmini/faceAuth")
public class WxFaceAuthController {

    @Resource
    private IFaceAuthService faceAuthService;

    /**
     * 初始化实人认证
     * POST /wxmini/faceAuth/init
     *
     * 小程序端先调用阿里云 getNativeParams 获取 metaInfo，再请求本接口。
     * 返回 certifyId 和 certifyUrl：
     *   H5模式：小程序 webview 打开 certifyUrl；
     *   原生SDK模式：使用 certifyId 调用阿里云小程序插件。
     *
     * @param bo 认证初始化请求（姓名、身份证号、metaInfo）
     */
    @PostMapping("/init")
    public AjaxResult initFaceAuth(@RequestBody FaceAuthInitBO bo) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("not login");
        }
        if (StringUtils.isBlank(bo.getName())) {
            return AjaxResult.error("name required");
        }
        if (StringUtils.isBlank(bo.getCertNo())) {
            return AjaxResult.error("certNo required");
        }
        try {
            return AjaxResult.success(faceAuthService.initCertify(userId, bo));
        } catch (Exception e) {
            log.error("[FaceAuth] init failed, userId={}", userId, e);
            return AjaxResult.error("init failed: " + e.getMessage());
        }
    }

    /**
     * 查询实人认证结果
     * GET /wxmini/faceAuth/query?certifyId=xxx
     *
     * 小程序完成人脸识别后（H5回调或小程序SDK回调），前端携带 certifyId 请求本接口。
     *
     * @param certifyId 认证流程唯一ID（由 /init 接口返回）
     */
    @GetMapping("/query")
    public AjaxResult queryFaceAuthResult(@RequestParam String certifyId) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("not login");
        }
        if (StringUtils.isBlank(certifyId)) {
            return AjaxResult.error("certifyId required");
        }
        try {
            return AjaxResult.success(faceAuthService.queryCertifyResult(certifyId));
        } catch (Exception e) {
            log.error("[FaceAuth] query failed, userId={}, certifyId={}", userId, certifyId, e);
            return AjaxResult.error("query failed: " + e.getMessage());
        }
    }
}
