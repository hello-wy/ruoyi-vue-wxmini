package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SalonPayOrder;
import com.ruoyi.system.domain.vo.SalonRefundOrderVo;

public interface ISalonPayOrderService {
    SalonPayOrder selectSalonPayOrderByOrderNo(String orderNo);

    List<SalonPayOrder> selectSalonPayOrderList(SalonPayOrder order);

    List<SalonPayOrder> selectMySalonOrders(String userId);

    SalonPayOrder selectLatestPaidOrderByUserIdAndSalonId(String userId, Long salonId);

    int insertSalonPayOrder(SalonPayOrder order);

    int updateSalonPayOrder(SalonPayOrder order);

    List<SalonRefundOrderVo> selectPaidSalonOrders(Long salonId, String keyword);
}
