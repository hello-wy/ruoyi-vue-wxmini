package com.ruoyi.wxmini.controller;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/wxmini/common")
public class WxCommonController {

    private static final String[] ALLOWED_IMAGE_EXTENSIONS = {"jpg", "jpeg", "png"};

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private IUserInfoService userInfoService;

    @PostMapping("/uploadCertification")
    public AjaxResult uploadCertification(MultipartFile file) {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            if (StringUtils.isBlank(userId)) {
                return AjaxResult.error("请先登录");
            }
            if (file == null || file.isEmpty()) {
                return AjaxResult.error("上传文件不能为空");
            }

            String extension = StringUtils.lowerCase(FileUploadUtils.getExtension(file));
            if (!FileUploadUtils.isAllowedExtension(extension, ALLOWED_IMAGE_EXTENSIONS)) {
                return AjaxResult.error("仅支持上传 JPG、JPEG、PNG 格式图片");
            }

            Path certificationDir = Paths.get(RuoYiConfig.getProfile(), "certification");
            Files.createDirectories(certificationDir);
            deleteOldFiles(certificationDir, userId, extension);

            String fileName = userId + "." + extension;
            Path target = certificationDir.resolve(fileName);
            file.transferTo(target.toFile());

            String resourcePath = "/profile/certification/" + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", serverConfig.getUrl() + resourcePath);
            ajax.put("fileName", resourcePath);
            ajax.put("newFileName", fileName);
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/uploadJobSignImage")
    public AjaxResult uploadJobSignImage(@RequestParam("jobId") Long jobId, MultipartFile file) {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            if (StringUtils.isBlank(userId)) {
                return AjaxResult.error("请先登录");
            }
            if (jobId == null) {
                return AjaxResult.error("岗位不能为空");
            }
            if (file == null || file.isEmpty()) {
                return AjaxResult.error("上传文件不能为空");
            }

            String extension = StringUtils.lowerCase(FileUploadUtils.getExtension(file));
            if (!FileUploadUtils.isAllowedExtension(extension, ALLOWED_IMAGE_EXTENSIONS)) {
                return AjaxResult.error("仅支持上传 JPG、JPEG、PNG 格式图片");
            }

            String uploadDir = Paths.get(RuoYiConfig.getProfile(), "sign", "job", String.valueOf(jobId)).toString();
            String resourcePath = FileUploadUtils.upload(uploadDir, file, ALLOWED_IMAGE_EXTENSIONS);
            String newFileName = Paths.get(resourcePath).getFileName().toString();

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", serverConfig.getUrl() + resourcePath);
            ajax.put("fileName", resourcePath);
            ajax.put("newFileName", newFileName);
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/uploadAvatar")
    public AjaxResult uploadAvatar(MultipartFile file) {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            if (StringUtils.isBlank(userId)) {
                return AjaxResult.error("请先登录");
            }
            if (file == null || file.isEmpty()) {
                return AjaxResult.error("上传文件不能为空");
            }

            String extension = StringUtils.lowerCase(FileUploadUtils.getExtension(file));
            if (!FileUploadUtils.isAllowedExtension(extension, ALLOWED_IMAGE_EXTENSIONS)) {
                return AjaxResult.error("仅支持上传 JPG、JPEG、PNG 格式图片");
            }

            Path avatarDir = Paths.get(RuoYiConfig.getProfile(), "avatar");
            Files.createDirectories(avatarDir);
            deleteOldFiles(avatarDir, userId, null);

            String fileName = userId + ".png";
            Path target = avatarDir.resolve(fileName);
            writeAvatarAsPng(file, target);

            String resourcePath = "/profile/avatar/" + fileName;
            int rows = userInfoService.updateAvatarUrlByUserId(userId, resourcePath);
            if (rows <= 0) {
                FileUtils.deleteFile(target.toString());
                return AjaxResult.error("用户不存在");
            }

            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", serverConfig.getUrl() + resourcePath);
            ajax.put("fileName", resourcePath);
            ajax.put("newFileName", fileName);
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (IllegalArgumentException e) {
            return AjaxResult.error(e.getMessage());
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    private void writeAvatarAsPng(MultipartFile file, Path target) throws Exception {
        BufferedImage image;
        try (InputStream inputStream = file.getInputStream()) {
            image = ImageIO.read(inputStream);
        }
        if (image == null) {
            throw new IllegalArgumentException("图片内容无效");
        }
        try (OutputStream outputStream = Files.newOutputStream(target)) {
            ImageIO.write(image, "png", outputStream);
        }
    }

    private void deleteOldFiles(Path certificationDir, String userId, String currentExtension) {
        for (String extension : ALLOWED_IMAGE_EXTENSIONS) {
            if (extension.equalsIgnoreCase(currentExtension)) {
                continue;
            }
            FileUtils.deleteFile(certificationDir.resolve(userId + "." + extension).toString());
        }
    }
}
