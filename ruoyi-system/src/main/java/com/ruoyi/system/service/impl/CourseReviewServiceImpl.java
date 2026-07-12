package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.CourseReview;
import com.ruoyi.system.mapper.CourseReviewMapper;
import com.ruoyi.system.service.ICourseReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CourseReviewServiceImpl implements ICourseReviewService {
    @Autowired
    private CourseReviewMapper courseReviewMapper;

    @Override
    public CourseReview selectCourseReviewByOrderNo(String orderNo) {
        return courseReviewMapper.selectCourseReviewByOrderNo(orderNo);
    }

    @Override
    public List<CourseReview> selectCourseReviewsByCourseId(Long courseId) {
        return courseReviewMapper.selectCourseReviewsByCourseId(courseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseReview saveOrUpdateCourseReview(CoursePayOrder order, String content) {
        CourseReview review = courseReviewMapper.selectCourseReviewByOrderNo(order.getOrderNo());
        Date now = DateUtils.getNowDate();
        if (review == null) {
            review = new CourseReview();
            review.setOrderNo(order.getOrderNo());
            review.setUserId(order.getUserId());
            review.setCourseId(order.getCourseId());
            review.setContent(content);
            review.setCreateTime(now);
            review.setUpdateTime(now);
            courseReviewMapper.insertCourseReview(review);
            return review;
        }
        review.setContent(content);
        review.setUpdateTime(now);
        courseReviewMapper.updateCourseReview(review);
        return review;
    }
}
