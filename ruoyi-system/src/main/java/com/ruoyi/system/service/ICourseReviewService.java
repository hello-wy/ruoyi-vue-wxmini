package com.ruoyi.system.service;

import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.CourseReview;

import java.util.List;

public interface ICourseReviewService {
    CourseReview selectCourseReviewByOrderNo(String orderNo);

    List<CourseReview> selectCourseReviewsByCourseId(Long courseId);

    CourseReview saveOrUpdateCourseReview(CoursePayOrder order, String content);
}
