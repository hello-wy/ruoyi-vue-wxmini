package com.ruoyi.system.domain.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 按课程名称聚合的学籍信息。
 */
public class StudentEnrollmentGroupVo {

    /** 课程名称 */
    private String lectureName;

    /** 累计学籍数 */
    private Integer total;

    /** 剩余学籍数 */
    private Integer remain;

    /** 已使用学籍数 */
    private Integer usedCount;

    /** 已分享学籍数 */
    private Integer sharedCount;

    /** 课程明细 */
    private List<EnrollmentWithLectureVo> items = new ArrayList<>();

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

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

    public List<EnrollmentWithLectureVo> getItems() {
        return items;
    }

    public void setItems(List<EnrollmentWithLectureVo> items) {
        this.items = items;
    }
}
