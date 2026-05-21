package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TutoringPayrollItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TutoringPayrollItemMapper {
    List<TutoringPayrollItem> selectPendingPayrollItems(TutoringPayrollItem query);

    TutoringPayrollItem selectByScheduleId(@Param("scheduleId") Long scheduleId);

    List<TutoringPayrollItem> selectByIdsForUpdate(@Param("ids") List<Long> ids);

    int insertTutoringPayrollItem(TutoringPayrollItem item);

    int updateTutoringPayrollItem(TutoringPayrollItem item);
}
