package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.JobSignupOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobSignupOrderMapper {
    JobSignupOrder selectJobSignupOrderByOrderNo(String orderNo);

    List<JobSignupOrder> selectJobSignupOrderList(JobSignupOrder order);

    List<JobSignupOrder> selectMyJobSignupOrders(@Param("userId") String userId);

    JobSignupOrder selectLatestPaidOrder(@Param("userId") String userId, @Param("jobId") Long jobId);

    JobSignupOrder selectLatestPendingOrder(@Param("userId") String userId, @Param("jobId") Long jobId);

    int insertJobSignupOrder(JobSignupOrder order);

    int updateJobSignupOrder(JobSignupOrder order);
}