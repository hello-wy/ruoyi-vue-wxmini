package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Lectures;

/**
 * 课程活动/讲座Service接口
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
public interface ILecturesService 
{
    /**
     * 查询课程活动/讲座
     * 
     * @param id 课程活动/讲座主键
     * @return 课程活动/讲座
     */
    public Lectures selectLecturesById(Long id);

    /**
     * 查询课程活动/讲座列表
     * 
     * @param lectures 课程活动/讲座
     * @return 课程活动/讲座集合
     */
    public List<Lectures> selectLecturesList(Lectures lectures);

    /**
     * 新增课程活动/讲座
     * 
     * @param lectures 课程活动/讲座
     * @return 结果
     */
    public int insertLectures(Lectures lectures);

    /**
     * 修改课程活动/讲座
     * 
     * @param lectures 课程活动/讲座
     * @return 结果
     */
    public int updateLectures(Lectures lectures);

    /**
     * 批量删除课程活动/讲座
     * 
     * @param ids 需要删除的课程活动/讲座主键集合
     * @return 结果
     */
    public int deleteLecturesByIds(Long[] ids);

    /**
     * 删除课程活动/讲座信息
     * 
     * @param id 课程活动/讲座主键
     * @return 结果
     */
    public int deleteLecturesById(Long id);

    public List<Lectures> selectRecentLecturesList();
}
