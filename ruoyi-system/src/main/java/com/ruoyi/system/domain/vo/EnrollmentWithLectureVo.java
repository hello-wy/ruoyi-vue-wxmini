package com.ruoyi.system.domain.vo;

/**
 * 用户课程报名信息 VO（含讲座基本信息）
 */
public class EnrollmentWithLectureVo {

    /** 学籍记录ID */
    private Long id;

    /** 讲座ID */
    private Long lectureId;

    /** 讲座名称 */
    private String lectureName;

    /** 总报名数/学籍数 */
    private Integer total;

    /** 剩余可用学籍数 */
    private Integer remain;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLectureId() {
        return lectureId;
    }

    public void setLectureId(Long lectureId) {
        this.lectureId = lectureId;
    }

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
