package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LectureMaterial;

/**
 * 资料中心数据Mapper接口
 *
 * @author ruoyi
 * @date 2026-03-07
 */
public interface LectureMaterialMapper
{
    /**
     * 查询资料中心数据
     *
     * @param id 资料中心数据主键
     * @return 资料中心数据
     */
    public LectureMaterial selectLectureMaterialById(Long id);

    /**
     * 根据文件 uuid 查询资料中心数据
     *
     * @param uuid 文件 uuid
     * @return 资料中心数据
     */
    public LectureMaterial selectLectureMaterialByUuid(String uuid);

    /**
     * 查询资料中心数据列表
     *
     * @param lectureMaterial 资料中心数据
     * @return 资料中心数据集合
     */
    public List<LectureMaterial> selectLectureMaterialList(LectureMaterial lectureMaterial);

    /**
     * 查询小程序可见资料中心数据列表
     *
     * @param lectureMaterial 资料中心数据筛选条件
     * @return 资料中心数据集合
     */
    public List<LectureMaterial> selectWxVisibleLectureMaterialList(LectureMaterial lectureMaterial);

    /**
     * 新增资料中心数据
     *
     * @param lectureMaterial 资料中心数据
     * @return 结果
     */
    public int insertLectureMaterial(LectureMaterial lectureMaterial);

    /**
     * 修改资料中心数据
     *
     * @param lectureMaterial 资料中心数据
     * @return 结果
     */
    public int updateLectureMaterial(LectureMaterial lectureMaterial);

    /**
     * 删除资料中心数据
     *
     * @param id 资料中心数据主键
     * @return 结果
     */
    public int deleteLectureMaterialById(Long id);

    /**
     * 批量删除资料中心数据
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLectureMaterialByIds(Long[] ids);
}
