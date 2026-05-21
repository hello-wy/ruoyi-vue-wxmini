package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TutoringOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TutoringOrderMapper {
    int insertTutoringOrder(TutoringOrder order);

    int updateTutoringOrder(TutoringOrder order);

    TutoringOrder selectByOrderNo(@Param("orderNo") String orderNo);

    TutoringOrder selectByOrderNoForUpdate(@Param("orderNo") String orderNo);

    TutoringOrder selectLatestByBindingId(@Param("bindingId") Long bindingId);

    List<TutoringOrder> selectMyOrders(@Param("parentUserId") Long parentUserId);
}
