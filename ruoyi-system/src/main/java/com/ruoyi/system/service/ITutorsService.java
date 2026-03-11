package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Tutors;

/**
 * 大学生/教员Service接口
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
public interface ITutorsService 
{
    /**
     * 查询大学生/教员
     * 
     * @param id 大学生/教员主键
     * @return 大学生/教员
     */
    public Tutors selectTutorsById(Long id);

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
     * @param ids 需要删除的大学生/教员主键集合
     * @return 结果
     */
    public int deleteTutorsByIds(Long[] ids);

    /**
     * 删除大学生/教员信息
     * 
     * @param id 大学生/教员主键
     * @return 结果
     */
    public int deleteTutorsById(Long id);

    /**
     * 查询已认证教员列表（isCertified=1），支持筛选（配合 PageHelper 分页）
     *
     * @param tutors 筛选条件
     * @return 教员列表
     */
    List<Tutors> selectCertifiedTutorsList(Tutors tutors);

    /**
     * 根据uid查询教员信息
     *
     * @param uid 关联的用户id
     * @return 教员信息
     */
    Tutors selectTutorsByUid(Long uid);
}
