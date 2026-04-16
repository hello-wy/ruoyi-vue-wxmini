package com.ruoyi.system.service;

import com.ruoyi.system.domain.JobSignupOrder;

import java.util.List;

public interface IJobSignupOrderService {
    JobSignupOrder selectJobSignupOrderByOrderNo(String orderNo);

    List<JobSignupOrder> selectJobSignupOrderList(JobSignupOrder order);

    List<JobSignupOrder> selectMyJobSignupOrders(String userId);

    JobSignupOrder selectLatestPaidOrder(String userId, Long jobId);

    JobSignupOrder selectLatestPendingOrder(String userId, Long jobId);

    int insertJobSignupOrder(JobSignupOrder order);

    int updateJobSignupOrder(JobSignupOrder order);
}