package com.ruoyi.system.service;

import com.ruoyi.system.domain.bo.StudentBindingBo;
import com.ruoyi.system.domain.bo.StudentFollowUpRecordBo;
import com.ruoyi.system.domain.bo.StudentQueryBo;
import com.ruoyi.system.domain.bo.StudentStaffCandidateQueryBo;
import com.ruoyi.system.domain.vo.StudentDetailVo;
import com.ruoyi.system.domain.vo.StudentEnrollmentSummaryVo;
import com.ruoyi.system.domain.vo.StudentFollowUpRecordVo;
import com.ruoyi.system.domain.vo.StudentLearningRecordsVo;
import com.ruoyi.system.domain.vo.StudentListVo;
import com.ruoyi.system.domain.vo.StudentSituationVo;
import com.ruoyi.system.domain.vo.StudentSalonPurchaseRecordVo;
import com.ruoyi.system.domain.vo.StudentStaffAssignmentVo;
import com.ruoyi.system.domain.vo.StudentStaffCandidateVo;

import java.util.List;

public interface IStudentService {

    List<StudentListVo> listStudents(StudentQueryBo queryBo);

    StudentDetailVo getStudentDetail(Long id);

    StudentEnrollmentSummaryVo getStudentEnrollments(Long id);

    StudentLearningRecordsVo getStudentLearningRecords(Long id);

    List<StudentFollowUpRecordVo> listStudentFollowUpRecords(Long id);

    List<StudentSalonPurchaseRecordVo> listStudentSalonPurchaseRecords(Long id);

    StudentFollowUpRecordVo addStudentFollowUpRecord(Long id, StudentFollowUpRecordBo bo, Long operatorId, String operatorName);

    StudentSituationVo updateStudentSituation(Long id, String studentSituation);

    List<StudentStaffCandidateVo> listStaffCandidates(StudentStaffCandidateQueryBo queryBo);

    StudentStaffAssignmentVo bindStudentStaff(Long id, StudentBindingBo bo, Long operatorId, String operatorName);
}
