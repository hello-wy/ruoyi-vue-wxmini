package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.domain.vo.JobRefundOrderVo;
import com.ruoyi.system.domain.vo.JobScheduleRecordVo;
import com.ruoyi.system.domain.vo.JobSignupUserRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobSignupOrderMapper {
    JobSignupOrder selectJobSignupOrderByOrderNo(String orderNo);

    List<JobSignupOrder> selectJobSignupOrderList(JobSignupOrder order);

    List<JobSignupOrder> selectMyJobSignupOrders(@Param("userId") String userId);

    List<JobScheduleRecordVo> selectMyPaidJobSchedules(@Param("userId") String userId, @Param("paidStatus") Integer paidStatus);

    List<JobSignupUserRecordVo> selectPaidSignupUsersByJobId(@Param("jobId") Long jobId,
                                                             @Param("paidStatus") Integer paidStatus,
                                                             @Param("keyword") String keyword);

    JobSignupOrder selectLatestPaidOrder(@Param("userId") String userId, @Param("jobId") Long jobId);

    JobSignupOrder selectLatestPendingOrder(@Param("userId") String userId, @Param("jobId") Long jobId);

    int insertJobSignupOrder(JobSignupOrder order);

    int updateJobSignupOrder(JobSignupOrder order);

    List<JobRefundOrderVo> selectPaidOrdersWithSignIn(@Param("jobId") Long jobId);
}
