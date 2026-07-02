package com.ruoyi.system.service;

import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.CourseReview;

public interface ICourseReviewService {
    CourseReview selectCourseReviewByOrderNo(String orderNo);

    CourseReview saveOrUpdateCourseReview(CoursePayOrder order, String content);
}
