package com.ruoyi.system.domain.vo;

import java.math.BigDecimal;

public class CourseDistributionCommissionSummaryVo {
    private BigDecimal level1Amount = BigDecimal.ZERO;
    private BigDecimal level2Amount = BigDecimal.ZERO;
    private BigDecimal reversedAmount = BigDecimal.ZERO;
    private BigDecimal manualCreditAmount = BigDecimal.ZERO;
    private BigDecimal manualDebitAmount = BigDecimal.ZERO;

    public BigDecimal getLevel1Amount() { return level1Amount; }
    public void setLevel1Amount(BigDecimal level1Amount) { this.level1Amount = level1Amount; }
    public BigDecimal getLevel2Amount() { return level2Amount; }
    public void setLevel2Amount(BigDecimal level2Amount) { this.level2Amount = level2Amount; }
    public BigDecimal getReversedAmount() { return reversedAmount; }
    public void setReversedAmount(BigDecimal reversedAmount) { this.reversedAmount = reversedAmount; }
    public BigDecimal getManualCreditAmount() { return manualCreditAmount; }
    public void setManualCreditAmount(BigDecimal manualCreditAmount) { this.manualCreditAmount = manualCreditAmount; }
    public BigDecimal getManualDebitAmount() { return manualDebitAmount; }
    public void setManualDebitAmount(BigDecimal manualDebitAmount) { this.manualDebitAmount = manualDebitAmount; }
}
