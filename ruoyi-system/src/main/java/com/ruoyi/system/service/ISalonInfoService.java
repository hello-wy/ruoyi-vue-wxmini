package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SalonInfo;

/**
 * 沙龙活动信息主Service接口
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
public interface ISalonInfoService 
{
    /**
     * 查询沙龙活动信息主
     * 
     * @param id 沙龙活动信息主主键
     * @return 沙龙活动信息主
     */
    public SalonInfo selectSalonInfoById(Long id);

    /**
     * 查询沙龙活动信息主列表
     * 
     * @param salonInfo 沙龙活动信息主
     * @return 沙龙活动信息主集合
     */
    public List<SalonInfo> selectSalonInfoList(SalonInfo salonInfo);

    /**
     * 新增沙龙活动信息主
     * 
     * @param salonInfo 沙龙活动信息主
     * @return 结果
     */
    public int insertSalonInfo(SalonInfo salonInfo);

    /**
     * 修改沙龙活动信息主
     * 
     * @param salonInfo 沙龙活动信息主
     * @return 结果
     */
    public int updateSalonInfo(SalonInfo salonInfo);

    /**
     * 批量删除沙龙活动信息主
     * 
     * @param ids 需要删除的沙龙活动信息主主键集合
     * @return 结果
     */
    public int deleteSalonInfoByIds(Long[] ids);

    /**
     * 删除沙龙活动信息主信息
     * 
     * @param id 沙龙活动信息主主键
     * @return 结果
     */
    public int deleteSalonInfoById(Long id);
}
