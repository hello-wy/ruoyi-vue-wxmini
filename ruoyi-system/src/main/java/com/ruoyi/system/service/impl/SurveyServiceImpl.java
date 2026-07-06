package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.system.domain.SurveyAnswer;
import com.ruoyi.system.domain.SurveyAssignment;
import com.ruoyi.system.domain.SurveyQuestion;
import com.ruoyi.system.domain.SurveyQuestionOption;
import com.ruoyi.system.domain.SurveySubmission;
import com.ruoyi.system.domain.vo.SurveyAnswerVo;
import com.ruoyi.system.domain.vo.SurveyAssignmentDetailVo;
import com.ruoyi.system.domain.vo.SurveyAssignmentListVo;
import com.ruoyi.system.domain.vo.SurveyAssignmentVo;
import com.ruoyi.system.domain.vo.SurveyCourseGroupVo;
import com.ruoyi.system.domain.vo.SurveyCourseVo;
import com.ruoyi.system.domain.vo.SurveyDistributeResultVo;
import com.ruoyi.system.domain.vo.SurveyFormVo;
import com.ruoyi.system.domain.vo.SurveyOptionVo;
import com.ruoyi.system.domain.vo.SurveyQuestionVo;
import com.ruoyi.system.domain.vo.SurveySubmitResultVo;
import com.ruoyi.system.domain.vo.SurveyUserVo;
import com.ruoyi.system.mapper.SurveyMapper;
import com.ruoyi.system.service.ISurveyService;
import com.ruoyi.system.domain.bo.SurveyAnswerBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.mapper.UserInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SurveyServiceImpl implements ISurveyService {
    private static final String TYPE_SINGLE = "single";
    private static final String TYPE_MULTIPLE = "multiple";
    private static final String TYPE_TEXT = "text";
    private static final String TYPE_MATRIX_SINGLE = "matrix_single";

    @Autowired
    private SurveyMapper surveyMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public TableDataInfoVo<SurveyFormVo> selectSurveyFormList(String title, Integer status) {
        PageUtils.startPage();
        List<SurveyFormVo> forms = surveyMapper.selectSurveyForms(title, status);
        PageInfo<SurveyFormVo> pageInfo = new PageInfo<>(forms);
        return table(forms, pageInfo.getTotal());
    }

    @Override
    public SurveyFormVo selectSurveyFormDetail(Long formId) {
        SurveyFormVo form = surveyMapper.selectSurveyFormById(formId);
        if (form == null) {
            throw new ServiceException("问卷不存在");
        }
        form.setQuestions(buildQuestionTree(formId));
        return form;
    }

    @Override
    public TableDataInfoVo<SurveyUserVo> searchWxUsers(String keyword) {
        PageUtils.startPage();
        List<SurveyUserVo> users = surveyMapper.selectWxUsers(keyword);
        PageInfo<SurveyUserVo> pageInfo = new PageInfo<>(users);
        return table(users, pageInfo.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SurveyDistributeResultVo distributeSurvey(Long formId, Long lectureId, List<Long> userInfoIds, String assignedBy) {
        if (formId == null || surveyMapper.countSurveyFormById(formId) == 0) {
            throw new ServiceException("问卷不存在或已停用");
        }
        if (lectureId == null || surveyMapper.countLectureById(lectureId) == 0) {
            throw new ServiceException("课程不存在");
        }
        if (userInfoIds == null || userInfoIds.isEmpty()) {
            throw new ServiceException("请选择分发用户");
        }

        Date now = DateUtils.getNowDate();
        SurveyDistributeResultVo result = new SurveyDistributeResultVo();
        Set<Long> handled = new HashSet<>();
        for (Long userInfoId : userInfoIds) {
            if (userInfoId == null || !handled.add(userInfoId)) {
                result.setSkippedCount(result.getSkippedCount() + 1);
                continue;
            }
            UserInfo userInfo = userInfoMapper.selectUserInfoById(userInfoId);
            if (userInfo == null || surveyMapper.countAssignment(formId, lectureId, userInfoId) > 0) {
                result.setSkippedCount(result.getSkippedCount() + 1);
                continue;
            }
            SurveyAssignment assignment = new SurveyAssignment();
            assignment.setFormId(formId);
            assignment.setLectureId(lectureId);
            assignment.setUserInfoId(userInfoId);
            assignment.setWxUserId(userInfo.getUserId());
            assignment.setStatus(SurveyAssignment.STATUS_PENDING);
            assignment.setAssignedBy(assignedBy);
            assignment.setAssignedAt(now);
            assignment.setCreateTime(now);
            assignment.setUpdateTime(now);
            surveyMapper.insertAssignment(assignment);
            result.setCreatedCount(result.getCreatedCount() + 1);
        }
        return result;
    }

    @Override
    public TableDataInfoVo<SurveyAssignmentListVo> selectAssignmentList(Long formId, Long lectureId, Integer status, String keyword) {
        if (formId == null) {
            throw new ServiceException("请选择问卷");
        }
        PageUtils.startPage();
        List<SurveyAssignmentListVo> rows = surveyMapper.selectAssignmentList(formId, lectureId, status, keyword);
        PageInfo<SurveyAssignmentListVo> pageInfo = new PageInfo<>(rows);
        return table(rows, pageInfo.getTotal());
    }

    @Override
    public SurveyAssignmentDetailVo selectAssignmentDetail(Long assignmentId) {
        SurveyAssignment assignment = requireAssignment(assignmentId);
        SurveyAssignmentListVo row = surveyMapper.selectAssignmentListVoById(assignmentId);
        if (row == null) {
            throw new ServiceException("分发记录不存在");
        }
        SurveyFormVo form = surveyMapper.selectSurveyFormById(row.getFormId());
        if (form == null) {
            throw new ServiceException("问卷不存在");
        }

        SurveyAssignmentDetailVo detail = new SurveyAssignmentDetailVo();
        detail.setAssignmentId(row.getAssignmentId());
        detail.setSubmissionId(assignment.getSubmissionId());
        detail.setStatus(row.getStatus());
        detail.setStatusLabel(row.getStatusLabel());
        detail.setSubmittedAt(row.getSubmittedAt());

        SurveyCourseVo course = new SurveyCourseVo();
        course.setId(row.getCourseId());
        course.setName(row.getCourseName());
        detail.setCourse(course);

        SurveyUserVo user = new SurveyUserVo();
        user.setUserInfoId(row.getUserInfoId());
        user.setUserId(row.getUserId());
        user.setUserName(row.getUserName());
        user.setRealName(row.getRealName());
        user.setPhone(row.getPhone());
        detail.setUser(user);

        SurveyFormVo formInfo = new SurveyFormVo();
        formInfo.setFormId(form.getFormId());
        formInfo.setTitle(form.getTitle());
        formInfo.setTotalQuestions(form.getTotalQuestions());
        detail.setForm(formInfo);

        if (assignment.getSubmissionId() != null) {
            List<SurveyAnswerVo> answers = surveyMapper.selectAnswerVosBySubmissionId(assignment.getSubmissionId());
            fillAnswerLabels(answers, assignment.getFormId());
            detail.setAnswers(answers);
        }
        return detail;
    }

    @Override
    public List<SurveyCourseGroupVo> selectWxAssignments(Long userInfoId) {
        List<SurveyCourseGroupVo> groups = surveyMapper.selectWxSurveyCourseGroups(userInfoId);
        for (SurveyCourseGroupVo group : groups) {
            List<SurveyAssignmentVo> surveys = surveyMapper.selectWxAssignmentsByCourse(userInfoId, group.getCourseId());
            group.setSurveys(surveys);
        }
        return groups;
    }

    @Override
    public SurveyFormVo selectWxAssignmentForm(Long assignmentId, Long userInfoId) {
        SurveyAssignment assignment = requireOwnedAssignment(assignmentId, userInfoId);
        SurveyFormVo form = selectSurveyFormDetail(assignment.getFormId());
        SurveyAssignmentListVo row = surveyMapper.selectAssignmentListVoById(assignmentId);
        form.setAssignmentId(assignmentId);
        form.setStatus(assignment.getStatus());
        if (row != null) {
            SurveyCourseVo course = new SurveyCourseVo();
            course.setId(row.getCourseId());
            course.setName(row.getCourseName());
            form.setCourse(course);
        }
        return form;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SurveySubmitResultVo submitWxAssignment(Long assignmentId, Long userInfoId, String wxUserId, List<SurveyAnswerBo> answers) {
        SurveyAssignment assignment = requireOwnedAssignment(assignmentId, userInfoId);
        if (!SurveyAssignment.STATUS_PENDING.equals(assignment.getStatus())) {
            throw new ServiceException("问卷已提交");
        }
        if (answers == null || answers.isEmpty()) {
            throw new ServiceException("请填写问卷");
        }

        List<SurveyQuestion> questions = surveyMapper.selectQuestionsByFormId(assignment.getFormId());
        List<SurveyQuestionOption> options = surveyMapper.selectOptionsByFormId(assignment.getFormId());
        Map<Long, SurveyQuestion> questionMap = new HashMap<>();
        for (SurveyQuestion question : questions) {
            questionMap.put(question.getId(), question);
        }
        Map<Long, List<SurveyQuestionOption>> optionMap = groupOptions(options);
        Map<Long, SurveyAnswerBo> answerMap = new HashMap<>();
        for (SurveyAnswerBo answer : answers) {
            if (answer != null && answer.getQuestionId() != null) {
                answerMap.put(answer.getQuestionId(), answer);
            }
        }
        validateAnswers(questions, optionMap, answerMap);

        Date now = DateUtils.getNowDate();
        SurveySubmission submission = new SurveySubmission();
        submission.setFormId(assignment.getFormId());
        submission.setUserInfoId(userInfoId);
        submission.setWxUserId(wxUserId);
        submission.setStatus(1);
        submission.setSubmittedAt(now);
        submission.setCreateTime(now);
        submission.setUpdateTime(now);
        surveyMapper.insertSubmission(submission);

        for (SurveyAnswerBo bo : answers) {
            if (bo == null || bo.getQuestionId() == null || !questionMap.containsKey(bo.getQuestionId())) {
                continue;
            }
            SurveyAnswer answer = new SurveyAnswer();
            answer.setSubmissionId(submission.getId());
            answer.setQuestionId(bo.getQuestionId());
            answer.setAnswerValue(toAnswerValueJson(bo.getAnswerValue()));
            answer.setAnswerText(StringUtils.trimToNull(bo.getAnswerText()));
            answer.setCreateTime(now);
            answer.setUpdateTime(now);
            surveyMapper.insertAnswer(answer);
        }
        surveyMapper.updateAssignmentSubmitted(assignmentId, submission.getId(), now);

        SurveySubmitResultVo result = new SurveySubmitResultVo();
        result.setAssignmentId(assignmentId);
        result.setSubmissionId(submission.getId());
        result.setSubmittedAt(now);
        return result;
    }

    private SurveyAssignment requireAssignment(Long assignmentId) {
        if (assignmentId == null) {
            throw new ServiceException("分发记录不存在");
        }
        SurveyAssignment assignment = surveyMapper.selectAssignmentById(assignmentId);
        if (assignment == null) {
            throw new ServiceException("分发记录不存在");
        }
        return assignment;
    }

    private SurveyAssignment requireOwnedAssignment(Long assignmentId, Long userInfoId) {
        SurveyAssignment assignment = requireAssignment(assignmentId);
        if (!userInfoId.equals(assignment.getUserInfoId())) {
            throw new ServiceException("无权访问该问卷");
        }
        return assignment;
    }

    private List<SurveyQuestionVo> buildQuestionTree(Long formId) {
        List<SurveyQuestion> questions = surveyMapper.selectQuestionsByFormId(formId);
        Map<Long, List<SurveyQuestionOption>> optionMap = groupOptions(surveyMapper.selectOptionsByFormId(formId));
        Map<Long, SurveyQuestionVo> voMap = new LinkedHashMap<>();
        List<SurveyQuestionVo> roots = new ArrayList<>();
        for (SurveyQuestion question : questions) {
            SurveyQuestionVo vo = toQuestionVo(question, optionMap.get(question.getId()));
            voMap.put(question.getId(), vo);
        }
        for (SurveyQuestion question : questions) {
            SurveyQuestionVo vo = voMap.get(question.getId());
            if (question.getParentQuestionId() != null && voMap.containsKey(question.getParentQuestionId())) {
                voMap.get(question.getParentQuestionId()).getChildren().add(vo);
            } else {
                roots.add(vo);
            }
        }
        return roots;
    }

    private SurveyQuestionVo toQuestionVo(SurveyQuestion question, List<SurveyQuestionOption> options) {
        SurveyQuestionVo vo = new SurveyQuestionVo();
        vo.setQuestionId(question.getId());
        vo.setQuestionNo(question.getQuestionNo());
        vo.setSectionTitle(question.getSectionTitle());
        vo.setQuestionType(question.getQuestionType());
        vo.setContent(question.getContent());
        vo.setInputPlaceholder(question.getInputPlaceholder());
        vo.setRequiredFlag(question.getRequiredFlag());
        List<SurveyOptionVo> optionVos = new ArrayList<>();
        if (options != null) {
            for (SurveyQuestionOption option : options) {
                SurveyOptionVo optionVo = new SurveyOptionVo();
                optionVo.setValue(option.getOptionValue());
                optionVo.setLabel(option.getOptionLabel());
                optionVo.setAllowTextInput(option.getAllowTextInput());
                optionVos.add(optionVo);
            }
        }
        vo.setOptions(optionVos);
        return vo;
    }

    private Map<Long, List<SurveyQuestionOption>> groupOptions(List<SurveyQuestionOption> options) {
        Map<Long, List<SurveyQuestionOption>> optionMap = new HashMap<>();
        for (SurveyQuestionOption option : options) {
            optionMap.computeIfAbsent(option.getQuestionId(), key -> new ArrayList<>()).add(option);
        }
        return optionMap;
    }

    private void validateAnswers(List<SurveyQuestion> questions, Map<Long, List<SurveyQuestionOption>> optionMap, Map<Long, SurveyAnswerBo> answerMap) {
        for (SurveyQuestion question : questions) {
            if (TYPE_MATRIX_SINGLE.equals(question.getQuestionType())) {
                continue;
            }
            SurveyAnswerBo answer = answerMap.get(question.getId());
            boolean required = Integer.valueOf(1).equals(question.getRequiredFlag());
            if (required && isBlankAnswer(answer)) {
                throw new ServiceException("请完成必答题" + question.getQuestionNo());
            }
            if (answer == null || isBlankAnswer(answer)) {
                continue;
            }
            validateAnswerValue(question, optionMap.get(question.getId()), answer.getAnswerValue());
        }
    }

    private boolean isBlankAnswer(SurveyAnswerBo answer) {
        if (answer == null) {
            return true;
        }
        Object value = answer.getAnswerValue();
        if (value instanceof List) {
            return ((List<?>) value).isEmpty();
        }
        return value == null && StringUtils.isBlank(answer.getAnswerText());
    }

    private void validateAnswerValue(SurveyQuestion question, List<SurveyQuestionOption> options, Object value) {
        if (TYPE_TEXT.equals(question.getQuestionType())) {
            return;
        }
        Set<String> validValues = new HashSet<>();
        if (options != null) {
            for (SurveyQuestionOption option : options) {
                validValues.add(option.getOptionValue());
            }
        }
        if (TYPE_SINGLE.equals(question.getQuestionType())) {
            if (value == null || !validValues.contains(String.valueOf(value))) {
                throw new ServiceException("请选择有效答案");
            }
            return;
        }
        if (TYPE_MULTIPLE.equals(question.getQuestionType())) {
            if (!(value instanceof List) || ((List<?>) value).isEmpty()) {
                throw new ServiceException("请选择有效答案");
            }
            for (Object item : (List<?>) value) {
                if (!validValues.contains(String.valueOf(item))) {
                    throw new ServiceException("请选择有效答案");
                }
            }
        }
    }

    private String toAnswerValueJson(Object answerValue) {
        if (answerValue == null) {
            return null;
        }
        return JSON.toJSONString(answerValue);
    }

    private void fillAnswerLabels(List<SurveyAnswerVo> answers, Long formId) {
        Map<Long, List<SurveyQuestionOption>> optionMap = groupOptions(surveyMapper.selectOptionsByFormId(formId));
        for (SurveyAnswerVo answer : answers) {
            List<SurveyQuestionOption> options = optionMap.get(answer.getQuestionId());
            if (options == null || StringUtils.isBlank(answer.getAnswerValue())) {
                continue;
            }
            Object value = JSON.parse(answer.getAnswerValue());
            List<String> labels = new ArrayList<>();
            if (value instanceof List) {
                for (Object item : (List<?>) value) {
                    addOptionLabel(labels, options, String.valueOf(item));
                }
            } else {
                addOptionLabel(labels, options, String.valueOf(value));
            }
            answer.setAnswerLabel(StringUtils.join(labels, "、"));
        }
    }

    private void addOptionLabel(List<String> labels, List<SurveyQuestionOption> options, String value) {
        for (SurveyQuestionOption option : options) {
            if (option.getOptionValue().equals(value)) {
                labels.add(option.getOptionLabel());
                return;
            }
        }
    }

    private <T> TableDataInfoVo<T> table(List<T> rows, long total) {
        TableDataInfoVo<T> table = new TableDataInfoVo<>();
        table.setCode(200);
        table.setMsg("查询成功");
        table.setTotal(total);
        table.setRows(rows);
        return table;
    }
}
