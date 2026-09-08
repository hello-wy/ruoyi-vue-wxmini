package com.ruoyi.wxmini.controller;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.system.domain.TutorMaterial;
import com.ruoyi.system.enums.TutorMaterialType;
import com.ruoyi.system.service.ITutorMaterialService;
import com.ruoyi.wxmini.bo.WxTutorMaterialBo;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Api(tags = "教员材料")
@RestController
@RequestMapping("/wxmini/tutoring/materials")
public class WxTutorMaterialController {
    private static final long MAX_MATERIAL_FILE_SIZE = 3L * 1024L * 1024L;
    private static final String MATERIAL_ROOT_DIRECTORY = "certification";
    private static final String[] ALLOWED_IMAGE_EXTENSIONS = {"jpg", "jpeg", "png"};

    @Resource
    private ITutorMaterialService tutorMaterialService;

    @Resource
    private ServerConfig serverConfig;

    @ApiOperation("上传身份证正面")
    @PostMapping("/upload/id-card-front")
    public AjaxResult uploadIdCardFront(MultipartFile file) {
        return uploadMaterial(file, TutorMaterialType.ID_CARD_FRONT);
    }

    @ApiOperation("上传身份证反面")
    @PostMapping("/upload/id-card-back")
    public AjaxResult uploadIdCardBack(MultipartFile file) {
        return uploadMaterial(file, TutorMaterialType.ID_CARD_BACK);
    }

    @ApiOperation("上传学生证")
    @PostMapping("/upload/student-card")
    public AjaxResult uploadStudentCard(MultipartFile file) {
        return uploadMaterial(file, TutorMaterialType.STUDENT_CARD);
    }

    @ApiOperation("上传证书")
    @PostMapping("/upload/certificate")
    public AjaxResult uploadCertificate(MultipartFile file) {
        return uploadMaterial(file, TutorMaterialType.CERTIFICATE);
    }

    @ApiOperation("登记已上传的教员审核材料")
    @PostMapping
    public AjaxResult create(@RequestBody WxTutorMaterialBo body) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        if (body == null) {
            return AjaxResult.error("材料参数不能为空");
        }
        TutorMaterial material = tutorMaterialService.createOwnedMaterial(
                userId,
                body.getType(),
                body.getUrl()
        );
        return AjaxResult.success("材料登记成功", material);
    }

    private AjaxResult uploadMaterial(MultipartFile file, TutorMaterialType materialType) {
        try {
            String userId = requireCurrentUserId();
            String extension = validateImage(file);
            Path directory = resolveMaterialDirectory(userId, materialType);
            Files.createDirectories(directory);

            String fileName = buildFileName(materialType, extension);
            file.transferTo(directory.resolve(fileName).toFile());
            String resourcePath = buildResourcePath(userId, materialType, fileName);
            return buildUploadResult(file, resourcePath, materialType);
        } catch (IllegalArgumentException e) {
            return AjaxResult.error(e.getMessage());
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    private String requireCurrentUserId() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            throw new IllegalArgumentException("请先登录");
        }
        return userId;
    }

    private String validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        if (file.getSize() > MAX_MATERIAL_FILE_SIZE) {
            throw new IllegalArgumentException("图片大小不能超过 3MB");
        }
        String extension = StringUtils.lowerCase(FileUploadUtils.getExtension(file));
        if (!FileUploadUtils.isAllowedExtension(extension, ALLOWED_IMAGE_EXTENSIONS)) {
            throw new IllegalArgumentException("仅支持上传 JPG、JPEG、PNG 格式图片");
        }
        return extension;
    }

    private Path resolveMaterialDirectory(String userId, TutorMaterialType materialType) {
        Path root = Paths.get(RuoYiConfig.getProfile(), MATERIAL_ROOT_DIRECTORY)
                .toAbsolutePath()
                .normalize();
        Path userDirectory = root.resolve(userId).normalize();
        if (!userDirectory.startsWith(root)) {
            throw new IllegalArgumentException("用户标识非法");
        }
        return userDirectory.resolve(materialType.getDirectoryName());
    }

    private String buildFileName(TutorMaterialType materialType, String extension) {
        return materialType.getDirectoryName() + "-" + IdUtils.fastUUID() + "." + extension;
    }

    private String buildResourcePath(
            String userId,
            TutorMaterialType materialType,
            String fileName
    ) {
        return String.format(
                "/profile/%s/%s/%s/%s",
                MATERIAL_ROOT_DIRECTORY,
                userId,
                materialType.getDirectoryName(),
                fileName
        );
    }

    private AjaxResult buildUploadResult(
            MultipartFile file,
            String resourcePath,
            TutorMaterialType materialType
    ) {
        String fileName = Paths.get(resourcePath).getFileName().toString();
        AjaxResult result = AjaxResult.success();
        result.put("url", serverConfig.getUrl() + resourcePath);
        result.put("fileName", resourcePath);
        result.put("newFileName", fileName);
        result.put("originalFilename", file.getOriginalFilename());
        result.put("type", materialType.getCode());
        result.put("typeName", materialType.getDisplayName());
        result.put("directoryName", materialType.getDirectoryName());
        return result;
    }
}
