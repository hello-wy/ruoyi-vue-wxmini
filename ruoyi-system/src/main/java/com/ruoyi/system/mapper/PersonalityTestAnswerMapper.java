package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.PersonalityTestAnswer;
import org.apache.ibatis.annotations.Param;

public interface PersonalityTestAnswerMapper {
    int countAnswersByAttemptId(@Param("attemptId") Long attemptId);

    int insertOrUpdateAnswer(PersonalityTestAnswer answer);
}
