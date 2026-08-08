package com.ruoyi.wxmini.mapper;

import com.ruoyi.wxmini.domain.UserReferral;
import com.ruoyi.wxmini.domain.vo.UserReferralVo;
import java.util.List;

/**
 * 用户邀请关系Mapper接口
 *
 * @author ruoyi
 * @date 2026-07-01
 */
public interface UserReferralMapper {

    /**
     * 新增邀请关系
     *
     * @param userReferral 邀请关系
     * @return 结果
     */
    public int insertUserReferral(UserReferral userReferral);

    /**
     * 查询我邀请的下级Vo列表
     *
     * @param inviterUserId 邀请人user_id
     * @return 下级Vo列表
     */
    public List<UserReferralVo> selectReferralVoListByInviterUserId(String inviterUserId);

    /**
     * 查询某被邀请人的邀请关系
     *
     * @param inviteeUserId 被邀请人user_id
     * @return 邀请关系
     */
    public UserReferral selectReferralByInviteeUserId(String inviteeUserId);

    UserReferral selectReferralByIdForUpdate(Long id);

    int rewardReferral(UserReferral userReferral);

    int deletePendingReferralById(Long id);

    /**
     * 统计邀请人数
     *
     * @param inviterUserId 邀请人user_id
     * @return 人数
     */
    public int selectReferralCountByInviterUserId(String inviterUserId);

    /**
     * 查询所有邀请关系列表（用于管理员后台）
     *
     * @param userReferral 筛选条件
     * @return 邀请关系集合
     */
    public List<UserReferral> selectUserReferralList(UserReferral userReferral);

    /**
     * 后台展示邀请关系视图（关联用户信息）
     *
     * @param userReferralVo 过滤条件
     * @return 邀请Vo集合
     */
    public List<UserReferralVo> selectUserReferralVoList(UserReferralVo userReferralVo);
}
