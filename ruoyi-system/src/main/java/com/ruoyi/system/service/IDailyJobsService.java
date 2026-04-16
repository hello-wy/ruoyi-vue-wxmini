package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.DailyJobs;

/**
 * 兼职日结工作Service接口
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
public interface IDailyJobsService 
{
    /**
     * 查询兼职日结工作
     * 
     * @param id 兼职日结工作主键
     * @return 兼职日结工作
     */
    public DailyJobs selectDailyJobsById(Long id);

    /**
     * 查询兼职日结工作列表
     * 
     * @param dailyJobs 兼职日结工作
     * @return 兼职日结工作集合
     */
    public List<DailyJobs> selectDailyJobsList(DailyJobs dailyJobs);

    /**
     * 新增兼职日结工作
     * 
     * @param dailyJobs 兼职日结工作
     * @return 结果
     */
    public int insertDailyJobs(DailyJobs dailyJobs);

    /**
     * 修改兼职日结工作
     * 
     * @param dailyJobs 兼职日结工作
     * @return 结果
     */
    public int updateDailyJobs(DailyJobs dailyJobs);

    /**
     * 批量删除兼职日结工作
     * 
     * @param ids 需要删除的兼职日结工作主键集合
     * @return 结果
     */
    public int deleteDailyJobsByIds(Long[] ids);

    DailyJobs selectDailyJobsByIdForUpdate(Long id);

    int countPaidSignupOrders(Long jobId, Integer paidStatus);

    /**
     * 删除兼职日结工作信息
     * 
     * @param id 兼职日结工作主键
     * @return 结果
     */
    public int deleteDailyJobsById(Long id);
}
