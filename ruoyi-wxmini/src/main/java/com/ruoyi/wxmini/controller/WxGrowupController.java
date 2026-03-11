package com.ruoyi.wxmini.controller;


import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.domain.vo.EnrollmentWithLectureVo;
import com.ruoyi.system.domain.vo.LecturesDetailVo;
import com.ruoyi.system.domain.vo.LecturesListVo;
import com.ruoyi.system.service.ILecturesService;
import com.ruoyi.system.service.IStudentEnrollmentService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/wxmini/growup")
public class WxGrowupController extends BaseController {

    @Autowired
    private ILecturesService lecturesService;

    @Autowired
    private IStudentEnrollmentService studentEnrollmentService;

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 获取课程/讲座列表（匿名，附带拼接讲师姓名）
     */
    @Anonymous
    @GetMapping("/courses")
    public TableDataInfo getCourseList(Lectures lectures) {
        startPage();
        List<LecturesListVo> list = lecturesService.selectLecturesListVo(lectures);
        return getDataTable(list);
    }

    /**
     * 获取课程/讲座详细信息（附带讲师 id/name/avatarUrl 列表）
     */
    @Anonymous
    @GetMapping("/courses/{id}")
    public AjaxResult getCourseInfo(@PathVariable("id") Long id) {
        LecturesDetailVo vo = lecturesService.selectLecturesDetailById(id);
        return success(vo);
    }

    /**
     * 获取当前登录小程序用户的个人学籍列表
     * 通过 WxMiniUserContext 中的 userId（String UUID）查找 user_info.id（Long），
     * 再以此 id 匹配 student_enrollment.uid
     */
    @GetMapping("/enrollments")
    public TableDataInfo myEnrollments() {
        startPage();
        String wxUserId = WxMiniUserContext.getCurrentUserId();
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(wxUserId);
        if (userInfo == null) {
            return getDataTable(Collections.emptyList());
        }
        // user_info.id（自增主键）对应 student_enrollment.uid
        Long uid = userInfo.getId();
        List<EnrollmentWithLectureVo> list = studentEnrollmentService.selectMyEnrollment(uid);
        return getDataTable(list);
    }
}
