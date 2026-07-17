package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CourseCashbackConfig;
import com.ruoyi.system.domain.CourseCashbackLedger;
import com.ruoyi.system.domain.bo.CourseFinanceQueryBo;
import com.ruoyi.system.domain.bo.StudentAccessScope;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;

public interface CourseCashbackMapper {
    CourseCashbackConfig selectConfig();
    int saveConfig(CourseCashbackConfig config);
    int insertLedger(CourseCashbackLedger ledger);
    CourseCashbackLedger selectLedgerByOrderNo(@Param("orderNo") String orderNo);
    int reverseLedger(@Param("orderNo") String orderNo);
    List<CourseCashbackLedger> selectLedgerList(@Param("scope") StudentAccessScope scope,
                                                @Param("query") CourseFinanceQueryBo query);
    List<CourseCashbackLedger> selectMyLedgerList(@Param("employeeUserId") Long employeeUserId);
    BigDecimal selectEmployeeAvailable(@Param("employeeUserId") Long employeeUserId);
    int lockFinanceConfig();
    List<java.util.Map<String, Object>> selectDeductionHistory(@Param("orderNo") String orderNo);
    int insertDeduction(@Param("employeeUserId") Long employeeUserId, @Param("amount") BigDecimal amount,
                        @Param("reason") String reason, @Param("operatorUserId") Long operatorUserId);
}
