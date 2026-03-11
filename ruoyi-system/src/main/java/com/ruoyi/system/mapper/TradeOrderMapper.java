package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.TradeOrder;

/**
 * 通用交易订单 Mapper 接口
 *
 * @author ruoyi
 * @date 2026-03-07
 */
public interface TradeOrderMapper extends BaseMapper<TradeOrder>
{
    /**
     * 查询交易订单
     *
     * @param id 主键
     * @return 交易订单
     */
    TradeOrder selectTradeOrderById(Long id);

    /**
     * 查询交易订单列表
     *
     * @param tradeOrder 查询条件
     * @return 交易订单集合
     */
    List<TradeOrder> selectTradeOrderList(TradeOrder tradeOrder);

    /**
     * 新增交易订单
     *
     * @param tradeOrder 交易订单
     * @return 影响行数
     */
    int insertTradeOrder(TradeOrder tradeOrder);

    /**
     * 修改交易订单
     *
     * @param tradeOrder 交易订单
     * @return 影响行数
     */
    int updateTradeOrder(TradeOrder tradeOrder);

    /**
     * 删除交易订单
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteTradeOrderById(Long id);

    /**
     * 批量删除交易订单
     *
     * @param ids 主键数组
     * @return 影响行数
     */
    int deleteTradeOrderByIds(Long[] ids);
}
