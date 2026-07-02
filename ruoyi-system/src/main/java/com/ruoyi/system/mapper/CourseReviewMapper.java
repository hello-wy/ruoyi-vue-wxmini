package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CourseReview;

public interface CourseReviewMapper {
    CourseReview selectCourseReviewByOrderNo(String orderNo);

    int insertCourseReview(CourseReview review);

    int updateCourseReview(CourseReview review);
}
