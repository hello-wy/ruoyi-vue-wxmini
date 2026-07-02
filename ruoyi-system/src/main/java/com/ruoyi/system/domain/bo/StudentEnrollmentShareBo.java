package com.ruoyi.system.domain.bo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 学籍分享请求。
 */
public class StudentEnrollmentShareBo {

    /** 分享方用户ID（user_info.id） */
    @NotNull(message = "分享方用户不能为空")
    private Long sourceUid;

    /** 接收方用户ID（user_info.id） */
    @NotNull(message = "接收方用户不能为空")
    private Long targetUid;

    /** 课程ID */
    @NotNull(message = "课程不能为空")
    private Long lectureId;

    /** 分享数量 */
    @NotNull(message = "分享数量不能为空")
    @Min(value = 1, message = "分享数量必须大于0")
    private Integer count;

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
