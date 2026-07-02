package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.bo.StudentFollowUpRecordBo;
import com.ruoyi.system.domain.bo.StudentQueryBo;
import com.ruoyi.system.domain.bo.StudentSituationUpdateBo;
import com.ruoyi.system.domain.vo.StudentDetailVo;
import com.ruoyi.system.domain.vo.StudentFollowUpRecordVo;
import com.ruoyi.system.domain.vo.StudentLearningRecordsVo;
import com.ruoyi.system.domain.vo.StudentListVo;
import com.ruoyi.system.domain.vo.StudentSituationVo;
import com.ruoyi.system.service.IStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "学员管理")
@RestController
@RequestMapping("/system/student")
public class StudentController extends BaseController {

    @Resource
    private IStudentService studentService;

    @ApiOperation("查询学员分页列表")
    @PreAuthorize("@ss.hasPermi('system:student:list')")
    @GetMapping("/list")
    public TableDataInfoVo<StudentListVo> list(StudentQueryBo queryBo) {
        startPage();
        List<StudentListVo> list = studentService.listStudents(queryBo);
        return getDataTable(list);
    }

    @ApiOperation("查询学员详情")
    @ApiImplicitParam(name = "id", value = "学员ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('system:student:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        StudentDetailVo detail = studentService.getStudentDetail(id);
        return detail == null ? error("学员不存在") : success(detail);
    }

    @ApiOperation("查询学员学习情况")
    @ApiImplicitParam(name = "id", value = "学员ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('system:student:query')")
    @GetMapping("/{id}/learning-records")
    public AjaxResult learningRecords(@PathVariable("id") Long id) {
        StudentLearningRecordsVo records = studentService.getStudentLearningRecords(id);
        return records == null ? error("学员不存在") : success(records);
    }

    @ApiOperation("查询学员回访记录")
    @ApiImplicitParam(name = "id", value = "学员ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('system:student:query')")
    @GetMapping("/{id}/follow-up-records")
    public AjaxResult followUpRecords(@PathVariable("id") Long id) {
        List<StudentFollowUpRecordVo> records = studentService.listStudentFollowUpRecords(id);
        return records == null ? error("学员不存在") : success(records);
    }

    @ApiOperation("新增学员回访记录")
    @ApiImplicitParam(name = "id", value = "学员ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('system:student:edit')")
    @Log(title = "学员回访", businessType = BusinessType.INSERT)
    @PostMapping("/{id}/follow-up-records")
    public AjaxResult addFollowUpRecord(@PathVariable("id") Long id,
                                        @Validated @RequestBody StudentFollowUpRecordBo bo) {
        StudentFollowUpRecordVo record = studentService.addStudentFollowUpRecord(id, bo, getUserId(), getUsername());
        return record == null ? error("学员不存在") : success(record);
    }

    @ApiOperation("保存学员情况")
    @ApiImplicitParam(name = "id", value = "学员ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('system:student:edit')")
    @Log(title = "学员管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/situation")
    public AjaxResult updateSituation(@PathVariable("id") Long id,
                                      @Validated @RequestBody StudentSituationUpdateBo updateBo) {
        StudentSituationVo result = studentService.updateStudentSituation(id, updateBo.getStudentSituation());
        return result == null ? error("学员不存在") : success(result);
    }
}
