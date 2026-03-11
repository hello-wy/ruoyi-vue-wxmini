package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DailyJobsMapper;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.service.IDailyJobsService;

/**
 * 兼职日结工作Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
@Service
public class DailyJobsServiceImpl implements IDailyJobsService 
{
    @Autowired
    private DailyJobsMapper dailyJobsMapper;

    /**
     * 查询兼职日结工作
     * 
     * @param id 兼职日结工作主键
     * @return 兼职日结工作
     */
    @Override
    public DailyJobs selectDailyJobsById(Long id)
    {
        return dailyJobsMapper.selectDailyJobsById(id);
    }

    /**
     * 查询兼职日结工作列表
     * 
     * @param dailyJobs 兼职日结工作
     * @return 兼职日结工作
     */
    @Override
    public List<DailyJobs> selectDailyJobsList(DailyJobs dailyJobs)
    {
        return dailyJobsMapper.selectDailyJobsList(dailyJobs);
    }

    /**
     * 新增兼职日结工作
     * 
     * @param dailyJobs 兼职日结工作
     * @return 结果
     */
    @Override
    public int insertDailyJobs(DailyJobs dailyJobs)
    {
        return dailyJobsMapper.insertDailyJobs(dailyJobs);
    }

    /**
     * 修改兼职日结工作
     * 
     * @param dailyJobs 兼职日结工作
     * @return 结果
     */
    @Override
    public int updateDailyJobs(DailyJobs dailyJobs)
    {
        return dailyJobsMapper.updateDailyJobs(dailyJobs);
    }

    /**
     * 批量删除兼职日结工作
     * 
     * @param ids 需要删除的兼职日结工作主键
     * @return 结果
     */
    @Override
    public int deleteDailyJobsByIds(Long[] ids)
    {
        return dailyJobsMapper.deleteDailyJobsByIds(ids);
    }

    /**
     * 删除兼职日结工作信息
     * 
     * @param id 兼职日结工作主键
     * @return 结果
     */
    @Override
    public int deleteDailyJobsById(Long id)
    {
        return dailyJobsMapper.deleteDailyJobsById(id);
    }
}
