package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LectureMaterialMapper;
import com.ruoyi.system.domain.LectureMaterial;
import com.ruoyi.system.service.ILectureMaterialService;

/**
 * 资料中心数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
@Service
public class LectureMaterialServiceImpl implements ILectureMaterialService 
{
    @Autowired
    private LectureMaterialMapper lectureMaterialMapper;

    /**
     * 查询资料中心数据
     * 
     * @param id 资料中心数据主键
     * @return 资料中心数据
     */
    @Override
    public LectureMaterial selectLectureMaterialById(Long id)
    {
        return lectureMaterialMapper.selectLectureMaterialById(id);
    }

    /**
     * 查询资料中心数据列表
     * 
     * @param lectureMaterial 资料中心数据
     * @return 资料中心数据
     */
    @Override
    public List<LectureMaterial> selectLectureMaterialList(LectureMaterial lectureMaterial)
    {
        return lectureMaterialMapper.selectLectureMaterialList(lectureMaterial);
    }

    /**
     * 新增资料中心数据
     * 
     * @param lectureMaterial 资料中心数据
     * @return 结果
     */
    @Override
    public int insertLectureMaterial(LectureMaterial lectureMaterial)
    {
        lectureMaterial.setCreateTime(DateUtils.getNowDate());
        return lectureMaterialMapper.insertLectureMaterial(lectureMaterial);
    }

    /**
     * 修改资料中心数据
     * 
     * @param lectureMaterial 资料中心数据
     * @return 结果
     */
    @Override
    public int updateLectureMaterial(LectureMaterial lectureMaterial)
    {
        lectureMaterial.setUpdateTime(DateUtils.getNowDate());
        return lectureMaterialMapper.updateLectureMaterial(lectureMaterial);
    }

    /**
     * 批量删除资料中心数据
     * 
     * @param ids 需要删除的资料中心数据主键
     * @return 结果
     */
    @Override
    public int deleteLectureMaterialByIds(Long[] ids)
    {
        return lectureMaterialMapper.deleteLectureMaterialByIds(ids);
    }

    /**
     * 删除资料中心数据信息
     * 
     * @param id 资料中心数据主键
     * @return 结果
     */
    @Override
    public int deleteLectureMaterialById(Long id)
    {
        return lectureMaterialMapper.deleteLectureMaterialById(id);
    }
}
