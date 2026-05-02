package com.ruoyi.system.service;

import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.domain.vo.JobScheduleRecordVo;
import com.ruoyi.system.domain.vo.JobSignupUserRecordVo;

import java.util.List;

public interface IJobSignupOrderService {
    JobSignupOrder selectJobSignupOrderByOrderNo(String orderNo);

    List<JobSignupOrder> selectJobSignupOrderList(JobSignupOrder order);

    List<JobSignupOrder> selectMyJobSignupOrders(String userId);

    List<JobScheduleRecordVo> selectMyPaidJobSchedules(String userId, Integer paidStatus);

    List<JobSignupUserRecordVo> selectPaidSignupUsersByJobId(Long jobId, Integer paidStatus, String keyword);

    JobSignupOrder selectLatestPaidOrder(String userId, Long jobId);

    JobSignupOrder selectLatestPendingOrder(String userId, Long jobId);

    int insertJobSignupOrder(JobSignupOrder order);

    int updateJobSignupOrder(JobSignupOrder order);
}
