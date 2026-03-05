package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Lectures;

/**
 * 课程活动/讲座Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
public interface LecturesMapper 
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
     * 删除课程活动/讲座
     * 
     * @param id 课程活动/讲座主键
     * @return 结果
     */
    public int deleteLecturesById(Long id);

    /**
     * 批量删除课程活动/讲座
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLecturesByIds(Long[] ids);
}
