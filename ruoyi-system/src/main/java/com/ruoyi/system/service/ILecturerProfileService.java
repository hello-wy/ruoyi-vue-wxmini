package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LecturerProfile;

/**
 * 讲师风采Service接口
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
public interface ILecturerProfileService 
{
    /**
     * 查询讲师风采
     * 
     * @param id 讲师风采主键
     * @return 讲师风采
     */
    public LecturerProfile selectLecturerProfileById(Long id);

    /**
     * 查询讲师风采列表
     * 
     * @param lecturerProfile 讲师风采
     * @return 讲师风采集合
     */
    public List<LecturerProfile> selectLecturerProfileList(LecturerProfile lecturerProfile);

    /**
     * 新增讲师风采
     * 
     * @param lecturerProfile 讲师风采
     * @return 结果
     */
    public int insertLecturerProfile(LecturerProfile lecturerProfile);

    /**
     * 修改讲师风采
     * 
     * @param lecturerProfile 讲师风采
     * @return 结果
     */
    public int updateLecturerProfile(LecturerProfile lecturerProfile);

    /**
     * 批量删除讲师风采
     * 
     * @param ids 需要删除的讲师风采主键集合
     * @return 结果
     */
    public int deleteLecturerProfileByIds(Long[] ids);

    /**
     * 删除讲师风采信息
     * 
     * @param id 讲师风采主键
     * @return 结果
     */
    public int deleteLecturerProfileById(Long id);
}
