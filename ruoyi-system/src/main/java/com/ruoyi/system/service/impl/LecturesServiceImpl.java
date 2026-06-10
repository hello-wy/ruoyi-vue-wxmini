package com.ruoyi.system.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.system.mapper.QuestionnaireMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LecturesMapper;
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.domain.vo.LecturesDetailVo;
import com.ruoyi.system.domain.vo.LecturesListVo;
import com.ruoyi.system.service.ILecturesService;

/**
 * 课程活动/讲座Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class LecturesServiceImpl implements ILecturesService
{
    @Autowired
    private LecturesMapper lecturesMapper;

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Override
    public Lectures selectLecturesById(Long id)
    {
        return lecturesMapper.selectLecturesById(id);
    }

    @Override
    public List<Lectures> selectLecturesList(Lectures lectures)
    {
        return lecturesMapper.selectLecturesList(lectures);
    }

    @Override
    public int insertLectures(Lectures lectures)
    {
        return lecturesMapper.insertLectures(lectures);
    }

    @Override
    public int updateLectures(Lectures lectures)
    {
        return lecturesMapper.updateLectures(lectures);
    }

    @Override
    public int increaseEnrolledCount(Long id)
    {
        return lecturesMapper.increaseEnrolledCount(id);
    }

    @Override
    public int deleteLecturesByIds(Long[] ids)
    {
        return lecturesMapper.deleteLecturesByIds(ids);
    }

    @Override
    public int deleteLecturesById(Long id)
    {
        return lecturesMapper.deleteLecturesById(id);
    }

    @Override
    public List<Lectures> selectRecentLecturesList()
    {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startTime = localDateTime.with(TemporalAdjusters.firstDayOfMonth())
                .with(LocalTime.MIN);
        LocalDateTime endTime = localDateTime.with(TemporalAdjusters.lastDayOfMonth())
                .with(LocalTime.MAX);

        LambdaQueryWrapper<Lectures> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(Lectures::getTime, startTime, endTime);
        return lecturesMapper.selectList(wrapper);
    }

    @Override
    public List<LecturesListVo> selectLecturesListVo(Lectures lectures)
    {
        return lecturesMapper.selectLecturesListVo(lectures);
    }

    @Override
    public LecturesDetailVo selectLecturesDetailById(Long id)
    {
        return lecturesMapper.selectLecturesDetailVoById(id);
    }
}
