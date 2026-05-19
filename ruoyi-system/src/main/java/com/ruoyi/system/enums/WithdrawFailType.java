package com.ruoyi.system.enums;

/**
 * 提现失败类型枚举
 * 每个枚举值对应一个面向用户的中文提示文案
 */
public enum WithdrawFailType {

    MERCHANT_PERMISSION_NOT_GRANTED("商户号未开通商家转账功能，请联系客服"),
    APPID_MCHID_NOT_BOUND("小程序与商户号未绑定，请联系客服"),
    CERT_OR_KEY_INVALID("支付证书配置异常，请联系客服"),
    USER_NOT_REALNAME("请先完成实名认证后再提现"),
    OPENID_MISSING("未获取到微信账户信息，请重新登录后重试"),
    AMOUNT_OUT_OF_LIMIT("提现金额超出限制"),
    BALANCE_INSUFFICIENT("可用余额不足"),
    PENDING_WITHDRAW_EXISTS("您有一笔提现正在处理中，请等待完成后再试"),
    RECIPIENT_NOT_REALNAME("收款人需在微信内完成实名认证后才能收款"),
    MERCHANT_BALANCE_INSUFFICIENT("商户余额不足，请联系客服"),
    RECONCILE_TIMEOUT("提现超时未完成，请联系客服"),
    UNKNOWN("提现失败，请稍后重试");

    private final String userMessage;

    WithdrawFailType(String userMessage)
    {
        this.userMessage = userMessage;
    }

    public String getUserMessage()
    {
        return userMessage;
    }
}
