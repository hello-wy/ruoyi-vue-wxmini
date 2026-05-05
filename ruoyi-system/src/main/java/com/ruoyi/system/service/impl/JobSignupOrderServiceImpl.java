package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.domain.vo.JobRefundOrderVo;
import com.ruoyi.system.domain.vo.JobScheduleRecordVo;
import com.ruoyi.system.domain.vo.JobSignupUserRecordVo;
import com.ruoyi.system.mapper.JobSignupOrderMapper;
import com.ruoyi.system.service.IJobSignupOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSignupOrderServiceImpl implements IJobSignupOrderService {
    @Autowired
    private JobSignupOrderMapper jobSignupOrderMapper;

    @Override
    public JobSignupOrder selectJobSignupOrderByOrderNo(String orderNo) {
        return jobSignupOrderMapper.selectJobSignupOrderByOrderNo(orderNo);
    }

    @Override
    public List<JobSignupOrder> selectJobSignupOrderList(JobSignupOrder order) {
        return jobSignupOrderMapper.selectJobSignupOrderList(order);
    }

    @Override
    public List<JobSignupOrder> selectMyJobSignupOrders(String userId) {
        return jobSignupOrderMapper.selectMyJobSignupOrders(userId);
    }

    @Override
    public List<JobScheduleRecordVo> selectMyPaidJobSchedules(String userId, Integer paidStatus) {
        return jobSignupOrderMapper.selectMyPaidJobSchedules(userId, paidStatus);
    }

    @Override
    public List<JobSignupUserRecordVo> selectPaidSignupUsersByJobId(Long jobId, Integer paidStatus, String keyword) {
        return jobSignupOrderMapper.selectPaidSignupUsersByJobId(jobId, paidStatus, keyword);
    }

    @Override
    public JobSignupOrder selectLatestPaidOrder(String userId, Long jobId) {
        return jobSignupOrderMapper.selectLatestPaidOrder(userId, jobId);
    }

    @Override
    public JobSignupOrder selectLatestPendingOrder(String userId, Long jobId) {
        return jobSignupOrderMapper.selectLatestPendingOrder(userId, jobId);
    }

    @Override
    public int insertJobSignupOrder(JobSignupOrder order) {
        order.setCreateTime(DateUtils.getNowDate());
        order.setUpdateTime(DateUtils.getNowDate());
        return jobSignupOrderMapper.insertJobSignupOrder(order);
    }

    @Override
    public int updateJobSignupOrder(JobSignupOrder order) {
        order.setUpdateTime(DateUtils.getNowDate());
        return jobSignupOrderMapper.updateJobSignupOrder(order);
    }

    @Override
    public List<JobRefundOrderVo> selectPaidOrdersWithSignIn(Long jobId) {
        return jobSignupOrderMapper.selectPaidOrdersWithSignIn(jobId);
    }
}
