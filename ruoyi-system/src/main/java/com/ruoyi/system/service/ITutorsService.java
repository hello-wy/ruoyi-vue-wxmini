package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Tutors;

public interface ITutorsService
{
    Tutors selectTutorsById(Long id);

    List<Tutors> selectTutorsList(Tutors tutors);

    int insertTutors(Tutors tutors);

    int updateTutors(Tutors tutors);

    int deleteTutorsByIds(Long[] ids);

    int deleteTutorsById(Long id);

    List<Tutors> selectCertifiedTutorsList(Tutors tutors);

    Tutors selectTutorsByUid(String uid);
}
