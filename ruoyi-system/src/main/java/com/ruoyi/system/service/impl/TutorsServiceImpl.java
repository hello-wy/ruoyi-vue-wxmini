package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TutorsMapper;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.service.ITutorsService;

/**
 * 大学生/教员Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class TutorsServiceImpl implements ITutorsService 
{
    @Autowired
    private TutorsMapper tutorsMapper;

    /**
     * 查询大学生/教员
     * 
     * @param uid 大学生/教员主键
     * @return 大学生/教员
     */
    @Override
    public Tutors selectTutorsByUid(Long uid)
    {
        return tutorsMapper.selectTutorsByUid(uid);
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
     * @param uids 需要删除的大学生/教员主键
     * @return 结果
     */
    @Override
    public int deleteTutorsByUids(Long[] uids)
    {
        return tutorsMapper.deleteTutorsByUids(uids);
    }

    /**
     * 删除大学生/教员信息
     * 
     * @param uid 大学生/教员主键
     * @return 结果
     */
    @Override
    public int deleteTutorsByUid(Long uid)
    {
        return tutorsMapper.deleteTutorsByUid(uid);
    }
}
