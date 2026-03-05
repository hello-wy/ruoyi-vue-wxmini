package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Tutors;

/**
 * 大学生/教员Service接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface ITutorsService 
{
    /**
     * 查询大学生/教员
     * 
     * @param uid 大学生/教员主键
     * @return 大学生/教员
     */
    public Tutors selectTutorsByUid(Long uid);

    /**
     * 查询大学生/教员列表
     * 
     * @param tutors 大学生/教员
     * @return 大学生/教员集合
     */
    public List<Tutors> selectTutorsList(Tutors tutors);

    /**
     * 新增大学生/教员
     * 
     * @param tutors 大学生/教员
     * @return 结果
     */
    public int insertTutors(Tutors tutors);

    /**
     * 修改大学生/教员
     * 
     * @param tutors 大学生/教员
     * @return 结果
     */
    public int updateTutors(Tutors tutors);

    /**
     * 批量删除大学生/教员
     * 
     * @param uids 需要删除的大学生/教员主键集合
     * @return 结果
     */
    public int deleteTutorsByUids(Long[] uids);

    /**
     * 删除大学生/教员信息
     * 
     * @param uid 大学生/教员主键
     * @return 结果
     */
    public int deleteTutorsByUid(Long uid);
}
