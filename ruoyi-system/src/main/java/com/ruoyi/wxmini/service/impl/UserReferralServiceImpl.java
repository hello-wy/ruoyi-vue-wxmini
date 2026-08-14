package com.ruoyi.wxmini.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.CourseDistributionCommissionConfig;
import com.ruoyi.system.service.ICourseDistributionCommissionService;
import com.ruoyi.system.service.IWalletService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.domain.UserReferral;
import com.ruoyi.wxmini.domain.vo.UserReferralVo;
import com.ruoyi.wxmini.mapper.UserInfoMapper;
import com.ruoyi.wxmini.mapper.UserReferralMapper;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IUserReferralService;
import com.ruoyi.wxmini.service.IUserReferralService.ReferralBindResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户邀请关系Service业务层处理
 *
 * @author ruoyi
 * @date 2026-07-01
 */
@Service
public class UserReferralServiceImpl implements IUserReferralService {

    private static final Logger log = LoggerFactory.getLogger(UserReferralServiceImpl.class);

    @Autowired
    private UserReferralMapper userReferralMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ICourseDistributionCommissionService commissionService;

    @Autowired
    private IWalletService walletService;

    @Override
    public int bindReferral(String inviteCode, String inviteeUserId) {
        return bindReferralWithResult(inviteCode, inviteeUserId) == ReferralBindResult.SUCCESS ? 1 : 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReferralBindResult bindReferralWithResult(String inviteCode, String inviteeUserId) {
        if (StringUtils.isBlank(inviteCode) || StringUtils.isBlank(inviteeUserId)) {
            return ReferralBindResult.EMPTY_INVITE_CODE;
        }

        String normalizedInviteCode = inviteCode.trim();
        UserInfo inviter = userInfoService.selectUserInfoByInviteCode(normalizedInviteCode);
        if (inviter == null) {
            log.warn("邀请码 {} 无效，绑定邀请关系失败", normalizedInviteCode);
            return ReferralBindResult.INVALID_INVITE_CODE;
        }
        if (inviter.getUserId().equals(inviteeUserId)) {
            log.warn("用户不能绑定自己为邀请人: {}", inviteeUserId);
            return ReferralBindResult.SELF_INVITE;
        }

        String firstUserId = inviter.getUserId().compareTo(inviteeUserId) < 0 ? inviter.getUserId() : inviteeUserId;
        String secondUserId = inviter.getUserId().compareTo(inviteeUserId) < 0 ? inviteeUserId : inviter.getUserId();
        userInfoMapper.selectUserInfosForUpdate(firstUserId, secondUserId);

        UserReferral existing = userReferralMapper.selectReferralByInviteeUserId(inviteeUserId);
        if (existing != null) {
            log.info("用户 {} 已经绑定过邀请人，跳过绑定", inviteeUserId);
            return ReferralBindResult.ALREADY_BOUND;
        }
        if (formsReferralCycle(inviter.getUserId(), inviteeUserId)) {
            log.warn("邀请关系会形成环: 邀请人={}，被邀请人={}", inviter.getUserId(), inviteeUserId);
            return ReferralBindResult.REFERRAL_CYCLE;
        }

        UserReferral userReferral = new UserReferral();
        userReferral.setInviterUserId(inviter.getUserId());
        userReferral.setInviteeUserId(inviteeUserId);
        userReferral.setInviteCode(normalizedInviteCode);
        userReferral.setRewardStatus("PENDING");
        userReferral.setCreateTime(new Date());

        try {
            int rows = userReferralMapper.insertUserReferral(userReferral);
            if (rows > 0) {
                log.info("成功绑定邀请关系: 邀请人={}，被邀请人={}，邀请码={}", inviter.getUserId(), inviteeUserId, normalizedInviteCode);
                return ReferralBindResult.SUCCESS;
            }
        } catch (DuplicateKeyException e) {
            log.info("用户 {} 并发绑定邀请关系时已存在绑定记录", inviteeUserId, e);
            return ReferralBindResult.ALREADY_BOUND;
        }

        log.warn("绑定邀请关系失败: 邀请人={}，被邀请人={}，邀请码={}", inviter.getUserId(), inviteeUserId, normalizedInviteCode);
        return ReferralBindResult.FAILED;
    }

    private boolean formsReferralCycle(String inviterUserId, String inviteeUserId) {
        Set<String> visitedUserIds = new HashSet<>();
        String currentUserId = inviterUserId;
        while (StringUtils.isNotBlank(currentUserId) && visitedUserIds.add(currentUserId)) {
            if (inviteeUserId.equals(currentUserId)) {
                return true;
            }
            UserReferral referral = userReferralMapper.selectReferralByInviteeUserId(currentUserId);
            if (referral == null) {
                return false;
            }
            currentUserId = referral.getInviterUserId();
        }
        return StringUtils.isNotBlank(currentUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rewardReferral(Long referralId, Long operatorUserId) {
        UserReferral referral = userReferralMapper.selectReferralByIdForUpdate(referralId);
        if (referral == null) {
            throw new ServiceException("邀请关系不存在");
        }
        if (!isPendingReward(referral)) {
            throw new ServiceException("邀请奖金已审核");
        }
        CourseDistributionCommissionConfig config = commissionService.selectConfig();
        BigDecimal amount = config == null || config.getInviteRewardAmount() == null
                ? BigDecimal.ZERO : config.getInviteRewardAmount();
        referral.setRewardAmount(amount);
        referral.setRewardTime(DateUtils.getNowDate());
        referral.setRewardOperatorUserId(operatorUserId);
        if (userReferralMapper.rewardReferral(referral) != 1) {
            throw new ServiceException("邀请奖金审核失败");
        }
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            walletService.creditReferralReward(walletService.resolveCurrentUserUid(referral.getInviterUserId()), amount,
                    "REFERRAL:" + referral.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeReferral(Long referralId) {
        UserReferral referral = userReferralMapper.selectReferralByIdForUpdate(referralId);
        if (referral == null) {
            throw new ServiceException("邀请关系不存在");
        }
        if (!isPendingReward(referral)) {
            throw new ServiceException("已发放奖金的邀请关系不可删除");
        }
        if (userReferralMapper.deletePendingReferralById(referralId) != 1) {
            throw new ServiceException("邀请关系删除失败");
        }
    }

    private boolean isPendingReward(UserReferral referral) {
        return StringUtils.isBlank(referral.getRewardStatus()) || "PENDING".equals(referral.getRewardStatus());
    }

    @Override
    public List<UserReferralVo> getMyInvitees(String inviterUserId) {
        return userReferralMapper.selectReferralVoListByInviterUserId(inviterUserId);
    }

    @Override
    public int getMyInviteeCount(String inviterUserId) {
        return userReferralMapper.selectReferralCountByInviterUserId(inviterUserId);
    }

    @Override
    public UserReferral getMyInviter(String inviteeUserId) {
        return userReferralMapper.selectReferralByInviteeUserId(inviteeUserId);
    }

    @Override
    public List<UserReferral> selectUserReferralList(UserReferral userReferral) {
        return userReferralMapper.selectUserReferralList(userReferral);
    }

    @Override
    public List<UserReferralVo> selectUserReferralVoList(UserReferralVo userReferralVo) {
        return userReferralMapper.selectUserReferralVoList(userReferralVo);
    }

    @Override
    public List<UserReferralVo> selectSecondLevelReferralVoList(String inviterUserId) {
        return userReferralMapper.selectSecondLevelReferralVoListByInviterUserId(inviterUserId);
    }
}
