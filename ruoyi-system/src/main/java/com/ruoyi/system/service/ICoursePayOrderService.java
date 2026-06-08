package com.ruoyi.system.service;

import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.vo.CourseRefundOrderVo;
import com.ruoyi.system.domain.vo.CourseScanSignInVo;

import java.util.Date;
import java.util.List;

public interface ICoursePayOrderService {
    CoursePayOrder selectCoursePayOrderByOrderNo(String orderNo);

    List<CoursePayOrder> selectMyCourseOrders(String userId);

    CoursePayOrder selectLatestPaidOrder(String userId, Long courseId);

    CoursePayOrder selectLatestPendingOrder(String userId, Long courseId);

    int insertCoursePayOrder(CoursePayOrder order);

    int updateCoursePayOrder(CoursePayOrder order);

    CoursePayOrder markPaid(String orderNo, String transactionId, String requestId, Date payTime);

    CoursePayOrder markRefunded(String orderNo, String refundNo, Date refundTime);

    CoursePayOrder scanSignIn(Long courseId, String userId);

    CourseScanSignInVo scanSignInVo(Long courseId, String userId);

    List<CourseRefundOrderVo> selectCourseRefundOrders(Long courseId, String keyword);
}
