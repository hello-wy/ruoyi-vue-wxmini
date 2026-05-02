package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.JobPayrollBatch;

public interface JobPayrollBatchMapper {
    JobPayrollBatch selectJobPayrollBatchByOrderNo(String orderNo);

    JobPayrollBatch selectJobPayrollBatchByOrderNoForUpdate(String orderNo);

    int insertJobPayrollBatch(JobPayrollBatch batch);

    int updateJobPayrollBatch(JobPayrollBatch batch);
}
