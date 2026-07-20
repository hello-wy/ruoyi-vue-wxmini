package com.ruoyi.wxmini.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户邀请关系对象 user_referral
 *
 * @author ruoyi
 * @date 2026-07-01
 */
public class UserReferral extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 邀请人user_id */
    private String inviterUserId;

    /** 被邀请人user_id */
    private String inviteeUserId;

    /** 使用的邀请码 */
    private String inviteCode;

    private String rewardStatus;
    private BigDecimal rewardAmount;
    private Date rewardTime;
    private Long rewardOperatorUserId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setInviterUserId(String inviterUserId) {
        this.inviterUserId = inviterUserId;
    }

    public String getInviterUserId() {
        return inviterUserId;
    }

    public void setInviteeUserId(String inviteeUserId) {
        this.inviteeUserId = inviteeUserId;
    }

    public String getInviteeUserId() {
        return inviteeUserId;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public String getRewardStatus() { return rewardStatus; }
    public void setRewardStatus(String rewardStatus) { this.rewardStatus = rewardStatus; }
    public BigDecimal getRewardAmount() { return rewardAmount; }
    public void setRewardAmount(BigDecimal rewardAmount) { this.rewardAmount = rewardAmount; }
    public Date getRewardTime() { return rewardTime; }
    public void setRewardTime(Date rewardTime) { this.rewardTime = rewardTime; }
    public Long getRewardOperatorUserId() { return rewardOperatorUserId; }
    public void setRewardOperatorUserId(Long rewardOperatorUserId) { this.rewardOperatorUserId = rewardOperatorUserId; }
}
