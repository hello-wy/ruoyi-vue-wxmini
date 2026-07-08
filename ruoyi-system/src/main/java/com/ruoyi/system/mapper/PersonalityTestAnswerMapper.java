package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.PersonalityTestAnswer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PersonalityTestAnswerMapper {
    int countAnswersByAttemptId(@Param("attemptId") Long attemptId);

    int insertOrUpdateAnswer(PersonalityTestAnswer answer);

    int deleteAnswersByAttemptId(@Param("attemptId") Long attemptId);

    List<PersonalityTestAnswer> selectAnswersByAttemptId(@Param("attemptId") Long attemptId);
}
