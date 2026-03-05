package com.ruoyi.wxmini.service.impl;

import com.aliyun.cloudauth20190307.Client;
import com.aliyun.cloudauth20190307.models.DescribeFaceVerifyRequest;
import com.aliyun.cloudauth20190307.models.DescribeFaceVerifyResponse;
import com.aliyun.cloudauth20190307.models.DescribeFaceVerifyResponseBody;
import com.aliyun.cloudauth20190307.models.InitFaceVerifyRequest;
import com.aliyun.cloudauth20190307.models.InitFaceVerifyResponse;
import com.aliyun.cloudauth20190307.models.InitFaceVerifyResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.ruoyi.wxmini.bo.FaceAuthInitBO;
import com.ruoyi.wxmini.config.AliyunFaceAuthProperties;
import com.ruoyi.wxmini.service.IFaceAuthService;
import com.ruoyi.wxmini.vo.FaceAuthInitVO;
import com.ruoyi.wxmini.vo.FaceAuthResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Service
public class FaceAuthServiceImpl implements IFaceAuthService {

    @Resource
    private AliyunFaceAuthProperties properties;

    private Client cloudAuthClient;

    @PostConstruct
    public void init() {
        try {
            Config config = new Config()
                    .setAccessKeyId(properties.getAccessKeyId())
                    .setAccessKeySecret(properties.getAccessKeySecret())
                    .setEndpoint(properties.getEndpoint());
            cloudAuthClient = new Client(config);
            log.info("[FaceAuth] client init ok, endpoint={}", properties.getEndpoint());
        } catch (Exception e) {
            log.error("[FaceAuth] client init failed", e);
            throw new RuntimeException("FaceAuth client init failed", e);
        }
    }

    @Override
    public FaceAuthInitVO initCertify(String userId, FaceAuthInitBO bo) {
        InitFaceVerifyRequest request = new InitFaceVerifyRequest()
                .setSceneId(properties.getSceneId())
                .setOuterOrderNo(userId + "_" + System.currentTimeMillis())
                .setProductCode("ID_PRO")
                .setCertName(bo.getName())
                .setCertNo(bo.getCertNo())
                .setCertType("IDENTITY_CARD")
                .setReturnUrl(StringUtils.isNotBlank(bo.getReturnUrl()) ? bo.getReturnUrl() : properties.getReturnUrl())
                .setMetaInfo(bo.getMetaInfo())
                .setUserId(userId);
        try {
            InitFaceVerifyResponse response = cloudAuthClient.initFaceVerify(request);
            InitFaceVerifyResponseBody body = response.getBody();
            log.info("[FaceAuth] init RequestId={} Code={} Message={}", body.getRequestId(), body.getCode(), body.getMessage());
            if (!"200".equals(body.getCode())) {
                throw new RuntimeException("initCertify failed: " + body.getMessage());
            }
            InitFaceVerifyResponseBody.InitFaceVerifyResponseBodyResultObject result = body.getResultObject();
            FaceAuthInitVO vo = new FaceAuthInitVO();
            vo.setCertifyId(result.getCertifyId());
            vo.setCertifyUrl(result.getCertifyUrl());
            return vo;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("[FaceAuth] initCertify error userId={}", userId, e);
            throw new RuntimeException("initCertify error: " + e.getMessage(), e);
        }
    }

    @Override
    public FaceAuthResultVO queryCertifyResult(String certifyId) {
        DescribeFaceVerifyRequest request = new DescribeFaceVerifyRequest()
                .setSceneId(properties.getSceneId())
                .setCertifyId(certifyId);
        try {
            DescribeFaceVerifyResponse response = cloudAuthClient.describeFaceVerify(request);
            DescribeFaceVerifyResponseBody body = response.getBody();
            log.info("[FaceAuth] query RequestId={} Code={} Message={}", body.getRequestId(), body.getCode(), body.getMessage());
            if (!"200".equals(body.getCode())) {
                throw new RuntimeException("queryCertifyResult failed: " + body.getMessage());
            }
            DescribeFaceVerifyResponseBody.DescribeFaceVerifyResponseBodyResultObject result = body.getResultObject();
            FaceAuthResultVO vo = new FaceAuthResultVO();
            vo.setCertifyId(certifyId);
            vo.setPassed("T".equals(result.getPassed()));
            vo.setSubCode(result.getSubCode());
            vo.setMaterialInfo(result.getMaterialInfo());
            return vo;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("[FaceAuth] queryCertifyResult error certifyId={}", certifyId, e);
            throw new RuntimeException("queryCertifyResult error: " + e.getMessage(), e);
        }
    }
}
