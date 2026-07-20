package com.ruoyi.system.domain;

import java.math.BigDecimal;

public class CourseDistributionCommissionConfig {
    private Byte id;
    private BigDecimal level1Ratio;
    private BigDecimal level2Ratio;
    private BigDecimal inviteRewardAmount;

    public Byte getId() { return id; }
    public void setId(Byte id) { this.id = id; }
    public BigDecimal getLevel1Ratio() { return level1Ratio; }
    public void setLevel1Ratio(BigDecimal level1Ratio) { this.level1Ratio = level1Ratio; }
    public BigDecimal getLevel2Ratio() { return level2Ratio; }
    public void setLevel2Ratio(BigDecimal level2Ratio) { this.level2Ratio = level2Ratio; }
    public BigDecimal getInviteRewardAmount() { return inviteRewardAmount; }
    public void setInviteRewardAmount(BigDecimal inviteRewardAmount) { this.inviteRewardAmount = inviteRewardAmount; }
}
