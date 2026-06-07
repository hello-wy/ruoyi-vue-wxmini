package com.ruoyi.wxmini.service;

import com.ruoyi.wxmini.bo.WxUserProfileUpdateBo;
import com.ruoyi.wxmini.domain.vo.WxUserProfileVo;

public interface IWxUserProfileService {

    WxUserProfileVo getCurrentUserProfile(String userId);

    int updateCurrentUserProfile(String userId, WxUserProfileUpdateBo bo);

    int initCurrentUserType(String userId, Integer userType);

    int switchCurrentUserType(String userId, Integer userType);

    int submitMerchantApplication(String userId, String businessLicenseUrl);
}
