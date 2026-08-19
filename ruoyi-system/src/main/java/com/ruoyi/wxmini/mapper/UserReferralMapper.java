package com.ruoyi.wxmini.mapper;

import com.ruoyi.wxmini.domain.UserReferral;
import com.ruoyi.wxmini.domain.vo.UserReferralVo;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 查询指定用户直接邀请的一、二级用户。
     *
     * @param inviterUserId 邀请人user_id
     * @return 二级邀请关系集合
     */
    public List<UserReferralVo> selectSecondLevelReferralVoListByInviterUserId(String inviterUserId);

    /**
     * 查询当前用户直接邀请的全部一级邀请用户，供分页使用。
     *
     * @param inviterUserId 当前登录邀请人 user_id
     * @return 一级邀请用户列表
     */
    public List<UserReferralVo> selectReferralTreeLevel1Invitees(String inviterUserId);

    /**
     * 批量查询指定一级邀请用户的二级邀请用户。
     *
     * @param inviterUserIds 一级邀请用户 user_id 列表
     * @return 二级邀请用户列表
     */
    public List<UserReferralVo> selectReferralVoListByInviterUserIds(
            @Param("inviterUserIds") List<String> inviterUserIds);
}
