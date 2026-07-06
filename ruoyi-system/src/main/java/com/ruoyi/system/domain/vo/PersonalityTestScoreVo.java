package com.ruoyi.system.domain.vo;

public class PersonalityTestScoreVo {
    private Integer type;
    private Integer yesCount;
    private Integer noCount;
    private Integer unsureCount;

    public PersonalityTestScoreVo() {
    }

    public PersonalityTestScoreVo(Integer type, Integer yesCount, Integer noCount, Integer unsureCount) {
        this.type = type;
        this.yesCount = yesCount;
        this.noCount = noCount;
        this.unsureCount = unsureCount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getYesCount() {
        return yesCount;
    }

    public void setYesCount(Integer yesCount) {
        this.yesCount = yesCount;
    }

    public Integer getNoCount() {
        return noCount;
    }

    public void setNoCount(Integer noCount) {
        this.noCount = noCount;
    }

    public Integer getUnsureCount() {
        return unsureCount;
    }

    public void setUnsureCount(Integer unsureCount) {
        this.unsureCount = unsureCount;
    }
}
