package com.ruoyi.wxmini.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.domain.TradeOrder;
import com.ruoyi.system.service.ILecturesService;
import com.ruoyi.system.service.IStudentEnrollmentService;
import com.ruoyi.system.service.ITradeOrderService;
import com.ruoyi.wxmini.bo.WxGrowupCourseEnrollBo;
import com.ruoyi.wxmini.bo.WxPayCreateOrderParam;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.AbsWxPayBaseService;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IWxGrowupPayService;
import com.ruoyi.wxmini.vo.WxGrowupCourseOrderVo;
import com.ruoyi.wxmini.vo.WxPayParamVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class WxGrowupPayServiceImpl extends AbsWxPayBaseService<WxGrowupCourseOrderVo>
        implements IWxGrowupPayService {
    private static final long ORDER_TYPE_LECTURE = 2L;
    private static final long PAY_STATUS_PENDING = 0L;
    private static final long PAY_STATUS_PAID = 1L;
    private static final String ORDER_PREFIX = "GRW";
    private static final String PAY_METHOD_WECHAT = "wechat_pay";
    private static final String PURPOSE_PREFIX = "课程报名：";

    @Resource
    private ILecturesService lecturesService;
    @Resource
    private IStudentEnrollmentService studentEnrollmentService;
    @Resource
    private ITradeOrderService tradeOrderService;
    @Resource
    private IUserInfoService userInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WxPayParamVo createCourseOrder(String userId, Long courseId, WxGrowupCourseEnrollBo bo) throws Exception {
        UserInfo userInfo = requireUser(userId);
        Lectures course = requireCourse(courseId);
        requireDeposit(course);
        decreaseCourseEnrollmentIfRequired(userInfo.getId(), courseId, course);
        WxPayParamVo payParamVo = createOrder(userId, toPayVo(userId, userInfo, course));
        if (payParamVo == null) {
            throw new ServiceException("微信支付订单创建失败");
        }
        return payParamVo;
    }

    @Override
    public boolean handleCoursePaidCallback(WxPayNotifyV3Result result, String requestId) {
        if (result == null || result.getResult() == null) {
            return false;
        }
        return markOrderPaid(result.getResult().getOutTradeNo());
    }

    @Override
    public String getResourceId(WxGrowupCourseOrderVo payVo) {
        return "growup:" + payVo.getUserId() + ":" + payVo.getCourseId() + ":" + UUID.fastUUID();
    }

    @Override
    public Boolean checkBeforeCreatOrder(String userId, WxGrowupCourseOrderVo payVo) {
        return payVo.getCourseId() != null && payVo.getDeposit() != null && payVo.getOpenId() != null;
    }

    @Override
    public Boolean checkUserOrderIsMatch(String userId, String orderNo) {
        TradeOrder order = selectOrderByNo(orderNo);
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        return order != null && userInfo != null && userInfo.getId().equals(order.getUserId());
    }

    @Override
    public WxPayCreateOrderParam buildOrderParam(String userId, WxGrowupCourseOrderVo payVo,
                                                 HashMap<String, Object> contextMap) {
        WxPayCreateOrderParam orderParam = new WxPayCreateOrderParam();
        orderParam.setOrderNo(ORDER_PREFIX + DateUtils.dateTimeNow("yyyyMMddHHmmss") + System.currentTimeMillis());
        orderParam.setOrderDesc(PURPOSE_PREFIX + payVo.getCourseName());
        orderParam.setAmount(toCents(payVo.getDeposit()));
        orderParam.setOpenId(payVo.getOpenId());
        orderParam.setTimeExpire(DateUtil.format(DateUtil.offsetMinute(new Date(), 5),
                DatePattern.UTC_WITH_XXX_OFFSET_PATTERN));
        return orderParam;
    }

    @Override
    public WxGrowupCourseOrderVo buildPayVoWithReCreatOrder(String userId, String orderNo) {
        throw new ServiceException("成长课程订单暂不支持重新支付");
    }

    @Override
    public Boolean saveOrderInfo(String orderNo, WxGrowupCourseOrderVo payVo,
                                 WxPayCreateOrderParam orderParam, HashMap<String, Object> contextMap) {
        TradeOrder order = new TradeOrder();
        order.setOrderNo(orderNo);
        order.setUserId(payVo.getUid());
        order.setOrderType(ORDER_TYPE_LECTURE);
        order.setLectureId(payVo.getCourseId());
        order.setPayAmount(payVo.getDeposit());
        order.setPayMethod(PAY_METHOD_WECHAT);
        order.setPurpose(PURPOSE_PREFIX + payVo.getCourseName());
        order.setPayStatus(PAY_STATUS_PENDING);
        return tradeOrderService.insertTradeOrder(order) > 0;
    }

    @Override
    public Boolean updOrderWithPaySuccess(String orderNo) {
        return markOrderPaid(orderNo);
    }

    @Override
    public Boolean closeOrder(String orderNo) {
        TradeOrder order = selectOrderByNo(orderNo);
        if (order == null) {
            return false;
        }
        order.setPayStatus(3L);
        return tradeOrderService.updateTradeOrder(order) > 0;
    }

    @Override
    protected String getNotifyUrl(WxGrowupCourseOrderVo payVo) {
        return "https://zhiyujia.xyz/api/wxmini/pay/growup/notify";
    }

    private boolean markOrderPaid(String orderNo) {
        TradeOrder order = selectOrderByNo(orderNo);
        if (order == null) {
            return false;
        }
        if (Objects.equals(PAY_STATUS_PAID, order.getPayStatus()) && order.getPayTime() != null) {
            return true;
        }
        order.setPayStatus(PAY_STATUS_PAID);
        order.setPayTime(DateUtils.getNowDate());
        return tradeOrderService.updateTradeOrder(order) > 0;
    }

    private TradeOrder selectOrderByNo(String orderNo) {
        TradeOrder query = new TradeOrder();
        query.setOrderNo(orderNo);
        List<TradeOrder> orders = tradeOrderService.selectTradeOrderList(query);
        return orders == null || orders.isEmpty() ? null : orders.get(0);
    }

    private UserInfo requireUser(String userId) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null) {
            throw new ServiceException("用户不存在");
        }
        if (userInfo.getOpenId() == null || userInfo.getOpenId().isEmpty()) {
            throw new ServiceException("当前用户缺少openId");
        }
        return userInfo;
    }

    private Lectures requireCourse(Long courseId) {
        Lectures course = lecturesService.selectLecturesById(courseId);
        if (course == null) {
            throw new ServiceException("课程不存在");
        }
        return course;
    }

    private void requireDeposit(Lectures course) {
        if (course.getDeposit() == null) {
            throw new ServiceException("课程押金未配置");
        }
    }

    private void decreaseCourseEnrollmentIfRequired(Long uid, Long courseId, Lectures course) {
        if (Boolean.FALSE.equals(course.getRequiresEnrollment())) {
            return;
        }
        studentEnrollmentService.decreaseRemain(uid, courseId, 1);
    }

    private WxGrowupCourseOrderVo toPayVo(String userId, UserInfo userInfo, Lectures course) {
        WxGrowupCourseOrderVo payVo = new WxGrowupCourseOrderVo();
        payVo.setUserId(userId);
        payVo.setUid(userInfo.getId());
        payVo.setCourseId(course.getId());
        payVo.setCourseName(course.getName());
        payVo.setDeposit(course.getDeposit());
        payVo.setOpenId(userInfo.getOpenId());
        return payVo;
    }

    private int toCents(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100")).intValueExact();
    }
}
