package com.ruoyi.wxmini.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.MerchantUserTypeWhitelist;
import com.ruoyi.system.service.IMerchantUserTypeWhitelistService;
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
import java.util.List;

@Service
public class WxUserProfileServiceImpl implements IWxUserProfileService {

    private static final Integer USER_TYPE_PARENT = 0;
    private static final Integer USER_TYPE_STUDENT = 1;
    private static final Integer USER_TYPE_MERCHANT = 2;
    private static final Integer USER_TYPE_AUNT = 3;
    private static final Integer MERCHANT_AUDIT_PENDING = 0;
    private static final Integer MERCHANT_AUDIT_APPROVED = 1;
    private static final Integer REALNAME_AUTHED = 1;
    private static final String MERCHANT_USER_TYPE_BLOCKED_MESSAGE = "当前账号暂未开通商家身份";

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private WxUserProfileMapper wxUserProfileMapper;

    @Resource
    private IMerchantUserTypeWhitelistService merchantUserTypeWhitelistService;

    @Override
    public WxUserProfileVo getCurrentUserProfile(String userId) {
        WxUserProfileVo profileVo = wxUserProfileMapper.selectProfileDetailByUserId(userId);
        if (profileVo == null) {
            return null;
        }
        if (StringUtils.isBlank(profileVo.getDisplayName())) {
            profileVo.setDisplayName(maskPhone(profileVo.getPhone()));
        }
        profileVo.setUserTypeLabel(resolveUserTypeLabel(profileVo.getUserType()));
        profileVo.setPrimaryAction(resolvePrimaryAction(profileVo.getUserType()));
        profileVo.setCanSwitchUserType(true);
        profileVo.setSwitchableUserTypes(resolveSwitchableUserTypes(userId));
        return profileVo;
    }

    @Override
    public int updateCurrentUserProfile(String userId, WxUserProfileUpdateBo bo) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null) {
            return 0;
        }

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

        if (!isRealnameAuthed(userInfo)) {
            profile.setRealName(bo.getRealName());
        }
        profile.setNickName(bo.getNickName());
        profile.setGender(bo.getGender());
        profile.setAge(bo.getAge());
        profile.setCompanyName(bo.getCompanyName());
        profile.setCompanyAddress(bo.getCompanyAddress());
        profile.setCompanyPosition(bo.getCompanyPosition());
        profile.setIndustry(bo.getIndustry());
        profile.setWorkYears(bo.getWorkYears());
        profile.setPersonalIntro(bo.getPersonalIntro());
        profile.setAvailableTime(bo.getAvailableTime());
        profile.setWorkExperience(bo.getWorkExperience());
        profile.setUpdateTime(DateUtils.getNowDate());

        if (isNew) {
            return wxUserProfileMapper.insert(profile);
        }
        return wxUserProfileMapper.updateById(profile);
    }

    @Override
    public int initCurrentUserType(String userId, Integer userType) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null || userInfo.getUserType() != null) {
            return 0;
        }
        if (!isFrontendAllowedUserType(userType)) {
            return 0;
        }
        validateMerchantUserTypeEligibility(userType, userInfo);
        userInfo.setUserType(userType);
        return userInfoService.updateUserInfo(userInfo);
    }

    @Override
    public int switchCurrentUserType(String userId, Integer userType) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null || !isFrontendAllowedUserType(userType)) {
            return 0;
        }
        validateMerchantUserTypeEligibility(userType, userInfo);
        userInfo.setUserType(userType);
        return userInfoService.updateUserInfo(userInfo);
    }

    @Override
    public int submitMerchantApplication(String userId, String businessLicenseUrl) {
        String licenseUrl = StringUtils.trimToEmpty(businessLicenseUrl);
        if (StringUtils.isBlank(licenseUrl)) {
            throw new ServiceException("请先上传营业执照");
        }
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (!hasRealnameInfo(userInfo)) {
            throw new ServiceException("请先完成实名认证后再提交商家申请");
        }
        MerchantUserTypeWhitelist existing = merchantUserTypeWhitelistService
                .selectMerchantUserTypeWhitelistByIdCard(userInfo.getIdCard());
        MerchantUserTypeWhitelist application = buildMerchantApplication(userId, userInfo, licenseUrl);
        if (existing == null) {
            application.setId(SnowflakeIdWorker.nextIdDefault());
            application.setCreateTime(DateUtils.getNowDate());
            return merchantUserTypeWhitelistService.insertMerchantUserTypeWhitelist(application);
        }
        if (MERCHANT_AUDIT_APPROVED.equals(existing.getStatus())) {
            throw new ServiceException("商家身份已审核通过，无需重复提交");
        }
        return merchantUserTypeWhitelistService.updateMerchantUserTypeWhitelistApplication(application);
    }

    private List<Integer> resolveSwitchableUserTypes(String userId) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (canUseMerchantUserType(userInfo)) {
            return Arrays.asList(USER_TYPE_PARENT, USER_TYPE_STUDENT, USER_TYPE_MERCHANT, USER_TYPE_AUNT);
        }
        return Arrays.asList(USER_TYPE_PARENT, USER_TYPE_STUDENT, USER_TYPE_AUNT);
    }

    private void validateMerchantUserTypeEligibility(Integer userType, UserInfo userInfo) {
        if (!USER_TYPE_MERCHANT.equals(userType)) {
            return;
        }
        if (!canUseMerchantUserType(userInfo)) {
            throw new ServiceException(MERCHANT_USER_TYPE_BLOCKED_MESSAGE);
        }
    }

    private boolean canUseMerchantUserType(UserInfo userInfo) {
        if (!hasRealnameInfo(userInfo)) {
            return false;
        }
        MerchantUserTypeWhitelist whitelist = merchantUserTypeWhitelistService
                .selectEnabledMerchantUserTypeWhitelistByIdCard(userInfo.getIdCard());
        return whitelist != null && StringUtils.equals(whitelist.getRealName(), userInfo.getRealName());
    }

    private MerchantUserTypeWhitelist buildMerchantApplication(
            String userId,
            UserInfo userInfo,
            String businessLicenseUrl) {
        MerchantUserTypeWhitelist application = new MerchantUserTypeWhitelist();
        application.setRealName(StringUtils.trimToEmpty(userInfo.getRealName()));
        application.setIdCard(StringUtils.upperCase(StringUtils.trimToEmpty(userInfo.getIdCard())));
        application.setApplyUserId(userId);
        application.setBusinessLicenseUrl(businessLicenseUrl);
        application.setStatus(MERCHANT_AUDIT_PENDING);
        application.setRemark("");
        application.setCreateBy(userId);
        application.setUpdateBy(userId);
        application.setUpdateTime(DateUtils.getNowDate());
        return application;
    }

    private boolean hasRealnameInfo(UserInfo userInfo) {
        return userInfo != null
                && StringUtils.isNotBlank(userInfo.getIdCard())
                && StringUtils.isNotBlank(userInfo.getRealName());
    }

    private boolean isFrontendAllowedUserType(Integer userType) {
        return USER_TYPE_PARENT.equals(userType)
                || USER_TYPE_STUDENT.equals(userType)
                || USER_TYPE_MERCHANT.equals(userType)
                || USER_TYPE_AUNT.equals(userType);
    }

    private String resolveUserTypeLabel(Integer userType) {
        if (USER_TYPE_PARENT.equals(userType)) {
            return "家长";
        }
        if (USER_TYPE_STUDENT.equals(userType)) {
            return "学生";
        }
        if (USER_TYPE_MERCHANT.equals(userType)) {
            return "商家";
        }
        if (USER_TYPE_AUNT.equals(userType)) {
            return "阿姨";
        }
        return "未知";
    }

    private String resolvePrimaryAction(Integer userType) {
        if (USER_TYPE_PARENT.equals(userType)) {
            return "/pages/tutoring/parent/apply";
        }
        if (USER_TYPE_STUDENT.equals(userType)) {
            return "/pages/tutoring/tutor/apply";
        }
        if (USER_TYPE_MERCHANT.equals(userType)) {
            return "/pages/jobs/apply";
        }
        if (USER_TYPE_AUNT.equals(userType)) {
            return "/pages/mine/info/index";
        }
        return "/pages/guide/index";
    }

    private boolean isRealnameAuthed(UserInfo userInfo) {
        return REALNAME_AUTHED.equals(userInfo.getIsRealnameAuth());
    }

    private String maskPhone(String phone) {
        if (StringUtils.length(phone) != 11) {
            return "";
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}
