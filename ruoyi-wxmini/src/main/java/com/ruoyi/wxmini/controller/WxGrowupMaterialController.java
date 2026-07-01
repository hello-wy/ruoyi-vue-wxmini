package com.ruoyi.wxmini.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.config.MaterialFileConfig;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.sign.Md5Utils;
import com.ruoyi.system.domain.LectureMaterial;
import com.ruoyi.system.domain.vo.WxLectureMaterialVo;
import com.ruoyi.system.service.ILectureMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信小程序 - 资料中心接口
 */
@Api(tags = "【小程序】资料中心")
@RestController
@RequestMapping("/wxmini/growup/materials")
public class WxGrowupMaterialController extends BaseController
{
    private static final Pattern SAFE_FILE_NAME_PATTERN = Pattern.compile("^[A-Za-z0-9._-]+$");

    @Autowired
    private ILectureMaterialService lectureMaterialService;

    @Autowired
    private MaterialFileConfig materialFileConfig;

    @ApiOperation("获取资料中心列表（需登录）")
    @GetMapping
    public TableDataInfoVo<WxLectureMaterialVo> list(LectureMaterial lectureMaterial,
                                                      @RequestParam(value = "courseId", required = false) Long courseId)
    {
        if (lectureMaterial.getLectureId() == null && courseId != null)
        {
            lectureMaterial.setLectureId(courseId);
        }
        startPage();
        List<LectureMaterial> list = lectureMaterialService.selectWxVisibleLectureMaterialList(lectureMaterial);
        return getDataTable(toWxVos(list));
    }

    @ApiOperation("资料文件签名校验（Nginx auth_request 内部调用）")
    @Anonymous
    @GetMapping("/files/auth")
    public ResponseEntity<Void> auth(javax.servlet.http.HttpServletRequest request)
    {
        SignedDownloadRequest downloadRequest = parseSignedDownloadRequest(getOriginalUri(request));
        if (downloadRequest == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        DownloadValidationResult validationResult = validateDownload(downloadRequest.getUuid(), downloadRequest.getExp(),
                downloadRequest.getSign());
        if (!validationResult.isAllowed())
        {
            return ResponseEntity.status(validationResult.getStatus()).build();
        }
        return ResponseEntity.ok().build();
    }

    @ApiOperation("签名下载资料文件（匿名，需 exp/sign）")
    @ApiImplicitParam(name = "uuid", value = "资料文件 uuid", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    @Anonymous
    @GetMapping("/files/{uuid}")
    public AjaxResult download(@PathVariable("uuid") String uuid,
                               @RequestParam(value = "exp", required = false) Long exp,
                               @RequestParam(value = "sign", required = false) String sign,
                               HttpServletResponse response)
    {
        DownloadValidationResult validationResult = validateDownload(uuid, exp, sign);
        if (!validationResult.isAllowed())
        {
            return error(validationResult.getMessage());
        }

        LectureMaterial material = validationResult.getMaterial();
        String filePath = validationResult.getFilePath();
        try
        {
            File file = new File(filePath);
            if (!file.exists() || !file.isFile())
            {
                return error("资料文件不存在");
            }
            if (file.length() <= Integer.MAX_VALUE)
            {
                response.setContentLength((int) file.length());
            }
            FileUtils.setAttachmentResponseHeader(response,
                    StringUtils.isNotEmpty(material.getOriginalName()) ? material.getOriginalName() : uuid);
            FileUtils.writeBytes(filePath, response.getOutputStream());
        }
        catch (Exception e)
        {
            logger.error("下载资料文件失败，uuid={}", uuid, e);
            return error("下载资料文件失败");
        }
        return null;
    }

    private List<WxLectureMaterialVo> toWxVos(List<LectureMaterial> materials)
    {
        List<WxLectureMaterialVo> vos = new ArrayList<WxLectureMaterialVo>();
        if (materials == null)
        {
            return vos;
        }
        for (LectureMaterial material : materials)
        {
            vos.add(toWxVo(material));
        }
        return vos;
    }

    private WxLectureMaterialVo toWxVo(LectureMaterial material)
    {
        WxLectureMaterialVo vo = new WxLectureMaterialVo();
        vo.setId(material.getId());
        vo.setName(StringUtils.isNotEmpty(material.getName()) ? material.getName() : material.getOriginalName());
        vo.setOriginalName(material.getOriginalName());
        vo.setFileType(material.getFileType());
        vo.setFileSize(material.getFileSize());
        vo.setFileSizeText(formatFileSize(material.getFileSize()));
        vo.setCategory(material.getTag());
        vo.setTag(material.getTag());
        vo.setLectureId(material.getLectureId());
        vo.setCreateTime(material.getCreateTime());
        vo.setRelativePath(material.getRelativePath());
        String downloadUrl = buildDownloadUrl(material.getUuid());
        vo.setDownloadUrl(downloadUrl);
        vo.setFilePath(downloadUrl);
        return vo;
    }

    private String buildDownloadUrl(String uuid)
    {
        if (StringUtils.isEmpty(uuid))
        {
            return null;
        }
        long exp = System.currentTimeMillis() / 1000 + getSignExpireSeconds();
        return joinUrl(materialFileConfig.getPublicPrefix(), uuid) + "?exp=" + exp + "&sign=" + buildSign(uuid, exp);
    }

    private DownloadValidationResult validateDownload(String uuid, Long exp, String sign)
    {
        if (StringUtils.isEmpty(uuid) || exp == null || StringUtils.isEmpty(sign))
        {
            return DownloadValidationResult.fail(HttpStatus.BAD_REQUEST, "下载链接无效");
        }
        if (!SAFE_FILE_NAME_PATTERN.matcher(uuid).matches())
        {
            return DownloadValidationResult.fail(HttpStatus.FORBIDDEN, "下载链接无效");
        }
        long now = System.currentTimeMillis() / 1000;
        if (now > exp)
        {
            return DownloadValidationResult.fail(HttpStatus.FORBIDDEN, "下载链接已过期");
        }
        String expectedSign = buildSign(uuid, exp);
        if (!expectedSign.equalsIgnoreCase(sign))
        {
            return DownloadValidationResult.fail(HttpStatus.FORBIDDEN, "下载签名无效");
        }

        LectureMaterial material = lectureMaterialService.selectLectureMaterialByUuid(uuid);
        if (material == null || !Long.valueOf(1L).equals(material.getStatus()) || !Long.valueOf(0L).equals(material.getIsDeleted()))
        {
            return DownloadValidationResult.fail(HttpStatus.NOT_FOUND, "资料不存在或不可下载");
        }

        String filePath = resolveMaterialFilePath(uuid);
        File file = new File(filePath);
        if (!file.exists() || !file.isFile())
        {
            return DownloadValidationResult.fail(HttpStatus.NOT_FOUND, "资料文件不存在");
        }
        return DownloadValidationResult.success(material, filePath);
    }

    private SignedDownloadRequest parseSignedDownloadRequest(String originalUri)
    {
        if (StringUtils.isEmpty(originalUri))
        {
            return null;
        }
        String normalized = originalUri.trim();
        int queryIndex = normalized.indexOf('?');
        String path = queryIndex >= 0 ? normalized.substring(0, queryIndex) : normalized;
        String query = queryIndex >= 0 ? normalized.substring(queryIndex + 1) : "";
        String filePrefix = materialFileConfig.getPublicPrefix();
        if (!path.startsWith(filePrefix + "/"))
        {
            if (!path.startsWith("/files/"))
            {
                return null;
            }
            filePrefix = "/files";
        }
        String uuid = path.substring(filePrefix.length() + 1);
        if (!SAFE_FILE_NAME_PATTERN.matcher(uuid).matches())
        {
            return null;
        }
        String expValue = null;
        String sign = null;
        if (StringUtils.isNotEmpty(query))
        {
            String[] pairs = query.split("&");
            for (String pair : pairs)
            {
                int idx = pair.indexOf('=');
                if (idx <= 0)
                {
                    continue;
                }
                String key = pair.substring(0, idx);
                String value = pair.substring(idx + 1);
                if ("exp".equals(key))
                {
                    expValue = value;
                }
                else if ("sign".equals(key))
                {
                    sign = value;
                }
            }
        }
        Long exp = null;
        try
        {
            if (StringUtils.isNotEmpty(expValue))
            {
                exp = Long.valueOf(expValue);
            }
        }
        catch (NumberFormatException ignored)
        {
            return null;
        }
        return new SignedDownloadRequest(uuid, exp, sign);
    }

    private String getOriginalUri(javax.servlet.http.HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }
        String originalUri = request.getHeader("X-Original-URI");
        if (StringUtils.isNotEmpty(originalUri))
        {
            return originalUri;
        }
        String requestUri = request.getRequestURI();
        String queryString = request.getQueryString();
        if (StringUtils.isNotEmpty(requestUri) && StringUtils.isNotEmpty(queryString))
        {
            return requestUri + "?" + queryString;
        }
        return requestUri;
    }

    private String resolveMaterialFilePath(String uuid)
    {
        return RuoYiConfig.getProfile() + File.separator + materialFileConfig.getStorageDir() + File.separator + uuid;
    }

    private String buildSign(String uuid, Long exp)
    {
        return Md5Utils.hash(uuid + exp + materialFileConfig.getSignSecret());
    }

    private long getSignExpireSeconds()
    {
        Long expireSeconds = materialFileConfig.getSignExpireSeconds();
        if (expireSeconds == null || expireSeconds <= 0)
        {
            return 600L;
        }
        return expireSeconds;
    }

    private String joinUrl(String prefix, String suffix)
    {
        String normalizedPrefix = prefix == null ? "" : prefix.trim();
        if (normalizedPrefix.isEmpty())
        {
            normalizedPrefix = "/";
        }
        if (!normalizedPrefix.startsWith("/"))
        {
            normalizedPrefix = "/" + normalizedPrefix;
        }
        while (normalizedPrefix.endsWith("/") && normalizedPrefix.length() > 1)
        {
            normalizedPrefix = normalizedPrefix.substring(0, normalizedPrefix.length() - 1);
        }
        String normalizedSuffix = suffix == null ? "" : suffix.trim();
        while (normalizedSuffix.startsWith("/"))
        {
            normalizedSuffix = normalizedSuffix.substring(1);
        }
        return normalizedPrefix + "/" + normalizedSuffix;
    }

    private String formatFileSize(Long fileSize)
    {
        if (fileSize == null)
        {
            return "";
        }
        double size = fileSize.doubleValue();
        if (size < 1024)
        {
            return fileSize + "B";
        }
        if (size < 1024 * 1024)
        {
            return String.format("%.1fKB", size / 1024);
        }
        if (size < 1024 * 1024 * 1024)
        {
            return String.format("%.1fMB", size / 1024 / 1024);
        }
        return String.format("%.1fGB", size / 1024 / 1024 / 1024);
    }

    private static class SignedDownloadRequest
    {
        private final String uuid;

        private final Long exp;

        private final String sign;

        private SignedDownloadRequest(String uuid, Long exp, String sign)
        {
            this.uuid = uuid;
            this.exp = exp;
            this.sign = sign;
        }

        public String getUuid()
        {
            return uuid;
        }

        public Long getExp()
        {
            return exp;
        }

        public String getSign()
        {
            return sign;
        }
    }

    private static class DownloadValidationResult
    {
        private final boolean allowed;

        private final HttpStatus status;

        private final String message;

        private final LectureMaterial material;

        private final String filePath;

        private DownloadValidationResult(boolean allowed, HttpStatus status, String message, LectureMaterial material,
                String filePath)
        {
            this.allowed = allowed;
            this.status = status;
            this.message = message;
            this.material = material;
            this.filePath = filePath;
        }

        public static DownloadValidationResult success(LectureMaterial material, String filePath)
        {
            return new DownloadValidationResult(true, HttpStatus.OK, null, material, filePath);
        }

        public static DownloadValidationResult fail(HttpStatus status, String message)
        {
            return new DownloadValidationResult(false, status, message, null, null);
        }

        public boolean isAllowed()
        {
            return allowed;
        }

        public HttpStatus getStatus()
        {
            return status;
        }

        public String getMessage()
        {
            return message;
        }

        public LectureMaterial getMaterial()
        {
            return material;
        }

        public String getFilePath()
        {
            return filePath;
        }
    }
}
