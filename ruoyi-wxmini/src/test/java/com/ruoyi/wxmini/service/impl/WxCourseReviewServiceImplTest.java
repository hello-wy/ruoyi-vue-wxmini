package com.ruoyi.wxmini.service.impl;

import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.CourseReview;
import com.ruoyi.system.service.ICoursePayOrderService;
import com.ruoyi.system.service.ICourseReviewService;
import com.ruoyi.wxmini.bo.WxCourseReviewCreateBo;
import com.ruoyi.wxmini.vo.WxCourseReviewPublicVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxCourseReviewServiceImplTest {

    @Mock private ICoursePayOrderService coursePayOrderService;
    @Mock private ICourseReviewService courseReviewService;
    @InjectMocks private WxCourseReviewServiceImpl service;

    @Test
    void listReviewsShouldExposeReviewerProfile() {
        CourseReview review = new CourseReview();
        review.setReviewerName("张三");
        review.setReviewerAvatarUrl("https://example.com/avatar.webp");
        when(courseReviewService.selectCourseReviewsByCourseId(8L))
                .thenReturn(Collections.singletonList(review));

        List<WxCourseReviewPublicVo> reviews = service.listReviews(8L);

        assertEquals("张三", reviews.get(0).getReviewerName());
        assertEquals("https://example.com/avatar.webp", reviews.get(0).getReviewerAvatarUrl());
    }

    @Test
    void saveReviewShouldUseUsersLatestPaidOrder() {
        CoursePayOrder order = new CoursePayOrder();
        order.setOrderNo("COURSE-1");
        CourseReview review = new CourseReview();
        review.setContent("很有收获");
        WxCourseReviewCreateBo bo = new WxCourseReviewCreateBo();
        bo.setContent(" 很有收获 ");
        when(coursePayOrderService.selectLatestPaidOrder("user-1", 8L)).thenReturn(order);
        when(courseReviewService.saveOrUpdateCourseReview(order, "很有收获")).thenReturn(review);

        service.saveReview("user-1", 8L, bo);

        verify(courseReviewService).saveOrUpdateCourseReview(order, "很有收获");
    }
}
