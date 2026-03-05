package com.ruoyi.wxmini.service;

import com.ruoyi.wxmini.bo.FaceAuthInitBO;
import com.ruoyi.wxmini.vo.FaceAuthInitVO;
import com.ruoyi.wxmini.vo.FaceAuthResultVO;

/**
 * 阿里云实人认证服务接口
 *
 * @author ruoyi
 */
public interface IFaceAuthService {
    /**
     * 初始化实人认证，获取 certifyId 和 certifyUrl
     *
     * @param userId 当前登录小程序用户ID
     * @param bo     认证初始化请求参数（姓名、身份证号等）
     * @return 包含 certifyId 和 certifyUrl 的响应VO
     */
    FaceAuthInitVO initCertify(String userId, FaceAuthInitBO bo);

    /**
     * 查询实人认证结果
     *
     * @param certifyId 认证流程唯一ID
     * @return 认证结果VO
     */
    FaceAuthResultVO queryCertifyResult(String certifyId);
}
