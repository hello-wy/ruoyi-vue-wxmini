package com.ruoyi.wxmini.domain;

import com.ruoyi.common.core.domain.BaseEntity;

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
}
