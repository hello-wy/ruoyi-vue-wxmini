package com.ruoyi.system.util;

import com.ruoyi.system.enums.WithdrawFailType;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信提现失败原因映射工具
 * 将微信 failReason 字符串和 WxPayException errCode 映射到 WithdrawFailType 枚举
 */
public class WithdrawFailReasonMapper {

    /** 微信转账明细查询返回的 failReason → WithdrawFailType */
    private static final Map<String, WithdrawFailType> FAIL_REASON_MAP = new HashMap<>();

    /** WxPayException 的 errCode → WithdrawFailType */
    private static final Map<String, WithdrawFailType> ERR_CODE_MAP = new HashMap<>();

    static
    {
        // ========== failReason 映射（微信商家转账明细查询返回） ==========
        FAIL_REASON_MAP.put("REALNAME_CHECK_FAIL", WithdrawFailType.RECIPIENT_NOT_REALNAME);
        FAIL_REASON_MAP.put("NAME_NOT_CORRECT", WithdrawFailType.RECIPIENT_NOT_REALNAME);
        FAIL_REASON_MAP.put("OPENID_INVALID", WithdrawFailType.OPENID_MISSING);
        FAIL_REASON_MAP.put("AMOUNT_LIMIT", WithdrawFailType.AMOUNT_OUT_OF_LIMIT);
        FAIL_REASON_MAP.put("TRANSFER_AMOUNT_LIMIT", WithdrawFailType.AMOUNT_OUT_OF_LIMIT);
        FAIL_REASON_MAP.put("DAY_RECEIVED_COUNT_LIMIT", WithdrawFailType.AMOUNT_OUT_OF_LIMIT);
        FAIL_REASON_MAP.put("FREQ_LIMIT", WithdrawFailType.AMOUNT_OUT_OF_LIMIT);
        FAIL_REASON_MAP.put("MERCHANT_BALANCE_NOT_ENOUGH", WithdrawFailType.MERCHANT_BALANCE_INSUFFICIENT);
        FAIL_REASON_MAP.put("NOT_ENOUGH", WithdrawFailType.MERCHANT_BALANCE_INSUFFICIENT);
        FAIL_REASON_MAP.put("ACCOUNT_NOT_EXIST", WithdrawFailType.OPENID_MISSING);
        FAIL_REASON_MAP.put("TRANSFER_SCENE_INVALID", WithdrawFailType.MERCHANT_PERMISSION_NOT_GRANTED);
        FAIL_REASON_MAP.put("PRODUCT_AUTH_CHECK_FAIL", WithdrawFailType.MERCHANT_PERMISSION_NOT_GRANTED);
        FAIL_REASON_MAP.put("EXCEED_PAYEE_LIMIT", WithdrawFailType.AMOUNT_OUT_OF_LIMIT);

        // ========== errCode 映射（WxPayException 抛出） ==========
        ERR_CODE_MAP.put("NO_AUTH", WithdrawFailType.MERCHANT_PERMISSION_NOT_GRANTED);
        ERR_CODE_MAP.put("APPID_MCHID_NOT_MATCH", WithdrawFailType.APPID_MCHID_NOT_BOUND);
        ERR_CODE_MAP.put("MCH_NOT_EXISTS", WithdrawFailType.MERCHANT_PERMISSION_NOT_GRANTED);
        ERR_CODE_MAP.put("SIGN_ERROR", WithdrawFailType.CERT_OR_KEY_INVALID);
        ERR_CODE_MAP.put("CERT_ERROR", WithdrawFailType.CERT_OR_KEY_INVALID);
        ERR_CODE_MAP.put("INVALID_REQUEST", WithdrawFailType.UNKNOWN);
        ERR_CODE_MAP.put("PARAM_ERROR", WithdrawFailType.UNKNOWN);
        ERR_CODE_MAP.put("SYSTEM_ERROR", WithdrawFailType.UNKNOWN);
        ERR_CODE_MAP.put("SYSTEMERROR", WithdrawFailType.UNKNOWN);
        ERR_CODE_MAP.put("FREQUENCY_LIMITED", WithdrawFailType.AMOUNT_OUT_OF_LIMIT);
        ERR_CODE_MAP.put("NOT_ENOUGH", WithdrawFailType.MERCHANT_BALANCE_INSUFFICIENT);
        ERR_CODE_MAP.put("AMOUNT_LIMIT", WithdrawFailType.AMOUNT_OUT_OF_LIMIT);
        ERR_CODE_MAP.put("OPENID_ERROR", WithdrawFailType.OPENID_MISSING);
        ERR_CODE_MAP.put("NAME_MISMATCH", WithdrawFailType.RECIPIENT_NOT_REALNAME);
    }

    private WithdrawFailReasonMapper()
    {
    }

    /**
     * 根据微信转账明细查询返回的 failReason 映射到 WithdrawFailType
     *
     * @param failReason 微信返回的失败原因字符串
     * @return 对应的 WithdrawFailType，未识别时返回 UNKNOWN
     */
    public static WithdrawFailType fromFailReason(String failReason)
    {
        if (failReason == null || failReason.isEmpty())
        {
            return WithdrawFailType.UNKNOWN;
        }
        WithdrawFailType type = FAIL_REASON_MAP.get(failReason.trim().toUpperCase());
        return type != null ? type : WithdrawFailType.UNKNOWN;
    }

    /**
     * 根据 WxPayException 的 errCode 映射到 WithdrawFailType
     *
     * @param errCode WxPayException 中的错误码
     * @return 对应的 WithdrawFailType，未识别时返回 UNKNOWN
     */
    public static WithdrawFailType fromErrCode(String errCode)
    {
        if (errCode == null || errCode.isEmpty())
        {
            return WithdrawFailType.UNKNOWN;
        }
        WithdrawFailType type = ERR_CODE_MAP.get(errCode.trim().toUpperCase());
        return type != null ? type : WithdrawFailType.UNKNOWN;
    }

    /**
     * 综合映射：优先使用 errCode，其次使用 failReason
     * 适用于 catch 块中同时拿到 errCode 和 failReason 的场景
     *
     * @param errCode    WxPayException 中的错误码（可为 null）
     * @param failReason 微信返回的失败原因字符串（可为 null）
     * @return 对应的 WithdrawFailType，未识别时返回 UNKNOWN
     */
    public static WithdrawFailType resolve(String errCode, String failReason)
    {
        if (errCode != null && !errCode.isEmpty())
        {
            WithdrawFailType type = fromErrCode(errCode);
            if (type != WithdrawFailType.UNKNOWN)
            {
                return type;
            }
        }
        if (failReason != null && !failReason.isEmpty())
        {
            return fromFailReason(failReason);
        }
        return WithdrawFailType.UNKNOWN;
    }
}
