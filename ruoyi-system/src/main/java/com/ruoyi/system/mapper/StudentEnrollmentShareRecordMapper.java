package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.StudentEnrollmentShareRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 学籍分享记录Mapper接口。
 */
public interface StudentEnrollmentShareRecordMapper {

    /**
     * 新增分享记录。
     *
     * @param record 分享记录
     * @return 结果
     */
    int insertStudentEnrollmentShareRecord(StudentEnrollmentShareRecord record);

    /**
     * 统计用户指定课程已分享数量。
     *
     * @param sourceUid 分享方用户ID
     * @param lectureId 课程ID
     * @return 已分享数量
     */
    int sumSharedCountBySourceAndLecture(@Param("sourceUid") Long sourceUid, @Param("lectureId") Long lectureId);
}
