package com.ruoyi.wxmini.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.domain.UserReferral;
import com.ruoyi.wxmini.domain.vo.UserReferralVo;
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

import java.util.Date;
import java.util.List;

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
    private IUserInfoService userInfoService;

    @Override
    public int bindReferral(String inviteCode, String inviteeUserId) {
        return bindReferralWithResult(inviteCode, inviteeUserId) == ReferralBindResult.SUCCESS ? 1 : 0;
    }

    @Override
    public ReferralBindResult bindReferralWithResult(String inviteCode, String inviteeUserId) {
        if (StringUtils.isBlank(inviteCode) || StringUtils.isBlank(inviteeUserId)) {
            return ReferralBindResult.EMPTY_INVITE_CODE;
        }

        String normalizedInviteCode = inviteCode.trim();

        // 1. 检查被邀请人是否已经有邀请人（不可重复绑定）
        UserReferral existing = userReferralMapper.selectReferralByInviteeUserId(inviteeUserId);
        if (existing != null) {
            log.info("用户 {} 已经绑定过邀请人，跳过绑定", inviteeUserId);
            return ReferralBindResult.ALREADY_BOUND;
        }

        // 2. 根据邀请码查询邀请人
        UserInfo inviter = userInfoService.selectUserInfoByInviteCode(normalizedInviteCode);
        if (inviter == null) {
            log.warn("邀请码 {} 无效，绑定邀请关系失败", normalizedInviteCode);
            return ReferralBindResult.INVALID_INVITE_CODE;
        }

        // 3. 不能邀请自己
        if (inviter.getUserId().equals(inviteeUserId)) {
            log.warn("用户不能绑定自己为邀请人: {}", inviteeUserId);
            return ReferralBindResult.SELF_INVITE;
        }

        // 4. 插入绑定关系
        UserReferral userReferral = new UserReferral();
        userReferral.setInviterUserId(inviter.getUserId());
        userReferral.setInviteeUserId(inviteeUserId);
        userReferral.setInviteCode(normalizedInviteCode);
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
}
