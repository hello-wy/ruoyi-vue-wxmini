package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.Questionnaire;
import com.ruoyi.system.domain.StudentEnrollment;
import com.ruoyi.system.domain.vo.EnrollmentWithLectureVo;
import com.ruoyi.system.domain.vo.LecturesDetailVo;
import com.ruoyi.system.domain.vo.LecturesListVo;
import com.ruoyi.system.service.ILecturesService;
import com.ruoyi.system.service.IQuestionnaireService;
import com.ruoyi.system.service.IStudentEnrollmentService;
import com.ruoyi.wxmini.bo.WxGrowupCourseEnrollBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IWxCourseReviewService;
import com.ruoyi.wxmini.service.IWxGrowupPayService;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import com.ruoyi.wxmini.vo.WxCourseReviewPublicVo;
import com.ruoyi.wxmini.vo.WxPayParamVo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxGrowupControllerTest {

    private static final String USER_ID = "user-123";

    @BeforeEach
    void setUpRequestContext() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Mock
    private ILecturesService lecturesService;
    @Mock
    private IStudentEnrollmentService studentEnrollmentService;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private IQuestionnaireService questionnaireService;
    @Mock
    private IWxGrowupPayService wxGrowupPayService;
    @Mock
    private IWxCourseReviewService wxCourseReviewService;

    @InjectMocks
    private WxGrowupController controller;

    @AfterEach
    void clearContext() {
        WxMiniUserContext.clear();
    }

    @Test
    void getCourseListShouldReturn200WhenDatabaseReturnsData() {
        LecturesListVo lecture = new LecturesListVo();
        lecture.setId(1L);
        lecture.setCoverId(9L);
        when(lecturesService.selectRemainingMonthLecturesListVo(any())).thenReturn(Collections.singletonList(lecture));
        when(questionnaireService.selectRecentQuestionnaireList(1L)).thenReturn(Collections.singletonList(new Questionnaire()));

        TableDataInfoVo<LecturesListVo> result = controller.getCourseList(null);
        assertEquals(200, result.getCode());
        assertEquals(9L, result.getRows().get(0).getCoverId());
    }

    @Test
    void getCourseInfoShouldReturn200WhenCourseMissing() {
        when(lecturesService.selectLecturesDetailById(1L)).thenReturn(null);
        AjaxResult result = controller.getCourseInfo(1L);
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void getCourseInfoShouldReturn200WhenCourseExists() {
        LecturesDetailVo vo = new LecturesDetailVo();
        vo.setId(1L);
        vo.setCoverId(9L);
        when(lecturesService.selectLecturesDetailById(1L)).thenReturn(vo);
        AjaxResult result = controller.getCourseInfo(1L);
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertNotNull(result.get(AjaxResult.DATA_TAG));
        assertEquals(9L, ((LecturesDetailVo) result.get(AjaxResult.DATA_TAG)).getCoverId());
    }

    @Test
    void myEnrollmentsShouldReturn200WhenUserMissing() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(null);
        TableDataInfoVo<EnrollmentWithLectureVo> result = controller.myEnrollments();
        assertEquals(200, result.getCode());
    }

    @Test
    void myTotalEnrollmentsShouldReturn200WhenUserExists() {
        EnrollmentWithLectureVo one = new EnrollmentWithLectureVo();
        one.setRemain(3);
        EnrollmentWithLectureVo two = new EnrollmentWithLectureVo();
        two.setRemain(5);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(10L);
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(studentEnrollmentService.selectMyEnrollment(eq(10L))).thenReturn(Arrays.asList(one, two));

        AjaxResult result = controller.myTotalEnrollments();
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void courseEnrollmentShouldReturnCurrentUsersCourseRemain() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(10L);
        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setId(20L);
        enrollment.setLectureId(99L);
        enrollment.setRemain(3);
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(userInfoService.selectUserInfoByUserId(USER_ID)).thenReturn(userInfo);
        when(studentEnrollmentService.selectEnrollmentByUidAndLectureId(10L, 99L)).thenReturn(enrollment);

        AjaxResult result = controller.myCourseEnrollment(99L);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals(enrollment, result.get(AjaxResult.DATA_TAG));
    }

    @Test
    void listCourseReviewsShouldReturnPublicReviewList() {
        WxCourseReviewPublicVo review = new WxCourseReviewPublicVo();
        review.setContent("很有收获");
        when(wxCourseReviewService.listReviews(99L)).thenReturn(Collections.singletonList(review));

        AjaxResult result = controller.listCourseReviews(99L);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals(1, ((java.util.List<?>) result.get(AjaxResult.DATA_TAG)).size());
    }

    @Test
    void enrollCourseShouldReturnWxPayParam() throws Exception {
        WxPayParamVo payParamVo = new WxPayParamVo();
        payParamVo.setOrderNo("GROWUP202606080001");
        WxGrowupCourseEnrollBo bo = new WxGrowupCourseEnrollBo();
        bo.setName("张三");
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(wxGrowupPayService.createCourseOrder(USER_ID, 99L, bo)).thenReturn(payParamVo);

        AjaxResult result = controller.enrollCourse(99L, bo);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals(payParamVo, result.get(AjaxResult.DATA_TAG));
    }
}
