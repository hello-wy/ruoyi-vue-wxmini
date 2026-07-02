package com.ruoyi.system.service;

import com.ruoyi.system.domain.bo.StudentQueryBo;
import com.ruoyi.system.domain.vo.StudentDetailVo;
import com.ruoyi.system.domain.vo.StudentLearningRecordsVo;
import com.ruoyi.system.domain.vo.StudentListVo;
import com.ruoyi.system.domain.vo.StudentSituationVo;

import java.util.List;

public interface IStudentService {

    List<StudentListVo> listStudents(StudentQueryBo queryBo);

    StudentDetailVo getStudentDetail(Long id);

    StudentLearningRecordsVo getStudentLearningRecords(Long id);

    StudentSituationVo updateStudentSituation(Long id, String studentSituation);
}
