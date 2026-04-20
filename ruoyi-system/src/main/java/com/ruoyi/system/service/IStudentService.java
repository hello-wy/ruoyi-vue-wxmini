package com.ruoyi.system.service;

import com.ruoyi.system.domain.bo.StudentQueryBo;
import com.ruoyi.system.domain.vo.StudentDetailVo;
import com.ruoyi.system.domain.vo.StudentListVo;

import java.util.List;

public interface IStudentService {

    List<StudentListVo> listStudents(StudentQueryBo queryBo);

    StudentDetailVo getStudentDetail(Long id);
}
