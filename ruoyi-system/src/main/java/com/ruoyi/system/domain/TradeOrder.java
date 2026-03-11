package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 通用交易订单对象 trade_order
 *
 * @author ruoyi
 * @date 2026-03-07
 */
public class TradeOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 内部订单ID，主键 */
    private Long id;

    /** 外部展示及支付网关使用的订单号（需唯一） */
    @Excel(name = "订单号")
    private String orderNo;

    /** 购买用户的唯一标识 */
    @Excel(name = "用户ID")
    private Long userId;

    /** 业务类型：1-沙龙(salon订单)，2-讲座(lecture订单) */
    @Excel(name = "业务类型", readConverterExp = "1=沙龙订单,2=讲座订单")
    private Long orderType;

    /** 关联的沙龙ID（当order_type=1时有值） */
    @Excel(name = "关联沙龙ID")
    private Long salonId;

    /** 关联的讲座ID（当order_type=2时有值） */
    @Excel(name = "关联讲座ID")
    private Long lectureId;

    /** 实际支付金额 */
    @Excel(name = "支付金额")
    private BigDecimal payAmount;

    /** 支付方式，如：wechat_pay, alipay, offline */
    @Excel(name = "支付方式")
    private String payMethod;

    /** 主要的用途/备注，如：报名听课、赞助商 */
    @Excel(name = "用途备注")
    private String purpose;

    /** 支付状态：0-待支付，1-已支付，2-已退款，3-已取消 */
    @Excel(name = "支付状态", readConverterExp = "0=待支付,1=已支付,2=已退款,3=已取消")
    private Long payStatus;

    /** 实际完成支付的时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    public void setId(Long id)          { this.id = id; }
    public Long getId()                 { return id; }
    public void setOrderNo(String orderNo)      { this.orderNo = orderNo; }
    public String getOrderNo()                  { return orderNo; }
    public void setUserId(Long userId)          { this.userId = userId; }
    public Long getUserId()                     { return userId; }
    public void setOrderType(Long orderType)    { this.orderType = orderType; }
    public Long getOrderType()                  { return orderType; }
    public void setSalonId(Long salonId)        { this.salonId = salonId; }
    public Long getSalonId()                    { return salonId; }
    public void setLectureId(Long lectureId)    { this.lectureId = lectureId; }
    public Long getLectureId()                  { return lectureId; }
    public void setPayAmount(BigDecimal payAmount) { this.payAmount = payAmount; }
    public BigDecimal getPayAmount()            { return payAmount; }
    public void setPayMethod(String payMethod)  { this.payMethod = payMethod; }
    public String getPayMethod()                { return payMethod; }
    public void setPurpose(String purpose)      { this.purpose = purpose; }
    public String getPurpose()                  { return purpose; }
    public void setPayStatus(Long payStatus)    { this.payStatus = payStatus; }
    public Long getPayStatus()                  { return payStatus; }
    public void setPayTime(Date payTime)        { this.payTime = payTime; }
    public Date getPayTime()                    { return payTime; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("orderNo", getOrderNo())
                .append("userId", getUserId())
                .append("orderType", getOrderType())
                .append("salonId", getSalonId())
                .append("lectureId", getLectureId())
                .append("payAmount", getPayAmount())
                .append("payMethod", getPayMethod())
                .append("purpose", getPurpose())
                .append("payStatus", getPayStatus())
                .append("payTime", getPayTime())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
