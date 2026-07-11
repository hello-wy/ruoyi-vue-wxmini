package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.vo.CourseRefundOrderVo;
import com.ruoyi.system.domain.vo.CourseScanSignInVo;
import com.ruoyi.system.mapper.CoursePayOrderMapper;
import com.ruoyi.system.service.ICoursePayOrderService;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CoursePayOrderServiceImpl implements ICoursePayOrderService {
    @Autowired
    private CoursePayOrderMapper coursePayOrderMapper;
    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public CoursePayOrder selectCoursePayOrderByOrderNo(String orderNo) {
        return coursePayOrderMapper.selectCoursePayOrderByOrderNo(orderNo);
    }

    @Override
    public List<CoursePayOrder> selectMyCourseOrders(String userId) {
        return coursePayOrderMapper.selectMyCourseOrders(userId);
    }

    @Override
    public CoursePayOrder selectLatestPaidOrder(String userId, Long courseId) {
        return coursePayOrderMapper.selectLatestPaidOrder(userId, courseId);
    }

    @Override
    public CoursePayOrder selectLatestPendingOrder(String userId, Long courseId) {
        return coursePayOrderMapper.selectLatestPendingOrder(userId, courseId);
    }

    @Override
    public int insertCoursePayOrder(CoursePayOrder order) {
        Date now = DateUtils.getNowDate();
        order.setCreateTime(now);
        order.setUpdateTime(now);
        return coursePayOrderMapper.insertCoursePayOrder(order);
    }

    @Override
    public int updateCoursePayOrder(CoursePayOrder order) {
        order.setUpdateTime(DateUtils.getNowDate());
        return coursePayOrderMapper.updateCoursePayOrder(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CoursePayOrder markPaid(String orderNo, String transactionId, String requestId, Date payTime) {
        CoursePayOrder order = requireOrder(orderNo);
        if (isPaidOrAfter(order.getStatus())) {
            userInfoService.markStudent(order.getUserId());
            return order;
        }
        if (!CoursePayOrder.STATUS_PENDING.equals(order.getStatus())) {
            throw new ServiceException("订单状态不允许支付确认");
        }
        order.setStatus(CoursePayOrder.STATUS_PAID_WAIT_SIGN);
        order.setWechatTransactionId(transactionId);
        order.setRequestId(requestId);
        order.setPayTime(payTime == null ? DateUtils.getNowDate() : payTime);
        updateCoursePayOrder(order);
        userInfoService.markStudent(order.getUserId());
        return order;
    }

    private boolean isPaidOrAfter(Integer status) {
        return CoursePayOrder.STATUS_PAID_WAIT_SIGN.equals(status)
                || CoursePayOrder.STATUS_SIGNED.equals(status)
                || CoursePayOrder.STATUS_REFUNDED.equals(status);
    }

    @Override
    public CoursePayOrder markRefunded(String orderNo, String refundNo, Date refundTime) {
        CoursePayOrder order = requireOrder(orderNo);
        if (!CoursePayOrder.STATUS_SIGNED.equals(order.getStatus())) {
            throw new ServiceException("只有已签到课程订单可退款");
        }
        order.setStatus(CoursePayOrder.STATUS_REFUNDED);
        order.setRefundNo(refundNo);
        order.setRefundTime(refundTime == null ? DateUtils.getNowDate() : refundTime);
        updateCoursePayOrder(order);
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CoursePayOrder scanSignIn(Long courseId, String userId) {
        CoursePayOrder order = coursePayOrderMapper.selectLatestByCourseIdAndUserId(courseId, userId);
        validateSignOrder(order);
        order.setStatus(CoursePayOrder.STATUS_SIGNED);
        order.setSignTime(DateUtils.getNowDate());
        updateCoursePayOrder(order);
        return order;
    }

    @Override
    public CourseScanSignInVo scanSignInVo(Long courseId, String userId) {
        return toSignVo(scanSignIn(courseId, userId));
    }

    @Override
    public List<CourseRefundOrderVo> selectCourseRefundOrders(Long courseId, String keyword) {
        return coursePayOrderMapper.selectCourseRefundOrders(courseId, keyword);
    }

    private CoursePayOrder requireOrder(String orderNo) {
        CoursePayOrder order = coursePayOrderMapper.selectCoursePayOrderByOrderNo(orderNo);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        return order;
    }

    private void validateSignOrder(CoursePayOrder order) {
        if (order == null) {
            throw new ServiceException("未找到该用户当前课程已支付待签到订单");
        }
        if (CoursePayOrder.STATUS_SIGNED.equals(order.getStatus())) {
            throw new ServiceException("课程订单已签到");
        }
        if (CoursePayOrder.STATUS_REFUNDED.equals(order.getStatus())) {
            throw new ServiceException("课程订单已退款，不能签到");
        }
        if (!CoursePayOrder.STATUS_PAID_WAIT_SIGN.equals(order.getStatus())) {
            throw new ServiceException("课程订单未支付，不能签到");
        }
    }

    private CourseScanSignInVo toSignVo(CoursePayOrder order) {
        CourseScanSignInVo vo = new CourseScanSignInVo();
        vo.setOrderNo(order.getOrderNo());
        vo.setUserId(order.getUserId());
        vo.setCourseId(order.getCourseId());
        vo.setStatus(order.getStatus());
        vo.setSignTime(order.getSignTime());
        return vo;
    }
}
