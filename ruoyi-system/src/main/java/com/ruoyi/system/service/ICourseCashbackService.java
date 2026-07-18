package com.ruoyi.system.service;
import com.ruoyi.system.domain.CourseCashbackConfig;
import com.ruoyi.system.domain.CourseCashbackLedger;
import com.ruoyi.system.domain.bo.CourseCashbackDeductionBo;
import com.ruoyi.system.domain.bo.CourseFinanceManualRecordBo;
import com.ruoyi.system.domain.bo.CourseFinanceQueryBo;
import com.ruoyi.system.domain.vo.CourseFinanceManualRecordVo;
import com.ruoyi.system.domain.vo.CourseFinanceSummaryVo;
import java.util.List;
import java.util.Map;

public interface ICourseCashbackService {
    CourseCashbackConfig selectConfig();
    void saveConfig(CourseCashbackConfig config);
    void recordPaidCourseCashback(String orderNo);
    void reversePaidCourseCashback(String orderNo);
    List<CourseCashbackLedger> selectLedgerList(CourseFinanceQueryBo query);
    List<CourseCashbackLedger> selectMyLedgerList();
    CourseCashbackLedger selectPayment(String orderNo);
    List<Map<String, Object>> selectDeductionHistory(String orderNo);
    CourseFinanceSummaryVo selectSummary();
    CourseFinanceSummaryVo selectMySummary();
    void deduct(CourseCashbackDeductionBo deduction);
    List<CourseFinanceManualRecordVo> selectManualRecordList();
    void createManualRecord(CourseFinanceManualRecordBo record);
}
