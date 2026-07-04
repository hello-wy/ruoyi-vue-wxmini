package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.StudentStaffAssignment;
import com.ruoyi.system.domain.bo.StudentAccessScope;
import com.ruoyi.system.domain.bo.StudentStaffCandidateQueryBo;
import com.ruoyi.system.domain.vo.StudentStaffAssignmentVo;
import com.ruoyi.system.domain.vo.StudentStaffCandidateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentStaffAssignmentMapper {

    StudentStaffAssignment selectByStudentId(@Param("studentId") Long studentId);

    StudentStaffAssignment selectByStudentIdForUpdate(@Param("studentId") Long studentId);

    int upsertAssignment(StudentStaffAssignment assignment);

    StudentStaffAssignmentVo selectAssignmentVoByStudentId(@Param("studentId") Long studentId);

    List<StudentStaffCandidateVo> selectStaffCandidates(StudentStaffCandidateQueryBo queryBo);

    int countAccessibleStudent(@Param("studentId") Long studentId, @Param("scope") StudentAccessScope scope);
}
