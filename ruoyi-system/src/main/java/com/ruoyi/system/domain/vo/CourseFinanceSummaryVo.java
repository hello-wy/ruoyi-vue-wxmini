package com.ruoyi.system.domain.vo;

import java.math.BigDecimal;

public class CourseFinanceSummaryVo {
    private int paymentCount;
    private BigDecimal paidAmount = BigDecimal.ZERO;
    private BigDecimal courseTotalAmount = BigDecimal.ZERO;
    private BigDecimal outstandingAmount = BigDecimal.ZERO;
    private BigDecimal refundedAmount = BigDecimal.ZERO;
    private BigDecimal grossCashbackAmount = BigDecimal.ZERO;
    private BigDecimal reversedCashbackAmount = BigDecimal.ZERO;
    private BigDecimal deductedCashbackAmount = BigDecimal.ZERO;
    private BigDecimal availableCashbackAmount = BigDecimal.ZERO;
    private BigDecimal unattributedPaidAmount = BigDecimal.ZERO;
    public int getPaymentCount(){return paymentCount;} public void setPaymentCount(int v){paymentCount=v;}
    public BigDecimal getPaidAmount(){return paidAmount;} public void setPaidAmount(BigDecimal v){paidAmount=v;}
    public BigDecimal getCourseTotalAmount(){return courseTotalAmount;} public void setCourseTotalAmount(BigDecimal v){courseTotalAmount=v;}
    public BigDecimal getOutstandingAmount(){return outstandingAmount;} public void setOutstandingAmount(BigDecimal v){outstandingAmount=v;}
    public BigDecimal getRefundedAmount(){return refundedAmount;} public void setRefundedAmount(BigDecimal v){refundedAmount=v;}
    public BigDecimal getGrossCashbackAmount(){return grossCashbackAmount;} public void setGrossCashbackAmount(BigDecimal v){grossCashbackAmount=v;}
    public BigDecimal getReversedCashbackAmount(){return reversedCashbackAmount;} public void setReversedCashbackAmount(BigDecimal v){reversedCashbackAmount=v;}
    public BigDecimal getDeductedCashbackAmount(){return deductedCashbackAmount;} public void setDeductedCashbackAmount(BigDecimal v){deductedCashbackAmount=v;}
    public BigDecimal getAvailableCashbackAmount(){return availableCashbackAmount;} public void setAvailableCashbackAmount(BigDecimal v){availableCashbackAmount=v;}
    public BigDecimal getUnattributedPaidAmount(){return unattributedPaidAmount;} public void setUnattributedPaidAmount(BigDecimal v){unattributedPaidAmount=v;}
}
