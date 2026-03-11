package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LecturerProfile;

/**
 * 讲师风采Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
public interface LecturerProfileMapper 
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
     * 删除讲师风采
     * 
     * @param id 讲师风采主键
     * @return 结果
     */
    public int deleteLecturerProfileById(Long id);

    /**
     * 批量删除讲师风采
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLecturerProfileByIds(Long[] ids);
}
