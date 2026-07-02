package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.vo.CourseLearningRecordVo;
import com.ruoyi.system.domain.vo.CourseRefundOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CoursePayOrderMapper {
    CoursePayOrder selectCoursePayOrderByOrderNo(String orderNo);

    List<CoursePayOrder> selectMyCourseOrders(@Param("userId") String userId);

    CoursePayOrder selectLatestPaidOrder(@Param("userId") String userId,
                                         @Param("courseId") Long courseId);

    CoursePayOrder selectLatestPendingOrder(@Param("userId") String userId,
                                            @Param("courseId") Long courseId);

    CoursePayOrder selectLatestByCourseIdAndUserId(@Param("courseId") Long courseId,
                                                   @Param("userId") String userId);

    int insertCoursePayOrder(CoursePayOrder order);

    int updateCoursePayOrder(CoursePayOrder order);

    List<CourseRefundOrderVo> selectCourseRefundOrders(@Param("courseId") Long courseId,
                                                       @Param("keyword") String keyword);

    List<CourseLearningRecordVo> selectStudentCourseEnrollmentRecords(@Param("studentId") Long studentId);

    List<CourseLearningRecordVo> selectStudentCourseSignInRecords(@Param("studentId") Long studentId);
}
