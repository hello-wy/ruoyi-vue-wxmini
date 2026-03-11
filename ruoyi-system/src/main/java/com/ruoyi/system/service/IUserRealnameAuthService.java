package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.UserRealnameAuth;

/**
 * 用户实名认证Service接口
 * 
 * @author ruoyi
 * @date 2026-03-06
 */
public interface IUserRealnameAuthService 
{
    /**
     * 查询用户实名认证
     * 
     * @param id 用户实名认证主键
     * @return 用户实名认证
     */
    public UserRealnameAuth selectUserRealnameAuthById(Long id);

    /**
     * 查询用户实名认证列表
     * 
     * @param userRealnameAuth 用户实名认证
     * @return 用户实名认证集合
     */
    public List<UserRealnameAuth> selectUserRealnameAuthList(UserRealnameAuth userRealnameAuth);

    /**
     * 新增用户实名认证
     * 
     * @param userRealnameAuth 用户实名认证
     * @return 结果
     */
    public int insertUserRealnameAuth(UserRealnameAuth userRealnameAuth);

    /**
     * 修改用户实名认证
     * 
     * @param userRealnameAuth 用户实名认证
     * @return 结果
     */
    public int updateUserRealnameAuth(UserRealnameAuth userRealnameAuth);

    /**
     * 批量删除用户实名认证
     * 
     * @param ids 需要删除的用户实名认证主键集合
     * @return 结果
     */
    public int deleteUserRealnameAuthByIds(Long[] ids);

    /**
     * 删除用户实名认证信息
     * 
     * @param id 用户实名认证主键
     * @return 结果
     */
    public int deleteUserRealnameAuthById(Long id);
}
