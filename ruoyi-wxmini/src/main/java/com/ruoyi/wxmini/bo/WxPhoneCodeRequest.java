package com.ruoyi.wxmini.bo;

import lombok.Data;

/**
 * 微信手机号实时验证请求
 */
@Data
public class WxPhoneCodeRequest {

    private String appid;

    private String phoneCode;
}
