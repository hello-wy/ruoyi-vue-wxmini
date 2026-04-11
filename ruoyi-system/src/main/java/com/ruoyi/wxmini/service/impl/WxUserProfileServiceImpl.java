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

@Service
public class WxUserProfileServiceImpl implements IWxUserProfileService {

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
