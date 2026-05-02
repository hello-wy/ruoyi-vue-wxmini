package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.JobPayrollItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobPayrollItemMapper {
    List<JobPayrollItem> selectJobPayrollItemsByBatchId(Long batchId);

    int batchInsertJobPayrollItems(@Param("items") List<JobPayrollItem> items);

    int updateJobPayrollItem(JobPayrollItem item);
}
