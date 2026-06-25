package com.ruoyi.system.service;

import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.vo.PersonalityTestAdminAttemptVo;
import com.ruoyi.system.domain.vo.PersonalityTestAdminDetailVo;
import com.ruoyi.system.domain.vo.PersonalityTestAnswerResultVo;
import com.ruoyi.system.domain.vo.PersonalityTestEntryVo;
import com.ruoyi.system.domain.vo.PersonalityTestQuestionVo;
import com.ruoyi.system.domain.vo.PersonalityTestResultVo;

public interface IPersonalityTestService {
    PersonalityTestEntryVo getEntry(Long userInfoId);

    PersonalityTestEntryVo startAttempt(Long userInfoId, String wxUserId);

    PersonalityTestQuestionVo getCurrentQuestion(Long attemptId, Long userInfoId);

    PersonalityTestQuestionVo getQuestion(Long attemptId, Long userInfoId, Integer questionNo);

    PersonalityTestAnswerResultVo saveAnswer(Long attemptId, Long userInfoId, Long questionId, Integer answerValue);

    PersonalityTestResultVo getResult(Long attemptId, Long userInfoId);

    int countCompletedAttempts();

    TableDataInfoVo<PersonalityTestAdminAttemptVo> selectAdminAttemptList(Long testId, String userInfoId, Integer status, String startTime, String endTime);

    PersonalityTestAdminDetailVo selectAdminAttemptDetail(Long attemptId);
}
