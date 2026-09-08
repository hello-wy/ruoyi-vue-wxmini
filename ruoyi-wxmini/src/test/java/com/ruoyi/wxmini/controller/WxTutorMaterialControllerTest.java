package com.ruoyi.wxmini.controller;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.system.enums.TutorMaterialType;
import com.ruoyi.system.service.ITutorMaterialService;
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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class WxTutorMaterialControllerTest {
    private static final int MAX_MATERIAL_FILE_SIZE = 3 * 1024 * 1024;

    @TempDir
    Path tempDir;

    private WxTutorMaterialController controller;

    @BeforeEach
    void setUp() {
        FileUploadUtils.setDefaultBaseDir(tempDir.toString());
        RuoYiConfig config = new RuoYiConfig();
        config.setProfile(tempDir.toString());
        controller = new WxTutorMaterialController();
        ReflectionTestUtils.setField(controller, "serverConfig", new ServerConfig());
        ReflectionTestUtils.setField(controller, "tutorMaterialService", mock(ITutorMaterialService.class));
        WxMiniUserContext.setCurrentUserId("321");
        setRequestContext("/wxmini/tutoring/materials/upload/id-card-front");
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
        WxMiniUserContext.clear();
    }

    @Test
    void dedicatedEndpointsShouldStoreFilesInTypeDirectories() throws Exception {
        List<UploadCase> cases = Arrays.asList(
                new UploadCase(TutorMaterialType.ID_CARD_FRONT, controller::uploadIdCardFront),
                new UploadCase(TutorMaterialType.ID_CARD_BACK, controller::uploadIdCardBack),
                new UploadCase(TutorMaterialType.STUDENT_CARD, controller::uploadStudentCard),
                new UploadCase(TutorMaterialType.CERTIFICATE, controller::uploadCertificate)
        );

        for (UploadCase uploadCase : cases) {
            assertUploadResult(uploadCase);
        }
    }

    @Test
    void uploadShouldRejectImageLargerThanThreeMegabytes() {
        byte[] oversizedContent = new byte[MAX_MATERIAL_FILE_SIZE + 1];
        MockMultipartFile file = imageFile("oversized.png", oversizedContent);

        AjaxResult result = controller.uploadCertificate(file);

        assertEquals(500, result.get("code"));
        assertEquals("图片大小不能超过 3MB", result.get("msg"));
    }

    @Test
    void uploadShouldAllowImageExactlyThreeMegabytes() {
        byte[] allowedContent = new byte[MAX_MATERIAL_FILE_SIZE];
        MockMultipartFile file = imageFile("allowed.png", allowedContent);

        AjaxResult result = controller.uploadCertificate(file);

        assertEquals(200, result.get("code"));
    }

    @Test
    void uploadShouldRejectNonImageExtension() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "certificate.pdf",
                "application/pdf",
                "fake-pdf".getBytes(StandardCharsets.UTF_8)
        );

        AjaxResult result = controller.uploadCertificate(file);

        assertEquals(500, result.get("code"));
        assertEquals("仅支持上传 JPG、JPEG、PNG 格式图片", result.get("msg"));
    }

    @Test
    void uploadShouldRequireLoggedInWxUser() {
        WxMiniUserContext.clear();

        AjaxResult result = controller.uploadCertificate(imageFile("certificate.png", new byte[]{1}));

        assertEquals(500, result.get("code"));
        assertEquals("请先登录", result.get("msg"));
    }

    private void assertUploadResult(UploadCase uploadCase) throws Exception {
        TutorMaterialType materialType = uploadCase.materialType;
        String directoryName = materialType.getDirectoryName();
        byte[] content = directoryName.getBytes(StandardCharsets.UTF_8);
        AjaxResult result = uploadCase.endpoint.upload(imageFile(directoryName + ".png", content));
        String fileName = String.valueOf(result.get("newFileName"));
        String resourcePath = "/profile/certification/321/" + directoryName + "/" + fileName;
        Path savedFile = tempDir.resolve("certification/321")
                .resolve(directoryName)
                .resolve(fileName);

        assertEquals(200, result.get("code"));
        assertEquals(materialType.getCode(), result.get("type"));
        assertEquals(materialType.getDisplayName(), result.get("typeName"));
        assertEquals(directoryName, result.get("directoryName"));
        assertTrue(fileName.startsWith(directoryName + "-"));
        assertTrue(fileName.endsWith(".png"));
        assertEquals(resourcePath, result.get("fileName"));
        assertEquals("http://localhost:8080" + resourcePath, result.get("url"));
        assertEquals(directoryName + ".png", result.get("originalFilename"));
        assertTrue(Files.exists(savedFile));
        assertFalse(Files.isDirectory(savedFile));
    }

    private MockMultipartFile imageFile(String originalFilename, byte[] content) {
        return new MockMultipartFile("file", originalFilename, "image/png", content);
    }

    private void setRequestContext(String requestUri) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(requestUri);
        request.setScheme("http");
        request.setServerName("localhost");
        request.setServerPort(8080);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @FunctionalInterface
    private interface UploadEndpoint {
        AjaxResult upload(MockMultipartFile file);
    }

    private static final class UploadCase {
        private final TutorMaterialType materialType;
        private final UploadEndpoint endpoint;

        private UploadCase(TutorMaterialType materialType, UploadEndpoint endpoint) {
            this.materialType = materialType;
            this.endpoint = endpoint;
        }
    }
}
