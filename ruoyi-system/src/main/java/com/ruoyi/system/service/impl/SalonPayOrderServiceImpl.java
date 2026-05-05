package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SalonPayOrder;
import com.ruoyi.system.domain.vo.SalonRefundOrderVo;
import com.ruoyi.system.mapper.SalonPayOrderMapper;
import com.ruoyi.system.service.ISalonPayOrderService;

@Service
public class SalonPayOrderServiceImpl implements ISalonPayOrderService {
    @Autowired
    private SalonPayOrderMapper salonPayOrderMapper;

    @Override
    public SalonPayOrder selectSalonPayOrderByOrderNo(String orderNo) {
        return salonPayOrderMapper.selectSalonPayOrderByOrderNo(orderNo);
    }

    @Override
    public List<SalonPayOrder> selectSalonPayOrderList(SalonPayOrder order) {
        return salonPayOrderMapper.selectSalonPayOrderList(order);
    }

    @Override
    public List<SalonPayOrder> selectMySalonOrders(String userId) {
        return salonPayOrderMapper.selectMySalonOrders(userId);
    }

    @Override
    public SalonPayOrder selectLatestPaidOrderByUserIdAndSalonId(String userId, Long salonId) {
        return salonPayOrderMapper.selectLatestPaidOrderByUserIdAndSalonId(userId, salonId);
    }

    @Override
    public int insertSalonPayOrder(SalonPayOrder order) {
        order.setCreateTime(DateUtils.getNowDate());
        order.setUpdateTime(DateUtils.getNowDate());
        return salonPayOrderMapper.insertSalonPayOrder(order);
    }

    @Override
    public int updateSalonPayOrder(SalonPayOrder order) {
        order.setUpdateTime(DateUtils.getNowDate());
        return salonPayOrderMapper.updateSalonPayOrder(order);
    }

    @Override
    public List<SalonRefundOrderVo> selectPaidSalonOrders(Long salonId, String keyword) {
        return salonPayOrderMapper.selectPaidSalonOrders(salonId, keyword);
    }
}
