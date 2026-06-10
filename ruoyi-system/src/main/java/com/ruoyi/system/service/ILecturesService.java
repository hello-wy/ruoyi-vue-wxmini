package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.domain.vo.LecturesDetailVo;
import com.ruoyi.system.domain.vo.LecturesListVo;

/**
 * 课程活动/讲座Service接口
 *
 * @author ruoyi
 */
public interface ILecturesService
{
    public Lectures selectLecturesById(Long id);

    public List<Lectures> selectLecturesList(Lectures lectures);

    public int insertLectures(Lectures lectures);

    public int updateLectures(Lectures lectures);

    public int increaseEnrolledCount(Long id);

    public int deleteLecturesByIds(Long[] ids);

    public int deleteLecturesById(Long id);

    public List<Lectures> selectRecentLecturesList();

    /**
     * 查询讲座列表，speaker 字段解析为拼接的讲师姓名字符串
     *
     * @param lectures 查询条件
     * @return 讲座列表 VO（含 speakerNames 字段）
     */
    List<LecturesListVo> selectLecturesListVo(Lectures lectures);

    /**
     * 查询讲座详情，speaker 字段解析为讲师 id/name/avatarUrl 对象列表
     *
     * @param id 讲座主键
     * @return 讲座详情 VO（含 speakers 列表）
     */
    LecturesDetailVo selectLecturesDetailById(Long id);
}
