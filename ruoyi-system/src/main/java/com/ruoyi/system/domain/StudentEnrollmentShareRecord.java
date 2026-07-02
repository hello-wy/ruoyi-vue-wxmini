package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学籍分享记录对象 student_enrollment_share_record。
 */
public class StudentEnrollmentShareRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 分享方用户ID（user_info.id） */
    @TableField("source_uid")
    private Long sourceUid;

    /** 接收方用户ID（user_info.id） */
    @TableField("target_uid")
    private Long targetUid;

    /** 课程ID */
    @TableField("lecture_id")
    private Long lectureId;

    /** 分享数量 */
    private Integer count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSourceUid() {
        return sourceUid;
    }

    public void setSourceUid(Long sourceUid) {
        this.sourceUid = sourceUid;
    }

    public Long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(Long targetUid) {
        this.targetUid = targetUid;
    }

    public Long getLectureId() {
        return lectureId;
    }

    public void setLectureId(Long lectureId) {
        this.lectureId = lectureId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
