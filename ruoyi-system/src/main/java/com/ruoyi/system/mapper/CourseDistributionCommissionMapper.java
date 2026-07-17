package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CourseDistributionCommissionConfig;
import com.ruoyi.system.domain.CourseDistributionCommissionLedger;
import com.ruoyi.system.domain.bo.CourseDistributionManualRecordBo;
import com.ruoyi.system.domain.vo.CourseDistributionCommissionSummaryVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface CourseDistributionCommissionMapper {
    CourseDistributionCommissionConfig selectConfig();
    int lockConfig();
    int saveConfig(CourseDistributionCommissionConfig config);
    int insertLedger(CourseDistributionCommissionLedger ledger);
    int reverseLedgers(@Param("orderNo") String orderNo);
    List<Map<String, Object>> selectRecords();
    CourseDistributionCommissionSummaryVo selectSummary();
    int insertManualRecord(@Param("record") CourseDistributionManualRecordBo record, @Param("operatorUserId") Long operatorUserId);
}
