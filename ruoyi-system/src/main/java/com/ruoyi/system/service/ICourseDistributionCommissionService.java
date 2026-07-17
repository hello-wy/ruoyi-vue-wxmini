package com.ruoyi.system.service;

import com.ruoyi.system.domain.CourseDistributionCommissionConfig;
import com.ruoyi.system.domain.bo.CourseDistributionManualRecordBo;
import com.ruoyi.system.domain.vo.CourseDistributionCommissionSummaryVo;
import java.util.List;
import java.util.Map;

public interface ICourseDistributionCommissionService {
    CourseDistributionCommissionConfig selectConfig();
    void saveConfig(CourseDistributionCommissionConfig config);
    void recordPaidCourseCommission(String orderNo);
    void reversePaidCourseCommission(String orderNo);
    CourseDistributionCommissionSummaryVo selectSummary();
    List<Map<String, Object>> selectRecords();
    void createManualRecord(CourseDistributionManualRecordBo record);
}
