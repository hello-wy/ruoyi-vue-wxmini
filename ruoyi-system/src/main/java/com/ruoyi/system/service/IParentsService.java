package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Parents;

/**
 * 家教订单Service接口
 * 
 * @author ruoyi
 * @date 2026-03-04
 */
public interface IParentsService 
{
    /**
     * 查询家教订单
     * 
     * @param id 家教订单主键
     * @return 家教订单
     */
    public Parents selectParentsById(Long id);

    /**
     * 查询家教订单列表
     * 
     * @param parents 家教订单
     * @return 家教订单集合
     */
    public List<Parents> selectParentsList(Parents parents);

    /**
     * 新增家教订单
     * 
     * @param parents 家教订单
     * @return 结果
     */
    public int insertParents(Parents parents);

    /**
     * 修改家教订单
     * 
     * @param parents 家教订单
     * @return 结果
     */
    public int updateParents(Parents parents);

    /**
     * 批量删除家教订单
     * 
     * @param ids 需要删除的家教订单主键集合
     * @return 结果
     */
    public int deleteParentsByIds(Long[] ids);

    /**
     * 删除家教订单信息
     * 
     * @param id 家教订单主键
     * @return 结果
     */
    public int deleteParentsById(Long id);

    /**
     * 查询有效家教单列表（status=0），支持筛选（配合 PageHelper 分页）
     *
     * @param parents 筛选条件
     * @return 家教单列表
     */
    List<Parents> selectActiveParentsList(Parents parents);

    /**
     * 根据微信用户ID查询家长家教单列表
     *
     * @param wechatUid 微信用户ID
     * @return 家教单列表
     */
    List<Parents> selectParentsByWechatUid(String wechatUid);
}
