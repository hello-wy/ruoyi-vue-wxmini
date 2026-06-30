package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.PersonalityTestQuestion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PersonalityTestQuestionMapper {
    PersonalityTestQuestion selectQuestionById(@Param("id") Long id);

    PersonalityTestQuestion selectQuestionByNo(@Param("testId") Long testId,
                                               @Param("questionNo") Integer questionNo);

    PersonalityTestQuestion selectFirstUnansweredQuestion(@Param("testId") Long testId,
                                                          @Param("attemptId") Long attemptId);

    List<PersonalityTestQuestion> selectEnabledQuestionsByTestId(@Param("testId") Long testId);

    List<PersonalityTestQuestion> selectQuestionsByTestId(@Param("testId") Long testId);

    List<PersonalityTestQuestion> selectQuestionsByIds(@Param("ids") List<Long> ids);
}
