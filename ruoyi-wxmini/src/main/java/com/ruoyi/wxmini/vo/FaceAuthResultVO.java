package com.ruoyi.wxmini.vo;

import lombok.Data;

/**
 * 实人认证结果查询响应
 *
 * @author ruoyi
 */
@Data
public class FaceAuthResultVO {
    /** 认证流程唯一ID */
    private String certifyId;
    /** 认证是否通过：true=通过，false=未通过 */
    private Boolean passed;
    /** 认证状态子码（如 FACE_RECOGNITION_SUCCESS） */
    private String subCode;
    /** 认证材料信息（JSON格式，包含人脸图片地址等） */
    private String materialInfo;
}
