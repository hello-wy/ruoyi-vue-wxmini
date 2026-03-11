package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.Tutors;

/**
 * 大学生/教员Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
public interface TutorsMapper extends BaseMapper<Tutors>
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
     * 删除大学生/教员
     * 
     * @param id 大学生/教员主键
     * @return 结果
     */
    public int deleteTutorsById(Long id);

    /**
     * 批量删除大学生/教员
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTutorsByIds(Long[] ids);
}
