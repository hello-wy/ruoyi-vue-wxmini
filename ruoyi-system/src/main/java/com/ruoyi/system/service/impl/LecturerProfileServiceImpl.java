package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LecturerProfileMapper;
import com.ruoyi.system.domain.LecturerProfile;
import com.ruoyi.system.service.ILecturerProfileService;

/**
 * 讲师风采Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
@Service
public class LecturerProfileServiceImpl implements ILecturerProfileService 
{
    @Autowired
    private LecturerProfileMapper lecturerProfileMapper;

    /**
     * 查询讲师风采
     * 
     * @param id 讲师风采主键
     * @return 讲师风采
     */
    @Override
    public LecturerProfile selectLecturerProfileById(Long id)
    {
        return lecturerProfileMapper.selectLecturerProfileById(id);
    }

    /**
     * 查询讲师风采列表
     * 
     * @param lecturerProfile 讲师风采
     * @return 讲师风采
     */
    @Override
    public List<LecturerProfile> selectLecturerProfileList(LecturerProfile lecturerProfile)
    {
        return lecturerProfileMapper.selectLecturerProfileList(lecturerProfile);
    }

    /**
     * 新增讲师风采
     * 
     * @param lecturerProfile 讲师风采
     * @return 结果
     */
    @Override
    public int insertLecturerProfile(LecturerProfile lecturerProfile)
    {
        lecturerProfile.setCreateTime(DateUtils.getNowDate());
        return lecturerProfileMapper.insertLecturerProfile(lecturerProfile);
    }

    /**
     * 修改讲师风采
     * 
     * @param lecturerProfile 讲师风采
     * @return 结果
     */
    @Override
    public int updateLecturerProfile(LecturerProfile lecturerProfile)
    {
        lecturerProfile.setUpdateTime(DateUtils.getNowDate());
        return lecturerProfileMapper.updateLecturerProfile(lecturerProfile);
    }

    /**
     * 批量删除讲师风采
     * 
     * @param ids 需要删除的讲师风采主键
     * @return 结果
     */
    @Override
    public int deleteLecturerProfileByIds(Long[] ids)
    {
        return lecturerProfileMapper.deleteLecturerProfileByIds(ids);
    }

    /**
     * 删除讲师风采信息
     * 
     * @param id 讲师风采主键
     * @return 结果
     */
    @Override
    public int deleteLecturerProfileById(Long id)
    {
        return lecturerProfileMapper.deleteLecturerProfileById(id);
    }
}
