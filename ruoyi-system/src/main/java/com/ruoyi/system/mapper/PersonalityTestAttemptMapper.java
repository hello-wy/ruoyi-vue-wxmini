package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.PersonalityTestAttempt;
import org.apache.ibatis.annotations.Param;

public interface PersonalityTestAttemptMapper {
    PersonalityTestAttempt selectAttemptById(@Param("id") Long id);

    PersonalityTestAttempt selectLatestInProgressAttempt(@Param("testId") Long testId,
                                                         @Param("userInfoId") Long userInfoId);

    int insertAttempt(PersonalityTestAttempt attempt);

    int updateAttempt(PersonalityTestAttempt attempt);
}
