package com.ruoyi.wxmini.service;

import com.ruoyi.wxmini.domain.UserReferral;
import com.ruoyi.wxmini.domain.vo.UserReferralVo;
import java.util.List;

/**
 * 用户邀请关系Service接口
 *
 * @author ruoyi
 * @date 2026-07-01
 */
public interface IUserReferralService {

    public enum ReferralBindResult {
        SUCCESS,
        EMPTY_INVITE_CODE,
        ALREADY_BOUND,
        INVALID_INVITE_CODE,
        SELF_INVITE,
        FAILED
    }

    /**
     * 绑定邀请关系
     *
     * @param inviteCode 邀请码
     * @param inviteeUserId 被邀请人user_id
     * @return 结果
     */
    public int bindReferral(String inviteCode, String inviteeUserId);

    /**
     * 绑定邀请关系并返回明确业务结果
     *
     * @param inviteCode 邀请码
     * @param inviteeUserId 被邀请人user_id
     * @return 绑定结果
     */
    public ReferralBindResult bindReferralWithResult(String inviteCode, String inviteeUserId);

    /**
     * 查询我的下级Vo列表
     *
     * @param inviterUserId 邀请人user_id
     * @return 下级列表
     */
    public List<UserReferralVo> getMyInvitees(String inviterUserId);

    /**
     * 统计邀请人数
     *
     * @param inviterUserId 邀请人user_id
     * @return 数量
     */
    public int getMyInviteeCount(String inviterUserId);

    /**
     * 查询我的邀请关系
     *
     * @param inviteeUserId 被邀请人user_id
     * @return 邀请关系
     */
    public UserReferral getMyInviter(String inviteeUserId);

    /**
     * 分页查询所有邀请关系列表
     *
     * @param userReferral 过滤条件
     * @return 邀请关系集合
     */
    public List<UserReferral> selectUserReferralList(UserReferral userReferral);

    /**
     * 分页查询所有邀请关系视图列表
     *
     * @param userReferralVo 过滤条件
     * @return 邀请关系视图集合
     */
    public List<UserReferralVo> selectUserReferralVoList(UserReferralVo userReferralVo);
}
