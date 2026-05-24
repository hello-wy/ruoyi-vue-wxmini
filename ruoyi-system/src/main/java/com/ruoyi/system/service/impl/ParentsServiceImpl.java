package com.ruoyi.system.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.ParentsMapper;
import com.ruoyi.system.mapper.TutoringBindingMapper;
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.service.IParentsService;

/**
 * 家教订单Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-04
 */
@Service
public class ParentsServiceImpl implements IParentsService
{
    private static final String SYSTEM_OPERATOR = "system";

    @Autowired
    private ParentsMapper parentsMapper;

    @Autowired
    private TutoringBindingMapper tutoringBindingMapper;

    /**
     * 查询家教订单
     *
     * @param id 家教订单主键
     * @return 家教订单
     */
    @Override
    public Parents selectParentsById(Long id)
    {
        return parentsMapper.selectParentsById(id);
    }

    /**
     * 查询家教订单列表
     *
     * @param parents 家教订单
     * @return 家教订单
     */
    @Override
    public List<Parents> selectParentsList(Parents parents)
    {
        return parentsMapper.selectParentsList(parents);
    }

    /**
     * 新增家教订单
     *
     * @param parents 家教订单
     * @return 结果
     */
    @Override
    public int insertParents(Parents parents)
    {
        return parentsMapper.insertParents(parents);
    }

    /**
     * 修改家教订单
     *
     * @param parents 家教订单
     * @return 结果
     */
    @Override
    public int updateParents(Parents parents)
    {
        return parentsMapper.updateParents(parents);
    }

    /**
     * 批量删除家教订单
     *
     * @param ids 需要删除的家教订单主键
     * @return 结果
     */
    @Override
    public int deleteParentsByIds(Long[] ids)
    {
        return parentsMapper.deleteParentsByIds(ids);
    }

    /**
     * 删除家教订单信息
     *
     * @param id 家教订单主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteParentsById(Long id)
    {
        tutoringBindingMapper.closeBindingsByParentId(id, null, SYSTEM_OPERATOR);
        return parentsMapper.deleteParentsById(id);
    }

    @Override
    public List<Parents> selectActiveParentsList(Parents parents) {
        LambdaQueryWrapper<Parents> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Parents::getStatus, 0L);
        if (StringUtils.isNotBlank(parents.getSubject())) {
            wrapper.like(Parents::getSubject, parents.getSubject());
        }
        if (StringUtils.isNotBlank(parents.getRegion())) {
            wrapper.like(Parents::getRegion, parents.getRegion());
        }
        if (parents.getMethods() != null) {
            wrapper.eq(Parents::getMethods, parents.getMethods());
        }
        if (StringUtils.isNotBlank(parents.getGrade())) {
            wrapper.like(Parents::getGrade, parents.getGrade());
        }
        return parentsMapper.selectList(wrapper);
    }

    @Override
    public List<Parents> selectParentsByWechatUid(String wechatUid) {
        LambdaQueryWrapper<Parents> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Parents::getWechatUid, wechatUid);
        return parentsMapper.selectList(wrapper);
    }

    @Override
    public Parents selectSingleParentByWechatUid(String wechatUid) {
        return parentsMapper.selectSingleParentByWechatUid(wechatUid);
    }

    @Override
    public long countParentsByWechatUid(String wechatUid) {
        return parentsMapper.countParentsByWechatUid(wechatUid);
    }
}
