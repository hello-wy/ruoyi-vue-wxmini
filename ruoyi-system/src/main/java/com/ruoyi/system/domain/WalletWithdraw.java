package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 钱包提现申请记录对象 wallet_withdraw
 *
 * @author ruoyi
 */
public class WalletWithdraw extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long uid;

    /** 申请提现金额（元） */
    @Excel(name = "提现金额")
    private BigDecimal amount;

    /** 状态: 0-打款中, 1-已打款, 2-打款失败 */
    @Excel(name = "状态", readConverterExp = "0=打款中,1=已打款,2=打款失败")
    private Integer status;

    /** 备注（失败原因等） */
    @Excel(name = "备注")
    private String remark;

    /** 微信批次单号 */
    @Excel(name = "微信批次单号")
    private String wxTransferNo;

    /** 商户批次号 */
    @Excel(name = "商户批次号")
    private String outBatchNo;

    /** 商户明细单号 */
    @Excel(name = "商户明细单号")
    private String outDetailNo;

    /** 微信明细单号 */
    @Excel(name = "微信明细单号")
    private String wxDetailNo;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    public Long getId()                                 { return id; }
    public void setId(Long id)                          { this.id = id; }
    public Long getUid()                                { return uid; }
    public void setUid(Long uid)                        { this.uid = uid; }
    public BigDecimal getAmount()                       { return amount; }
    public void setAmount(BigDecimal amount)            { this.amount = amount; }
    public Integer getStatus()                          { return status; }
    public void setStatus(Integer status)               { this.status = status; }
    public String getRemark()                           { return remark; }
    public void setRemark(String remark)                { this.remark = remark; }
    public String getWxTransferNo()                     { return wxTransferNo; }
    public void setWxTransferNo(String wxTransferNo)    { this.wxTransferNo = wxTransferNo; }
    public String getOutBatchNo()                       { return outBatchNo; }
    public void setOutBatchNo(String outBatchNo)        { this.outBatchNo = outBatchNo; }
    public String getOutDetailNo()                      { return outDetailNo; }
    public void setOutDetailNo(String outDetailNo)      { this.outDetailNo = outDetailNo; }
    public String getWxDetailNo()                       { return wxDetailNo; }
    public void setWxDetailNo(String wxDetailNo)        { this.wxDetailNo = wxDetailNo; }
    public Date getApplyTime()                          { return applyTime; }
    public void setApplyTime(Date applyTime)            { this.applyTime = applyTime; }
}
