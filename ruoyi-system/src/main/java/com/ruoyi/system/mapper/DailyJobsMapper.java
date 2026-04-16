package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.Parents;

/**
 * 兼职日结工作Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
public interface DailyJobsMapper extends BaseMapper<DailyJobs>
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
     * 删除兼职日结工作
     * 
     * @param id 兼职日结工作主键
     * @return 结果
     */
    public int deleteDailyJobsById(Long id);

    /**
     * 批量删除兼职日结工作
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDailyJobsByIds(Long[] ids);

    DailyJobs selectDailyJobsByIdForUpdate(@Param("id") Long id);

    int countPaidSignupOrders(@Param("jobId") Long jobId, @Param("paidStatus") Integer paidStatus);
}
