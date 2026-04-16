package com.ruoyi.wxmini.enums;

public enum JobSignupOrderStatusEnum {
    PENDING(0),
    PAID(1),
    REFUNDING(2),
    REFUNDED(3),
    CANCELED(4);

    private final int code;

    JobSignupOrderStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
