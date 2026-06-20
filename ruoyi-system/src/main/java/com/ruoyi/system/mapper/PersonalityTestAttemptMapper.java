package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.PersonalityTestAttempt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PersonalityTestAttemptMapper {
    PersonalityTestAttempt selectAttemptById(@Param("id") Long id);

    PersonalityTestAttempt selectLatestInProgressAttempt(@Param("testId") Long testId,
                                                         @Param("userInfoId") Long userInfoId);

    int insertAttempt(PersonalityTestAttempt attempt);

    int updateAttempt(PersonalityTestAttempt attempt);

    int countCompletedAttempts(@Param("testId") Long testId);

    List<PersonalityTestAttempt> selectAttemptList(@Param("testId") Long testId,
                                                   @Param("userInfoId") String userInfoId,
                                                   @Param("status") Integer status,
                                                   @Param("startTime") String startTime,
                                                   @Param("endTime") String endTime);
}
