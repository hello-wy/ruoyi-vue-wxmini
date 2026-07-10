package com.ruoyi.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.system.domain.bo.PersonalityTestAnswerBo;
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
import com.ruoyi.system.domain.vo.PersonalityTestReportItemVo;
import com.ruoyi.system.domain.vo.PersonalityTestResultAnswerVo;
import com.ruoyi.system.domain.vo.PersonalityTestResultVo;
import com.ruoyi.system.domain.vo.PersonalityTestScoreVo;
import com.ruoyi.system.mapper.PersonalityTestAnswerMapper;
import com.ruoyi.system.mapper.PersonalityTestAttemptMapper;
import com.ruoyi.system.mapper.PersonalityTestMapper;
import com.ruoyi.system.mapper.PersonalityTestQuestionMapper;
import com.ruoyi.system.service.IPersonalityTestService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.mapper.UserInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonalityTestServiceImpl implements IPersonalityTestService {
    private static final Logger log = LoggerFactory.getLogger(PersonalityTestServiceImpl.class);
    private static final PersonalityReportCopy[][] REPORT_COPIES = buildReportCopies();

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
    public PersonalityTestEntryVo startAttempt(Long userInfoId, String wxUserId, boolean restart) {
        PersonalityTest test = requireEnabledTest();
        PersonalityTestAttempt attempt = personalityTestAttemptMapper.selectLatestInProgressAttempt(test.getId(), userInfoId);
        if (attempt != null && restart) {
            Date now = DateUtils.getNowDate();
            personalityTestAnswerMapper.deleteAnswersByAttemptId(attempt.getId());
            attempt.setStatus(PersonalityTestAttempt.STATUS_CANCELED);
            attempt.setAnsweredCount(0);
            attempt.setCurrentQuestionNo(1);
            attempt.setUpdateTime(now);
            personalityTestAttemptMapper.updateAttempt(attempt);
            attempt = null;
        }
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
    public PersonalityTestQuestionVo getQuestion(Long attemptId, Long userInfoId, Integer questionNo) {
        PersonalityTestAttempt attempt = requireOwnedAttempt(attemptId, userInfoId);
        PersonalityTest test = requireEnabledAttemptTest(attempt);
        if (questionNo == null || questionNo < 1 || questionNo > test.getTotalQuestions()) {
            throw new ServiceException("题号不存在");
        }
        if (PersonalityTestAttempt.STATUS_COMPLETED.equals(attempt.getStatus())) {
            return null;
        }
        PersonalityTestQuestion question = personalityTestQuestionMapper.selectQuestionByNo(test.getId(), questionNo);
        if (question == null) {
            throw new ServiceException("题目不存在");
        }
        return toQuestionVo(attempt.getId(), test, question);
    }

    @Override
    public List<PersonalityTestQuestionVo> getQuestions(Long attemptId, Long userInfoId) {
        PersonalityTestAttempt attempt = requireOwnedAttempt(attemptId, userInfoId);
        PersonalityTest test = requireEnabledAttemptTest(attempt);
        List<PersonalityTestAnswer> answers = personalityTestAnswerMapper.selectAnswersByAttemptId(attempt.getId());
        Map<Long, Integer> answerMap = new HashMap<>();
        for (PersonalityTestAnswer answer : answers) {
            answerMap.put(answer.getQuestionId(), answer.getAnswerValue());
        }
        List<PersonalityTestQuestion> questions = personalityTestQuestionMapper.selectEnabledQuestionsByTestId(test.getId());
        List<PersonalityTestQuestionVo> vos = new ArrayList<>(questions.size());
        for (PersonalityTestQuestion question : questions) {
            PersonalityTestQuestionVo vo = toQuestionVo(attempt.getId(), test, question);
            Integer answerValue = answerMap.get(question.getId());
            vo.setAnswerValue(answerValue);
            vo.setSelectedOptionId(answerValue);
            vo.setAnswerOptionId(answerValue);
            vos.add(vo);
        }
        return vos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonalityTestAnswerResultVo saveAnswer(Long attemptId, Long userInfoId, Long questionId, Integer answerValue) {
        PersonalityTestAnswerBo answer = new PersonalityTestAnswerBo();
        answer.setQuestionId(questionId);
        answer.setAnswerValue(answerValue);
        return saveAnswers(attemptId, userInfoId, Arrays.asList(answer));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonalityTestAnswerResultVo saveAnswers(Long attemptId, Long userInfoId, List<PersonalityTestAnswerBo> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new ServiceException("答案列表不能为空");
        }
        PersonalityTestAttempt attempt = requireOwnedAttempt(attemptId, userInfoId);
        if (!PersonalityTestAttempt.STATUS_IN_PROGRESS.equals(attempt.getStatus())) {
            throw new ServiceException("当前测试已完成");
        }
        PersonalityTest test = requireEnabledAttemptTest(attempt);
        Date now = DateUtils.getNowDate();
        for (PersonalityTestAnswerBo answerBo : answers) {
            saveOneAnswer(attempt, test, userInfoId, answerBo, now);
        }
        return refreshAttemptProgress(attempt, test, now);
    }

    @Override
    public PersonalityTestResultVo getResult(Long attemptId, Long userInfoId) {
        PersonalityTestAttempt attempt = requireOwnedAttempt(attemptId, userInfoId);
        PersonalityTest test = requireEnabledTest();
        if (!test.getId().equals(attempt.getTestId())) {
            throw new ServiceException("测试记录不存在");
        }

        List<PersonalityTestAnswer> answers = personalityTestAnswerMapper.selectAnswersByAttemptId(attempt.getId());
        List<Long> questionIds = new ArrayList<>(answers.size());
        for (PersonalityTestAnswer answer : answers) {
            questionIds.add(answer.getQuestionId());
        }
        List<PersonalityTestQuestion> questions = questionIds.isEmpty()
                ? new ArrayList<>()
                : personalityTestQuestionMapper.selectQuestionsByIds(questionIds);
        List<PersonalityTestResultAnswerVo> answerVos = buildResultAnswers(attempt.getId(), answers, questions);
        int actualAnsweredCount = answerVos.size();

        if (attempt.getAnsweredCount() != null && !attempt.getAnsweredCount().equals(actualAnsweredCount)) {
            log.warn("性格测试答题数与记录不一致: attemptId={}, storedAnsweredCount={}, actualAnsweredCount={}",
                    attempt.getId(), attempt.getAnsweredCount(), actualAnsweredCount);
        }

        int[][] counts = countAnswersByDimension(answerVos);
        PersonalityTestResultVo vo = new PersonalityTestResultVo();
        vo.setAttemptId(attempt.getId());
        vo.setStatus(attempt.getStatus());
        vo.setCompleted(PersonalityTestAttempt.STATUS_COMPLETED.equals(attempt.getStatus()));
        vo.setAnsweredCount(actualAnsweredCount);
        vo.setTotalQuestions(test.getTotalQuestions());
        vo.setCompletedAt(attempt.getCompletedAt());
        vo.setAnswers(answerVos);
        vo.setScores(buildScores(counts));
        vo.setReports(buildReports(counts));
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
                answerVo.setAnswerLabel(answerLabel(answer.getAnswerValue(), attempt.getId(), answer.getQuestionId()));
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

    private PersonalityTest requireEnabledAttemptTest(PersonalityTestAttempt attempt) {
        PersonalityTest test = requireEnabledTest();
        if (!test.getId().equals(attempt.getTestId())) {
            throw new ServiceException("测试记录不存在");
        }
        return test;
    }

    private PersonalityTestAttempt requireAttemptById(Long attemptId) {
        if (attemptId == null) {
            throw new ServiceException("测试记录不能为空");
        }
        PersonalityTestAttempt attempt = personalityTestAttemptMapper.selectAttemptById(attemptId);
        if (attempt == null) {
            throw new ServiceException("测试记录不存在");
        }
        return attempt;
    }

    private PersonalityTestAttempt requireOwnedAttempt(Long attemptId, Long userInfoId) {
        PersonalityTestAttempt attempt = requireAttemptById(attemptId);
        if (!userInfoId.equals(attempt.getUserInfoId())) {
            throw new ServiceException("无权查看该测试记录");
        }
        return attempt;
    }

    private void saveOneAnswer(PersonalityTestAttempt attempt, PersonalityTest test, Long userInfoId,
                               PersonalityTestAnswerBo answerBo, Date now) {
        if (answerBo == null || answerBo.getQuestionId() == null) {
            throw new ServiceException("题目不能为空");
        }
        if (!isValidAnswer(answerBo.getAnswerValue())) {
            throw new ServiceException("请选择有效答案");
        }
        PersonalityTestQuestion question = personalityTestQuestionMapper.selectQuestionById(answerBo.getQuestionId());
        if (question == null || !test.getId().equals(question.getTestId()) || !PersonalityTestQuestion.STATUS_ENABLED.equals(question.getStatus())) {
            throw new ServiceException("题目不存在");
        }

        PersonalityTestAnswer answer = new PersonalityTestAnswer();
        answer.setAttemptId(attempt.getId());
        answer.setTestId(test.getId());
        answer.setUserInfoId(userInfoId);
        answer.setQuestionId(question.getId());
        answer.setQuestionNo(question.getQuestionNo());
        answer.setAnswerValue(answerBo.getAnswerValue());
        answer.setCreateTime(now);
        answer.setUpdateTime(now);
        personalityTestAnswerMapper.insertOrUpdateAnswer(answer);
    }

    private PersonalityTestAnswerResultVo refreshAttemptProgress(PersonalityTestAttempt attempt, PersonalityTest test, Date now) {
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

    private List<PersonalityTestResultAnswerVo> buildResultAnswers(Long attemptId,
                                                                    List<PersonalityTestAnswer> answers,
                                                                    List<PersonalityTestQuestion> questions) {
        Map<Long, PersonalityTestQuestion> questionMap = new HashMap<>();
        for (PersonalityTestQuestion question : questions) {
            if (question == null || question.getId() == null) {
                log.error("性格测试结果题目数据异常: attemptId={}, question={}", attemptId, question);
                throw new ServiceException("题目数据异常");
            }
            if (question.getQuestionNo() == null) {
                log.error("性格测试结果题号缺失: attemptId={}, questionId={}", attemptId, question.getId());
                throw new ServiceException("题目数据异常");
            }
            if (!isValidDimensionNo(question.getDimensionNo())) {
                log.error("性格测试题目维度异常: attemptId={}, questionId={}, dimensionNo={}",
                        attemptId, question.getId(), question.getDimensionNo());
                throw new ServiceException("题目维度数据异常");
            }
            questionMap.put(question.getId(), question);
        }

        List<PersonalityTestResultAnswerVo> answerVos = new ArrayList<>(answers.size());
        for (PersonalityTestAnswer answer : answers) {
            if (answer == null || answer.getQuestionId() == null) {
                log.error("性格测试答题记录异常: attemptId={}, answer={}", attemptId, answer);
                throw new ServiceException("答题记录数据异常");
            }
            PersonalityTestQuestion question = questionMap.get(answer.getQuestionId());
            if (question == null) {
                log.error("性格测试结果缺少题目信息: attemptId={}, questionId={}", attemptId, answer.getQuestionId());
                throw new ServiceException("答题记录题目信息不存在");
            }
            if (answer.getQuestionNo() != null && !answer.getQuestionNo().equals(question.getQuestionNo())) {
                log.error("性格测试答题题号不一致: attemptId={}, questionId={}, answerQuestionNo={}, actualQuestionNo={}",
                        attemptId, answer.getQuestionId(), answer.getQuestionNo(), question.getQuestionNo());
                throw new ServiceException("答题记录题号不一致");
            }
            PersonalityTestResultAnswerVo answerVo = new PersonalityTestResultAnswerVo();
            answerVo.setQuestionId(question.getId());
            answerVo.setQuestionNo(question.getQuestionNo());
            answerVo.setDimensionNo(question.getDimensionNo());
            answerVo.setAnswerValue(answer.getAnswerValue());
            answerVo.setAnswerLabel(answerLabel(answer.getAnswerValue(), attemptId, question.getId()));
            answerVos.add(answerVo);
        }
        return answerVos;
    }

    private int[][] countAnswersByDimension(List<PersonalityTestResultAnswerVo> answerVos) {
        int[][] counts = new int[3][10];
        for (PersonalityTestResultAnswerVo answerVo : answerVos) {
            if (answerVo == null || !isValidDimensionNo(answerVo.getDimensionNo())) {
                continue;
            }
            if (PersonalityTestAnswer.ANSWER_YES.equals(answerVo.getAnswerValue())) {
                counts[PersonalityTestAnswer.ANSWER_YES][answerVo.getDimensionNo()]++;
            } else if (PersonalityTestAnswer.ANSWER_NO.equals(answerVo.getAnswerValue())) {
                counts[PersonalityTestAnswer.ANSWER_NO][answerVo.getDimensionNo()]++;
            } else if (PersonalityTestAnswer.ANSWER_UNSURE.equals(answerVo.getAnswerValue())) {
                counts[PersonalityTestAnswer.ANSWER_UNSURE][answerVo.getDimensionNo()]++;
            }
        }
        return counts;
    }

    private List<PersonalityTestScoreVo> buildScores(int[][] counts) {
        List<PersonalityTestScoreVo> scores = new ArrayList<>(9);
        for (int type = 1; type <= 9; type++) {
            scores.add(new PersonalityTestScoreVo(type,
                    counts[PersonalityTestAnswer.ANSWER_YES][type],
                    counts[PersonalityTestAnswer.ANSWER_NO][type],
                    counts[PersonalityTestAnswer.ANSWER_UNSURE][type]));
        }
        return scores;
    }

    private List<PersonalityTestReportItemVo> buildReports(int[][] counts) {
        List<PersonalityTestReportItemVo> reports = new ArrayList<>(2);
        for (Integer type : sortedTypesByCount(counts[PersonalityTestAnswer.ANSWER_YES])) {
            int score = counts[PersonalityTestAnswer.ANSWER_YES][type];
            PersonalityReportCopy copy = reportCopy(type, score);
            if (copy != null) {
                reports.add(toReportItem(type, score, copy));
                if (reports.size() == 2) {
                    break;
                }
            }
        }
        return reports;
    }

    private PersonalityReportCopy reportCopy(Integer type, int score) {
        if (score >= 18) {
            return REPORT_COPIES[0][type];
        }
        if (score >= 14) {
            return REPORT_COPIES[1][type];
        }
        if (score >= 10) {
            return REPORT_COPIES[2][type];
        }
        return null;
    }

    private List<Integer> sortedTypesByCount(int[] counts) {
        List<Integer> types = new ArrayList<>(9);
        for (int type = 1; type <= 9; type++) {
            types.add(type);
        }
        types.sort(Comparator.comparingInt((Integer type) -> counts[type]).reversed().thenComparingInt(Integer::intValue));
        return types;
    }

    private PersonalityTestReportItemVo toReportItem(Integer type, Integer score, PersonalityReportCopy copy) {
        PersonalityTestReportItemVo vo = new PersonalityTestReportItemVo();
        vo.setCategory(copy.category);
        vo.setType(type);
        vo.setName(copy.name);
        vo.setScore(score);
        vo.setLevel(copy.level);
        vo.setCoreFear(copy.coreFear);
        vo.setCoreDesire(copy.coreDesire);
        vo.setSummary(copy.summary);
        vo.setAdvantages(copy.advantages);
        vo.setWeaknesses(copy.weaknesses);
        vo.setStress(copy.stress);
        vo.setRelax(copy.relax);
        vo.setGrowth(copy.growth);
        vo.setCareer(copy.career);
        vo.setRelationship(copy.relationship);
        vo.setHealth(copy.health);
        vo.setSort(copy.sort);
        return vo;
    }

    private boolean isValidDimensionNo(Integer dimensionNo) {
        return dimensionNo != null && dimensionNo >= 1 && dimensionNo <= 9;
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

    private String answerLabel(Integer answerValue, Long attemptId, Long questionId) {
        if (PersonalityTestAnswer.ANSWER_YES.equals(answerValue)) {
            return "是";
        }
        if (PersonalityTestAnswer.ANSWER_NO.equals(answerValue)) {
            return "否";
        }
        if (PersonalityTestAnswer.ANSWER_UNSURE.equals(answerValue)) {
            return "不确定";
        }
        log.error("性格测试答题值异常: attemptId={}, questionId={}, answerValue={}", attemptId, questionId, answerValue);
        throw new ServiceException("答题记录答案数据异常");
    }

    private static PersonalityReportCopy[][] buildReportCopies() {
        PersonalityReportCopy[][] copies = new PersonalityReportCopy[3][10];
        copies[0][1] = high(1, "完美型", "害怕自己犯错、被批评、不完美", "追求公平正义、做正确的事、成为完美的人", "你是一个极具责任心、追求极致完美的人，做事严谨细致，对人对己都有极高的标准", "1. 极强的责任心和原则性，做事有始有终，值得信赖；2. 细节把控能力极强，能发现他人忽略的问题；3. 追求公平正义，敢于指出问题，是团队中的\"纠错者\"；4. 自律性极强，对自己有严格的要求，不断自我提升", "1. 过于追求完美，容易陷入细节内耗，导致做事效率降低；2. 对他人和自己都过于严苛，容易产生批判情绪，引发人际关系冲突；3. 害怕犯错，不敢尝试新事物，缺乏灵活性；4. 容易压抑情绪，长期积累导致身心疲惫", "压力状态下会变得更加挑剔、易怒，对身边的人和事充满批判，容易陷入\"应该思维\"，觉得所有人都应该遵守规则，甚至会因为一点小错误就自我否定", "放松状态下会变得温和包容，能接纳自己和他人的不完美，做事依然严谨但不会过度纠结细节，能在规则内灵活变通，内心充满平静和掌控感", "1. 学会区分\"完美\"和\"完善\"，接受自己和他人的不完美，完成比完美更重要；2. 放下\"应该思维\"，学会用\"希望\"代替\"应该\"，减少对自己和他人的苛责；3. 允许自己犯错，把错误当成成长的机会，而不是自我否定的理由；4. 学会表达情绪，而不是压抑情绪，找到健康的情绪释放方式", "适合从事需要严谨性、细节把控、规则执行的工作，如财务、法务、质检、管理等；财富上靠勤奋和细节积累，能守住财富，但要避免因过于保守而错过机会", "在关系中会默默付出，用行动表达爱，但容易因为过于挑剔引发矛盾；需要学会接纳伴侣的不完美，学会直接表达感受，而不是用批判的方式沟通", "容易因压抑情绪和追求完美引发焦虑、失眠、肠胃不适等问题；需要学会放松，放下对结果的执念，多关注当下的感受");
        copies[0][2] = high(2, "助人型", "害怕被拒绝、不被需要、失去他人的关爱", "渴望被爱、被认可、成为他人需要的人", "你是一个温暖善良、极具同理心的人，天生擅长感知他人的需求，愿意为他人默默付出", "1. 极强的同理心，能精准感知他人的需求和情绪；2. 热心肠，乐于助人，愿意为他人付出，不求回报；3. 人际关系处理能力极强，是团队中的\"粘合剂\"；4. 善于发现他人的优点，懂得赞美和鼓励他人", "1. 过度迎合他人，忽略自己的需求，容易产生委屈和内耗；2. 付出得不到回应时，会产生怨恨和不平衡；3. 缺乏边界感，容易过度介入他人的生活；4. 把自己的价值建立在他人的认可上，缺乏自我认同", "压力状态下会变得更加讨好，甚至牺牲自己的需求去满足他人，渴望得到他人的认可，一旦得不到回应就会感到受伤和委屈，甚至会刻意讨好来换取关注", "放松状态下会变得自信从容，能在付出的同时照顾好自己的需求，边界感清晰，不会过度迎合他人，内心充满安全感，能坦然接受他人的拒绝", "1. 学会爱自己，把自己的需求放在第一位，先满足自己，再考虑他人；2. 建立清晰的边界感，学会拒绝他人的不合理要求；3. 放下\"付出必须有回报\"的执念，付出是自己的选择，不是换取认可的工具；4. 建立内在的自我认同，你的价值不需要他人的认可来证明", "适合从事与人打交道的工作，如客服、销售、教育、心理咨询、人力资源等；财富上靠人脉和好人缘积累，但要避免因过度付出而损失利益", "在关系中会全心全意付出，把伴侣的需求放在第一位，但容易失去自我；需要学会在关系中保持独立，直接表达自己的需求，而不是一味迎合", "容易因过度付出和压抑自我需求引发疲惫、抑郁、胆囊、甲状腺等相关问题；需要学会关注自己的感受，给自己留出独处的时间");
        copies[0][3] = high(3, "成就型", "害怕失败、不被认可、一事无成", "渴望成功、被赞美、成为众人瞩目的焦点", "你是一个目标导向、精力充沛的实干家，天生追求成功和成就，擅长快速拿到结果", "1. 极强的目标感和行动力，一旦设定目标就会全力以赴；2. 适应能力极强，能快速应对各种挑战和变化；3. 擅长自我展示和包装，能快速获得他人的认可和信任；4. 效率极高，擅长多任务处理，能快速推进工作", "1. 过于追求结果，容易忽略过程和他人的感受，变得功利；2. 把自我价值和成就绑定，一旦失败就会产生严重的自我否定；3. 工作狂，容易过度透支自己，忽略生活和健康；4. 不擅长表达真实情绪，习惯用成功来掩盖内心的空虚", "压力状态下会变得更加拼命工作，试图用更多的成就来证明自己，害怕失败，一旦遇到挫折就会极度焦虑，甚至会为了成功不择手段，忽略规则和他人的感受", "放松状态下会变得从容平和，能享受过程，而不是只关注结果，能坦然面对失败，把失败当成成长的机会，会平衡工作和生活，关注自己的真实感受", "1. 区分\"做事\"和\"做人\"，你的价值不取决于你的成就，而在于你本身；2. 学会享受过程，而不是只盯着结果，人生的美好藏在过程里；3. 放下\"必须成功\"的执念，允许自己失败，失败不是你的终点，而是成长的起点；4. 学会表达真实的情绪，不要用工作来逃避内心的感受", "天生的创业者和领导者，适合从事管理、销售、创业、公关等需要目标达成和结果导向的工作；财富上积累速度快，能抓住各种机会，但要避免因急于求成而踩坑", "在关系中会用物质和成就来表达爱，但容易忽略情感的交流；需要学会放下工作，多陪伴伴侣，直接表达自己的感受，而不是只用物质来弥补", "容易因过度工作和压力引发焦虑、失眠、心脏、高血压等问题；需要学会劳逸结合，给自己留出休息的时间，关注自己的心理健康");
        copies[0][4] = high(4, "自我型", "害怕自己不独特、平庸、失去自我", "渴望独特、被理解、找到生命的意义", "你是一个情感细腻、富有创造力的理想主义者，天生追求独特和深度，对美和情感有极强的感知力", "1. 极强的情感感知力，能捕捉到他人忽略的细腻情感；2. 富有创造力和想象力，对美有独特的审美；3. 追求深度和真诚，不喜欢敷衍和表面的关系；4. 能深刻理解他人的痛苦，极具同理心", "1. 情绪波动大，容易陷入负面情绪，多愁善感；2. 过于关注自己的感受，容易陷入自我中心；3. 追求完美的关系，对他人和关系有极高的期待，容易失望；4. 不擅长表达自己的需求，习惯用情绪来表达，容易引发误解", "压力状态下会变得更加情绪化，陷入自我否定和痛苦中，觉得自己不被理解，孤独感极强，会刻意疏远他人，甚至会用自我伤害的方式来表达痛苦", "放松状态下会变得平静从容，能接纳自己的情绪，不会被情绪左右，能看到生活中的美好，创造力极强，能把自己的情感转化为有价值的作品，内心充满富足感", "1. 学会和情绪共处，不要被情绪控制，情绪不是你的全部，只是你的一部分；2. 放下\"必须被理解\"的执念，不是所有人都能懂你，这是正常的；3. 学会用直接的语言表达自己的需求和感受，而不是用情绪；4. 关注当下，不要活在过去的痛苦和未来的想象里，当下才是最真实的", "适合从事需要创造力和情感表达的工作，如艺术、设计、写作、心理咨询、创意策划等；财富上靠创意和独特的能力积累，但要避免因情绪波动而影响工作状态", "在关系中追求灵魂契合，渴望深度的情感连接，但容易因过高的期待而失望；需要学会接纳伴侣的不完美，直接表达自己的需求，而不是让对方猜", "容易因情绪波动引发抑郁、焦虑、肠胃、妇科等相关问题；需要学会调节情绪，找到健康的情绪释放方式，如艺术创作、运动等");
        copies[0][5] = high(5, "理智型", "害怕被侵犯、无知、无能为力", "渴望全知全能、掌控自己的生活、不被他人打扰", "你是一个冷静理智、善于思考的观察者，天生追求知识和深度，喜欢独处，擅长用逻辑分析问题", "1. 极强的逻辑思维和分析能力，能快速看透问题的本质；2. 知识储备丰富，对感兴趣的领域有深入的研究；3. 冷静理智，情绪稳定，不会被情绪左右；4. 独立思考能力极强，不随波逐流，有自己的主见", "1. 过于理性，忽略情感，显得冷漠和疏离；2. 不擅长社交，喜欢独处，容易脱离人群；3. 过度思考，容易陷入内耗，行动力不足；4. 害怕被他人打扰，边界感过强，容易疏远他人", "压力状态下会变得更加封闭，拒绝和他人交流，试图用更多的知识来武装自己，害怕自己无能为力，会反复思考同一个问题，陷入思维的死胡同，行动力极低", "放松状态下会变得从容自信，能把自己的知识和思考转化为实际的行动，能和他人进行有深度的交流，不会刻意封闭自己，内心充满掌控感和安全感", "1. 学会平衡理性和感性，不要过度压抑自己的情感，情感不是弱点；2. 学会行动，不要只停留在思考层面，先做再完美，行动能解决80%的焦虑；3. 适度社交，不要完全封闭自己，高质量的交流能拓宽你的思路；4. 放下\"必须全知全能\"的执念，承认自己的无知，是成长的开始", "适合从事需要深度思考和逻辑分析的工作，如科研、技术、金融、数据分析、战略规划等；财富上靠专业能力和深度研究积累，能守住财富，但要避免因过于保守而错过机会", "在关系中会用理性的方式表达爱，不擅长情感交流，容易显得冷漠；需要学会表达自己的情感，多和伴侣进行深度的沟通，而不是只用逻辑来处理关系", "容易因过度思考和封闭自己引发焦虑、失眠、神经衰弱等问题；需要学会放下思考，多进行身体运动，减少独处的时间，多和他人交流");
        copies[0][6] = high(6, "忠诚型", "害怕不确定性、背叛、失控", "渴望安全感、稳定、他人的信任和支持", "你是一个谨慎负责、忠诚可靠的守护者，天生追求安全和稳定，对风险有极强的预判能力，是团队中最值得信赖的伙伴", "1. 极强的风险预判能力，能提前发现潜在的风险和问题；2. 忠诚可靠，一旦认定就会全力以赴，是团队中最稳定的力量；3. 责任心极强，做事严谨，有始有终；4. 注重规则和流程，能确保事情平稳推进", "1. 过于谨慎，害怕风险，不敢尝试新事物，缺乏灵活性；2. 容易焦虑，对不确定性充满恐惧，会反复思考最坏的结果；3. 依赖性强，需要他人的支持和认可，不敢自己做决定；4. 容易怀疑他人，对他人的信任度低，引发人际关系矛盾", "压力状态下会变得更加焦虑和多疑，反复确认同一件事，害怕出现意外，会过度准备，甚至会因为害怕风险而放弃行动，对他人的信任度极低，会刻意试探他人的忠诚度", "放松状态下会变得自信从容，能坦然面对不确定性，相信自己的判断，能在风险中找到机会，做事依然谨慎但不会过度焦虑，能信任他人，内心充满安全感", "1. 学会和不确定性共处，人生唯一确定的就是不确定性，学会在不确定中寻找机会；2. 放下对风险的过度恐惧，不是所有的风险都会带来坏的结果，勇敢尝试才会成长；3. 建立内在的安全感，你的安全感不取决于他人，而在于你自己；4. 学会信任他人，不是所有人都会背叛你，给他人和自己一个机会", "适合从事需要稳定和风险把控的工作，如行政、财务、安保、管理、质检等；财富上靠稳定的积累，能守住财富，但要避免因过于保守而错过机会", "在关系中会全心全意付出，忠诚可靠，但容易因多疑和焦虑引发矛盾；需要学会信任伴侣，直接表达自己的不安，而不是反复试探", "容易因焦虑和多疑引发失眠、肠胃、心脏等相关问题；需要学会放松，放下对不确定性的恐惧，多关注当下的感受");
        copies[0][7] = high(7, "活跃型", "害怕痛苦、无聊、束缚", "渴望快乐、自由、充满乐趣的生活", "你是一个乐观开朗、精力充沛的乐天派，天生追求快乐和自由，对新鲜事物充满好奇，是团队中的开心果", "1. 乐观积极，总能看到事情好的一面，给身边的人带来快乐；2. 好奇心极强，对新鲜事物充满兴趣，学习能力强；3. 精力充沛，行动力强，擅长同时推进多个事情；4. 思维活跃，富有创造力，能想出很多新的点子和方案", "1. 注意力不集中，容易三分钟热度，做事难以坚持到底；2. 逃避痛苦和负面情绪，遇到问题容易逃避，而不是面对；3. 过于追求新鲜感，容易忽略深度和细节；4. 不擅长承诺，害怕束缚，容易在关系中摇摆不定", "压力状态下会变得更加浮躁，试图用更多的新鲜事物来逃避痛苦，做事虎头蛇尾，无法集中注意力，会刻意回避负面情绪和问题，甚至会用享乐来麻痹自己", "放松状态下会变得沉稳踏实，能专注于一件事，坚持到底，能坦然面对痛苦和负面情绪，把问题当成成长的机会，能平衡自由和责任，内心充满平静和富足感", "1. 学会面对痛苦和负面情绪，逃避解决不了问题，只有面对才能成长；2. 学会专注，一次只做一件事，完成比新鲜更重要；3. 放下对\"快乐\"的执念，人生不是只有快乐，痛苦也是人生的一部分；4. 学会承诺，承担责任，自由不是随心所欲，而是自我主宰", "适合从事需要创意和新鲜感的工作，如策划、营销、公关、创业、演艺等；财富上靠灵活的思维和机会积累，但要避免因三分钟热度而错失机会", "在关系中能带来很多快乐和新鲜感，但容易因害怕束缚而逃避承诺；需要学会承担责任，给伴侣足够的安全感，不要只享受恋爱的快乐，而忽略关系的经营", "容易因过度享乐和逃避问题引发焦虑、抑郁、酒精依赖等问题；需要学会沉稳，面对自己的负面情绪，建立健康的生活习惯");
        copies[0][8] = high(8, "领袖型", "害怕被控制、被欺负、软弱无能", "渴望掌控一切、保护自己和他人、成为强者", "你是一个强势自信、果敢担当的天生领袖，天生追求掌控和保护，敢于直面挑战，是团队中的主心骨", "1. 极强的领导力和决策力，能快速做出决定，带领团队前进；2. 果敢担当，敢于直面挑战和困难，从不退缩；3. 保护欲极强，会拼尽全力保护自己在乎的人；4. 气场强大，自信果断，能快速掌控局面", "1. 过于强势，控制欲强，容易忽略他人的感受和想法；2. 脾气暴躁，容易发怒，引发人际关系冲突；3. 过于好胜，不愿意承认自己的错误，容易固执己见；4. 不擅长表达柔软的情感，显得强硬和冷漠", "压力状态下会变得更加强势和暴躁，控制欲极强，试图掌控身边的一切，会因为一点小事就发怒，不愿意承认自己的错误，会把所有的问题都归咎于他人，甚至会用强硬的手段来解决问题", "放松状态下会变得温和包容，能接纳他人的不同意见，控制欲会降低，能柔软地表达自己的情感，依然果敢但不会强势，能保护他人的同时也尊重他人的独立性，内心充满平静和力量", "1. 学会放下控制欲，你不能掌控一切，尊重他人的独立性和选择；2. 学会柔软，强硬不是唯一的解决方式，温柔更有力量；3. 学会承认自己的错误，认错不是软弱，而是强大的表现；4. 学会表达自己的真实感受，而不是用愤怒来掩饰脆弱", "天生的领导者和创业者，适合从事管理、创业、销售、领导等需要掌控和决策的工作；财富上靠魄力和领导力积累，能抓住大的机会，但要避免因过于强势而踩坑", "在关系中会拼尽全力保护伴侣，但容易因控制欲和强势引发矛盾；需要学会放下控制，尊重伴侣的独立空间，学会温柔地表达爱，而不是用强硬的方式", "容易因暴躁和压力引发高血压、心脏、肝脏等相关问题；需要学会控制情绪，学会放松，放下对掌控的执念");
        copies[0][9] = high(9, "和平型", "害怕冲突、分离、压力", "渴望和平、和谐、舒适的生活", "你是一个温和友善、与世无争的和平使者，天生追求和谐与平静，擅长化解冲突，是团队中的润滑剂", "1. 性格温和，待人友善，不喜欢和他人发生冲突；2. 极具同理心，能理解他人的感受，擅长化解矛盾；3. 心态平和，不容易被外界影响，能给身边的人带来平静；4. 包容度极强，能接纳他人的不同，是很好的倾听者", "1. 过于追求和平，不敢表达自己的想法和需求，容易委曲求全；2. 缺乏主见，容易被他人影响，不敢做决定；3. 逃避冲突和压力，遇到问题容易拖延，不愿意面对；4. 过于佛系，缺乏上进心，容易安于现状", "压力状态下会变得更加逃避，不敢表达自己的想法，会为了和平而委屈自己，把所有的情绪都压抑在心里，内心充满矛盾和内耗，会拖延所有需要做决定的事情，甚至会刻意回避冲突和问题", "放松状态下会变得自信坚定，能清晰地表达自己的想法和需求，不会为了和平而委曲求全，能坦然面对冲突和问题，有自己的主见和原则，内心充满平静和力量", "1. 学会表达自己的需求和想法，和平不是委曲求全，而是平等的沟通；2. 学会面对冲突，逃避解决不了问题，勇敢表达自己的感受，才能真正化解矛盾；3. 建立自己的主见和原则，不要一味迎合他人，你的想法同样重要；4. 学会拒绝，不要为了迎合他人而委屈自己，你的感受同样值得被尊重", "适合从事需要和谐和沟通的工作，如行政、客服、人力资源、教育、心理咨询等；财富上靠稳定的积累，能守住财富，但要避免因过于佛系而错过成长的机会", "在关系中会默默付出，追求和谐，但容易因委曲求全而压抑自己；需要学会表达自己的需求，和伴侣进行平等的沟通，不要一味迎合", "容易因压抑情绪和逃避问题引发肠胃、抑郁、焦虑等相关问题；需要学会表达自己的感受，不要把情绪都压抑在心里，勇敢面对问题");

        copies[1][1] = relativeHigh(10, "完美型", "害怕自己犯错、被批评、不完美", "追求公平正义、做正确的事、成为完美的人", "你有较强的责任心和原则性，做事严谨细致，对自己有一定的要求，追求公平正义", "1. 有责任心，做事有始有终，值得信赖；2. 注重细节，能发现问题；3. 有原则，追求公平正义；4. 有自律性，对自己有要求", "1. 对细节有一定的执念，偶尔会陷入小细节内耗；2. 对他人有一定的期待，容易因他人的不完美而失望；3. 害怕犯错，偶尔会不敢尝试新事物；4. 偶尔会压抑自己的情绪", "压力状态下会变得更加挑剔，对自己和他人的要求会提高，容易因为一点小错误而自我否定，情绪会变得更加严谨和压抑", "放松状态下会变得温和，能接纳自己和他人的不完美，做事依然严谨但不会过度纠结细节，能灵活变通", "1. 学会区分完美和完善，接受不完美；2. 放下应该思维，减少对自己和他人的苛责；3. 允许自己犯错，把错误当成成长的机会；4. 学会表达情绪，不要压抑自己", "适合从事需要一定严谨性的工作，能在工作中把控细节，靠责任心和细节积累财富", "在关系中会默默付出，用行动表达爱，需要学会接纳伴侣的不完美，直接表达感受", "需要学会放松，放下对结果的执念，多关注当下的感受，避免过度焦虑");
        copies[1][2] = relativeHigh(11, "助人型", "害怕被拒绝、不被需要、失去他人的关爱", "渴望被爱、被认可、成为他人需要的人", "你是一个温暖善良的人，有较强的同理心，愿意为他人付出，渴望被认可和关爱", "1. 有同理心，能感知他人的需求；2. 热心肠，乐于助人；3. 人际关系好，是团队中的粘合剂；4. 懂得赞美和鼓励他人", "1. 偶尔会过度迎合他人，忽略自己的需求；2. 付出得不到回应时，会有不平衡感；3. 边界感偶尔会模糊；4. 把自己的价值和他人的认可绑定", "压力状态下会变得更加讨好，试图用付出来换取他人的认可，一旦得不到回应就会感到受伤和委屈", "放松状态下会变得自信，能在付出的同时照顾好自己的需求，边界感清晰，能坦然接受他人的拒绝", "1. 学会爱自己，先满足自己的需求；2. 建立清晰的边界感，学会拒绝；3. 放下付出必须有回报的执念；4. 建立内在的自我认同", "适合从事与人打交道的工作，靠人脉和好人缘积累财富", "在关系中会付出，但需要学会保持独立，直接表达自己的需求", "需要关注自己的感受，给自己留出独处的时间，避免过度疲惫");
        copies[1][3] = relativeHigh(12, "成就型", "害怕失败、不被认可、一事无成", "渴望成功、被赞美、成为众人瞩目的焦点", "你是一个目标导向的人，有较强的行动力，追求成功和认可，擅长拿到结果", "1. 有目标感和行动力，能全力以赴完成目标；2. 适应能力强，能应对各种挑战；3. 擅长自我展示，能获得他人的认可；4. 效率高，能快速推进工作", "1. 过于追求结果，偶尔会忽略他人的感受；2. 把自我价值和成就绑定，失败时容易自我否定；3. 偶尔会过度工作，忽略生活；4. 不擅长表达真实情绪", "压力状态下会变得更加拼命工作，试图用成就来证明自己，遇到挫折时会极度焦虑", "放松状态下会变得从容，能享受过程，坦然面对失败，平衡工作和生活", "1. 你的价值不取决于你的成就；2. 学会享受过程，而不是只盯着结果；3. 允许自己失败，失败是成长的机会；4. 学会表达真实的情绪", "适合从事结果导向的工作，靠行动力和结果积累财富", "在关系中会用物质表达爱，需要学会多陪伴伴侣，直接表达感受", "需要劳逸结合，给自己留出休息的时间，关注心理健康");
        copies[1][4] = relativeHigh(13, "自我型", "害怕自己不独特、平庸、失去自我", "渴望独特、被理解、找到生命的意义", "你是一个情感细腻的人，有较强的创造力，追求独特和深度，对美有感知力", "1. 情感感知力强，能捕捉细腻的情感；2. 富有创造力和想象力；3. 追求深度和真诚；4. 能理解他人的痛苦", "1. 情绪波动较大，容易陷入负面情绪；2. 过于关注自己的感受，容易自我中心；3. 对关系有较高的期待，容易失望；4. 不擅长直接表达需求", "压力状态下会变得更加情绪化，陷入自我否定和痛苦中，孤独感极强", "放松状态下会变得平静，能接纳自己的情绪，看到生活中的美好，创造力极强", "1. 学会和情绪共处，不要被情绪控制；2. 放下必须被理解的执念；3. 学会用直接的语言表达需求；4. 关注当下，不要活在过去和未来", "适合从事需要创造力的工作，靠创意和能力积累财富", "在关系中追求灵魂契合，需要学会接纳伴侣的不完美，直接表达需求", "需要学会调节情绪，找到健康的情绪释放方式");
        copies[1][5] = relativeHigh(14, "理智型", "害怕被侵犯、无知、无能为力", "渴望全知全能、掌控自己的生活、不被他人打扰", "你是一个冷静理智的人，善于思考，追求知识和深度，喜欢独处，擅长逻辑分析", "1. 逻辑思维和分析能力强，能看透问题本质；2. 知识储备丰富，对感兴趣的领域有深入研究；3. 冷静理智，情绪稳定；4. 独立思考能力强，有自己的主见", "1. 过于理性，偶尔会忽略情感；2. 不擅长社交，喜欢独处；3. 过度思考，行动力不足；4. 边界感过强，容易疏远他人", "压力状态下会变得更加封闭，拒绝和他人交流，反复思考同一个问题，陷入思维死胡同", "放松状态下会变得从容，能把思考转化为行动，能和他人进行有深度的交流", "1. 平衡理性和感性，不要压抑情感；2. 学会行动，先做再完美；3. 适度社交，高质量的交流能拓宽思路；4. 承认自己的无知，是成长的开始", "适合从事需要深度思考的工作，靠专业能力积累财富", "在关系中会用理性表达爱，需要学会表达情感，多和伴侣沟通", "需要学会放下思考，多进行身体运动，减少独处的时间");
        copies[1][6] = relativeHigh(15, "忠诚型", "害怕不确定性、背叛、失控", "渴望安全感、稳定、他人的信任和支持", "你是一个谨慎负责的人，忠诚可靠，对风险有预判能力，追求安全和稳定", "1. 风险预判能力强，能提前发现问题；2. 忠诚可靠，是团队中稳定的力量；3. 责任心强，做事严谨；4. 注重规则和流程", "1. 过于谨慎，害怕风险，不敢尝试新事物；2. 容易焦虑，对不确定性充满恐惧；3. 依赖性强，需要他人的支持；4. 容易怀疑他人，信任度低", "压力状态下会变得更加焦虑和多疑，反复确认同一件事，害怕出现意外", "放松状态下会变得自信，能坦然面对不确定性，相信自己的判断，能信任他人", "1. 学会和不确定性共处；2. 放下对风险的过度恐惧，勇敢尝试；3. 建立内在的安全感；4. 学会信任他人", "适合从事需要稳定和风险把控的工作，靠稳定积累财富", "在关系中会全心全意付出，需要学会信任伴侣，直接表达不安", "需要学会放松，放下对不确定性的恐惧，多关注当下");
        copies[1][7] = relativeHigh(16, "活跃型", "害怕痛苦、无聊、束缚", "渴望快乐、自由、充满乐趣的生活", "你是一个乐观开朗的人，精力充沛，追求快乐和自由，对新鲜事物充满好奇", "1. 乐观积极，能给身边的人带来快乐；2. 好奇心强，学习能力强；3. 精力充沛，行动力强；4. 思维活跃，富有创造力", "1. 注意力不集中，容易三分钟热度；2. 逃避痛苦和负面情绪；3. 过于追求新鲜感，忽略深度；4. 不擅长承诺，害怕束缚", "压力状态下会变得更加浮躁，试图用新鲜事物逃避痛苦，做事虎头蛇尾", "放松状态下会变得沉稳，能专注于一件事，坚持到底，能坦然面对负面情绪", "1. 学会面对痛苦和负面情绪；2. 学会专注，一次只做一件事；3. 放下对快乐的执念；4. 学会承诺，承担责任", "适合从事需要创意和新鲜感的工作，靠灵活的思维积累财富", "在关系中能带来快乐，需要学会承担责任，给伴侣足够的安全感", "需要学会沉稳，面对自己的负面情绪，建立健康的生活习惯");
        copies[1][8] = relativeHigh(17, "领袖型", "害怕被控制、被欺负、软弱无能", "渴望掌控一切、保护自己和他人、成为强者", "你是一个强势自信的人，果敢担当，有领导力，追求掌控和保护，敢于直面挑战", "1. 领导力和决策力强，能快速做决定；2. 果敢担当，敢于直面挑战；3. 保护欲强，会保护自己在乎的人；4. 气场强大，能掌控局面", "1. 过于强势，控制欲强，忽略他人感受；2. 脾气暴躁，容易发怒；3. 过于好胜，不愿意承认错误；4. 不擅长表达柔软的情感", "压力状态下会变得更加强势和暴躁，控制欲极强，容易发怒，不愿意承认错误", "放松状态下会变得温和包容，能接纳他人的不同意见，能柔软地表达情感", "1. 放下控制欲，尊重他人的独立性；2. 学会柔软，温柔更有力量；3. 学会承认自己的错误；4. 学会表达真实的感受", "天生的领导者，适合从事管理、创业等工作，靠魄力和领导力积累财富", "在关系中会保护伴侣，需要学会放下控制，尊重伴侣的独立空间", "需要学会控制情绪，学会放松，放下对掌控的执念");
        copies[1][9] = relativeHigh(18, "和平型", "害怕冲突、分离、压力", "渴望和平、和谐、舒适的生活", "你是一个温和友善的人，与世无争，追求和谐与平静，擅长化解冲突", "1. 性格温和，待人友善；2. 同理心强，擅长化解矛盾；3. 心态平和，能给身边的人带来平静；4. 包容度强，是很好的倾听者", "1. 过于追求和平，不敢表达自己的想法；2. 缺乏主见，容易被他人影响；3. 逃避冲突和压力，容易拖延；4. 过于佛系，缺乏上进心", "压力状态下会变得更加逃避，不敢表达自己的想法，委曲求全，内心充满内耗", "放松状态下会变得自信坚定，能清晰表达自己的想法，能坦然面对冲突，有自己的主见", "1. 学会表达自己的需求和想法；2. 学会面对冲突，勇敢表达感受；3. 建立自己的主见和原则；4. 学会拒绝，不要委曲求全", "适合从事需要和谐和沟通的工作，靠稳定积累财富", "在关系中会默默付出，需要学会表达自己的需求，和伴侣平等沟通", "需要学会表达自己的感受，不要压抑情绪，勇敢面对问题");

        copies[2][1] = relativeLow(28, "完美型", "害怕自己犯错、被批评、不完美", "追求公平正义、做正确的事、成为完美的人", "你是一个不太追求完美的人，做事不拘小节，对自己和他人的要求不高，不喜欢被规则束缚", "1. 缺乏一定的责任心，做事偶尔会敷衍，不够严谨；2. 细节把控能力不足；3. 对自己的要求不高，偶尔会放纵自己", "1. 容易粗心、得过且过；3. 规则意识不强，偶尔会不按流程做事；3. 缺乏责任心，偶尔会推卸责任", "1. 工作中偶尔会因细节出错影响工作结果；2. 人际关系中偶尔会因不负责任引发矛盾；3. 自我成长上，偶尔会因缺乏自律而无法实现目标；4. 生活中偶尔会因粗心大意出现问题", "1. 培养责任心，做事有始有终；2. 提升细节意识，做事多检查；3. 建立规则意识，遵守基本的规则；4. 培养自律性，给自己设定合理的要求", "工作中需要提升严谨性和责任心，避免因粗心出错影响职业发展", "在关系中需要学会承担责任，对伴侣和关系负责", "需要建立规律的生活习惯，提升自律性");
        copies[2][2] = relativeLow(29, "助人型", "害怕被拒绝、不被需要、失去他人的关爱", "渴望被爱、被认可、成为他人需要的人", "你是一个不太擅长付出的人，自我意识较强，不擅长感知他人的需求，讨厌他人过度依赖自己", "1. 不太能发现他人的需求和情绪；2. 缺乏一定人际关系处理能力不足", "1. 对他人的需求不够敏感，缺乏同情心；", "1. 人际关系不够好，难以和他人建立深度的关系；2. 工作中难以和同事很好地合作；3. 情感关系中，容易因自私和不关心伴侣引发矛盾；4. 容易因自我封闭失去很多机会", "1. 学会共情，站在他人的角度思考问题；2. 学会付出，力所能及地帮助他人；3. 放下自我中心，多关注他人的感受；4. 学会和他人相处，建立健康的人际关系", "工作中需要提升团队协作能力，学会和他人相处，否则会影响职业发展", "在关系中需要学会关心伴侣，为关系付出", "需要多和他人交流，建立健康的人际关系，避免自我封闭");
        copies[2][3] = relativeLow(30, "成就型", "害怕失败、不被认可、一事无成", "渴望成功、被赞美、成为众人瞩目的焦点", "你是一个不太追求成就的人，对成功的欲望不强，不在乎他人的认可，做事缺乏目标感", "1. 目标感不强，做事缺乏明确的方向；2. 上进心不足，安于现状，不愿意努力改变；", "1. 没有明确的人生目标；2. 不在乎他人的评价3. 做事拖延，效率不高", "1. 事业上难以获得成长和晋升；2. 生活中容易因没有目标陷入迷茫；3. 人际关系中难以获得他人的尊重；4. 无法实现自己的潜力", "1. 给自己设定合理的小目标；2. 培养上进心，努力完成每一个任务；3. 学会展示自己的优势；4. 克服拖延，提升行动力", "需要建立职业目标，努力提升自己的能力，否则会一直停滞不前", "在关系中需要建立自己的人生目标，为自己和关系负责", "需要找到自己的人生方向，建立生活的动力");
        copies[2][4] = relativeLow(31, "自我型", "害怕自己不独特、平庸、失去自我", "渴望独特、被理解、找到生命的意义", "你是一个不太追求独特的人，随波逐流，对美和情感的感知力不足，缺乏创造力", "1. 缺乏一定的情感感知力；2. 缺乏深度追求，只关注表面的东西；3. 自我意识不足，随波逐流", "1. 缺乏自己的想法；2. 缺乏创造力；", "1. 工作中难以做出创新的成果；2. 人际关系中难以和他人建立深度的情感连接；3. 自我成长上难以找到自己的人生意义；4. 生活中缺乏乐趣，枯燥乏味", "1. 培养情感感知力，提升审美能力；2. 培养创造力，多尝试新的事物；3. 追求深度，多思考事物的本质；4. 建立自我意识，找到自己的独特性", "需要培养创造力和创新能力，才能获得职业发展", "在关系中需要学会感知伴侣的情感，建立深度的沟通", "需要找到自己的人生意义，丰富自己的生活");
        copies[2][5] = relativeLow(32, "理智型", "害怕被侵犯、无知、无能为力", "渴望全知全能、掌控自己的生活、不被他人打扰", "你是一个不太擅长思考的人，学习意愿不强，做事凭感觉，逻辑分析能力一般", "1. 逻辑思维和分析能力不足；2. 学习意识不强；3. 独立思考能力不足", "1. 做事凭感觉；2. 不太喜欢学习；3. 情绪容易冲动行事；4. 缺少自己的主见，容易被他人影响", "1. 工作中容易因缺乏思考做出错误的决策；2. 人际关系中容易因冲动行事引发矛盾；3. 自我成长上无法提升自己的能力；4. 生活中容易因冲动行事出现问题", "1. 培养逻辑思维能力，做事多思考分析；2. 培养学习意识，每天学习新的知识；3. 学会控制情绪，不要被情绪左右；4. 建立独立思考能力，有自己的主见", "需要提升逻辑思维和学习能力，否则会影响职业发展", "在关系中需要学会冷静思考，控制自己的情绪", "需要学会控制情绪，建立健康的生活习惯");
        copies[2][6] = relativeLow(33, "忠诚型", "害怕不确定性、背叛、失控", "渴望安全感、稳定、他人的信任和支持", "你是一个不太追求安全感的人，做事鲁莽，风险预判能力不足，缺乏责任心", "1. 缺乏一定的风险预判能力；2. 缺乏安全感", "1. 做事有些鲁莽冲动；2. 缺乏一定的风险意识；3. 缺乏责任心，做事不够严谨；", "1. 工作中容易因鲁莽冲动导致项目失败；2. 人际关系中容易因缺乏忠诚度失去他人的信任；3. 自我成长上容易因冒险陷入困境；4. 生活中容易因冲动行事出现问题", "1. 培养风险预判能力，做事多思考潜在的风险；2. 建立责任心，做事有始有终；3. 建立规则意识，遵守基本的规则；4. 培养忠诚度，珍惜他人的信任", "需要培养责任心和风险意识，才能获得职业发展", "在关系中需要学会珍惜伴侣的信任，对关系负责", "需要学会冷静行事，建立健康的生活习惯");
        copies[2][7] = relativeLow(34, "活跃型", "害怕痛苦、无聊、束缚", "渴望快乐、自由、充满乐趣的生活", "你是一个不太追求快乐的人，心态消极，对生活的兴趣不足，缺乏活力", "1. 乐观积极的心态不足，总是看到事情负面的一面；2. 缺乏快乐元素；3. 精力和行动力不足；", "1. 思想容易消极悲观；2. 对新鲜事物缺乏一定兴趣3. 做事喜欢拖延；4. 思维有些僵化，缺乏一定的创造力", "1. 工作中容易因消极心态影响工作效率；2. 人际关系中容易因消极情绪影响他人；3. 自我成长上无法提升自己的能力；4. 生活中缺乏乐趣，容易陷入抑郁", "1. 培养乐观积极的心态，看到事情好的一面；2. 培养好奇心，多尝试新的事物；3. 提升行动力，克服拖延；4. 培养创造力，打破思维定势", "需要调整心态，提升行动力，才能获得职业发展", "在关系中需要调整自己的心态，给伴侣带来积极的影响", "需要调整心态，多运动，建立健康的生活习惯");
        copies[2][8] = relativeLow(35, "领袖型", "害怕被控制、被欺负、软弱无能", "渴望掌控一切、保护自己和他人、成为强者", "你是一个不太强势的人，缺乏领导力，遇事容易退缩，不敢承担责任", "1. 缺乏一定领导力和决策力；2. 不够果敢和担当，遇到困难习惯退缩，害怕承担责任；", "1. 遇事习惯退缩，不太敢做决定；2. 容易迎合他人，不敢表达；3. 内心缺乏自信心", "1. 工作中难以获得晋升，职业发展受限；2. 人际关系中容易被他人欺负和利用；3. 自我成长上无法实现自己的潜力；4. 生活中容易让自己和家人陷入困境", "1. 培养自信，相信自己的能力；2. 培养担当，遇到问题不退缩；3. 培养决策力，敢于做决定；4. 学会保护自己，维护自己的利益", "需要培养自信和担当，提升领导力和决策力，才能获得发展", "在关系中需要学会担当，为伴侣和关系负责", "需要提升自信，建立健康的心态");
        copies[2][9] = relativeLow(36, "和平型", "害怕冲突、分离、压力", "渴望和平、和谐、舒适的生活", "你是一个不太追求和平的人，脾气暴躁，喜欢制造冲突，难以和他人和谐相处", "1. 待人接物不够温和友善，脾气容易急躁、发怒；3. 缺乏一定的包容度；", "1. 脾气不稳定，容易急躁，容易于他人发生冲突；3. 情绪不稳定，容易被小事影响", "1. 工作中难以和同事和谐相处，影响工作推进；2. 人际关系极差，容易被孤立；3. 情感关系中容易因暴躁导致关系破裂；4. 生活中充满冲突和矛盾，无法获得平静", "1. 学会控制情绪，控制自己的脾气；2. 学会包容，接纳他人的不同；3. 学会化解矛盾，培养同理心；4. 培养平和的心态，和自己和解", "需要学会控制情绪，和谐相处，否则会影响职业发展", "在关系中需要学会控制情绪，包容伴侣，建立和谐的关系", "需要学会控制情绪，建立平和的心态");
        return copies;
    }

    private static PersonalityReportCopy high(int sort, String name, String coreFear, String coreDesire, String summary,
                                              String advantages, String weaknesses, String stress, String relax,
                                              String growth, String career, String relationship, String health) {
        return new PersonalityReportCopy("人格高：极致高", "极致高", sort, name, coreFear, coreDesire, summary,
                advantages, weaknesses, stress, relax, growth, career, relationship, health);
    }

    private static PersonalityReportCopy relativeHigh(int sort, String name, String coreFear, String coreDesire, String summary,
                                                      String advantages, String weaknesses, String stress, String relax,
                                                      String growth, String career, String relationship, String health) {
        return new PersonalityReportCopy("人格高：相对高", "相对高", sort, name, coreFear, coreDesire, summary,
                advantages, weaknesses, stress, relax, growth, career, relationship, health);
    }

    private static PersonalityReportCopy relativeLow(int sort, String name, String coreFear, String coreDesire, String summary,
                                                     String advantages, String weaknesses, String stress, String growth,
                                                     String career, String relationship, String health) {
        return new PersonalityReportCopy("人格低：相对低", "相对低", sort, name, coreFear, coreDesire, summary,
                advantages, weaknesses, stress, "", growth, career, relationship, health);
    }

    private static class PersonalityReportCopy {
        private final String category;
        private final String level;
        private final Integer sort;
        private final String name;
        private final String coreFear;
        private final String coreDesire;
        private final String summary;
        private final String advantages;
        private final String weaknesses;
        private final String stress;
        private final String relax;
        private final String growth;
        private final String career;
        private final String relationship;
        private final String health;

        private PersonalityReportCopy(String category, String level, Integer sort, String name, String coreFear,
                                      String coreDesire, String summary, String advantages, String weaknesses,
                                      String stress, String relax, String growth, String career,
                                      String relationship, String health) {
            this.category = category;
            this.level = level;
            this.sort = sort;
            this.name = name;
            this.coreFear = coreFear;
            this.coreDesire = coreDesire;
            this.summary = summary;
            this.advantages = advantages;
            this.weaknesses = weaknesses;
            this.stress = stress;
            this.relax = relax;
            this.growth = growth;
            this.career = career;
            this.relationship = relationship;
            this.health = health;
        }
    }
}
