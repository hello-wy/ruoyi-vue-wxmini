package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TradeOrderMapper;
import com.ruoyi.system.domain.TradeOrder;
import com.ruoyi.system.service.ITradeOrderService;

/**
 * 通用交易订单 Service 业务层处理
 *
 * @author ruoyi
 * @date 2026-03-07
 */
@Service
public class TradeOrderServiceImpl implements ITradeOrderService
{
    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    @Override
    public TradeOrder selectTradeOrderById(Long id)
    {
        return tradeOrderMapper.selectTradeOrderById(id);
    }

    @Override
    public List<TradeOrder> selectTradeOrderList(TradeOrder tradeOrder)
    {
        return tradeOrderMapper.selectTradeOrderList(tradeOrder);
    }

    @Override
    public int insertTradeOrder(TradeOrder tradeOrder)
    {
        tradeOrder.setCreateTime(DateUtils.getNowDate());
        return tradeOrderMapper.insertTradeOrder(tradeOrder);
    }

    @Override
    public int updateTradeOrder(TradeOrder tradeOrder)
    {
        tradeOrder.setUpdateTime(DateUtils.getNowDate());
        return tradeOrderMapper.updateTradeOrder(tradeOrder);
    }

    @Override
    public int deleteTradeOrderByIds(Long[] ids)
    {
        return tradeOrderMapper.deleteTradeOrderByIds(ids);
    }

    @Override
    public int deleteTradeOrderById(Long id)
    {
        return tradeOrderMapper.deleteTradeOrderById(id);
    }
}
