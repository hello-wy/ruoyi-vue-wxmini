package com.ruoyi.web.controller.bussiness;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.system.domain.vo.EnrollmentWithLectureVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.StudentEnrollment;
import com.ruoyi.system.service.IStudentEnrollmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学籍信息Controller
 * 学生购买这个课程后。系统记录学生的学籍信息。包括学生姓名，课程名称，购买时间，课程有效期等信息。
 * @author ruoyi
 * @date 2026-03-07
 */
@RestController
@RequestMapping("/system/enrollment")
public class StudentEnrollmentController extends BaseController
{
    @Autowired
    private IStudentEnrollmentService studentEnrollmentService;

    /**
     * 查询学籍信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:enrollment:list')")
    @GetMapping("/list")
    public TableDataInfo list(StudentEnrollment studentEnrollment)
    {
        startPage();
        List<StudentEnrollment> list = studentEnrollmentService.selectStudentEnrollmentList(studentEnrollment);
        return getDataTable(list);
    }

    /**
     * 导出学籍信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:enrollment:export')")
    @Log(title = "学籍信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StudentEnrollment studentEnrollment)
    {
        List<StudentEnrollment> list = studentEnrollmentService.selectStudentEnrollmentList(studentEnrollment);
        ExcelUtil<StudentEnrollment> util = new ExcelUtil<StudentEnrollment>(StudentEnrollment.class);
        util.exportExcel(response, list, "学籍信息数据");
    }

    /**
     * 获取学籍信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:enrollment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(studentEnrollmentService.selectStudentEnrollmentById(id));
    }

    /**
     * 新增学籍信息
     */
    @PreAuthorize("@ss.hasPermi('system:enrollment:add')")
    @Log(title = "学籍信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StudentEnrollment studentEnrollment)
    {
        return toAjax(studentEnrollmentService.insertStudentEnrollment(studentEnrollment));
    }

    /**
     * 修改学籍信息
     */
    @PreAuthorize("@ss.hasPermi('system:enrollment:edit')")
    @Log(title = "学籍信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StudentEnrollment studentEnrollment)
    {
        return toAjax(studentEnrollmentService.updateStudentEnrollment(studentEnrollment));
    }

    /**
     * 删除学籍信息
     */
    @PreAuthorize("@ss.hasPermi('system:enrollment:remove')")
    @Log(title = "学籍信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(studentEnrollmentService.deleteStudentEnrollmentByIds(ids));
    }

    /**
     * 查询用户个人学籍信息
     */
    @GetMapping("/myEnrollment")
    public TableDataInfo myEnrollment() {
        startPage();
        Long uid = getUserId();
        List<EnrollmentWithLectureVo> enrollments = studentEnrollmentService.selectMyEnrollment(uid);
        return getDataTable(enrollments);
    }

    /**
     * 核销：当前用户指定课程剩余次数减num（余额不足时报错并回滚）
     */
    @Log(title = "学籍核销", businessType = BusinessType.UPDATE)
    @PutMapping("/decreaseRemain/{lectureId}")
    public AjaxResult decreaseRemain(@PathVariable Long lectureId,
                                     @RequestParam(defaultValue = "1") int num)
    {
        studentEnrollmentService.decreaseRemain(getUserId(), lectureId, num);
        return success();
    }

    /**
     * 充值：当前用户指定课程剩余次数加num
     */
    @Log(title = "学籍充值", businessType = BusinessType.UPDATE)
    @PutMapping("/increaseRemain/{lectureId}")
    public AjaxResult increaseRemain(@PathVariable Long lectureId,
                                     @RequestParam(defaultValue = "1") int num)
    {
        studentEnrollmentService.increaseRemain(getUserId(), lectureId, num);
        return success();
    }

}
