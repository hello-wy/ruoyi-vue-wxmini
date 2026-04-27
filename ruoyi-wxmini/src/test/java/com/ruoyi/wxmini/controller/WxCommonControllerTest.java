package com.ruoyi.wxmini.controller;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WxCommonControllerTest {

    @TempDir
    Path tempDir;

    private WxCommonController controller;
    private IUserInfoService userInfoService;

    @BeforeEach
    void setUp() {
        FileUploadUtils.setDefaultBaseDir(tempDir.toString());
        RuoYiConfig config = new RuoYiConfig();
        config.setProfile(tempDir.toString());
        controller = new WxCommonController();
        userInfoService = mock(IUserInfoService.class);
        ReflectionTestUtils.setField(controller, "serverConfig", new ServerConfig());
        ReflectionTestUtils.setField(controller, "userInfoService", userInfoService);
        WxMiniUserContext.setCurrentUserId("321");
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
        WxMiniUserContext.clear();
    }

    @Test
    void serverConfigInjectionShouldNotRequireRequestContextAtBeanInitialization() {
        WxCommonController freshController = new WxCommonController();
        assertDoesNotThrow(() -> ReflectionTestUtils.setField(freshController, "serverConfig", new ServerConfig()));
    }

    @Test
    void uploadCertificationShouldSaveAsUserIdAndDeleteOlderExtensionFile() throws Exception {
        Path certificationDir = tempDir.resolve("certification");
        Files.createDirectories(certificationDir);
        Files.write(certificationDir.resolve("321.jpg"), "old".getBytes(StandardCharsets.UTF_8));

        MockHttpServletRequest request = buildRequest("/wxmini/common/uploadCertification");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        MockMultipartFile file = new MockMultipartFile(
            "file",
            "certificate.png",
            "image/png",
            "new-image".getBytes(StandardCharsets.UTF_8)
        );

        AjaxResult result = controller.uploadCertification(file);

        assertEquals(200, result.get("code"));
        assertEquals("http://localhost:8080/profile/certification/321.png", result.get("url"));
        assertEquals("/profile/certification/321.png", result.get("fileName"));
        assertEquals("321.png", result.get("newFileName"));
        assertEquals("certificate.png", result.get("originalFilename"));
        assertTrue(Files.exists(certificationDir.resolve("321.png")));
        assertFalse(Files.exists(certificationDir.resolve("321.jpg")));
    }

    @Test
    void uploadAvatarShouldTranscodeJpgToPngUpdateAvatarUrlAndDeleteOlderExtensionFiles() throws Exception {
        Path avatarDir = tempDir.resolve("avatar");
        Files.createDirectories(avatarDir);
        Files.write(avatarDir.resolve("321.jpeg"), "older-jpeg".getBytes(StandardCharsets.UTF_8));
        Files.write(avatarDir.resolve("321.png"), "older-png".getBytes(StandardCharsets.UTF_8));
        when(userInfoService.updateAvatarUrlByUserId("321", "/profile/avatar/321.png")).thenReturn(1);

        MockHttpServletRequest request = buildRequest("/wxmini/common/uploadAvatar");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        MockMultipartFile file = new MockMultipartFile(
            "file",
            "avatar.jpg",
            "image/jpeg",
            createImageBytes("jpg")
        );

        AjaxResult result = controller.uploadAvatar(file);

        Path savedFile = avatarDir.resolve("321.png");
        assertEquals(200, result.get("code"));
        assertEquals("http://localhost:8080/profile/avatar/321.png", result.get("url"));
        assertEquals("/profile/avatar/321.png", result.get("fileName"));
        assertEquals("321.png", result.get("newFileName"));
        assertEquals("avatar.jpg", result.get("originalFilename"));
        assertTrue(Files.exists(savedFile));
        assertFalse(Files.exists(avatarDir.resolve("321.jpg")));
        assertFalse(Files.exists(avatarDir.resolve("321.jpeg")));
        assertEquals("png", readImageFormat(savedFile).toLowerCase());
        verify(userInfoService).updateAvatarUrlByUserId("321", "/profile/avatar/321.png");
    }

    @Test
    void uploadAvatarShouldDeleteUploadedFileWhenUserDoesNotExist() throws Exception {
        Path avatarDir = tempDir.resolve("avatar");
        Files.createDirectories(avatarDir);
        when(userInfoService.updateAvatarUrlByUserId("321", "/profile/avatar/321.png")).thenReturn(0);

        MockHttpServletRequest request = buildRequest("/wxmini/common/uploadAvatar");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        MockMultipartFile file = new MockMultipartFile(
            "file",
            "avatar.jpg",
            "image/jpeg",
            createImageBytes("jpg")
        );

        AjaxResult result = controller.uploadAvatar(file);

        assertEquals(500, result.get("code"));
        assertEquals("用户不存在", result.get("msg"));
        assertFalse(Files.exists(avatarDir.resolve("321.png")));
    }

    @Test
    void uploadAvatarShouldRejectUnreadableImageContent() {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "avatar.jpg",
            "image/jpeg",
            "not-an-image".getBytes(StandardCharsets.UTF_8)
        );

        AjaxResult result = controller.uploadAvatar(file);

        assertEquals(500, result.get("code"));
        assertEquals("图片内容无效", result.get("msg"));
    }

    @Test
    void uploadCertificationShouldRejectNonImageExtension() {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "certificate.pdf",
            "application/pdf",
            "fake-pdf".getBytes(StandardCharsets.UTF_8)
        );

        AjaxResult result = controller.uploadCertification(file);

        assertEquals(500, result.get("code"));
        assertEquals("仅支持上传 JPG、JPEG、PNG 格式图片", result.get("msg"));
    }

    @Test
    void uploadCertificationShouldRequireLoggedInWxUser() {
        WxMiniUserContext.clear();
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "certificate.png",
            "image/png",
            "new-image".getBytes(StandardCharsets.UTF_8)
        );

        AjaxResult result = controller.uploadCertification(file);

        assertEquals(500, result.get("code"));
        assertEquals("请先登录", result.get("msg"));
    }

    private byte[] createImageBytes(String format) throws Exception {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x112233);
        image.setRGB(1, 0, 0x445566);
        image.setRGB(0, 1, 0x778899);
        image.setRGB(1, 1, 0xAABBCC);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, format, output);
        return output.toByteArray();
    }

    private String readImageFormat(Path path) throws Exception {
        try (ByteArrayInputStream input = new ByteArrayInputStream(Files.readAllBytes(path))) {
            return ImageIO.getImageReaders(ImageIO.createImageInputStream(input)).next().getFormatName();
        }
    }

    private MockHttpServletRequest buildRequest(String requestUri) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(requestUri);
        request.setContextPath("");
        request.setScheme("http");
        request.setServerName("localhost");
        request.setServerPort(8080);
        return request;
    }
}
