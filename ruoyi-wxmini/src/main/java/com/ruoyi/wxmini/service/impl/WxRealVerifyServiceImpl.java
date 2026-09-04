package com.ruoyi.wxmini.service.impl;

import com.aliyun.cloudauth20190307.Client;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyRequest;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyResponse;
import com.aliyun.cloudauth20190307.models.Id2MetaVerifyResponseBody;
import com.aliyun.credentials.models.CredentialModel;
import com.aliyun.credentials.provider.StaticCredentialsProvider;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.wxmini.bo.WxRealVerifyRequestBo;
import com.ruoyi.wxmini.config.AliyunCloudauthProperties;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IWxRealVerifyService;
import com.ruoyi.wxmini.vo.WxRealVerifyResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class WxRealVerifyServiceImpl implements IWxRealVerifyService {

    private static final String MATCHED_BIZ_CODE = "1";
    private static final String NOT_MATCHED_BIZ_CODE = "2";
    private static final String SUCCESS_CODE = "200";
    private static final String BAD_REQUEST_CODE = "400";
    private static final String UNAUTHORIZED_CODE = "401";
    private static final String PAYMENT_REQUIRED_CODE = "402";
    private static final String FORBIDDEN_CODE = "403";
    private static final String TOO_MANY_REQUESTS_CODE = "429";
    private static final String NOT_MATCHED_REASON = "姓名或身份证信息不匹配，请重新填写";
    private static final String CONFIGURATION_MESSAGE = "实名认证服务未配置，请联系管理员";
    private static final String REMOTE_REQUEST_MESSAGE = "实名认证请求参数无效，请检查姓名和身份证号";
    private static final String REMOTE_AUTHORIZATION_MESSAGE = "实名认证服务授权失败，请联系管理员";
    private static final String REMOTE_BALANCE_MESSAGE = "实名认证服务余额不足，请联系管理员";
    private static final String REMOTE_FREQUENCY_MESSAGE = "实名认证请求过于频繁，请稍后再试";
    private static final String REMOTE_TIMEOUT_MESSAGE = "实名认证服务响应超时，请稍后重试";
    private static final String REMOTE_UNAVAILABLE_MESSAGE = "实名认证服务暂不可用，请稍后重试";
    private static final String REMOTE_RESPONSE_MESSAGE = "实名认证服务返回异常，请稍后重试";
    private static final String REMOTE_RESULT_MESSAGE = "实名认证结果状态异常，请稍后重试";
    private static final String REMOTE_UNKNOWN_MESSAGE = "实名认证服务调用出现未预期异常，请联系管理员";
    private static final String ID_CARD_ALREADY_AUTHENTICATED_MESSAGE = "该身份证号已被其他账号实名认证";
    private static final String PERSISTENCE_CONSTRAINT_MESSAGE = "实名认证信息不符合保存要求，请检查后重试";
    private static final String PERSISTENCE_BUSY_MESSAGE = "实名认证信息保存繁忙，请稍后重试";
    private static final String PERSISTENCE_UNAVAILABLE_MESSAGE = "实名认证信息保存服务暂不可用，请稍后重试";
    private static final String PERSISTENCE_DATABASE_MESSAGE = "实名认证信息保存发生数据库异常，请稍后重试";
    private static final String PERSISTENCE_UNKNOWN_MESSAGE = "实名认证信息保存出现未预期异常，请联系管理员";
    private static final int MAX_REAL_NAME_LENGTH = 50;
    private static final int ID_CARD_DATE_BEGIN_INDEX = 6;
    private static final int ID_CARD_DATE_END_INDEX = 14;
    private static final int ID_CARD_CHECK_CODE_INDEX = 17;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_PAYMENT_REQUIRED = 402;
    private static final int HTTP_FORBIDDEN = 403;
    private static final int HTTP_TOO_MANY_REQUESTS = 429;
    private static final int HTTP_SERVER_ERROR = 500;
    private static final int[] ID_CARD_WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final char[] ID_CARD_CHECK_CODES = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^\\d{17}[\\dXx]$");
    private static final DateTimeFormatter ID_CARD_DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuuMMdd")
            .withResolverStyle(ResolverStyle.STRICT);

    private final AliyunCloudauthProperties aliyunCloudauthProperties;
    private final IUserInfoService userInfoService;

    @Override
    public WxRealVerifyResultVo verify(String userId, WxRealVerifyRequestBo request) {
        validateRequest(userId, request);
        String realName = StringUtils.trimToEmpty(request.getRealName());
        String idCard = StringUtils.upperCase(StringUtils.trimToEmpty(request.getIdCard()));
        WxRealVerifyResultVo result = verifyRemote(realName, idCard);
        if (Boolean.TRUE.equals(result.getMatched())) {
            persistVerifiedUser(userId, realName, idCard);
        }
        return result;
    }

    protected Client buildClient() throws Exception {
        String accessKeyId = StringUtils.trimToNull(aliyunCloudauthProperties.getAccessKeyId());
        String accessKeySecret = StringUtils.trimToNull(aliyunCloudauthProperties.getAccessKeySecret());
        if (accessKeyId == null || accessKeySecret == null) {
            throw new ServiceException(CONFIGURATION_MESSAGE);
        }
        CredentialModel credential = CredentialModel.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                .build();
        Config config = new Config()
                .setRegionId(StringUtils.defaultIfBlank(aliyunCloudauthProperties.getRegionId(), "cn-hangzhou"))
                .setEndpoint(StringUtils.defaultIfBlank(aliyunCloudauthProperties.getEndpoint(), "cloudauth.aliyuncs.com"))
                .setCredential(new com.aliyun.credentials.Client(StaticCredentialsProvider.builder()
                        .credential(credential)
                        .build()));
        return new Client(config);
    }

    protected WxRealVerifyResultVo toResult(Id2MetaVerifyResponseBody body) {
        if (body == null) {
            log.error("阿里云实名认证未返回响应体");
            throw new ServiceException(REMOTE_RESPONSE_MESSAGE);
        }
        if (!SUCCESS_CODE.equals(body.getCode())) {
            log.error("阿里云实名认证返回失败，code={}, message={}, requestId={}",
                    body.getCode(), body.getMessage(), body.getRequestId());
            throw new ServiceException(resolveRemoteResponseMessage(body.getCode()));
        }
        String bizCode = body.getResultObject() == null ? null : body.getResultObject().getBizCode();
        WxRealVerifyResultVo result = new WxRealVerifyResultVo();
        if (MATCHED_BIZ_CODE.equals(bizCode)) {
            result.setMatched(Boolean.TRUE);
            result.setReason("");
            return result;
        }
        if (NOT_MATCHED_BIZ_CODE.equals(bizCode)) {
            result.setMatched(Boolean.FALSE);
            result.setReason(NOT_MATCHED_REASON);
            return result;
        }
        log.error("阿里云实名认证返回未知业务状态，bizCode={}, requestId={}", bizCode, body.getRequestId());
        throw new ServiceException(REMOTE_RESULT_MESSAGE);
    }

    private void validateRequest(String userId, WxRealVerifyRequestBo request) {
        if (StringUtils.isBlank(userId)) {
            throw new ServiceException("请先登录");
        }
        if (request == null) {
            throw new ServiceException("参数错误");
        }
        String realName = StringUtils.trimToEmpty(request.getRealName());
        if (StringUtils.isBlank(realName)) {
            throw new ServiceException("请填写真实姓名");
        }
        if (realName.length() > MAX_REAL_NAME_LENGTH) {
            throw new ServiceException("真实姓名不能超过50个字符");
        }
        validateIdCard(StringUtils.upperCase(StringUtils.trimToEmpty(request.getIdCard())));
    }

    private void validateIdCard(String idCard) {
        if (!ID_CARD_PATTERN.matcher(idCard).matches()) {
            throw new ServiceException("请填写正确的18位身份证号");
        }
        if (!hasValidBirthDate(idCard)) {
            throw new ServiceException("身份证号中的出生日期不正确");
        }
        if (!hasValidCheckCode(idCard)) {
            throw new ServiceException("身份证号校验位不正确");
        }
    }

    private boolean hasValidBirthDate(String idCard) {
        try {
            LocalDate birthDate = LocalDate.parse(
                    idCard.substring(ID_CARD_DATE_BEGIN_INDEX, ID_CARD_DATE_END_INDEX), ID_CARD_DATE_FORMATTER);
            return !birthDate.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean hasValidCheckCode(String idCard) {
        int sum = 0;
        for (int index = 0; index < ID_CARD_CHECK_CODE_INDEX; index++) {
            sum += Character.digit(idCard.charAt(index), 10) * ID_CARD_WEIGHTS[index];
        }
        return idCard.charAt(ID_CARD_CHECK_CODE_INDEX) == ID_CARD_CHECK_CODES[sum % ID_CARD_CHECK_CODES.length];
    }

    private WxRealVerifyResultVo verifyRemote(String realName, String idCard) {
        try {
            Id2MetaVerifyResponse response = buildClient().id2MetaVerify(new Id2MetaVerifyRequest()
                    .setParamType("normal")
                    .setUserName(realName)
                    .setIdentifyNum(idCard));
            return toResult(response == null ? null : response.getBody());
        } catch (ServiceException e) {
            throw e;
        } catch (TeaException e) {
            log.error("调用阿里云实名认证接口失败，code={}, statusCode={}", e.getCode(), e.getStatusCode(), e);
            throw new ServiceException(resolveTeaExceptionMessage(e));
        } catch (SocketTimeoutException e) {
            log.error("调用阿里云实名认证接口超时", e);
            throw new ServiceException(REMOTE_TIMEOUT_MESSAGE);
        } catch (ConnectException | UnknownHostException e) {
            log.error("连接阿里云实名认证服务失败", e);
            throw new ServiceException(REMOTE_UNAVAILABLE_MESSAGE);
        } catch (Exception e) {
            log.error("调用阿里云实名认证接口出现未预期异常", e);
            throw new ServiceException(REMOTE_UNKNOWN_MESSAGE);
        }
    }

    private String resolveRemoteResponseMessage(String code) {
        if (BAD_REQUEST_CODE.equals(code)) {
            return REMOTE_REQUEST_MESSAGE;
        }
        if (UNAUTHORIZED_CODE.equals(code) || FORBIDDEN_CODE.equals(code)) {
            return REMOTE_AUTHORIZATION_MESSAGE;
        }
        if (PAYMENT_REQUIRED_CODE.equals(code)) {
            return REMOTE_BALANCE_MESSAGE;
        }
        if (TOO_MANY_REQUESTS_CODE.equals(code)) {
            return REMOTE_FREQUENCY_MESSAGE;
        }
        if (code != null && code.startsWith(String.valueOf(HTTP_SERVER_ERROR))) {
            return REMOTE_UNAVAILABLE_MESSAGE;
        }
        return REMOTE_RESPONSE_MESSAGE;
    }

    private String resolveTeaExceptionMessage(TeaException exception) {
        Integer statusCode = exception.getStatusCode();
        if (hasAuthorizationFailure(exception.getCode(), statusCode)) {
            return REMOTE_AUTHORIZATION_MESSAGE;
        }
        if (Integer.valueOf(HTTP_BAD_REQUEST).equals(statusCode)) {
            return REMOTE_REQUEST_MESSAGE;
        }
        if (Integer.valueOf(HTTP_PAYMENT_REQUIRED).equals(statusCode)) {
            return REMOTE_BALANCE_MESSAGE;
        }
        if (hasFrequencyFailure(exception.getCode(), statusCode)) {
            return REMOTE_FREQUENCY_MESSAGE;
        }
        if (hasTimeoutFailure(exception.getCode())) {
            return REMOTE_TIMEOUT_MESSAGE;
        }
        if (statusCode != null && statusCode >= HTTP_SERVER_ERROR) {
            return REMOTE_UNAVAILABLE_MESSAGE;
        }
        return REMOTE_RESPONSE_MESSAGE;
    }

    private boolean hasAuthorizationFailure(String code, Integer statusCode) {
        return Integer.valueOf(HTTP_UNAUTHORIZED).equals(statusCode)
                || Integer.valueOf(HTTP_FORBIDDEN).equals(statusCode)
                || StringUtils.containsAnyIgnoreCase(code, "AccessDenied", "Unauthorized", "Forbidden",
                "InvalidAccessKey", "SignatureDoesNotMatch");
    }

    private boolean hasFrequencyFailure(String code, Integer statusCode) {
        return Integer.valueOf(HTTP_TOO_MANY_REQUESTS).equals(statusCode)
                || StringUtils.containsAnyIgnoreCase(code, "Throttling", "TooManyRequests", "LimitExceeded");
    }

    private boolean hasTimeoutFailure(String code) {
        return StringUtils.containsIgnoreCase(code, "Timeout");
    }

    private void persistVerifiedUser(String userId, String realName, String idCard) {
        try {
            int updated = userInfoService.updateRealnameInfo(userId, realName, idCard);
            if (updated <= 0) {
                throw new ServiceException("用户不存在");
            }
        } catch (DuplicateKeyException e) {
            log.warn("实名认证身份证号已被其他账号使用，userId={}", userId);
            throw new ServiceException(ID_CARD_ALREADY_AUTHENTICATED_MESSAGE);
        } catch (CannotAcquireLockException e) {
            log.warn("实名认证信息保存锁冲突，userId={}", userId, e);
            throw new ServiceException(PERSISTENCE_BUSY_MESSAGE);
        } catch (DataIntegrityViolationException e) {
            log.error("实名认证信息违反数据库约束，userId={}", userId, e);
            throw new ServiceException(PERSISTENCE_CONSTRAINT_MESSAGE);
        } catch (DataAccessResourceFailureException e) {
            log.error("实名认证信息保存数据库资源不可用，userId={}", userId, e);
            throw new ServiceException(PERSISTENCE_UNAVAILABLE_MESSAGE);
        } catch (DataAccessException e) {
            log.error("实名认证信息保存数据库异常，userId={}", userId, e);
            throw new ServiceException(PERSISTENCE_DATABASE_MESSAGE);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("实名认证信息保存出现未预期异常，userId={}", userId, e);
            throw new ServiceException(PERSISTENCE_UNKNOWN_MESSAGE);
        }
    }
}
