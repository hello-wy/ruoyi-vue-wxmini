package com.ruoyi.system.domain.vo;

/**
 * 用户课程报名信息 VO（含讲座基本信息）
 */
public class EnrollmentWithLectureVo {

    /** 讲座名称 */
    private String lectureName;

    /** 总报名数/学籍数 */
    private Integer total;

    /** 剩余可用学籍数 */
    private Integer remain;

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
}

