package com.ruoyi.system.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LecturesMapper;
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.service.ILecturesService;

/**
 * 课程活动/讲座Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-05
 */
@Service
public class LecturesServiceImpl implements ILecturesService
{
    @Autowired
    private LecturesMapper lecturesMapper;

     /*
     * @param id 课程活动/讲座主键
     *
     */
    @Override
    public Lectures selectLecturesById(Long id)
    {
        return lecturesMapper.selectLecturesById(id);
    }

    /**
     * 查询课程活动/讲座列表
     *
     * @param lectures 课程活动/讲座
     *
     */
    @Override
    public List<Lectures> selectLecturesList(Lectures lectures)
    {
        return lecturesMapper.selectLecturesList(lectures);
    }

    /**
     * 新增课程活动/讲座
     *
     * @param lectures 课程活动/讲座
     *
     */
    @Override
    public int insertLectures(Lectures lectures)
    {
        return lecturesMapper.insertLectures(lectures);
    }

    /**
     * 修改课程活动/讲座
     *
     * @param lectures 课程活动/讲座
     *
     */
    @Override
    public int updateLectures(Lectures lectures)
    {
        return lecturesMapper.updateLectures(lectures);
    }

    /**
     * 批量删除课程活动/讲座
     *
     * @param ids 需要删除的课程活动/讲座主键
     *
     */
    @Override
    public int deleteLecturesByIds(Long[] ids)
    {
        return lecturesMapper.deleteLecturesByIds(ids);
    }

    /**
     * 删除课程活动/讲座信息
     *
     * @param id 课程活动/讲座主键
     * @return 结果
     */
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

}
