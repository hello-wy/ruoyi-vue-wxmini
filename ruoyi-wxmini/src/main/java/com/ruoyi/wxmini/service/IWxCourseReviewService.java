package com.ruoyi.wxmini.service;

import com.ruoyi.wxmini.bo.WxCourseReviewCreateBo;
import com.ruoyi.wxmini.bo.WxCourseReviewSaveBo;
import com.ruoyi.wxmini.vo.WxCourseReviewPublicVo;
import com.ruoyi.wxmini.vo.WxCourseReviewVo;

import java.util.List;

public interface IWxCourseReviewService {
    List<WxCourseReviewPublicVo> listReviews(Long courseId);

    WxCourseReviewPublicVo saveReview(String userId, Long courseId, WxCourseReviewCreateBo bo);

    WxCourseReviewVo getMyReview(String userId, Long courseId, String orderNo);

    WxCourseReviewVo saveMyReview(String userId, Long courseId, WxCourseReviewSaveBo bo);
}
