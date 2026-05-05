package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.SalonPayOrder;
import com.ruoyi.system.domain.vo.SalonRefundOrderVo;

public interface SalonPayOrderMapper {
    SalonPayOrder selectSalonPayOrderByOrderNo(String orderNo);

    List<SalonPayOrder> selectSalonPayOrderList(SalonPayOrder order);

    List<SalonPayOrder> selectMySalonOrders(@Param("userId") String userId);

    SalonPayOrder selectLatestPaidOrderByUserIdAndSalonId(@Param("userId") String userId, @Param("salonId") Long salonId);

    int insertSalonPayOrder(SalonPayOrder order);

    int updateSalonPayOrder(SalonPayOrder order);

    List<SalonRefundOrderVo> selectPaidSalonOrders(@Param("salonId") Long salonId, @Param("keyword") String keyword);
}
