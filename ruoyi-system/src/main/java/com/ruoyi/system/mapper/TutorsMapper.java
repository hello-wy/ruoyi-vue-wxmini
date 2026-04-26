package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.Tutors;

public interface TutorsMapper extends BaseMapper<Tutors>
{
    Tutors selectTutorsById(Long id);

    List<Tutors> selectTutorsList(Tutors tutors);

    int insertTutors(Tutors tutors);

    int updateTutors(Tutors tutors);

    int deleteTutorsById(Long id);

    int deleteTutorsByIds(Long[] ids);

    List<Tutors> selectCertifiedTutorsList(Tutors tutors);

    Tutors selectTutorsByUid(String uid);
}
