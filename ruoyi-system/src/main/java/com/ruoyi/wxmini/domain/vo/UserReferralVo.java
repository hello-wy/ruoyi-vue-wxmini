package com.ruoyi.wxmini.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户邀请关系视图对象
 *
 * @author ruoyi
 * @date 2026-07-01
 */
public class UserReferralVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 被邀请人user_id */
    private String userId;

    /** 被邀请人用户名/昵称 */
    private String userName;

    /** 被邀请人手机号 */
    private String phone;

    /** 被邀请人头像 */
    private String avatarUrl;

    /** 邀请人user_id */
    private String inviterUserId;

    /** 邀请人用户名/昵称 */
    private String inviterUserName;

    /** 邀请人手机号 */
    private String inviterPhone;

    /** 使用的邀请码 */
    private String inviteCode;

    /** 绑定时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String rewardStatus;
    private BigDecimal rewardAmount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rewardTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getInviterUserId() {
        return inviterUserId;
    }

    public void setInviterUserId(String inviterUserId) {
        this.inviterUserId = inviterUserId;
    }

    public String getInviterUserName() {
        return inviterUserName;
    }

    public void setInviterUserName(String inviterUserName) {
        this.inviterUserName = inviterUserName;
    }

    public String getInviterPhone() {
        return inviterPhone;
    }

    public void setInviterPhone(String inviterPhone) {
        this.inviterPhone = inviterPhone;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRewardStatus() { return rewardStatus; }
    public void setRewardStatus(String rewardStatus) { this.rewardStatus = rewardStatus; }
    public BigDecimal getRewardAmount() { return rewardAmount; }
    public void setRewardAmount(BigDecimal rewardAmount) { this.rewardAmount = rewardAmount; }
    public Date getRewardTime() { return rewardTime; }
    public void setRewardTime(Date rewardTime) { this.rewardTime = rewardTime; }
}
