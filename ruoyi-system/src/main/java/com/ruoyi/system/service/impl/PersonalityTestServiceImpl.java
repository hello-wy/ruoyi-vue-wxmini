package com.ruoyi.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.system.domain.PersonalityTest;
import com.ruoyi.system.domain.PersonalityTestAnswer;
import com.ruoyi.system.domain.PersonalityTestAttempt;
import com.ruoyi.system.domain.PersonalityTestQuestion;
import com.ruoyi.system.domain.vo.PersonalityTestAdminAnswerVo;
import com.ruoyi.system.domain.vo.PersonalityTestAdminAttemptVo;
import com.ruoyi.system.domain.vo.PersonalityTestAdminDetailVo;
import com.ruoyi.system.domain.vo.PersonalityTestAnswerResultVo;
import com.ruoyi.system.domain.vo.PersonalityTestEntryVo;
import com.ruoyi.system.domain.vo.PersonalityTestOptionVo;
import com.ruoyi.system.domain.vo.PersonalityTestQuestionVo;
import com.ruoyi.system.domain.vo.PersonalityTestResultVo;
import com.ruoyi.system.mapper.PersonalityTestAnswerMapper;
import com.ruoyi.system.mapper.PersonalityTestAttemptMapper;
import com.ruoyi.system.mapper.PersonalityTestMapper;
import com.ruoyi.system.mapper.PersonalityTestQuestionMapper;
import com.ruoyi.system.service.IPersonalityTestService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonalityTestServiceImpl implements IPersonalityTestService {
    @Autowired
    private PersonalityTestMapper personalityTestMapper;

    @Autowired
    private PersonalityTestQuestionMapper personalityTestQuestionMapper;

    @Autowired
    private PersonalityTestAttemptMapper personalityTestAttemptMapper;

    @Autowired
    private PersonalityTestAnswerMapper personalityTestAnswerMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public PersonalityTestEntryVo getEntry(Long userInfoId) {
        PersonalityTest test = requireEnabledTest();
        PersonalityTestAttempt attempt = personalityTestAttemptMapper.selectLatestInProgressAttempt(test.getId(), userInfoId);
        return toEntryVo(test, attempt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonalityTestEntryVo startAttempt(Long userInfoId, String wxUserId) {
        PersonalityTest test = requireEnabledTest();
        PersonalityTestAttempt attempt = personalityTestAttemptMapper.selectLatestInProgressAttempt(test.getId(), userInfoId);
        if (attempt == null) {
            Date now = DateUtils.getNowDate();
            attempt = new PersonalityTestAttempt();
            attempt.setTestId(test.getId());
            attempt.setUserInfoId(userInfoId);
            attempt.setWxUserId(wxUserId);
            attempt.setStatus(PersonalityTestAttempt.STATUS_IN_PROGRESS);
            attempt.setAnsweredCount(0);
            attempt.setCurrentQuestionNo(1);
            attempt.setStartedAt(now);
            attempt.setCreateTime(now);
            attempt.setUpdateTime(now);
            personalityTestAttemptMapper.insertAttempt(attempt);
        }
        return toEntryVo(test, attempt);
    }

    @Override
    public PersonalityTestQuestionVo getCurrentQuestion(Long attemptId, Long userInfoId) {
        PersonalityTestAttempt attempt = requireOwnedAttempt(attemptId, userInfoId);
        PersonalityTest test = requireEnabledTest();
        if (!test.getId().equals(attempt.getTestId())) {
            throw new ServiceException("测试记录不存在");
        }
        if (PersonalityTestAttempt.STATUS_COMPLETED.equals(attempt.getStatus())) {
            return null;
        }
        PersonalityTestQuestion question = personalityTestQuestionMapper.selectFirstUnansweredQuestion(attempt.getTestId(), attempt.getId());
        if (question == null) {
            return null;
        }
        return toQuestionVo(attempt.getId(), test, question);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonalityTestAnswerResultVo saveAnswer(Long attemptId, Long userInfoId, Long questionId, Integer answerValue) {
        if (!isValidAnswer(answerValue)) {
            throw new ServiceException("请选择有效答案");
        }
        PersonalityTestAttempt attempt = requireOwnedAttempt(attemptId, userInfoId);
        if (!PersonalityTestAttempt.STATUS_IN_PROGRESS.equals(attempt.getStatus())) {
            throw new ServiceException("当前测试已完成");
        }
        PersonalityTest test = requireEnabledTest();
        if (!test.getId().equals(attempt.getTestId())) {
            throw new ServiceException("测试记录不存在");
        }
        PersonalityTestQuestion question = personalityTestQuestionMapper.selectQuestionById(questionId);
        if (question == null || !test.getId().equals(question.getTestId()) || !PersonalityTestQuestion.STATUS_ENABLED.equals(question.getStatus())) {
            throw new ServiceException("题目不存在");
        }

        Date now = DateUtils.getNowDate();
        PersonalityTestAnswer answer = new PersonalityTestAnswer();
        answer.setAttemptId(attempt.getId());
        answer.setTestId(test.getId());
        answer.setUserInfoId(userInfoId);
        answer.setQuestionId(question.getId());
        answer.setQuestionNo(question.getQuestionNo());
        answer.setAnswerValue(answerValue);
        answer.setCreateTime(now);
        answer.setUpdateTime(now);
        personalityTestAnswerMapper.insertOrUpdateAnswer(answer);

        int answeredCount = personalityTestAnswerMapper.countAnswersByAttemptId(attempt.getId());
        PersonalityTestQuestion nextQuestion = personalityTestQuestionMapper.selectFirstUnansweredQuestion(test.getId(), attempt.getId());
        PersonalityTestAnswerResultVo result = new PersonalityTestAnswerResultVo();
        result.setAttemptId(attempt.getId());
        result.setAnsweredCount(answeredCount);

        attempt.setAnsweredCount(answeredCount);
        attempt.setUpdateTime(now);
        if (nextQuestion == null || answeredCount >= test.getTotalQuestions()) {
            attempt.setStatus(PersonalityTestAttempt.STATUS_COMPLETED);
            attempt.setCurrentQuestionNo(test.getTotalQuestions());
            attempt.setCompletedAt(now);
            personalityTestAttemptMapper.updateAttempt(attempt);
            result.setCompleted(true);
            return result;
        }

        attempt.setCurrentQuestionNo(nextQuestion.getQuestionNo());
        personalityTestAttemptMapper.updateAttempt(attempt);
        result.setCompleted(false);
        result.setNextQuestion(toQuestionVo(attempt.getId(), test, nextQuestion));
        return result;
    }

    @Override
    public PersonalityTestResultVo getResult(Long attemptId, Long userInfoId) {
        PersonalityTestAttempt attempt = requireOwnedAttempt(attemptId, userInfoId);
        PersonalityTest test = requireEnabledTest();
        if (!test.getId().equals(attempt.getTestId())) {
            throw new ServiceException("测试记录不存在");
        }
        PersonalityTestResultVo vo = new PersonalityTestResultVo();
        vo.setAttemptId(attempt.getId());
        vo.setCompleted(PersonalityTestAttempt.STATUS_COMPLETED.equals(attempt.getStatus()));
        vo.setAnsweredCount(attempt.getAnsweredCount());
        vo.setTotalQuestions(test.getTotalQuestions());
        vo.setCompletedAt(attempt.getCompletedAt());
        return vo;
    }

    @Override
    public int countCompletedAttempts() {
        PersonalityTest test = requireEnabledTest();
        return personalityTestAttemptMapper.countCompletedAttempts(test.getId());
    }

    @Override
    public TableDataInfoVo<PersonalityTestAdminAttemptVo> selectAdminAttemptList(Long testId, String userInfoId, Integer status, String startTime, String endTime) {
        PersonalityTest test = requireEnabledTest();
        Long actualTestId = testId == null ? test.getId() : testId;
        PageUtils.startPage();
        List<PersonalityTestAttempt> attempts = personalityTestAttemptMapper.selectAttemptList(actualTestId, userInfoId, status, startTime, endTime);
        PageInfo<PersonalityTestAttempt> pageInfo = new PageInfo<>(attempts);
        List<PersonalityTestAdminAttemptVo> rows = new ArrayList<>();
        for (PersonalityTestAttempt attempt : attempts) {
            rows.add(toAdminAttemptVo(attempt));
        }
        TableDataInfoVo<PersonalityTestAdminAttemptVo> table = new TableDataInfoVo<>();
        table.setCode(200);
        table.setMsg("查询成功");
        table.setTotal(pageInfo.getTotal());
        table.setRows(rows);
        return table;
    }

    @Override
    public PersonalityTestAdminDetailVo selectAdminAttemptDetail(Long attemptId) {
        PersonalityTestAttempt attempt = personalityTestAttemptMapper.selectAttemptById(attemptId);
        if (attempt == null) {
            throw new ServiceException("测试记录不存在");
        }
        PersonalityTest test = requireEnabledTest();
        if (!test.getId().equals(attempt.getTestId())) {
            throw new ServiceException("测试记录不存在");
        }
        UserInfo userInfo = attempt.getUserInfoId() == null ? null : userInfoMapper.selectUserInfoById(attempt.getUserInfoId());
        List<PersonalityTestAnswer> answers = personalityTestAnswerMapper.selectAnswersByAttemptId(attempt.getId());
        Map<Integer, PersonalityTestAnswer> answerMap = new HashMap<>();
        for (PersonalityTestAnswer answer : answers) {
            answerMap.put(answer.getQuestionNo(), answer);
        }
        List<PersonalityTestQuestion> questions = personalityTestQuestionMapper.selectEnabledQuestionsByTestId(test.getId());
        Map<Integer, PersonalityTestQuestion> questionMap = new HashMap<>();
        for (PersonalityTestQuestion question : questions) {
            questionMap.put(question.getQuestionNo(), question);
        }

        List<PersonalityTestAdminAnswerVo> answerVos = new ArrayList<>();
        for (int i = 1; i <= test.getTotalQuestions(); i++) {
            PersonalityTestAdminAnswerVo answerVo = new PersonalityTestAdminAnswerVo();
            answerVo.setQuestionNo(i);
            PersonalityTestQuestion question = questionMap.get(i);
            answerVo.setQuestionContent(question == null ? i + "题" : question.getContent());
            PersonalityTestAnswer answer = answerMap.get(i);
            if (answer == null) {
                answerVo.setAnswerValue(null);
                answerVo.setAnswerLabel("未答");
            } else {
                answerVo.setAnswerValue(answer.getAnswerValue());
                answerVo.setAnswerLabel(answerLabel(answer.getAnswerValue()));
            }
            answerVos.add(answerVo);
        }

        PersonalityTestAdminDetailVo vo = new PersonalityTestAdminDetailVo();
        vo.setAttemptId(attempt.getId());
        vo.setTestId(attempt.getTestId());
        vo.setUserInfoId(attempt.getUserInfoId());
        vo.setUserId(userInfo == null ? null : userInfo.getUserId());
        vo.setUserName(userInfo == null ? null : userInfo.getUserName());
        vo.setRealName(userInfo == null ? null : userInfo.getRealName());
        vo.setStatus(attempt.getStatus());
        vo.setStatusLabel(statusLabel(attempt.getStatus()));
        vo.setAnsweredCount(attempt.getAnsweredCount());
        vo.setTotalQuestions(test.getTotalQuestions());
        vo.setStartedAt(attempt.getStartedAt());
        vo.setCompletedAt(attempt.getCompletedAt());
        vo.setAnswers(answerVos);
        return vo;
    }

    private PersonalityTest requireEnabledTest() {
        PersonalityTest test = personalityTestMapper.selectEnabledPersonalityTest();
        if (test == null) {
            throw new ServiceException("性格测试暂未开放");
        }
        return test;
    }

    private PersonalityTestAttempt requireOwnedAttempt(Long attemptId, Long userInfoId) {
        if (attemptId == null) {
            throw new ServiceException("测试记录不能为空");
        }
        PersonalityTestAttempt attempt = personalityTestAttemptMapper.selectAttemptById(attemptId);
        if (attempt == null || !userInfoId.equals(attempt.getUserInfoId())) {
            throw new ServiceException("测试记录不存在");
        }
        return attempt;
    }

    private PersonalityTestEntryVo toEntryVo(PersonalityTest test, PersonalityTestAttempt attempt) {
        PersonalityTestEntryVo vo = new PersonalityTestEntryVo();
        vo.setTestId(test.getId());
        vo.setTitle(test.getTitle());
        vo.setDescription(test.getDescription());
        vo.setTotalQuestions(test.getTotalQuestions());
        vo.setHasInProgress(attempt != null);
        if (attempt != null) {
            vo.setAttemptId(attempt.getId());
            vo.setAnsweredCount(attempt.getAnsweredCount());
            vo.setCurrentQuestionNo(attempt.getCurrentQuestionNo());
        } else {
            vo.setAnsweredCount(0);
            vo.setCurrentQuestionNo(1);
        }
        return vo;
    }

    private PersonalityTestQuestionVo toQuestionVo(Long attemptId, PersonalityTest test, PersonalityTestQuestion question) {
        PersonalityTestQuestionVo vo = new PersonalityTestQuestionVo();
        vo.setAttemptId(attemptId);
        vo.setTestId(test.getId());
        vo.setQuestionId(question.getId());
        vo.setQuestionNo(question.getQuestionNo());
        vo.setTotalQuestions(test.getTotalQuestions());
        vo.setContent(question.getContent());
        vo.setOptions(options());
        return vo;
    }

    private PersonalityTestAdminAttemptVo toAdminAttemptVo(PersonalityTestAttempt attempt) {
        PersonalityTestAdminAttemptVo vo = new PersonalityTestAdminAttemptVo();
        vo.setAttemptId(attempt.getId());
        vo.setTestId(attempt.getTestId());
        vo.setUserInfoId(attempt.getUserInfoId());
        if (attempt.getUserInfoId() != null) {
            UserInfo userInfo = userInfoMapper.selectUserInfoById(attempt.getUserInfoId());
            if (userInfo != null) {
                vo.setUserId(userInfo.getUserId());
                vo.setUserName(userInfo.getUserName());
                vo.setRealName(userInfo.getRealName());
            }
        }
        vo.setStatus(attempt.getStatus());
        vo.setStatusLabel(statusLabel(attempt.getStatus()));
        vo.setAnsweredCount(attempt.getAnsweredCount());
        vo.setCurrentQuestionNo(attempt.getCurrentQuestionNo());
        vo.setStartedAt(attempt.getStartedAt());
        vo.setCompletedAt(attempt.getCompletedAt());
        return vo;
    }

    private List<PersonalityTestOptionVo> options() {
        return Arrays.asList(
                new PersonalityTestOptionVo("是", PersonalityTestAnswer.ANSWER_YES),
                new PersonalityTestOptionVo("否", PersonalityTestAnswer.ANSWER_NO),
                new PersonalityTestOptionVo("不确定", PersonalityTestAnswer.ANSWER_UNSURE)
        );
    }

    private boolean isValidAnswer(Integer answerValue) {
        return PersonalityTestAnswer.ANSWER_YES.equals(answerValue)
                || PersonalityTestAnswer.ANSWER_NO.equals(answerValue)
                || PersonalityTestAnswer.ANSWER_UNSURE.equals(answerValue);
    }

    private String statusLabel(Integer status) {
        if (PersonalityTestAttempt.STATUS_IN_PROGRESS.equals(status)) {
            return "进行中";
        }
        if (PersonalityTestAttempt.STATUS_COMPLETED.equals(status)) {
            return "已完成";
        }
        if (PersonalityTestAttempt.STATUS_CANCELED.equals(status)) {
            return "已取消";
        }
        return "未知";
    }

    private String answerLabel(Integer answerValue) {
        if (PersonalityTestAnswer.ANSWER_YES.equals(answerValue)) {
            return "是";
        }
        if (PersonalityTestAnswer.ANSWER_NO.equals(answerValue)) {
            return "否";
        }
        if (PersonalityTestAnswer.ANSWER_UNSURE.equals(answerValue)) {
            return "不确定";
        }
        return "否";
    }
}
