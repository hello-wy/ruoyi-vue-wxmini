package com.ruoyi.system.service;

import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.vo.SurveyAssignmentDetailVo;
import com.ruoyi.system.domain.vo.SurveyAssignmentListVo;
import com.ruoyi.system.domain.vo.SurveyCourseGroupVo;
import com.ruoyi.system.domain.vo.SurveyDistributeResultVo;
import com.ruoyi.system.domain.vo.SurveyFormVo;
import com.ruoyi.system.domain.vo.SurveySubmitResultVo;
import com.ruoyi.system.domain.vo.SurveyUserVo;
import com.ruoyi.system.domain.bo.SurveyAnswerBo;

import java.util.List;

public interface ISurveyService {
    TableDataInfoVo<SurveyFormVo> selectSurveyFormList(String title, Integer status);

    SurveyFormVo selectSurveyFormDetail(Long formId);

    TableDataInfoVo<SurveyUserVo> searchWxUsers(String keyword);

    SurveyDistributeResultVo distributeSurvey(Long formId, Long lectureId, List<Long> userInfoIds, String assignedBy);

    TableDataInfoVo<SurveyAssignmentListVo> selectAssignmentList(Long formId, Long lectureId, Integer status, String keyword);

    SurveyAssignmentDetailVo selectAssignmentDetail(Long assignmentId);

    List<SurveyCourseGroupVo> selectWxAssignments(Long userInfoId);

    SurveyFormVo selectWxAssignmentForm(Long assignmentId, Long userInfoId);

    SurveySubmitResultVo submitWxAssignment(Long assignmentId, Long userInfoId, String wxUserId, List<SurveyAnswerBo> answers);
}
