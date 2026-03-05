package com.ruoyi.wxmini.vo;

import lombok.Data;

/**
 * 实人认证初始化响应
 *
 * @author ruoyi
 */
@Data
public class FaceAuthInitVO {
    /** 认证流程唯一ID，用于后续查询认证结果 */
    private String certifyId;
    /** 认证页面跳转URL（H5模式下跳转此地址完成人脸识别） */
    private String certifyUrl;
}
