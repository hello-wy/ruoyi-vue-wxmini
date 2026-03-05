package com.ruoyi.wxmini.bo;

import lombok.Data;

/**
 * 实人认证初始化请求参数
 *
 * @author ruoyi
 */
@Data
public class FaceAuthInitBO {
    /** 真实姓名 */
    private String name;
    /** 身份证号码 */
    private String certNo;
    /** 认证完成后前端跳转的回调地址（可选，H5模式使用） */
    private String returnUrl;
    /** 设备元信息（小程序端通过阿里云SDK获取，用于活体检测） */
    private String metaInfo;
}
