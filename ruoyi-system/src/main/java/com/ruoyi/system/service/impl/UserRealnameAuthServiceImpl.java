package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.UserRealnameAuthMapper;
import com.ruoyi.system.domain.UserRealnameAuth;
import com.ruoyi.system.service.IUserRealnameAuthService;

/**
 * 用户实名认证Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-06
 */
@Service
public class UserRealnameAuthServiceImpl implements IUserRealnameAuthService 
{
    @Autowired
    private UserRealnameAuthMapper userRealnameAuthMapper;

    /**
     * 查询用户实名认证
     * 
     * @param id 用户实名认证主键
     * @return 用户实名认证
     */
    @Override
    public UserRealnameAuth selectUserRealnameAuthById(Long id)
    {
        return userRealnameAuthMapper.selectUserRealnameAuthById(id);
    }

    /**
     * 查询用户实名认证列表
     * 
     * @param userRealnameAuth 用户实名认证
     * @return 用户实名认证
     */
    @Override
    public List<UserRealnameAuth> selectUserRealnameAuthList(UserRealnameAuth userRealnameAuth)
    {
        return userRealnameAuthMapper.selectUserRealnameAuthList(userRealnameAuth);
    }

    /**
     * 新增用户实名认证
     * 
     * @param userRealnameAuth 用户实名认证
     * @return 结果
     */
    @Override
    public int insertUserRealnameAuth(UserRealnameAuth userRealnameAuth)
    {
        userRealnameAuth.setCreateTime(DateUtils.getNowDate());
        return userRealnameAuthMapper.insertUserRealnameAuth(userRealnameAuth);
    }

    /**
     * 修改用户实名认证
     * 
     * @param userRealnameAuth 用户实名认证
     * @return 结果
     */
    @Override
    public int updateUserRealnameAuth(UserRealnameAuth userRealnameAuth)
    {
        userRealnameAuth.setUpdateTime(DateUtils.getNowDate());
        return userRealnameAuthMapper.updateUserRealnameAuth(userRealnameAuth);
    }

    /**
     * 批量删除用户实名认证
     * 
     * @param ids 需要删除的用户实名认证主键
     * @return 结果
     */
    @Override
    public int deleteUserRealnameAuthByIds(Long[] ids)
    {
        return userRealnameAuthMapper.deleteUserRealnameAuthByIds(ids);
    }

    /**
     * 删除用户实名认证信息
     * 
     * @param id 用户实名认证主键
     * @return 结果
     */
    @Override
    public int deleteUserRealnameAuthById(Long id)
    {
        return userRealnameAuthMapper.deleteUserRealnameAuthById(id);
    }
}
