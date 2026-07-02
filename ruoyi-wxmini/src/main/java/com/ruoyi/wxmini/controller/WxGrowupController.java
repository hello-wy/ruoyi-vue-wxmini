package com.ruoyi.wxmini.controller;


import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.domain.Questionnaire;
import com.ruoyi.system.domain.StudentEnrollment;
import com.ruoyi.system.domain.vo.EnrollmentWithLectureVo;
import com.ruoyi.system.domain.vo.LecturesDetailVo;
import com.ruoyi.system.domain.vo.LecturesListVo;
import com.ruoyi.system.service.ILecturesService;
import com.ruoyi.system.service.IQuestionnaireService;
import com.ruoyi.system.service.IStudentEnrollmentService;
import com.ruoyi.wxmini.bo.WxCourseReviewSaveBo;
import com.ruoyi.wxmini.bo.WxGrowupCourseEnrollBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IWxCourseReviewService;
import com.ruoyi.wxmini.service.IWxGrowupPayService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * 微信小程序 - 成长课程接口
 */
@Api(tags = "【小程序】成长课程与学籍")
@RestController
@RequestMapping("/wxmini/growup")
public class WxGrowupController extends BaseController {

    @Autowired
    private ILecturesService lecturesService;

    @Autowired
    private IStudentEnrollmentService studentEnrollmentService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IQuestionnaireService questionnaireService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IWxGrowupPayService wxGrowupPayService;

    @Autowired
    private IWxCourseReviewService wxCourseReviewService;

    /**
     * 获取课程/讲座列表（匿名，附带拼接讲师姓名）
     */
    @ApiOperation("获取课程/讲座列表（公开，附带讲师姓名）")
    @Anonymous
    @GetMapping("/courses")
    public TableDataInfoVo<LecturesListVo> getCourseList(Lectures lectures) {

        String cacheJson = redisCache.getCacheObject(Constants.LECTURE_KEY_PREFIX);
        if (cacheJson != null) {
            List<LecturesListVo> cacheList = parseCoursesCache(cacheJson);
            if (cacheList != null) {
                return getDataTable(cacheList);
            }
            logger.warn("课程列表缓存解析失败，回源数据库，key={}", Constants.LECTURE_KEY_PREFIX);
        }

        startPage();
        List<LecturesListVo> list = lecturesService.selectLecturesListVo(lectures);
        if (!list.isEmpty()) {
            List<Questionnaire> questionnaires = questionnaireService.selectRecentQuestionnaireList(list.get(0).getId());
            list.get(0).setQuestionnaire(questionnaires);
        }
//        redisCache.setCacheObject(Constants.LECTURE_KEY_PREFIX, JSON.toJSONString(list), Constants.LECTURE_EXPIRATION, TimeUnit.SECONDS);
        return getDataTable(list);
    }

    private List<LecturesListVo> parseCoursesCache(String cacheJson) {
        try {
            return JSON.parseArray(cacheJson, LecturesListVo.class);
        } catch (Exception ignored) {
            try {
                String rawJsonArray = JSON.parseObject(cacheJson, String.class);
                return JSON.parseArray(rawJsonArray, LecturesListVo.class);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * 获取课程/讲座详细信息（附带讲师 id/name/avatarUrl 列表）
     */
    @ApiOperation("获取课程/讲座详细信息（公开，附带讲师信息）")
    @ApiImplicitParam(name = "id", value = "课程/讲座ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    @Anonymous
    @GetMapping("/courses/{id}")
    public AjaxResult getCourseInfo(@PathVariable("id") Long id) {
        LecturesDetailVo vo = lecturesService.selectLecturesDetailById(id);
        return success(vo);
    }

    @ApiOperation("获取当前登录用户指定课程的学籍余量（需登录）")
    @GetMapping("/courses/{id}/enrollment")
    public AjaxResult myCourseEnrollment(@PathVariable("id") Long id) {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return error("用户不存在");
        }
        StudentEnrollment enrollment = studentEnrollmentService.selectEnrollmentByUidAndLectureId(userInfo.getId(), id);
        return success(enrollment);
    }

    @ApiOperation("报名课程并创建微信支付订单（需登录）")
    @PostMapping("/courses/{id}/enroll")
    public AjaxResult enrollCourse(@PathVariable("id") Long id,
                                   @RequestBody @Valid WxGrowupCourseEnrollBo bo) {
        try {
            String wxUserId = WxMiniUserContext.getCurrentUserId();
            return success(wxGrowupPayService.createCourseOrder(wxUserId, id, bo));
        } catch (Exception e) {
            logger.error("创建成长课程报名支付订单失败", e);
            return error(e.getMessage());
        }
    }

    @ApiOperation("查询当前用户当前订单的课程评价（需登录）")
    @GetMapping("/courses/{id}/reviews/my")
    public AjaxResult myCourseReview(@PathVariable("id") Long id,
                                     @RequestParam("orderNo") String orderNo) {
        try {
            String wxUserId = WxMiniUserContext.getCurrentUserId();
            return success(wxCourseReviewService.getMyReview(wxUserId, id, orderNo));
        } catch (Exception e) {
            logger.error("查询课程评价失败", e);
            return error(e.getMessage());
        }
    }

    @ApiOperation("保存当前用户当前订单的课程评价（需登录）")
    @PostMapping("/courses/{id}/reviews/my")
    public AjaxResult saveMyCourseReview(@PathVariable("id") Long id,
                                         @RequestBody @Valid WxCourseReviewSaveBo bo) {
        try {
            String wxUserId = WxMiniUserContext.getCurrentUserId();
            return success(wxCourseReviewService.saveMyReview(wxUserId, id, bo));
        } catch (Exception e) {
            logger.error("保存课程评价失败", e);
            return error(e.getMessage());
        }
    }

    /**
     * 获取当前登录小程序用户的个人学籍列表
     * 通过 WxMiniUserContext 中的 userId（String UUID）查找 user_info.id（Long），
     * 再以此 id 匹配 student_enrollment.uid
     */
    @ApiOperation("获取当前登录用户的个人学籍列表（需登录）")
    @GetMapping("/enrollments/list")
    public TableDataInfoVo<EnrollmentWithLectureVo> myEnrollments() {
        startPage();
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return getDataTable(Collections.emptyList());
        }
        Long uid = userInfo.getId();
        List<EnrollmentWithLectureVo> list = studentEnrollmentService.selectMyEnrollment(uid);
        return getDataTable(list);
    }

    /**
     * 获取当前登录用户的总学时余量
     */
    @ApiOperation("获取当前登录用户的总学时余量（需登录）")
    @GetMapping("/enrollments/total")
    public AjaxResult myTotalEnrollments() {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return error("用户不存在");
        }
        Long uid = userInfo.getId();
        List<EnrollmentWithLectureVo> list = studentEnrollmentService.selectMyEnrollment(uid);
        Integer total = 0;
        for (EnrollmentWithLectureVo vo : list) {
            total = total + vo.getRemain();
        }
        return success(total);
    }

    private UserInfo getCurrentUserInfo() {
        String wxUserId = WxMiniUserContext.getCurrentUserId();
        return userInfoService.selectUserInfoByUserId(wxUserId);
    }
}
