package com.ruoyi.wxmini.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.domain.TutoringBinding;
import com.ruoyi.system.domain.TutoringOrder;
import com.ruoyi.system.domain.TutoringSchedule;
import com.ruoyi.system.mapper.ParentsMapper;
import com.ruoyi.system.mapper.TutoringBindingMapper;
import com.ruoyi.system.mapper.TutoringOrderMapper;
import com.ruoyi.system.mapper.TutoringScheduleMapper;
import com.ruoyi.wxmini.bo.WxPayCreateOrderParam;
import com.ruoyi.wxmini.bo.WxTutoringCreateOrderBo;
import com.ruoyi.wxmini.bo.WxTutoringScheduleActionBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.AbsWxPayBaseService;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IWxMiniTutoringService;
import com.ruoyi.wxmini.vo.WxPayParamVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class WxTutoringPayServiceImpl extends AbsWxPayBaseService<WxTutoringCreateOrderBo> implements IWxMiniTutoringService {

    private static final String ORDER_PREFIX = "TUTOR";
    private static final int BINDING_STATUS_ORDERED = 1;
    private static final int ORDER_STATUS_PENDING = 0;
    private static final int ORDER_STATUS_PAID = 1;
    private static final int ORDER_STATUS_CANCELED = 2;
    private static final int SCHEDULE_STATUS_PENDING = 0;
    private static final int SCHEDULE_STATUS_WAIT_PARENT_CONFIRM = 1;

    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private ParentsMapper parentsMapper;
    @Resource
    private TutoringBindingMapper tutoringBindingMapper;
    @Resource
    private TutoringOrderMapper tutoringOrderMapper;
    @Resource
    private TutoringScheduleMapper tutoringScheduleMapper;
    @Resource
    private WxPayService wxPayService;

    @Override
    public List<TutoringBinding> listAvailableBindings(String wxUserId) {
        UserInfo currentUser = requireCurrentUser(wxUserId);
        return tutoringBindingMapper.selectAvailableBindingsByParentUserId(currentUser.getId());
    }

    @Override
    public WxPayParamVo createOrder(String wxUserId, WxTutoringCreateOrderBo bo) throws Exception {
        UserInfo currentUser = requireCurrentUser(wxUserId);
        if (StringUtils.isBlank(currentUser.getOpenId())) {
            throw new ServiceException("当前用户缺少openId");
        }
        if (bo == null || bo.getBindingId() == null) {
            throw new ServiceException("绑定关系不能为空");
        }
        requireOwnedBinding(bo.getBindingId(), currentUser.getId());
        closeExistingPendingOrder(bo.getBindingId());
        return super.createOrder(wxUserId, bo);
    }

    @Override
    public WxPayParamVo payPendingOrder(String wxUserId, String orderNo) throws Exception {
        UserInfo currentUser = requireCurrentUser(wxUserId);
        if (StringUtils.isBlank(currentUser.getOpenId())) {
            throw new ServiceException("当前用户缺少openId");
        }
        TutoringOrder order = loadOwnedOrder(currentUser.getId(), orderNo);
        if (order.getStatus() == null || order.getStatus() != ORDER_STATUS_PENDING) {
            throw new ServiceException("当前订单状态不可支付");
        }
        if (order.getTotalAmount() == null || order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("订单金额不合法");
        }
        WxPayCreateOrderParam orderParam = new WxPayCreateOrderParam();
        orderParam.setOrderNo(order.getOrderNo());
        orderParam.setOrderDesc("家教课时费");
        orderParam.setAmount(order.getTotalAmount().multiply(new BigDecimal("100")).intValue());
        orderParam.setOpenId(currentUser.getOpenId());
        orderParam.setTimeExpire(DateUtil.format(DateUtil.offsetMinute(new Date(), 5), DatePattern.UTC_WITH_XXX_OFFSET_PATTERN));

        WxPayUnifiedOrderV3Request v3Request = new WxPayUnifiedOrderV3Request();
        v3Request.setAppid(wxPayService.getConfig().getAppId());
        v3Request.setMchid(wxPayService.getConfig().getMchId());
        v3Request.setDescription(orderParam.getOrderDesc());
        v3Request.setOutTradeNo(orderParam.getOrderNo());
        v3Request.setTimeExpire(orderParam.getTimeExpire());
        v3Request.setNotifyUrl(getNotifyUrl(null));
        WxPayUnifiedOrderV3Request.Amount amountObj = new WxPayUnifiedOrderV3Request.Amount();
        amountObj.setTotal(orderParam.getAmount());
        v3Request.setAmount(amountObj);
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid(orderParam.getOpenId());
        v3Request.setPayer(payer);

        WxPayUnifiedOrderV3Result.JsapiResult jsapiResult = wxPayService.createOrderV3(TradeTypeEnum.JSAPI, v3Request);
        order.setWechatOpenId(currentUser.getOpenId());
        order.setUpdateBy(wxUserId);
        order.setUpdateTime(DateUtils.getNowDate());
        tutoringOrderMapper.updateTutoringOrder(order);

        WxPayParamVo payParamVo = new WxPayParamVo();
        payParamVo.setOrderNo(order.getOrderNo());
        payParamVo.setPayParam(jsapiResult);
        return payParamVo;
    }

    @Override
    public List<TutoringOrder> listMyOrders(String wxUserId) {
        UserInfo currentUser = requireCurrentUser(wxUserId);
        return tutoringOrderMapper.selectMyOrders(currentUser.getId());
    }

    @Override
    public TutoringOrder getOrderDetail(String wxUserId, String orderNo) {
        UserInfo currentUser = requireCurrentUser(wxUserId);
        TutoringOrder order = loadOwnedOrder(currentUser.getId(), orderNo);
        return compensatePendingOrder(order);
    }

    @Override
    public List<TutoringSchedule> listMySchedules(String wxUserId) {
        UserInfo currentUser = requireCurrentUser(wxUserId);
        Long parentUserId = isParentUser(currentUser) ? currentUser.getId() : null;
        Long tutorUserId = isParentUser(currentUser) ? null : currentUser.getId();
        return tutoringScheduleMapper.selectMySchedules(parentUserId, tutorUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishSchedule(String wxUserId, Long scheduleId, String remark) {
        UserInfo currentUser = requireCurrentUser(wxUserId);
        TutoringSchedule schedule = tutoringScheduleMapper.selectByIdForUpdate(scheduleId);
        if (schedule == null) {
            throw new ServiceException("课表不存在");
        }
        if (!currentUser.getId().equals(schedule.getTutorUserId())) {
            throw new ServiceException("无权操作该课表");
        }
        if (schedule.getStatus() == null || schedule.getStatus() != SCHEDULE_STATUS_PENDING) {
            throw new ServiceException("当前课表状态不可签到");
        }
        schedule.setStatus(SCHEDULE_STATUS_WAIT_PARENT_CONFIRM);
        schedule.setFinishTime(DateUtils.getNowDate());
        schedule.setFinishRemark(StringUtils.defaultString(remark));
        schedule.setUpdateBy(wxUserId);
        schedule.setUpdateTime(DateUtils.getNowDate());
        tutoringScheduleMapper.updateTutoringSchedule(schedule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmSchedule(String wxUserId, Long scheduleId, String remark) {
        UserInfo currentUser = requireCurrentUser(wxUserId);
        TutoringSchedule schedule = tutoringScheduleMapper.selectByIdForUpdate(scheduleId);
        if (schedule == null) {
            throw new ServiceException("课表不存在");
        }
        if (!currentUser.getId().equals(schedule.getParentUserId())) {
            throw new ServiceException("无权操作该课表");
        }
        if (schedule.getStatus() == null || schedule.getStatus() != SCHEDULE_STATUS_WAIT_PARENT_CONFIRM) {
            throw new ServiceException("当前课表状态不可确认");
        }
        if (schedule.getFinishTime() == null) {
            throw new ServiceException("学生尚未上课签到");
        }
        if (schedule.getConfirmTime() != null) {
            throw new ServiceException("家长已提交确认，等待管理员审核");
        }
        schedule.setConfirmTime(DateUtils.getNowDate());
        schedule.setConfirmRemark(StringUtils.defaultString(remark));
        schedule.setUpdateBy(wxUserId);
        schedule.setUpdateTime(DateUtils.getNowDate());
        tutoringScheduleMapper.updateTutoringSchedule(schedule);
    }

    @Override
    public boolean handleTutoringPaidCallback(WxPayNotifyV3Result result, String requestId) {
        if (result == null || result.getResult() == null || StringUtils.isBlank(result.getResult().getOutTradeNo())) {
            return false;
        }
        Date successTime = parseSuccessTime(result.getResult().getSuccessTime());
        if (successTime == null) {
            successTime = DateUtils.getNowDate();
        }
        return finalizePaidOrder(result.getResult().getOutTradeNo(), result.getResult().getTransactionId(), requestId, successTime);
    }

    @Override
    public String getResourceId(WxTutoringCreateOrderBo bo) {
        return "tutoring:" + (bo == null ? "unknown" : bo.getBindingId()) + ":" + UUID.fastUUID();
    }

    @Override
    public Boolean checkBeforeCreatOrder(String userId, WxTutoringCreateOrderBo bo) {
        return bo != null && bo.getBindingId() != null;
    }

    @Override
    public Boolean checkUserOrderIsMatch(String userId, String orderNo) {
        UserInfo currentUser = userInfoService.selectUserInfoByUserId(userId);
        if (currentUser == null || currentUser.getId() == null) {
            return false;
        }
        TutoringOrder order = tutoringOrderMapper.selectByOrderNo(orderNo);
        return order != null && currentUser.getId().equals(order.getParentUserId());
    }

    @Override
    public WxPayCreateOrderParam buildOrderParam(String userId, WxTutoringCreateOrderBo bo, HashMap<String, Object> contextMap) {
        UserInfo currentUser = requireCurrentUser(userId);
        TutoringBinding binding = requireOwnedBinding(bo.getBindingId(), currentUser.getId());
        Parents parent = parentsMapper.selectParentsById(binding.getParentId());
        if (parent == null) {
            throw new ServiceException("家长需求不存在");
        }
        if (parent.getHourlyBudget() == null || parent.getHourlyBudget().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("需求时薪预算无效");
        }
        String snapshot = resolveOrderServiceTimes(bo, binding, parent);
        JSONArray slots = parseScheduleSnapshot(snapshot);
        int lessonCount = slots.size();
        BigDecimal totalAmount = calculateTotalAmount(parent.getHourlyBudget(), slots);
        BigDecimal commissionRate = defaultCommissionRate();

        WxPayCreateOrderParam orderParam = new WxPayCreateOrderParam();
        orderParam.setOrderNo(ORDER_PREFIX + DateUtils.dateTimeNow("yyyyMMddHHmmss") + System.currentTimeMillis());
        orderParam.setOrderDesc(buildOrderDesc(parent));
        orderParam.setAmount(totalAmount.multiply(new BigDecimal("100")).intValue());
        orderParam.setOpenId(currentUser.getOpenId());
        orderParam.setTimeExpire(DateUtil.format(DateUtil.offsetMinute(new Date(), 5), DatePattern.UTC_WITH_XXX_OFFSET_PATTERN));

        contextMap.put("binding", binding);
        contextMap.put("parent", parent);
        contextMap.put("currentUser", currentUser);
        contextMap.put("serviceTimesSnapshot", snapshot);
        contextMap.put("lessonCount", lessonCount);
        contextMap.put("hourlyPrice", parent.getHourlyBudget().setScale(2, RoundingMode.HALF_UP));
        contextMap.put("totalAmount", totalAmount);
        contextMap.put("commissionRate", commissionRate);
        return orderParam;
    }

    @Override
    public WxTutoringCreateOrderBo buildPayVoWithReCreatOrder(String userId, String orderNo) {
        TutoringOrder order = tutoringOrderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return null;
        }
        WxTutoringCreateOrderBo bo = new WxTutoringCreateOrderBo();
        bo.setBindingId(order.getBindingId());
        return bo;
    }

    @Override
    public Boolean saveOrderInfo(String orderNo, WxTutoringCreateOrderBo bo, WxPayCreateOrderParam orderParam, HashMap<String, Object> contextMap) {
        TutoringBinding binding = (TutoringBinding) contextMap.get("binding");
        Parents parent = (Parents) contextMap.get("parent");
        UserInfo currentUser = (UserInfo) contextMap.get("currentUser");
        TutoringOrder order = new TutoringOrder();
        order.setId(SnowflakeIdWorker.nextIdDefault());
        order.setOrderNo(orderNo);
        order.setBindingId(binding.getId());
        order.setParentId(binding.getParentId());
        order.setParentUserId(binding.getParentUserId());
        order.setTutorId(binding.getTutorId());
        order.setTutorUserId(binding.getTutorUserId());
        order.setWechatOpenId(currentUser.getOpenId());
        order.setServiceTimesSnapshot((String) contextMap.get("serviceTimesSnapshot"));
        order.setLessonCount((Integer) contextMap.get("lessonCount"));
        order.setHourlyPrice((BigDecimal) contextMap.get("hourlyPrice"));
        order.setTotalAmount((BigDecimal) contextMap.get("totalAmount"));
        order.setCommissionRate((BigDecimal) contextMap.get("commissionRate"));
        order.setStatus(ORDER_STATUS_PENDING);
        order.setCreateBy(parent.getWechatUid());
        order.setCreateTime(DateUtils.getNowDate());
        order.setUpdateBy(parent.getWechatUid());
        order.setUpdateTime(DateUtils.getNowDate());
        return tutoringOrderMapper.insertTutoringOrder(order) > 0;
    }

    @Override
    public Boolean updOrderWithPaySuccess(String orderNo) {
        return finalizePaidOrder(orderNo, null, null, resolvePaidTime(orderNo));
    }

    @Override
    public Boolean closeOrder(String orderNo) {
        TutoringOrder order = tutoringOrderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return false;
        }
        if (order.getStatus() != null && order.getStatus() == ORDER_STATUS_PAID) {
            return true;
        }
        order.setStatus(ORDER_STATUS_CANCELED);
        order.setUpdateTime(DateUtils.getNowDate());
        return tutoringOrderMapper.updateTutoringOrder(order) > 0;
    }

    @Override
    protected String getNotifyUrl(WxTutoringCreateOrderBo bo) {
        return "https://zhiyujia.xyz/api/wxmini/pay/tutoring/notify";
    }

    @Transactional(rollbackFor = Exception.class)
    protected boolean finalizePaidOrder(String orderNo, String transactionId, String requestId, Date payTime) {
        TutoringOrder order = tutoringOrderMapper.selectByOrderNoForUpdate(orderNo);
        if (order == null) {
            return false;
        }
        if (order.getStatus() != null && order.getStatus() == ORDER_STATUS_PAID) {
            createSchedulesForPaidOrder(order, StringUtils.defaultIfBlank(order.getUpdateBy(), "system"));
            return true;
        }
        order.setStatus(ORDER_STATUS_PAID);
        if (StringUtils.isNotBlank(transactionId)) {
            order.setWechatTransactionId(transactionId);
        }
        if (StringUtils.isNotBlank(requestId)) {
            order.setRequestId(requestId);
        }
        order.setPayTime(payTime == null ? DateUtils.getNowDate() : payTime);
        order.setUpdateBy(StringUtils.defaultIfBlank(order.getUpdateBy(), "system"));
        order.setUpdateTime(DateUtils.getNowDate());
        tutoringOrderMapper.updateTutoringOrder(order);

        TutoringBinding binding = tutoringBindingMapper.selectByIdForUpdate(order.getBindingId());
        if (binding != null) {
            binding.setStatus(BINDING_STATUS_ORDERED);
            binding.setUpdateBy(StringUtils.defaultIfBlank(binding.getUpdateBy(), "system"));
            binding.setUpdateTime(DateUtils.getNowDate());
            tutoringBindingMapper.updateTutoringBinding(binding);
        }
        createSchedulesForPaidOrder(order, StringUtils.defaultIfBlank(order.getUpdateBy(), "system"));
        return true;
    }

    private void closeExistingPendingOrder(Long bindingId) {
        TutoringOrder latestOrder = tutoringOrderMapper.selectLatestByBindingId(bindingId);
        if (latestOrder == null || latestOrder.getStatus() == null) {
            return;
        }
        if (latestOrder.getStatus() != ORDER_STATUS_PENDING) {
            return;
        }
        try {
            wxPayService.closeOrderV3(latestOrder.getOrderNo());
        } catch (Exception ignored) {
        }
        latestOrder.setStatus(ORDER_STATUS_CANCELED);
        latestOrder.setUpdateTime(DateUtils.getNowDate());
        tutoringOrderMapper.updateTutoringOrder(latestOrder);
    }

    private TutoringOrder loadOwnedOrder(Long parentUserId, String orderNo) {
        TutoringOrder order = tutoringOrderMapper.selectByOrderNo(orderNo);
        if (order == null || !parentUserId.equals(order.getParentUserId())) {
            throw new ServiceException("订单不存在");
        }
        return order;
    }

    private TutoringOrder compensatePendingOrder(TutoringOrder order) {
        if (order == null || order.getStatus() == null || order.getStatus() != ORDER_STATUS_PENDING) {
            return order;
        }
        try {
            queryPayResultAndUpdOrderStatus(order.getOrderNo());
        } catch (Exception e) {
            throw new ServiceException("同步支付状态失败");
        }
        return tutoringOrderMapper.selectByOrderNo(order.getOrderNo());
    }

    private TutoringBinding requireOwnedBinding(Long bindingId, Long parentUserId) {
        TutoringBinding binding = tutoringBindingMapper.selectById(bindingId);
        if (binding == null || !parentUserId.equals(binding.getParentUserId())) {
            throw new ServiceException("绑定关系不存在");
        }
        return binding;
    }

    private String resolveOrderServiceTimes(WxTutoringCreateOrderBo bo, TutoringBinding binding, Parents parent) {
        if (StringUtils.isNotBlank(bo.getServiceTimes())) {
            return bo.getServiceTimes();
        }
        return StringUtils.defaultIfBlank(binding.getServiceTimesSnapshot(), parent.getServiceTimes());
    }

    private UserInfo requireCurrentUser(String wxUserId) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(wxUserId);
        if (userInfo == null || userInfo.getId() == null) {
            throw new ServiceException("用户不存在");
        }
        return userInfo;
    }

    private boolean isParentUser(UserInfo userInfo) {
        return userInfo != null && userInfo.getUserType() != null && userInfo.getUserType() == 0;
    }

    private JSONArray parseScheduleSnapshot(String snapshot) {
        JSONArray array = JSON.parseArray(StringUtils.defaultString(snapshot));
        if (array == null || array.isEmpty()) {
            throw new ServiceException("服务时段不能为空");
        }
        return array;
    }

    private BigDecimal calculateTotalAmount(BigDecimal hourlyPrice, JSONArray slots) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < slots.size(); i++) {
            JSONObject slot = slots.getJSONObject(i);
            LocalTime startTime = LocalTime.parse(slot.getString("startTime"));
            LocalTime endTime = LocalTime.parse(slot.getString("endTime"));
            BigDecimal hours = BigDecimal.valueOf(Duration.between(startTime, endTime).toMinutes())
                    .divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP);
            if (hours.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ServiceException("服务时段不合法");
            }
            total = total.add(hourlyPrice.multiply(hours).setScale(2, RoundingMode.HALF_UP));
        }
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("订单金额不合法");
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private String buildOrderDesc(Parents parent) {
        String subject = StringUtils.defaultIfBlank(parent.getSubject(), "家教");
        return "家教课时费：" + subject;
    }

    private BigDecimal defaultCommissionRate() {
        return new BigDecimal("10.00");
    }

    private Date resolvePaidTime(String orderNo) {
        try {
            WxPayOrderQueryV3Request request = new WxPayOrderQueryV3Request();
            request.setOutTradeNo(orderNo);
            WxPayOrderQueryV3Result result = wxPayService.queryOrderV3(request);
            if (result != null && "SUCCESS".equals(result.getTradeState())) {
                Date successTime = parseSuccessTime(result.getSuccessTime());
                if (successTime != null) {
                    return successTime;
                }
            }
        } catch (Exception ignored) {
        }
        return DateUtils.getNowDate();
    }

    private Date parseSuccessTime(String successTime) {
        if (StringUtils.isBlank(successTime)) {
            return null;
        }
        return DateUtil.parse(successTime, DatePattern.UTC_WITH_XXX_OFFSET_PATTERN);
    }

    private void createSchedulesForPaidOrder(TutoringOrder order, String operator) {
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        if (tutoringScheduleMapper.countByOrderId(order.getId()) > 0) {
            return;
        }
        JSONArray array = parseScheduleSnapshot(order.getServiceTimesSnapshot());
        List<TutoringSchedule> items = new ArrayList<>();
        BigDecimal commissionRate = order.getCommissionRate() == null ? defaultCommissionRate() : order.getCommissionRate();
        for (int i = 0; i < array.size(); i++) {
            JSONObject slot = array.getJSONObject(i);
            LocalDate serviceDate = LocalDate.parse(slot.getString("serviceDate"));
            LocalTime startTime = LocalTime.parse(slot.getString("startTime"));
            LocalTime endTime = LocalTime.parse(slot.getString("endTime"));
            BigDecimal hours = BigDecimal.valueOf(Duration.between(startTime, endTime).toMinutes())
                    .divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP);
            BigDecimal grossAmount = order.getHourlyPrice().multiply(hours).setScale(2, RoundingMode.HALF_UP);
            BigDecimal commissionAmount = grossAmount.multiply(commissionRate)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal netAmount = grossAmount.subtract(commissionAmount);
            TutoringSchedule schedule = new TutoringSchedule();
            schedule.setId(SnowflakeIdWorker.nextIdDefault());
            schedule.setOrderId(order.getId());
            schedule.setOrderNo(order.getOrderNo());
            schedule.setBindingId(order.getBindingId());
            schedule.setParentId(order.getParentId());
            schedule.setParentUserId(order.getParentUserId());
            schedule.setTutorId(order.getTutorId());
            schedule.setTutorUserId(order.getTutorUserId());
            schedule.setServiceDate(Date.from(serviceDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            schedule.setStartTime(startTime.toString());
            schedule.setEndTime(endTime.toString());
            schedule.setHours(hours);
            schedule.setGrossAmount(grossAmount);
            schedule.setCommissionAmount(commissionAmount);
            schedule.setNetAmount(netAmount);
            schedule.setStatus(SCHEDULE_STATUS_PENDING);
            schedule.setCreateBy(operator);
            schedule.setCreateTime(DateUtils.getNowDate());
            schedule.setUpdateBy(operator);
            schedule.setUpdateTime(DateUtils.getNowDate());
            items.add(schedule);
        }
        tutoringScheduleMapper.batchInsertTutoringSchedules(items);
    }
}
