package com.ruoyi.system.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import com.ruoyi.common.config.MaterialFileConfig;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.exception.file.FileNameLengthLimitExceededException;
import com.ruoyi.common.exception.file.FileSizeLimitExceededException;
import com.ruoyi.common.exception.file.InvalidExtensionException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

    @Autowired
    private MaterialFileConfig materialFileConfig;

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
     * 根据文件 uuid 查询资料中心数据
     *
     * @param uuid 文件 uuid
     * @return 资料中心数据
     */
    @Override
    public LectureMaterial selectLectureMaterialByUuid(String uuid)
    {
        return lectureMaterialMapper.selectLectureMaterialByUuid(uuid);
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
     * 查询小程序可见资料中心数据列表
     *
     * @param lectureMaterial 资料中心数据
     * @return 资料中心数据集合
     */
    @Override
    public List<LectureMaterial> selectWxVisibleLectureMaterialList(LectureMaterial lectureMaterial)
    {
        return lectureMaterialMapper.selectWxVisibleLectureMaterialList(lectureMaterial);
    }

    /**
     * 上传资料文件并返回元信息
     *
     * @param file 上传文件
     * @return 资料文件元信息
     */
    @Override
    public LectureMaterial uploadMaterialFile(MultipartFile file) throws IOException, FileSizeLimitExceededException,
            FileNameLengthLimitExceededException, InvalidExtensionException
    {
        FileUploadUtils.assertAllowed(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);

        String extension = FileUploadUtils.getExtension(file);
        String normalizedExtension = StringUtils.isNotEmpty(extension) ? extension.toLowerCase() : "";
        String uuid = IdUtils.fastSimpleUUID() + (StringUtils.isNotEmpty(normalizedExtension) ? "." + normalizedExtension : "");
        String storageDir = materialFileConfig.getStorageDir();
        File target = FileUploadUtils.getAbsoluteFile(RuoYiConfig.getProfile(), storageDir + File.separator + uuid);
        file.transferTo(Paths.get(target.getAbsolutePath()));

        String originalFilename = file.getOriginalFilename();
        LectureMaterial material = new LectureMaterial();
        material.setName(FileUtils.getNameNotSuffix(originalFilename));
        material.setUuid(uuid);
        material.setOriginalName(originalFilename);
        material.setRelativePath("/" + storageDir + "/" + uuid);
        material.setUrl(material.getRelativePath());
        material.setFileType(resolveFileType(normalizedExtension));
        material.setFileSize(file.getSize());
        return material;
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
        normalizeMaterialForSave(lectureMaterial);
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
        normalizeMaterialForSave(lectureMaterial);
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

    private void normalizeMaterialForSave(LectureMaterial lectureMaterial)
    {
        if (lectureMaterial == null)
        {
            return;
        }
        String storageDir = materialFileConfig.getStorageDir();
        if (StringUtils.isEmpty(lectureMaterial.getName()) && StringUtils.isNotEmpty(lectureMaterial.getOriginalName()))
        {
            lectureMaterial.setName(FileUtils.getNameNotSuffix(lectureMaterial.getOriginalName()));
        }
        if (StringUtils.isEmpty(lectureMaterial.getRelativePath()) && StringUtils.isNotEmpty(lectureMaterial.getUuid()))
        {
            lectureMaterial.setRelativePath("/" + storageDir + "/" + lectureMaterial.getUuid());
        }
        if (StringUtils.isEmpty(lectureMaterial.getUrl()) && StringUtils.isNotEmpty(lectureMaterial.getRelativePath()))
        {
            lectureMaterial.setUrl(lectureMaterial.getRelativePath());
        }
        if (StringUtils.isEmpty(lectureMaterial.getFileType()) && StringUtils.isNotEmpty(lectureMaterial.getUuid()))
        {
            lectureMaterial.setFileType(resolveFileType(org.apache.commons.io.FilenameUtils.getExtension(lectureMaterial.getUuid())));
        }
        if (lectureMaterial.getStatus() == null)
        {
            lectureMaterial.setStatus(1L);
        }
        if (lectureMaterial.getIsDeleted() == null)
        {
            lectureMaterial.setIsDeleted(0L);
        }
    }

    private String resolveFileType(String extension)
    {
        if (StringUtils.isEmpty(extension))
        {
            return "other";
        }
        String ext = extension.toLowerCase();
        if ("pdf".equals(ext))
        {
            return "pdf";
        }
        if ("ppt".equals(ext) || "pptx".equals(ext))
        {
            return "ppt";
        }
        if ("mp4".equals(ext) || "avi".equals(ext) || "rmvb".equals(ext) || "flv".equals(ext)
                || "wmv".equals(ext) || "mov".equals(ext))
        {
            return "video";
        }
        if ("doc".equals(ext) || "docx".equals(ext) || "txt".equals(ext))
        {
            return "doc";
        }
        if ("xls".equals(ext) || "xlsx".equals(ext))
        {
            return "xls";
        }
        return "other";
    }
}
