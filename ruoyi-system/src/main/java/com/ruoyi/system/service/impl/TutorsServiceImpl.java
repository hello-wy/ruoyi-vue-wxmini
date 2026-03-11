package com.ruoyi.system.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TutorsMapper;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.service.ITutorsService;

/**
 * 大学生/教员Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
@Service
public class TutorsServiceImpl implements ITutorsService 
{
    @Autowired
    private TutorsMapper tutorsMapper;

    /**
     * 查询大学生/教员
     * 
     * @param id 大学生/教员主键
     * @return 大学生/教员
     */
    @Override
    public Tutors selectTutorsById(Long id)
    {
        return tutorsMapper.selectTutorsById(id);
    }

    /**
     * 查询大学生/教员列表
     * 
     * @param tutors 大学生/教员
     * @return 大学生/教员
     */
    @Override
    public List<Tutors> selectTutorsList(Tutors tutors)
    {
        return tutorsMapper.selectTutorsList(tutors);
    }

    /**
     * 新增大学生/教员
     * 
     * @param tutors 大学生/教员
     * @return 结果
     */
    @Override
    public int insertTutors(Tutors tutors)
    {
        return tutorsMapper.insertTutors(tutors);
    }

    /**
     * 修改大学生/教员
     * 
     * @param tutors 大学生/教员
     * @return 结果
     */
    @Override
    public int updateTutors(Tutors tutors)
    {
        return tutorsMapper.updateTutors(tutors);
    }

    /**
     * 批量删除大学生/教员
     * 
     * @param ids 需要删除的大学生/教员主键
     * @return 结果
     */
    @Override
    public int deleteTutorsByIds(Long[] ids)
    {
        return tutorsMapper.deleteTutorsByIds(ids);
    }

    /**
     * 删除大学生/教员信息
     * 
     * @param id 大学生/教员主键
     * @return 结果
     */
    @Override
    public int deleteTutorsById(Long id)
    {
        return tutorsMapper.deleteTutorsById(id);
    }

    @Override
    public List<Tutors> selectCertifiedTutorsList(Tutors tutors) {
        LambdaQueryWrapper<Tutors> wrapper = new LambdaQueryWrapper<>();
        // 只查询已认证（isCertified = 1）
        wrapper.eq(Tutors::getIsCertified, 1L);
        // 可选筛选：科目
        if (StringUtils.isNotBlank(tutors.getSubjects())) {
            wrapper.like(Tutors::getSubjects, tutors.getSubjects());
        }
        // 可选筛选：区域
        if (StringUtils.isNotBlank(tutors.getAreas())) {
            wrapper.like(Tutors::getAreas, tutors.getAreas());
        }
        // 可选筛选：授课方式
        if (tutors.getMethods() != null) {
            wrapper.eq(Tutors::getMethods, tutors.getMethods());
        }
        // 可选筛选：学历
        if (tutors.getDegree() != null) {
            wrapper.eq(Tutors::getDegree, tutors.getDegree());
        }
        return tutorsMapper.selectList(wrapper);
    }

    @Override
    public Tutors selectTutorsByUid(Long uid) {
        LambdaQueryWrapper<Tutors> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tutors::getUid, uid);
        return tutorsMapper.selectOne(wrapper);
    }
}
