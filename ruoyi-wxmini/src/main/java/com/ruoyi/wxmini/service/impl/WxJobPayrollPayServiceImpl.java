package com.ruoyi.wxmini.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.domain.JobPayrollBatch;
import com.ruoyi.system.domain.JobPayrollItem;
import com.ruoyi.system.domain.vo.JobSignupUserRecordVo;
import com.ruoyi.system.mapper.JobPayrollBatchMapper;
import com.ruoyi.system.mapper.JobPayrollItemMapper;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.IWalletService;
import com.ruoyi.wxmini.bo.WxJobPayrollCreateOrderBo;
import com.ruoyi.wxmini.bo.WxPayrollEmployeeBo;
import com.ruoyi.wxmini.bo.WxPayCreateOrderParam;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.AbsWxPayBaseService;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IWxJobPayrollPayService;
import com.ruoyi.wxmini.vo.WxJobPayrollItemVo;
import com.ruoyi.wxmini.vo.WxJobPayrollOrderDetailVo;
import com.ruoyi.wxmini.vo.WxPayParamVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WxJobPayrollPayServiceImpl extends AbsWxPayBaseService<WxJobPayrollOrderDetailVo> implements IWxJobPayrollPayService {

    private static final String ORDER_PREFIX = "PAYROLL";
    private static final Integer BATCH_STATUS_PENDING = 0;
    private static final Integer BATCH_STATUS_PAID = 1;
    private static final Integer BATCH_STATUS_CLOSED = 2;
    private static final Integer ITEM_STATUS_PENDING = 0;
    private static final Integer ITEM_STATUS_CREDITED = 1;
    private static final Integer JOB_SIGNUP_PAID = 1;

    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private com.ruoyi.system.service.IDailyJobsService dailyJobsService;
    @Resource
    private IJobSignupOrderService jobSignupOrderService;
    @Resource
    private JobPayrollBatchMapper jobPayrollBatchMapper;
    @Resource
    private JobPayrollItemMapper jobPayrollItemMapper;
    @Resource
    private IWalletService walletService;
    @Resource
    private WxPayService wxPayService;

    @Override
    public WxPayParamVo createPayrollOrder(String userId, WxJobPayrollCreateOrderBo bo) throws Exception {
        UserInfo merchant = requireCurrentUser(userId);
        DailyJobs job = requireOwnedJob(merchant, bo.getJobId());
        if (bo.getEmployees() == null || bo.getEmployees().isEmpty()) {
            throw new ServiceException("请选择发薪员工");
        }
        List<JobSignupUserRecordVo> signupUsers = jobSignupOrderService.selectPaidSignupUsersByJobId(job.getId(), JOB_SIGNUP_PAID, null);
        Map<Long, JobSignupUserRecordVo> signupUserMap = signupUsers.stream()
                .collect(Collectors.toMap(JobSignupUserRecordVo::getUserInfoId, item -> item, (a, b) -> a));

        List<WxJobPayrollItemVo> itemVos = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (WxPayrollEmployeeBo employeeBo : bo.getEmployees()) {
            JobSignupUserRecordVo signupUser = signupUserMap.get(employeeBo.getEmployeeUserId());
            if (signupUser == null) {
                throw new ServiceException("存在未报名该岗位的员工，无法发薪");
            }
            BigDecimal hours = parsePositiveDecimal(employeeBo.getHours(), "工时格式不正确");
            BigDecimal hourlyRate = parsePositiveDecimal(employeeBo.getHourlyRate(), "时薪格式不正确");
            BigDecimal amount = hours.multiply(hourlyRate).setScale(2, BigDecimal.ROUND_HALF_UP);
            WxJobPayrollItemVo itemVo = new WxJobPayrollItemVo();
            itemVo.setEmployeeUserId(employeeBo.getEmployeeUserId());
            itemVo.setEmployeeName(signupUser.getDisplayName());
            itemVo.setPhoneMasked(signupUser.getPhoneMasked());
            itemVo.setHours(hours);
            itemVo.setHourlyRate(hourlyRate);
            itemVo.setAmount(amount);
            itemVo.setStatus(ITEM_STATUS_PENDING);
            itemVos.add(itemVo);
            totalAmount = totalAmount.add(amount);
        }

        UserInfo merchantUser = userInfoService.selectUserInfoByUserId(userId);
        if (merchantUser == null || StringUtils.isBlank(merchantUser.getOpenId())) {
            throw new ServiceException("当前用户缺少openId");
        }
        WxJobPayrollOrderDetailVo payVo = new WxJobPayrollOrderDetailVo();
        payVo.setJobId(job.getId());
        payVo.setJobTitle("兼职工资：" + job.getTitle());
        payVo.setTotalAmount(totalAmount);
        payVo.setStatus(BATCH_STATUS_PENDING);
        payVo.setItems(itemVos);
        return createOrder(userId, payVo);
    }

    @Override
    public WxJobPayrollOrderDetailVo queryPayrollOrder(String userId, String orderNo) {
        UserInfo merchant = requireCurrentUser(userId);
        JobPayrollBatch batch = loadOwnedBatch(merchant.getId(), orderNo);
        batch = compensatePendingBatch(batch);
        return toDetailVo(batch);
    }

    @Override
    public boolean handlePayrollPaidCallback(WxPayNotifyV3Result result, String requestId) {
        if (result == null || result.getResult() == null) {
            return false;
        }
        String orderNo = result.getResult().getOutTradeNo();
        JobPayrollBatch batch = jobPayrollBatchMapper.selectJobPayrollBatchByOrderNo(orderNo);
        if (batch == null) {
            return false;
        }
        if (BATCH_STATUS_PAID == batch.getStatus()) {
            return true;
        }
        Date successTime = parseSuccessTime(result.getResult().getSuccessTime());
        if (successTime == null) {
            successTime = DateUtils.getNowDate();
        }
        return finalizePaidBatch(orderNo, result.getResult().getTransactionId(), requestId, successTime);
    }

    @Override
    public String getResourceId(WxJobPayrollOrderDetailVo payVo) {
        return "payroll:" + payVo.getJobId() + ":" + UUID.fastUUID();
    }

    @Override
    public Boolean checkBeforeCreatOrder(String userId, WxJobPayrollOrderDetailVo payVo) {
        return payVo.getJobId() != null && payVo.getTotalAmount() != null && payVo.getTotalAmount().compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public Boolean checkUserOrderIsMatch(String userId, String orderNo) {
        UserInfo merchant = userInfoService.selectUserInfoByUserId(userId);
        if (merchant == null) {
            return false;
        }
        JobPayrollBatch batch = jobPayrollBatchMapper.selectJobPayrollBatchByOrderNo(orderNo);
        return batch != null && merchant.getId().equals(batch.getMerchantUid());
    }

    @Override
    public WxPayCreateOrderParam buildOrderParam(String userId, WxJobPayrollOrderDetailVo payVo, HashMap<String, Object> contextMap) {
        UserInfo merchant = userInfoService.selectUserInfoByUserId(userId);
        if (merchant == null || StringUtils.isBlank(merchant.getOpenId())) {
            throw new ServiceException("当前用户缺少openId");
        }
        WxPayCreateOrderParam orderParam = new WxPayCreateOrderParam();
        orderParam.setOrderNo(ORDER_PREFIX + DateUtils.dateTimeNow("yyyyMMddHHmmss") + System.currentTimeMillis());
        orderParam.setOrderDesc(payVo.getJobTitle());
        orderParam.setAmount(payVo.getTotalAmount().multiply(new BigDecimal("100")).intValue());
        orderParam.setOpenId(merchant.getOpenId());
        orderParam.setTimeExpire(DateUtil.format(DateUtil.offsetMinute(new Date(), 5), DatePattern.UTC_WITH_XXX_OFFSET_PATTERN));
        contextMap.put("jobId", payVo.getJobId());
        contextMap.put("merchantUid", merchant.getId());
        contextMap.put("items", payVo.getItems());
        return orderParam;
    }

    @Override
    public WxJobPayrollOrderDetailVo buildPayVoWithReCreatOrder(String userId, String orderNo) {
        JobPayrollBatch batch = jobPayrollBatchMapper.selectJobPayrollBatchByOrderNo(orderNo);
        if (batch == null) {
            return null;
        }
        return toDetailVo(batch);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Boolean saveOrderInfo(String orderNo, WxJobPayrollOrderDetailVo payVo, WxPayCreateOrderParam orderParam, HashMap<String, Object> contextMap) {
        JobPayrollBatch batch = new JobPayrollBatch();
        batch.setId(SnowflakeIdWorker.nextIdDefault());
        batch.setOrderNo(orderNo);
        batch.setJobId((Long) contextMap.get("jobId"));
        batch.setMerchantUid((Long) contextMap.get("merchantUid"));
        batch.setTotalAmount(payVo.getTotalAmount());
        batch.setStatus(BATCH_STATUS_PENDING);
        batch.setCreateTime(DateUtils.getNowDate());
        batch.setUpdateTime(DateUtils.getNowDate());
        jobPayrollBatchMapper.insertJobPayrollBatch(batch);

        List<WxJobPayrollItemVo> itemVos = (List<WxJobPayrollItemVo>) contextMap.get("items");
        List<JobPayrollItem> items = new ArrayList<>();
        for (WxJobPayrollItemVo itemVo : itemVos) {
            JobPayrollItem item = new JobPayrollItem();
            item.setId(SnowflakeIdWorker.nextIdDefault());
            item.setBatchId(batch.getId());
            item.setJobId(batch.getJobId());
            item.setEmployeeUserId(itemVo.getEmployeeUserId());
            item.setHours(itemVo.getHours());
            item.setHourlyRate(itemVo.getHourlyRate());
            item.setAmount(itemVo.getAmount());
            item.setStatus(ITEM_STATUS_PENDING);
            item.setCreateTime(DateUtils.getNowDate());
            item.setUpdateTime(DateUtils.getNowDate());
            items.add(item);
        }
        return jobPayrollItemMapper.batchInsertJobPayrollItems(items) > 0;
    }

    @Override
    public Boolean updOrderWithPaySuccess(String orderNo) {
        Date payTime = resolvePaidTime(orderNo);
        return finalizePaidBatch(orderNo, null, null, payTime);
    }

    @Override
    public Boolean closeOrder(String orderNo) {
        JobPayrollBatch batch = jobPayrollBatchMapper.selectJobPayrollBatchByOrderNo(orderNo);
        if (batch == null) {
            return false;
        }
        batch.setStatus(BATCH_STATUS_CLOSED);
        batch.setUpdateTime(DateUtils.getNowDate());
        return jobPayrollBatchMapper.updateJobPayrollBatch(batch) > 0;
    }

    @Override
    protected String getNotifyUrl(WxJobPayrollOrderDetailVo payVo) {
        return "https://zhiyujia.xyz/api/wxmini/pay/payroll/notify";
    }

    @Transactional(rollbackFor = Exception.class)
    protected boolean finalizePaidBatch(String orderNo, String transactionId, String requestId, Date payTime) {
        JobPayrollBatch batch = jobPayrollBatchMapper.selectJobPayrollBatchByOrderNoForUpdate(orderNo);
        if (batch == null) {
            return false;
        }
        if (BATCH_STATUS_PAID == batch.getStatus()) {
            return true;
        }
        batch.setStatus(BATCH_STATUS_PAID);
        if (StringUtils.isNotBlank(transactionId)) {
            batch.setWechatTransactionId(transactionId);
        }
        if (StringUtils.isNotBlank(requestId)) {
            batch.setRequestId(requestId);
        }
        batch.setPayTime(payTime == null ? DateUtils.getNowDate() : payTime);
        batch.setUpdateTime(DateUtils.getNowDate());
        jobPayrollBatchMapper.updateJobPayrollBatch(batch);

        List<JobPayrollItem> items = jobPayrollItemMapper.selectJobPayrollItemsByBatchId(batch.getId());
        for (JobPayrollItem item : items) {
            if (ITEM_STATUS_CREDITED == item.getStatus()) {
                continue;
            }
            walletService.creditPayroll(item.getEmployeeUserId(), item.getAmount(), batch.getOrderNo(), "兼职工资入账");
            item.setStatus(ITEM_STATUS_CREDITED);
            item.setUpdateTime(DateUtils.getNowDate());
            jobPayrollItemMapper.updateJobPayrollItem(item);
        }
        return true;
    }

    private UserInfo requireCurrentUser(String userId) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null || userInfo.getId() == null) {
            throw new ServiceException("用户不存在");
        }
        return userInfo;
    }

    private DailyJobs requireOwnedJob(UserInfo merchant, Long jobId) {
        DailyJobs job = dailyJobsService.selectDailyJobsById(jobId);
        if (job == null) {
            throw new ServiceException("岗位不存在");
        }
        if (!merchant.getId().equals(job.getPublisherUid())) {
            throw new ServiceException("仅岗位发布商家可发工资");
        }
        return job;
    }

    private JobPayrollBatch loadOwnedBatch(Long merchantUid, String orderNo) {
        JobPayrollBatch batch = jobPayrollBatchMapper.selectJobPayrollBatchByOrderNo(orderNo);
        if (batch == null || !merchantUid.equals(batch.getMerchantUid())) {
            throw new ServiceException("工资订单不存在");
        }
        return batch;
    }

    private JobPayrollBatch compensatePendingBatch(JobPayrollBatch batch) {
        if (batch == null || batch.getStatus() != BATCH_STATUS_PENDING) {
            return batch;
        }
        try {
            queryPayResultAndUpdOrderStatus(batch.getOrderNo());
        } catch (Exception e) {
            throw new ServiceException("同步支付状态失败");
        }
        return jobPayrollBatchMapper.selectJobPayrollBatchByOrderNo(batch.getOrderNo());
    }

    private WxJobPayrollOrderDetailVo toDetailVo(JobPayrollBatch batch) {
        DailyJobs job = dailyJobsService.selectDailyJobsById(batch.getJobId());
        List<JobPayrollItem> items = jobPayrollItemMapper.selectJobPayrollItemsByBatchId(batch.getId());
        List<JobSignupUserRecordVo> signupUsers = jobSignupOrderService.selectPaidSignupUsersByJobId(batch.getJobId(), JOB_SIGNUP_PAID, null);
        Map<Long, JobSignupUserRecordVo> userMap = signupUsers.stream().collect(Collectors.toMap(JobSignupUserRecordVo::getUserInfoId, item -> item, (a, b) -> a));

        WxJobPayrollOrderDetailVo vo = new WxJobPayrollOrderDetailVo();
        vo.setOrderNo(batch.getOrderNo());
        vo.setJobId(batch.getJobId());
        vo.setJobTitle(job == null ? null : job.getTitle());
        vo.setTotalAmount(batch.getTotalAmount());
        vo.setStatus(batch.getStatus());
        vo.setPayTime(batch.getPayTime());
        vo.setCreateTime(batch.getCreateTime());
        List<WxJobPayrollItemVo> itemVos = new ArrayList<>();
        for (JobPayrollItem item : items) {
            WxJobPayrollItemVo itemVo = new WxJobPayrollItemVo();
            itemVo.setEmployeeUserId(item.getEmployeeUserId());
            JobSignupUserRecordVo userRecord = userMap.get(item.getEmployeeUserId());
            itemVo.setEmployeeName(userRecord == null ? null : userRecord.getDisplayName());
            itemVo.setPhoneMasked(userRecord == null ? null : userRecord.getPhoneMasked());
            itemVo.setHours(item.getHours());
            itemVo.setHourlyRate(item.getHourlyRate());
            itemVo.setAmount(item.getAmount());
            itemVo.setStatus(item.getStatus());
            itemVos.add(itemVo);
        }
        vo.setItems(itemVos);
        return vo;
    }

    private BigDecimal parsePositiveDecimal(String text, String message) {
        try {
            BigDecimal value = new BigDecimal(text);
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ServiceException(message);
            }
            return value.setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (NumberFormatException ex) {
            throw new ServiceException(message);
        }
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
}
