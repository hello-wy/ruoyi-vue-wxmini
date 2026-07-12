package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CourseReview;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseReviewMapper {
    CourseReview selectCourseReviewByOrderNo(String orderNo);

    List<CourseReview> selectCourseReviewsByCourseId(@Param("courseId") Long courseId);

    int insertCourseReview(CourseReview review);

    int updateCourseReview(CourseReview review);
}
