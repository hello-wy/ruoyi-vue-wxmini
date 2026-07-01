package com.ruoyi.system.service;

import java.io.IOException;
import java.util.List;
import com.ruoyi.common.exception.file.FileNameLengthLimitExceededException;
import com.ruoyi.common.exception.file.FileSizeLimitExceededException;
import com.ruoyi.common.exception.file.InvalidExtensionException;
import com.ruoyi.system.domain.LectureMaterial;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资料中心数据Service接口
 *
 * @author ruoyi
 * @date 2026-03-07
 */
public interface ILectureMaterialService
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
     * @param lectureMaterial 资料中心数据
     * @return 资料中心数据集合
     */
    public List<LectureMaterial> selectWxVisibleLectureMaterialList(LectureMaterial lectureMaterial);

    /**
     * 上传资料文件并返回元信息
     *
     * @param file 上传文件
     * @return 资料文件元信息
     */
    public LectureMaterial uploadMaterialFile(MultipartFile file) throws IOException, FileSizeLimitExceededException,
            FileNameLengthLimitExceededException, InvalidExtensionException;

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
     * 批量删除资料中心数据
     *
     * @param ids 需要删除的资料中心数据主键集合
     * @return 结果
     */
    public int deleteLectureMaterialByIds(Long[] ids);

    /**
     * 删除资料中心数据信息
     *
     * @param id 资料中心数据主键
     * @return 结果
     */
    public int deleteLectureMaterialById(Long id);
}
