package com.ruoyi.wxmini.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.service.ICoursePayOrderService;
import com.ruoyi.system.service.ILecturesService;
import com.ruoyi.system.service.IStudentEnrollmentService;
import com.ruoyi.wxmini.bo.WxCoursePayCreateOrderBo;
import com.ruoyi.wxmini.bo.WxPayCreateOrderParam;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.AbsWxPayBaseService;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IWxCoursePayService;
import com.ruoyi.wxmini.vo.WxCoursePayOrderDetailVo;
import com.ruoyi.wxmini.vo.WxPayParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class WxCoursePayServiceImpl extends AbsWxPayBaseService<WxCoursePayOrderDetailVo> implements IWxCoursePayService {
    private static final String ORDER_PREFIX = "CRS";

    @Autowired private ILecturesService lecturesService;
    @Autowired private ICoursePayOrderService coursePayOrderService;
    @Autowired private IUserInfoService userInfoService;
    @Autowired private IStudentEnrollmentService studentEnrollmentService;
    @Resource private WxPayService wxPayService;

    @Override
    public List<WxCoursePayOrderDetailVo> listMyOrders(String userId) {
        List<WxCoursePayOrderDetailVo> result = new ArrayList<>();
        for (CoursePayOrder order : coursePayOrderService.selectMyCourseOrders(userId)) {
            result.add(toDetailVo(order));
        }
        return result;
    }

    @Override
    public WxPayParamVo createCourseOrder(String userId, WxCoursePayCreateOrderBo bo) throws Exception {
        Lectures course = requirePayableCourse(bo.getCourseId());
        CoursePayOrder existing = coursePayOrderService.selectLatestPaidOrder(userId, bo.getCourseId());
        if (existing != null) {
            throw new ServiceException("当前课程已报名");
        }
        closePendingOrder(userId, bo.getCourseId());
        UserInfo userInfo = requireOpenIdUser(userId);
        assertCourseEnrollmentAvailable(course, userInfo.getId());
        WxCoursePayOrderDetailVo payVo = buildPayVo(bo, course, userInfo);
        return createOrder(userId, payVo);
    }

    @Override
    public WxCoursePayOrderDetailVo queryCourseOrder(String userId, String orderNo) {
        CoursePayOrder order = loadOwnedCourseOrder(userId, orderNo);
        return toDetailVo(compensatePendingOrder(order));
    }

    @Override
    public WxCoursePayOrderDetailVo queryPaidCourseOrder(String userId, Long courseId) {
        CoursePayOrder order = coursePayOrderService.selectLatestPaidOrder(userId, courseId);
        return order == null ? null : toDetailVo(order);
    }

    @Override
    public WxCoursePayOrderDetailVo cancelCourseOrder(String userId, String orderNo) {
        CoursePayOrder order = loadOwnedCourseOrder(userId, orderNo);
        if (CoursePayOrder.STATUS_CANCELED.equals(order.getStatus())) {
            return toDetailVo(order);
        }
        if (!CoursePayOrder.STATUS_PENDING.equals(order.getStatus())) {
            throw new ServiceException("只有待支付课程订单可取消");
        }
        try {
            cancelOrder(userId, orderNo);
        } catch (WxPayException e) {
            throw new ServiceException("取消课程订单失败: " + e.getMessage());
        }
        return queryCourseOrder(userId, orderNo);
    }

    @Override
    public boolean handleCoursePaidCallback(WxPayNotifyV3Result result, String requestId) {
        if (result == null || result.getResult() == null) {
            return false;
        }
        CoursePayOrder order = coursePayOrderService.selectCoursePayOrderByOrderNo(
                result.getResult().getOutTradeNo());
        if (order == null) {
            return false;
        }
        Date payTime = parseSuccessTime(result.getResult().getSuccessTime());
        coursePayOrderService.markPaid(order.getOrderNo(), result.getResult().getTransactionId(), requestId, payTime);
        return true;
    }

    @Override
    public String getResourceId(WxCoursePayOrderDetailVo payVo) {
        return "course:" + payVo.getCourseId() + ":" + payVo.getName() + ":" + UUID.fastUUID();
    }

    @Override
    public Boolean checkBeforeCreatOrder(String userId, WxCoursePayOrderDetailVo payVo) {
        return payVo.getCourseId() != null && isPositive(payVo.getAmount());
    }

    @Override
    public Boolean checkUserOrderIsMatch(String userId, String orderNo) {
        CoursePayOrder order = coursePayOrderService.selectCoursePayOrderByOrderNo(orderNo);
        return order != null && userId.equals(order.getUserId());
    }

    @Override
    public WxPayCreateOrderParam buildOrderParam(String userId, WxCoursePayOrderDetailVo payVo,
                                                 HashMap<String, Object> contextMap) {
        UserInfo userInfo = requireOpenIdUser(userId);
        WxPayCreateOrderParam orderParam = new WxPayCreateOrderParam();
        orderParam.setOrderNo(ORDER_PREFIX + DateUtils.dateTimeNow("yyyyMMddHHmmss") + System.currentTimeMillis());
        orderParam.setOrderDesc("课程报名：" + payVo.getCourseName());
        orderParam.setAmount(payVo.getAmount().multiply(new BigDecimal("100")).intValue());
        orderParam.setOpenId(userInfo.getOpenId());
        orderParam.setTimeExpire(DateUtil.format(DateUtil.offsetMinute(new Date(), 5),
                DatePattern.UTC_WITH_XXX_OFFSET_PATTERN));
        contextMap.put("userId", userId);
        return orderParam;
    }

    @Override
    public WxCoursePayOrderDetailVo buildPayVoWithReCreatOrder(String userId, String orderNo) {
        CoursePayOrder order = loadOwnedCourseOrder(userId, orderNo);
        return toDetailVo(order);
    }

    @Override
    public Boolean saveOrderInfo(String orderNo, WxCoursePayOrderDetailVo payVo,
                                 WxPayCreateOrderParam orderParam, HashMap<String, Object> contextMap) {
        CoursePayOrder order = toOrder(orderNo, payVo, (String) contextMap.get("userId"));
        return coursePayOrderService.insertCoursePayOrder(order) > 0;
    }

    @Override
    public Boolean updOrderWithPaySuccess(String orderNo) {
        WxPayOrderQueryV3Result result = queryWxOrder(orderNo);
        if (!"SUCCESS".equals(result.getTradeState())) {
            return false;
        }
        coursePayOrderService.markPaid(orderNo, result.getTransactionId(), null, parseSuccessTime(result.getSuccessTime()));
        return true;
    }

    @Override
    public Boolean closeOrder(String orderNo) {
        CoursePayOrder order = coursePayOrderService.selectCoursePayOrderByOrderNo(orderNo);
        if (order == null) {
            return false;
        }
        order.setStatus(CoursePayOrder.STATUS_CANCELED);
        return coursePayOrderService.updateCoursePayOrder(order) > 0;
    }

    @Override
    protected String getNotifyUrl(WxCoursePayOrderDetailVo payVo) {
        return "https://zhiyujia.xyz/api/wxmini/pay/courses/notify";
    }

    private CoursePayOrder compensatePendingOrder(CoursePayOrder order) {
        if (!CoursePayOrder.STATUS_PENDING.equals(order.getStatus())) {
            return order;
        }
        WxPayOrderQueryV3Result result = queryWxOrder(order.getOrderNo());
        if ("SUCCESS".equals(result.getTradeState())) {
            return coursePayOrderService.markPaid(order.getOrderNo(), result.getTransactionId(), null,
                    parseSuccessTime(result.getSuccessTime()));
        }
        return order;
    }

    private WxPayOrderQueryV3Result queryWxOrder(String orderNo) {
        try {
            WxPayOrderQueryV3Request request = new WxPayOrderQueryV3Request();
            request.setOutTradeNo(orderNo);
            return wxPayService.queryOrderV3(request);
        } catch (Exception e) {
            throw new ServiceException("同步支付状态失败: " + e.getMessage());
        }
    }

    private Lectures requirePayableCourse(Long courseId) {
        Lectures course = lecturesService.selectLecturesById(courseId);
        if (course == null) {
            throw new ServiceException("课程不存在");
        }
        if (!isPositive(course.getRegistrationFee())) {
            throw new ServiceException("课程报名费必须大于0");
        }
        return course;
    }

    private UserInfo requireOpenIdUser(String userId) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null || userInfo.getOpenId() == null || userInfo.getOpenId().isEmpty()) {
            throw new ServiceException("当前用户缺少openId");
        }
        return userInfo;
    }

    private void assertCourseEnrollmentAvailable(Lectures course, Long uid) {
        if (!requiresEnrollment(course)) {
            return;
        }
        studentEnrollmentService.assertCourseEnrollmentAvailable(uid, course.getId());
    }

    private boolean requiresEnrollment(Lectures course) {
        return !Boolean.FALSE.equals(course.getRequiresEnrollment());
    }

    private void closePendingOrder(String userId, Long courseId) throws Exception {
        CoursePayOrder pending = coursePayOrderService.selectLatestPendingOrder(userId, courseId);
        if (pending == null) {
            return;
        }
        wxPayService.closeOrderV3(pending.getOrderNo());
        pending.setStatus(CoursePayOrder.STATUS_CANCELED);
        coursePayOrderService.updateCoursePayOrder(pending);
    }

    private WxCoursePayOrderDetailVo buildPayVo(WxCoursePayCreateOrderBo bo, Lectures course, UserInfo userInfo) {
        WxCoursePayOrderDetailVo vo = new WxCoursePayOrderDetailVo();
        vo.setCourseId(course.getId());
        vo.setCourseName(course.getName());
        vo.setAmount(course.getRegistrationFee());
        vo.setName(defaultText(bo.getName(), userInfo.getRealName()));
        vo.setGender(bo.getGender());
        vo.setPhone(defaultText(bo.getPhone(), userInfo.getPhone()));
        vo.setCompany(bo.getCompany());
        vo.setAccommodation(bo.getAccommodation());
        vo.setEnrollmentId(bo.getEnrollmentId());
        return vo;
    }

    private CoursePayOrder toOrder(String orderNo, WxCoursePayOrderDetailVo payVo, String userId) {
        CoursePayOrder order = new CoursePayOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setCourseId(payVo.getCourseId());
        order.setName(payVo.getName());
        order.setGender(payVo.getGender());
        order.setPhone(payVo.getPhone());
        order.setCompany(payVo.getCompany());
        order.setAccommodation(payVo.getAccommodation());
        order.setEnrollmentId(payVo.getEnrollmentId());
        order.setAmount(payVo.getAmount());
        order.setStatus(CoursePayOrder.STATUS_PENDING);
        return order;
    }

    private WxCoursePayOrderDetailVo toDetailVo(CoursePayOrder order) {
        Lectures course = lecturesService.selectLecturesById(order.getCourseId());
        WxCoursePayOrderDetailVo vo = new WxCoursePayOrderDetailVo();
        vo.setOrderNo(order.getOrderNo());
        vo.setCourseId(order.getCourseId());
        vo.setCourseName(course == null ? null : course.getName());
        if (course != null) {
            vo.setCourseTime(course.getTime());
            vo.setCourseEndDate(course.getEndDate());
            vo.setCourseLocation(course.getLocation());
            vo.setCourseCover(course.getCover());
            vo.setCourseCoverId(course.getCoverId());
        }
        vo.setName(order.getName());
        vo.setGender(order.getGender());
        vo.setPhone(order.getPhone());
        vo.setCompany(order.getCompany());
        vo.setAccommodation(order.getAccommodation());
        vo.setEnrollmentId(order.getEnrollmentId());
        vo.setAmount(order.getAmount());
        vo.setStatus(order.getStatus());
        vo.setPayTime(order.getPayTime());
        vo.setSignTime(order.getSignTime());
        vo.setRefundTime(order.getRefundTime());
        vo.setCreateTime(order.getCreateTime());
        return vo;
    }

    private CoursePayOrder loadOwnedCourseOrder(String userId, String orderNo) {
        CoursePayOrder order = coursePayOrderService.selectCoursePayOrderByOrderNo(orderNo);
        if (order == null || !userId.equals(order.getUserId())) {
            throw new ServiceException("订单不存在");
        }
        return order;
    }

    private boolean isPositive(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private Date parseSuccessTime(String successTime) {
        return successTime == null || successTime.isEmpty() ? null
                : DateUtil.parse(successTime, DatePattern.UTC_WITH_XXX_OFFSET_PATTERN);
    }

    private String defaultText(String first, String second) {
        return first == null || first.trim().isEmpty() ? second : first;
    }
}
