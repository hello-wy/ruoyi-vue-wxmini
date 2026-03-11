package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SalonInfoMapper;
import com.ruoyi.system.domain.SalonInfo;
import com.ruoyi.system.service.ISalonInfoService;

/**
 * 沙龙活动信息主Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
@Service
public class SalonInfoServiceImpl implements ISalonInfoService 
{
    @Autowired
    private SalonInfoMapper salonInfoMapper;

    /**
     * 查询沙龙活动信息主
     * 
     * @param id 沙龙活动信息主主键
     * @return 沙龙活动信息主
     */
    @Override
    public SalonInfo selectSalonInfoById(Long id)
    {
        return salonInfoMapper.selectSalonInfoById(id);
    }

    /**
     * 查询沙龙活动信息主列表
     * 
     * @param salonInfo 沙龙活动信息主
     * @return 沙龙活动信息主
     */
    @Override
    public List<SalonInfo> selectSalonInfoList(SalonInfo salonInfo)
    {
        return salonInfoMapper.selectSalonInfoList(salonInfo);
    }

    /**
     * 新增沙龙活动信息主
     * 
     * @param salonInfo 沙龙活动信息主
     * @return 结果
     */
    @Override
    public int insertSalonInfo(SalonInfo salonInfo)
    {
        salonInfo.setCreateTime(DateUtils.getNowDate());
        return salonInfoMapper.insertSalonInfo(salonInfo);
    }

    /**
     * 修改沙龙活动信息主
     * 
     * @param salonInfo 沙龙活动信息主
     * @return 结果
     */
    @Override
    public int updateSalonInfo(SalonInfo salonInfo)
    {
        salonInfo.setUpdateTime(DateUtils.getNowDate());
        return salonInfoMapper.updateSalonInfo(salonInfo);
    }

    /**
     * 批量删除沙龙活动信息主
     * 
     * @param ids 需要删除的沙龙活动信息主主键
     * @return 结果
     */
    @Override
    public int deleteSalonInfoByIds(Long[] ids)
    {
        return salonInfoMapper.deleteSalonInfoByIds(ids);
    }

    /**
     * 删除沙龙活动信息主信息
     * 
     * @param id 沙龙活动信息主主键
     * @return 结果
     */
    @Override
    public int deleteSalonInfoById(Long id)
    {
        return salonInfoMapper.deleteSalonInfoById(id);
    }
}
