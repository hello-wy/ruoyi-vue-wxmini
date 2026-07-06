package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SurveyAnswer;
import com.ruoyi.system.domain.SurveyAssignment;
import com.ruoyi.system.domain.SurveyQuestion;
import com.ruoyi.system.domain.SurveyQuestionOption;
import com.ruoyi.system.domain.SurveySubmission;
import com.ruoyi.system.domain.vo.SurveyAnswerVo;
import com.ruoyi.system.domain.vo.SurveyAssignmentListVo;
import com.ruoyi.system.domain.vo.SurveyAssignmentVo;
import com.ruoyi.system.domain.vo.SurveyCourseGroupVo;
import com.ruoyi.system.domain.vo.SurveyFormVo;
import com.ruoyi.system.domain.vo.SurveyUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SurveyMapper {
    List<SurveyFormVo> selectSurveyForms(@Param("title") String title, @Param("status") Integer status);

    SurveyFormVo selectSurveyFormById(Long formId);

    int countSurveyFormById(Long formId);

    int countLectureById(Long lectureId);

    List<SurveyQuestion> selectQuestionsByFormId(Long formId);

    List<SurveyQuestionOption> selectOptionsByFormId(Long formId);

    List<SurveyCourseGroupVo> selectWxSurveyCourseGroups(Long userInfoId);

    List<SurveyAssignmentVo> selectWxAssignmentsByCourse(@Param("userInfoId") Long userInfoId, @Param("courseId") Long courseId);

    SurveyAssignment selectAssignmentById(Long assignmentId);

    int countAssignment(@Param("formId") Long formId, @Param("lectureId") Long lectureId, @Param("userInfoId") Long userInfoId);

    int insertAssignment(SurveyAssignment assignment);

    int insertSubmission(SurveySubmission submission);

    int insertAnswer(SurveyAnswer answer);

    int updateAssignmentSubmitted(@Param("assignmentId") Long assignmentId, @Param("submissionId") Long submissionId, @Param("submittedAt") java.util.Date submittedAt);

    List<SurveyUserVo> selectWxUsers(@Param("keyword") String keyword);

    List<SurveyAssignmentListVo> selectAssignmentList(@Param("formId") Long formId, @Param("lectureId") Long lectureId, @Param("status") Integer status, @Param("keyword") String keyword);

    SurveyAssignmentListVo selectAssignmentListVoById(Long assignmentId);

    List<SurveyAnswerVo> selectAnswerVosBySubmissionId(Long submissionId);
}
