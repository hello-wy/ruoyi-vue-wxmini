package com.ruoyi.wxmini.service;

import com.ruoyi.wxmini.bo.WxCourseReviewSaveBo;
import com.ruoyi.wxmini.vo.WxCourseReviewVo;

public interface IWxCourseReviewService {
    WxCourseReviewVo getMyReview(String userId, Long courseId, String orderNo);

    WxCourseReviewVo saveMyReview(String userId, Long courseId, WxCourseReviewSaveBo bo);
}
