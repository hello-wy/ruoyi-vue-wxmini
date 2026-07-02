package com.ruoyi.system.domain.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 学员学籍信息汇总。
 */
public class StudentEnrollmentSummaryVo {

    /** 累计学籍数 */
    private Integer total;

    /** 剩余学籍数 */
    private Integer remain;

    /** 已使用学籍数 */
    private Integer usedCount;

    /** 已分享学籍数 */
    private Integer sharedCount;

    /** 按课程名称分组后的学籍 */
    private List<StudentEnrollmentGroupVo> groups = new ArrayList<>();

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Integer getSharedCount() {
        return sharedCount;
    }

    public void setSharedCount(Integer sharedCount) {
        this.sharedCount = sharedCount;
    }

    public List<StudentEnrollmentGroupVo> getGroups() {
        return groups;
    }

    public void setGroups(List<StudentEnrollmentGroupVo> groups) {
        this.groups = groups;
    }
}
