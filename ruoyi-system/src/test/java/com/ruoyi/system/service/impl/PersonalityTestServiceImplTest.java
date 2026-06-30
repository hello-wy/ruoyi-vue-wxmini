package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.PersonalityTest;
import com.ruoyi.system.domain.PersonalityTestAnswer;
import com.ruoyi.system.domain.PersonalityTestAttempt;
import com.ruoyi.system.domain.PersonalityTestQuestion;
import com.ruoyi.system.domain.vo.PersonalityTestResultAnswerVo;
import com.ruoyi.system.domain.vo.PersonalityTestResultVo;
import com.ruoyi.system.mapper.PersonalityTestAnswerMapper;
import com.ruoyi.system.mapper.PersonalityTestAttemptMapper;
import com.ruoyi.system.mapper.PersonalityTestMapper;
import com.ruoyi.system.mapper.PersonalityTestQuestionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonalityTestServiceImplTest {

    private static final Long TEST_ID = 100L;
    private static final Long ATTEMPT_ID = 200L;
    private static final Long USER_INFO_ID = 300L;

    @Mock
    private PersonalityTestMapper personalityTestMapper;

    @Mock
    private PersonalityTestQuestionMapper personalityTestQuestionMapper;

    @Mock
    private PersonalityTestAttemptMapper personalityTestAttemptMapper;

    @Mock
    private PersonalityTestAnswerMapper personalityTestAnswerMapper;

    @Mock
    private com.ruoyi.wxmini.mapper.UserInfoMapper userInfoMapper;

    @InjectMocks
    private PersonalityTestServiceImpl service;

    @Test
    void getResultShouldReturn180AnswersForCompletedAttempt() {
        when(personalityTestMapper.selectEnabledPersonalityTest()).thenReturn(enabledTest());
        when(personalityTestAttemptMapper.selectAttemptById(ATTEMPT_ID)).thenReturn(completedAttempt(180));
        when(personalityTestAnswerMapper.selectAnswersByAttemptId(ATTEMPT_ID)).thenReturn(buildAnswers(180));
        when(personalityTestQuestionMapper.selectQuestionsByIds(anyList())).thenReturn(buildQuestions(180));

        PersonalityTestResultVo result = service.getResult(ATTEMPT_ID, USER_INFO_ID);

        assertEquals(ATTEMPT_ID, result.getAttemptId());
        assertEquals(Integer.valueOf(PersonalityTestAttempt.STATUS_COMPLETED), result.getStatus());
        assertTrue(result.getCompleted());
        assertEquals(Integer.valueOf(180), result.getAnsweredCount());
        assertEquals(Integer.valueOf(180), result.getTotalQuestions());
        assertEquals(180, result.getAnswers().size());
        assertTrue(result.getAnswers().stream().allMatch(item -> item.getDimensionNo() != null));
    }

    @Test
    void getResultShouldMapAnswerValuesToLabels() {
        when(personalityTestMapper.selectEnabledPersonalityTest()).thenReturn(enabledTest());
        when(personalityTestAttemptMapper.selectAttemptById(ATTEMPT_ID)).thenReturn(completedAttempt(3));
        when(personalityTestAnswerMapper.selectAnswersByAttemptId(ATTEMPT_ID)).thenReturn(buildAnswers(3));
        when(personalityTestQuestionMapper.selectQuestionsByIds(anyList())).thenReturn(buildQuestions(3));

        PersonalityTestResultVo result = service.getResult(ATTEMPT_ID, USER_INFO_ID);
        List<PersonalityTestResultAnswerVo> answers = result.getAnswers();

        assertEquals("是", answers.get(0).getAnswerLabel());
        assertEquals("否", answers.get(1).getAnswerLabel());
        assertEquals("不确定", answers.get(2).getAnswerLabel());
        assertEquals(Integer.valueOf(1), answers.get(0).getAnswerValue());
        assertEquals(Integer.valueOf(0), answers.get(1).getAnswerValue());
        assertEquals(Integer.valueOf(2), answers.get(2).getAnswerValue());
    }

    @Test
    void getResultShouldRejectMissingAttemptId() {
        ServiceException ex = assertThrows(ServiceException.class, () -> service.getResult(null, USER_INFO_ID));
        assertEquals("测试记录不能为空", ex.getMessage());
    }

    @Test
    void getResultShouldRejectOtherUserAttempt() {
        PersonalityTestAttempt attempt = completedAttempt(1);
        attempt.setUserInfoId(USER_INFO_ID + 1);
        when(personalityTestAttemptMapper.selectAttemptById(ATTEMPT_ID)).thenReturn(attempt);

        ServiceException ex = assertThrows(ServiceException.class, () -> service.getResult(ATTEMPT_ID, USER_INFO_ID));
        assertEquals("无权查看该测试记录", ex.getMessage());
    }

    @Test
    void getResultShouldFailOnInvalidDimensionData() {
        when(personalityTestMapper.selectEnabledPersonalityTest()).thenReturn(enabledTest());
        when(personalityTestAttemptMapper.selectAttemptById(ATTEMPT_ID)).thenReturn(completedAttempt(1));
        when(personalityTestAnswerMapper.selectAnswersByAttemptId(ATTEMPT_ID)).thenReturn(buildAnswers(1));
        when(personalityTestQuestionMapper.selectQuestionsByIds(Collections.singletonList(1001L)))
                .thenReturn(buildQuestionsWithInvalidDimension());

        ServiceException ex = assertThrows(ServiceException.class, () -> service.getResult(ATTEMPT_ID, USER_INFO_ID));
        assertEquals("题目维度数据异常", ex.getMessage());
    }

    @Test
    void getResultShouldReturnTruthfulSummaryForIncompleteAttempt() {
        when(personalityTestMapper.selectEnabledPersonalityTest()).thenReturn(enabledTest());
        when(personalityTestAttemptMapper.selectAttemptById(ATTEMPT_ID)).thenReturn(inProgressAttempt(2));
        when(personalityTestAnswerMapper.selectAnswersByAttemptId(ATTEMPT_ID)).thenReturn(buildAnswers(2));
        when(personalityTestQuestionMapper.selectQuestionsByIds(anyList())).thenReturn(buildQuestions(2));

        PersonalityTestResultVo result = service.getResult(ATTEMPT_ID, USER_INFO_ID);

        assertFalse(result.getCompleted());
        assertEquals(Integer.valueOf(PersonalityTestAttempt.STATUS_IN_PROGRESS), result.getStatus());
        assertEquals(Integer.valueOf(2), result.getAnsweredCount());
        assertEquals(2, result.getAnswers().size());
        assertNull(result.getCompletedAt());
    }

    private PersonalityTest enabledTest() {
        PersonalityTest test = new PersonalityTest();
        test.setId(TEST_ID);
        test.setTotalQuestions(180);
        test.setTitle("天人合一·性格测试");
        return test;
    }

    private PersonalityTestAttempt completedAttempt(int answeredCount) {
        PersonalityTestAttempt attempt = new PersonalityTestAttempt();
        attempt.setId(ATTEMPT_ID);
        attempt.setTestId(TEST_ID);
        attempt.setUserInfoId(USER_INFO_ID);
        attempt.setStatus(PersonalityTestAttempt.STATUS_COMPLETED);
        attempt.setAnsweredCount(answeredCount);
        attempt.setCompletedAt(new Date());
        return attempt;
    }

    private PersonalityTestAttempt inProgressAttempt(int answeredCount) {
        PersonalityTestAttempt attempt = new PersonalityTestAttempt();
        attempt.setId(ATTEMPT_ID);
        attempt.setTestId(TEST_ID);
        attempt.setUserInfoId(USER_INFO_ID);
        attempt.setStatus(PersonalityTestAttempt.STATUS_IN_PROGRESS);
        attempt.setAnsweredCount(answeredCount);
        attempt.setCompletedAt(null);
        return attempt;
    }

    private List<PersonalityTestAnswer> buildAnswers(int count) {
        List<PersonalityTestAnswer> answers = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            PersonalityTestAnswer answer = new PersonalityTestAnswer();
            answer.setId((long) i);
            answer.setAttemptId(ATTEMPT_ID);
            answer.setTestId(TEST_ID);
            answer.setUserInfoId(USER_INFO_ID);
            answer.setQuestionId(1000L + i);
            answer.setQuestionNo(i);
            answer.setAnswerValue(answerValueForIndex(i));
            answers.add(answer);
        }
        return answers;
    }

    private List<PersonalityTestQuestion> buildQuestions(int count) {
        List<PersonalityTestQuestion> questions = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            PersonalityTestQuestion question = new PersonalityTestQuestion();
            question.setId(1000L + i);
            question.setTestId(TEST_ID);
            question.setQuestionNo(i);
            question.setDimensionNo(((i - 1) % 9) + 1);
            question.setContent("题目" + i);
            question.setStatus(PersonalityTestQuestion.STATUS_ENABLED);
            questions.add(question);
        }
        return questions;
    }

    private List<PersonalityTestQuestion> buildQuestionsWithInvalidDimension() {
        List<PersonalityTestQuestion> questions = new ArrayList<>();
        PersonalityTestQuestion question = new PersonalityTestQuestion();
        question.setId(1001L);
        question.setTestId(TEST_ID);
        question.setQuestionNo(1);
        question.setDimensionNo(0);
        question.setContent("题目1");
        question.setStatus(PersonalityTestQuestion.STATUS_ENABLED);
        questions.add(question);
        return questions;
    }

    private Integer answerValueForIndex(int index) {
        int mod = index % 3;
        if (mod == 1) {
            return PersonalityTestAnswer.ANSWER_YES;
        }
        if (mod == 2) {
            return PersonalityTestAnswer.ANSWER_NO;
        }
        return PersonalityTestAnswer.ANSWER_UNSURE;
    }
}
