package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TutoringSchedule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TutoringScheduleMapper {
    int countByOrderId(@Param("orderId") Long orderId);

    int batchInsertTutoringSchedules(@Param("items") List<TutoringSchedule> items);

    List<TutoringSchedule> selectMySchedules(@Param("parentUserId") Long parentUserId, @Param("tutorUserId") Long tutorUserId);

    TutoringSchedule selectById(@Param("id") Long id);

    TutoringSchedule selectByIdForUpdate(@Param("id") Long id);

    List<TutoringSchedule> selectAdminSchedules(TutoringSchedule query);

    int updateTutoringSchedule(TutoringSchedule schedule);
}
