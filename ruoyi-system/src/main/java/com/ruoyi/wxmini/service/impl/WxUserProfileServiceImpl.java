package com.ruoyi.wxmini.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.wxmini.bo.WxUserProfileUpdateBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.domain.WxUserProfile;
import com.ruoyi.wxmini.domain.vo.WxUserProfileVo;
import com.ruoyi.wxmini.mapper.WxUserProfileMapper;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IWxUserProfileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;

@Service
public class WxUserProfileServiceImpl implements IWxUserProfileService {

    private static final String USER_TYPE_PARENT = "0";
    private static final String USER_TYPE_STUDENT = "1";
    private static final String USER_TYPE_MERCHANT = "2";

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private WxUserProfileMapper wxUserProfileMapper;

    @Override
    public WxUserProfileVo getCurrentUserProfile(String userId) {
        WxUserProfileVo profileVo = wxUserProfileMapper.selectProfileDetailByUserId(userId);
        if (profileVo == null) {
            return null;
        }
        if (StringUtils.isBlank(profileVo.getDisplayName())) {
            profileVo.setDisplayName(profileVo.getUserName());
        }
        profileVo.setUserTypeLabel(resolveUserTypeLabel(profileVo.getUserType()));
        profileVo.setPrimaryAction(resolvePrimaryAction(profileVo.getUserType()));
        profileVo.setCanSwitchUserType(!USER_TYPE_MERCHANT.equals(profileVo.getUserType()));
        profileVo.setSwitchableUserTypes(USER_TYPE_MERCHANT.equals(profileVo.getUserType())
                ? Collections.singletonList(USER_TYPE_MERCHANT)
                : Arrays.asList(USER_TYPE_PARENT, USER_TYPE_STUDENT));
        return profileVo;
    }

    @Override
    public int updateCurrentUserProfile(String userId, WxUserProfileUpdateBo bo) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null) {
            return 0;
        }

        userInfo.setUserName(resolveName(bo));
        userInfo.setPhone(bo.getPhone());
        userInfoService.updateUserInfo(userInfo);

        LambdaQueryWrapper<WxUserProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WxUserProfile::getUserInfoId, userInfo.getId());
        WxUserProfile profile = wxUserProfileMapper.selectOne(wrapper);
        boolean isNew = profile == null;
        if (profile == null) {
            profile = new WxUserProfile();
            profile.setUserInfoId(userInfo.getId());
            profile.setCreateTime(DateUtils.getNowDate());
        }

        profile.setRealName(bo.getRealName());
        profile.setNickName(bo.getNickName());
        profile.setGender(bo.getGender());
        profile.setCompanyName(bo.getCompanyName());
        profile.setCompanyAddress(bo.getCompanyAddress());
        profile.setCompanyPosition(bo.getCompanyPosition());
        profile.setIndustry(bo.getIndustry());
        profile.setWorkYears(bo.getWorkYears());
        profile.setPersonalIntro(bo.getPersonalIntro());
        profile.setUpdateTime(DateUtils.getNowDate());

        if (isNew) {
            return wxUserProfileMapper.insert(profile);
        }
        return wxUserProfileMapper.updateById(profile);
    }

    @Override
    public int initCurrentUserType(String userId, String userType) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null || StringUtils.isNotBlank(userInfo.getUserType())) {
            return 0;
        }
        if (!isFrontendAllowedUserType(userType)) {
            return 0;
        }
        userInfo.setUserType(userType);
        return userInfoService.updateUserInfo(userInfo);
    }

    @Override
    public int switchCurrentUserType(String userId, String userType) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null || !isFrontendAllowedUserType(userType)) {
            return 0;
        }
        userInfo.setUserType(userType);
        return userInfoService.updateUserInfo(userInfo);
    }

    private boolean isFrontendAllowedUserType(String userType) {
        return USER_TYPE_PARENT.equals(userType) || USER_TYPE_STUDENT.equals(userType);
    }

    private String resolveUserTypeLabel(String userType) {
        if (USER_TYPE_PARENT.equals(userType)) {
            return "家长";
        }
        if (USER_TYPE_STUDENT.equals(userType)) {
            return "学生";
        }
        if (USER_TYPE_MERCHANT.equals(userType)) {
            return "商家";
        }
        return "未选择";
    }

    private String resolvePrimaryAction(String userType) {
        if (USER_TYPE_PARENT.equals(userType)) {
            return "/pages/tutoring/parent/apply";
        }
        if (USER_TYPE_STUDENT.equals(userType)) {
            return "/pages/tutoring/tutor/apply";
        }
        if (USER_TYPE_MERCHANT.equals(userType)) {
            return "/pages/jobs/apply";
        }
        return "/pages/guide/index";
    }

    private String resolveName(WxUserProfileUpdateBo bo) {
        if (StringUtils.isNotBlank(bo.getRealName())) {
            return bo.getRealName().trim();
        }
        if (StringUtils.isNotBlank(bo.getNickName())) {
            return bo.getNickName().trim();
        }
        return bo.getUserName();
    }
}
