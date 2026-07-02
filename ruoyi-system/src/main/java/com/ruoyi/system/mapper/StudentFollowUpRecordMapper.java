package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.StudentFollowUpRecord;
import com.ruoyi.system.domain.vo.StudentFollowUpRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentFollowUpRecordMapper {

    List<StudentFollowUpRecordVo> selectStudentFollowUpRecordsByStudentId(@Param("studentId") Long studentId);

    StudentFollowUpRecordVo selectStudentFollowUpRecordById(@Param("id") Long id);

    int insertStudentFollowUpRecord(StudentFollowUpRecord record);
}
