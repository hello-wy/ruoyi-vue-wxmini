package com.ruoyi.system.domain;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户钱包对象 user_wallet
 *
 * @author ruoyi
 */
public class UserWallet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long uid;

    /** 可用余额（元） */
    @Excel(name = "可用余额")
    private BigDecimal balance;

    /** 冻结金额（元，提现审核中） */
    @Excel(name = "冻结金额")
    private BigDecimal frozen;

    /** 累计收入（元） */
    @Excel(name = "累计收入")
    private BigDecimal totalEarned;

    /** 累计提现（元） */
    @Excel(name = "累计提现")
    private BigDecimal totalWithdrawn;

    public Long getId()                             { return id; }
    public void setId(Long id)                      { this.id = id; }
    public Long getUid()                            { return uid; }
    public void setUid(Long uid)                    { this.uid = uid; }
    public BigDecimal getBalance()                  { return balance; }
    public void setBalance(BigDecimal balance)      { this.balance = balance; }
    public BigDecimal getFrozen()                   { return frozen; }
    public void setFrozen(BigDecimal frozen)        { this.frozen = frozen; }
    public BigDecimal getTotalEarned()              { return totalEarned; }
    public void setTotalEarned(BigDecimal totalEarned) { this.totalEarned = totalEarned; }
    public BigDecimal getTotalWithdrawn()           { return totalWithdrawn; }
    public void setTotalWithdrawn(BigDecimal totalWithdrawn) { this.totalWithdrawn = totalWithdrawn; }
}
