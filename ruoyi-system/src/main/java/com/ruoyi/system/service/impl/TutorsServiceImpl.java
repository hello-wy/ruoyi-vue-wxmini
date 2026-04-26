package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TutorsMapper;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.service.ITutorsService;

@Service
public class TutorsServiceImpl implements ITutorsService
{
    @Autowired
    private TutorsMapper tutorsMapper;

    @Override
    public Tutors selectTutorsById(Long id)
    {
        return tutorsMapper.selectTutorsById(id);
    }

    @Override
    public List<Tutors> selectTutorsList(Tutors tutors)
    {
        return tutorsMapper.selectTutorsList(tutors);
    }

    @Override
    public int insertTutors(Tutors tutors)
    {
        return tutorsMapper.insertTutors(tutors);
    }

    @Override
    public int updateTutors(Tutors tutors)
    {
        return tutorsMapper.updateTutors(tutors);
    }

    @Override
    public int deleteTutorsByIds(Long[] ids)
    {
        return tutorsMapper.deleteTutorsByIds(ids);
    }

    @Override
    public int deleteTutorsById(Long id)
    {
        return tutorsMapper.deleteTutorsById(id);
    }

    @Override
    public List<Tutors> selectCertifiedTutorsList(Tutors tutors) {
        return tutorsMapper.selectCertifiedTutorsList(tutors);
    }

    @Override
    public Tutors selectTutorsByUid(String uid) {
        return tutorsMapper.selectTutorsByUid(uid);
    }
}
