package com.ruoyi.wxmini.controller;


import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ILecturesService;
import com.ruoyi.system.service.IStudentEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wxmini/growup")
public class WxGrowupController extends BaseController {

    @Autowired
    private ILecturesService lecturesService;

    @Autowired
    private IStudentEnrollmentService studentEnrollmentService;

    /**
     * 获取课程/讲座详细信息
     */
    @Anonymous
    @GetMapping("/courses/{id}")
    public AjaxResult getCourseInfo(@PathVariable("id") Long id)
    {
        return success(lecturesService.selectLecturesById(id));
    }

    /**
     * 获取当前用户这个 lecture 的 个人学籍
     * @param id
     * @return
     */
//    @GetMapping("/enrollments/{id}")
//    public AjaxResult getEnrollmentInfo(@PathVariable("id") Long id)
//    {
//        return success(studentEnrollmentService.selectStudentEnrollmentById(id));
//    }

}
