package com.ruoyi.system.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.domain.vo.LecturesDetailVo;
import com.ruoyi.system.domain.vo.LecturesListVo;

/**
 * 课程活动/讲座Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
public interface LecturesMapper extends BaseMapper<Lectures>
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
     * 查询课程模板列表。
     *
     * @return 按课程名称去重后的课程配置集合
     */
    public List<Lectures> selectLecturesTemplateList();

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
     * 已报名人数加一。
     *
     * @param id 课程活动/讲座主键
     * @return 结果
     */
    public int increaseEnrolledCount(Long id);

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

    /**
     * 查询讲座列表 VO（通过 SQL JOIN 拼接讲师姓名）
     *
     * @param lectures 查询条件
     * @return 讲座列表 VO 集合
     */
    public List<LecturesListVo> selectLecturesListVo(Lectures lectures);

    /**
     * 查询本月剩余时间内的讲座列表 VO。
     *
     * @param lectures 查询条件
     * @param currentTime 当前时刻（含）
     * @param nextMonthStart 下个月开始时间（不含）
     * @return 本月剩余时间内的讲座列表 VO 集合
     */
    public List<LecturesListVo> selectRemainingMonthLecturesListVo(
            @Param("lectures") Lectures lectures,
            @Param("currentTime") Date currentTime,
            @Param("nextMonthStart") Date nextMonthStart);

    /**
     * 查询讲座详情 VO（通过 SQL JOIN 关联讲师信息）
     *
     * @param id 讲座主键
     * @return 讲座详情 VO
     */
    public LecturesDetailVo selectLecturesDetailVoById(Long id);
}
