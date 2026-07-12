package com.ruoyi.wxmini.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.CourseReview;
import com.ruoyi.system.service.ICoursePayOrderService;
import com.ruoyi.system.service.ICourseReviewService;
import com.ruoyi.wxmini.bo.WxCourseReviewCreateBo;
import com.ruoyi.wxmini.bo.WxCourseReviewSaveBo;
import com.ruoyi.wxmini.service.IWxCourseReviewService;
import com.ruoyi.wxmini.vo.WxCourseReviewPublicVo;
import com.ruoyi.wxmini.vo.WxCourseReviewVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WxCourseReviewServiceImpl implements IWxCourseReviewService {
    @Autowired
    private ICoursePayOrderService coursePayOrderService;

    @Autowired
    private ICourseReviewService courseReviewService;

    @Override
    public List<WxCourseReviewPublicVo> listReviews(Long courseId) {
        return courseReviewService.selectCourseReviewsByCourseId(courseId)
                .stream()
                .map(this::toPublicVo)
                .collect(Collectors.toList());
    }

    @Override
    public WxCourseReviewPublicVo saveReview(String userId, Long courseId, WxCourseReviewCreateBo bo) {
        CoursePayOrder order = requireReviewableOrder(userId, courseId);
        CourseReview review = courseReviewService.saveOrUpdateCourseReview(order, normalizeContent(bo.getContent()));
        return toPublicVo(review);
    }

    @Override
    public WxCourseReviewVo getMyReview(String userId, Long courseId, String orderNo) {
        CoursePayOrder order = requireReviewableOrder(userId, courseId, orderNo);
        return toVo(courseReviewService.selectCourseReviewByOrderNo(order.getOrderNo()));
    }

    @Override
    public WxCourseReviewVo saveMyReview(String userId, Long courseId, WxCourseReviewSaveBo bo) {
        CoursePayOrder order = requireReviewableOrder(userId, courseId, bo.getOrderNo());
        CourseReview review = courseReviewService.saveOrUpdateCourseReview(order, normalizeContent(bo.getContent()));
        return toVo(review);
    }

    private CoursePayOrder requireReviewableOrder(String userId, Long courseId) {
        CoursePayOrder order = coursePayOrderService.selectLatestPaidOrder(userId, courseId);
        if (order == null) {
            throw new ServiceException("仅已报名课程可评价");
        }
        return order;
    }

    private CoursePayOrder requireReviewableOrder(String userId, Long courseId, String orderNo) {
        if (orderNo == null || orderNo.trim().isEmpty()) {
            throw new ServiceException("orderNo不能为空");
        }
        CoursePayOrder order = coursePayOrderService.selectCoursePayOrderByOrderNo(orderNo.trim());
        if (order == null || !userId.equals(order.getUserId())) {
            throw new ServiceException("订单不存在");
        }
        if (courseId == null || !courseId.equals(order.getCourseId())) {
            throw new ServiceException("订单与课程不匹配");
        }
        if (!CoursePayOrder.STATUS_PAID_WAIT_SIGN.equals(order.getStatus())
                && !CoursePayOrder.STATUS_SIGNED.equals(order.getStatus())) {
            throw new ServiceException("仅已报名课程可评价");
        }
        return order;
    }

    private String normalizeContent(String content) {
        String value = content == null ? "" : content.trim();
        if (value.isEmpty()) {
            throw new ServiceException("评价内容不能为空");
        }
        if (value.length() > 1000) {
            throw new ServiceException("评价内容不能超过1000字");
        }
        return value;
    }

    private WxCourseReviewPublicVo toPublicVo(CourseReview review) {
        WxCourseReviewPublicVo vo = new WxCourseReviewPublicVo();
        vo.setId(review.getId());
        vo.setCourseId(review.getCourseId());
        vo.setContent(review.getContent());
        vo.setReviewerName(review.getReviewerName());
        vo.setReviewerAvatarUrl(review.getReviewerAvatarUrl());
        vo.setCreateTime(review.getCreateTime());
        vo.setUpdateTime(review.getUpdateTime());
        return vo;
    }

    private WxCourseReviewVo toVo(CourseReview review) {
        if (review == null) {
            return null;
        }
        WxCourseReviewVo vo = new WxCourseReviewVo();
        vo.setId(review.getId());
        vo.setOrderNo(review.getOrderNo());
        vo.setCourseId(review.getCourseId());
        vo.setContent(review.getContent());
        vo.setReviewerName(review.getReviewerName());
        vo.setReviewerAvatarUrl(review.getReviewerAvatarUrl());
        vo.setCreateTime(review.getCreateTime());
        vo.setUpdateTime(review.getUpdateTime());
        return vo;
    }
}
