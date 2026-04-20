package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.bo.StudentQueryBo;
import com.ruoyi.system.domain.vo.StudentDetailVo;
import com.ruoyi.system.domain.vo.StudentListVo;
import com.ruoyi.wxmini.mapper.WxUserProfileMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private WxUserProfileMapper wxUserProfileMapper;

    @InjectMocks
    private StudentServiceImpl service;

    @Test
    void listStudentsShouldReturnMapperRowsWithDisplayLabels() {
        StudentQueryBo queryBo = new StudentQueryBo();
        queryBo.setRealName("张");

        StudentListVo student = new StudentListVo();
        student.setId(1L);
        student.setDisplayName("张三");
        student.setUserType(1);

        when(wxUserProfileMapper.selectAdminStudentList(queryBo)).thenReturn(Arrays.asList(student));

        List<StudentListVo> result = service.listStudents(queryBo);

        assertEquals(1, result.size());
        assertEquals("学生", result.get(0).getUserTypeLabel());
    }

    @Test
    void getStudentDetailShouldFillDisplayNameAndUserTypeLabel() {
        StudentDetailVo detail = new StudentDetailVo();
        detail.setId(9L);
        detail.setUserName("systemName");
        detail.setUserType(2);
        when(wxUserProfileMapper.selectAdminStudentDetailById(9L)).thenReturn(detail);

        StudentDetailVo result = service.getStudentDetail(9L);

        assertEquals("systemName", result.getDisplayName());
        assertEquals("商家", result.getUserTypeLabel());
    }
}
