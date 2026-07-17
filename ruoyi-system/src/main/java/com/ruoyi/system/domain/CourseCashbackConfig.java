package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;

/** Global current course cashback ratio. */
public class CourseCashbackConfig extends BaseEntity {
    private Long id;
    private BigDecimal cashbackRatio;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getCashbackRatio() { return cashbackRatio; }
    public void setCashbackRatio(BigDecimal cashbackRatio) { this.cashbackRatio = cashbackRatio; }
}
