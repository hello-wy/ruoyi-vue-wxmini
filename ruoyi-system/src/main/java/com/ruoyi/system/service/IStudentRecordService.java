package com.ruoyi.system.service;

import com.ruoyi.system.domain.bo.StudentFollowUpRecordBo;
import com.ruoyi.system.domain.vo.StudentEnrollmentSummaryVo;
import com.ruoyi.system.domain.vo.StudentFollowUpRecordVo;
import com.ruoyi.system.domain.vo.StudentLearningRecordsVo;
import com.ruoyi.system.domain.vo.StudentSalonPurchaseRecordVo;
import com.ruoyi.system.domain.vo.StudentSituationVo;

import java.util.List;

public interface IStudentRecordService {

    StudentEnrollmentSummaryVo getStudentEnrollments(Long id);

    StudentLearningRecordsVo getStudentLearningRecords(Long id);

    List<StudentSalonPurchaseRecordVo> listStudentSalonPurchaseRecords(Long id);

    List<StudentFollowUpRecordVo> listStudentFollowUpRecords(Long id);

    StudentFollowUpRecordVo addStudentFollowUpRecord(Long id, StudentFollowUpRecordBo bo, Long operatorId, String operatorName);

    StudentSituationVo updateStudentSituation(Long id, String studentSituation);
}
