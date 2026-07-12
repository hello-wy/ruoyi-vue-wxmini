package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.bo.StudentQueryBo;
import com.ruoyi.system.domain.bo.StudentSituationUpdateBo;
import com.ruoyi.system.domain.vo.StudentDetailVo;
import com.ruoyi.system.domain.vo.StudentEnrollmentSummaryVo;
import com.ruoyi.system.domain.vo.StudentListVo;
import com.ruoyi.system.domain.vo.StudentSituationVo;
import com.ruoyi.system.domain.vo.StudentSalonPurchaseRecordVo;
import com.ruoyi.system.service.IStudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @BeforeEach
    void setUpRequestContext() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Mock
    private IStudentService studentService;

    @InjectMocks
    private StudentController controller;

    @Test
    void listShouldReturnPagedRows() {
        StudentListVo student = new StudentListVo();
        student.setId(1L);
        student.setDisplayName("张三");
        when(studentService.listStudents(any(StudentQueryBo.class)))
                .thenReturn(Collections.singletonList(student));

        TableDataInfoVo<StudentListVo> tableDataInfo = controller.list(new StudentQueryBo());

        assertEquals(1, tableDataInfo.getRows().size());
    }

    @Test
    void getInfoShouldReturn200WhenStudentExists() {
        StudentDetailVo detail = new StudentDetailVo();
        detail.setId(8L);
        detail.setDisplayName("李四");
        when(studentService.getStudentDetail(8L)).thenReturn(detail);

        AjaxResult result = controller.getInfo(8L);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void getInfoShouldReturnNon200WhenStudentMissing() {
        when(studentService.getStudentDetail(99L)).thenReturn(null);

        AjaxResult result = controller.getInfo(99L);

        assertEquals(false, Integer.valueOf(200).equals(result.get(AjaxResult.CODE_TAG)));
    }

    @Test
    void enrollmentsShouldReturn200WhenStudentExists() {
        StudentEnrollmentSummaryVo summary = new StudentEnrollmentSummaryVo();
        summary.setTotal(1);
        when(studentService.getStudentEnrollments(8L)).thenReturn(summary);

        AjaxResult result = controller.enrollments(8L);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void enrollmentsShouldReturnNon200WhenStudentMissing() {
        when(studentService.getStudentEnrollments(99L)).thenReturn(null);

        AjaxResult result = controller.enrollments(99L);

        assertEquals(false, Integer.valueOf(200).equals(result.get(AjaxResult.CODE_TAG)));
    }


    @Test
    void salonPurchaseRecordsShouldReturn200WhenStudentExists() {
        StudentSalonPurchaseRecordVo record = new StudentSalonPurchaseRecordVo();
        record.setOrderNo("SALON-001");
        when(studentService.listStudentSalonPurchaseRecords(8L)).thenReturn(Collections.singletonList(record));

        AjaxResult result = controller.salonPurchaseRecords(8L);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals(1, ((List<?>) result.get(AjaxResult.DATA_TAG)).size());
    }


    @Test
    void updateSituationShouldReturn200WhenStudentExists() {
        StudentSituationUpdateBo updateBo = new StudentSituationUpdateBo();
        updateBo.setStudentSituation("学习主动");
        StudentSituationVo situation = new StudentSituationVo();
        situation.setId(8L);
        situation.setStudentSituation("学习主动");
        when(studentService.updateStudentSituation(8L, "学习主动")).thenReturn(situation);

        AjaxResult result = controller.updateSituation(8L, updateBo);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void updateSituationShouldReturnNon200WhenStudentMissing() {
        StudentSituationUpdateBo updateBo = new StudentSituationUpdateBo();
        updateBo.setStudentSituation("学习主动");
        when(studentService.updateStudentSituation(99L, "学习主动")).thenReturn(null);

        AjaxResult result = controller.updateSituation(99L, updateBo);

        assertEquals(false, Integer.valueOf(200).equals(result.get(AjaxResult.CODE_TAG)));
    }
}
