package com.ruoyi.wxmini.service.impl;

import com.aliyun.cloudauth20190307.Client;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyRequest;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyResponse;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyResponseBody;
import com.aliyun.credentials.models.CredentialModel;
import com.aliyun.credentials.provider.DefaultCredentialsProvider;
import com.aliyun.credentials.provider.StaticCredentialsProvider;
import com.aliyun.teaopenapi.models.Config;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.wxmini.bo.WxRealVerifyRequestBo;
import com.ruoyi.wxmini.config.AliyunCloudauthProperties;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IWxRealVerifyService;
import com.ruoyi.wxmini.vo.WxRealVerifyResultVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WxRealVerifyServiceImpl implements IWxRealVerifyService {

    private static final String MATCHED_BIZ_CODE = "1";
    private static final String NOT_MATCHED_BIZ_CODE = "2";
    private static final String SUCCESS_CODE = "200";
    private static final String NOT_MATCHED_REASON = "姓名或身份证信息不匹配，请重新填写";
    private static final String FAILED_MESSAGE = "实名认证失败，请稍后重试";

    private final AliyunCloudauthProperties aliyunCloudauthProperties;
    private final IUserInfoService userInfoService;

    @Override
    public WxRealVerifyResultVo verify(String userId, WxRealVerifyRequestBo request) {
        if (StringUtils.isBlank(userId)) {
            throw new ServiceException("请先登录");
        }
        if (request == null) {
            throw new ServiceException("参数错误");
        }
        String realName = StringUtils.trimToEmpty(request.getRealName());
        String idCard = StringUtils.trimToEmpty(request.getIdCard());
        if (StringUtils.isBlank(realName)) {
            throw new ServiceException("请填写真实姓名");
        }
        if (!idCard.matches("^\\d{17}[\\dXx]$")) {
            throw new ServiceException("请填写正确的18位身份证号");
        }
        try {
            Id2MetaVerifyResponse response = buildClient().id2MetaVerify(new Id2MetaVerifyRequest()
                    .setParamType("normal")
                    .setUserName(realName)
                    .setIdentifyNum(idCard));
            WxRealVerifyResultVo result = toResult(response == null ? null : response.getBody());
            if (Boolean.TRUE.equals(result.getMatched())) {
                int updated = userInfoService.updateRealnameInfo(userId, realName, idCard);
                if (updated <= 0) {
                    throw new ServiceException("用户不存在");
                }
            }
            return result;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(FAILED_MESSAGE);
        }
    }

    protected Client buildClient() throws Exception {
        Config config = new Config()
                .setRegionId(StringUtils.defaultIfBlank(aliyunCloudauthProperties.getRegionId(), "cn-hangzhou"))
                .setEndpoint(StringUtils.defaultIfBlank(aliyunCloudauthProperties.getEndpoint(), "cloudauth.aliyuncs.com"));
        String accessKeyId = StringUtils.trimToNull(aliyunCloudauthProperties.getAccessKeyId());
        String accessKeySecret = StringUtils.trimToNull(aliyunCloudauthProperties.getAccessKeySecret());
        if (accessKeyId != null && accessKeySecret != null) {
            CredentialModel credential = CredentialModel.builder()
                    .accessKeyId(accessKeyId)
                    .accessKeySecret(accessKeySecret)
                    .build();
            config.setCredential(new com.aliyun.credentials.Client(StaticCredentialsProvider.builder()
                    .credential(credential)
                    .build()));
        } else {
            config.setCredential(new com.aliyun.credentials.Client(DefaultCredentialsProvider.builder().build()));
        }
        return new Client(config);
    }

    protected WxRealVerifyResultVo toResult(Id2MetaVerifyResponseBody body) {
        if (body == null || !SUCCESS_CODE.equals(body.getCode())) {
            throw new ServiceException(FAILED_MESSAGE);
        }
        String bizCode = body.getResultObject() == null ? null : body.getResultObject().getBizCode();
        WxRealVerifyResultVo result = new WxRealVerifyResultVo();
        if (MATCHED_BIZ_CODE.equals(bizCode)) {
            result.setMatched(Boolean.TRUE);
            result.setReason("");
            return result;
        }
        result.setMatched(Boolean.FALSE);
        result.setReason(NOT_MATCHED_BIZ_CODE.equals(bizCode) ? NOT_MATCHED_REASON : FAILED_MESSAGE);
        return result;
    }
}
