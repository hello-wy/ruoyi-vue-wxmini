package com.ruoyi.system.service;

import com.ruoyi.system.domain.vo.PersonalityTestAnswerResultVo;
import com.ruoyi.system.domain.vo.PersonalityTestEntryVo;
import com.ruoyi.system.domain.vo.PersonalityTestQuestionVo;
import com.ruoyi.system.domain.vo.PersonalityTestResultVo;

public interface IPersonalityTestService {
    PersonalityTestEntryVo getEntry(Long userInfoId);

    PersonalityTestEntryVo startAttempt(Long userInfoId, String wxUserId);

    PersonalityTestQuestionVo getCurrentQuestion(Long attemptId, Long userInfoId);

    PersonalityTestAnswerResultVo saveAnswer(Long attemptId, Long userInfoId, Long questionId, Integer answerValue);

    PersonalityTestResultVo getResult(Long attemptId, Long userInfoId);
}
