package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.TutorMaterial;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.mapper.TutorMaterialMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TutorMaterialServiceImplTest {
    private static final long TUTOR_ID = 100L;
    private static final String OWNER_UID = "wx-user-1";

    @Mock
    private TutorMaterialMapper tutorMaterialMapper;

    @InjectMocks
    private TutorMaterialServiceImpl service;

    @Test
    void createOwnedMaterialAcceptsMatchingUserAndTypePath() {
        String url = "/profile/certification/wx-user-1/sfz_front/sfz_front-uuid.png";

        TutorMaterial material = service.createOwnedMaterial(OWNER_UID, 1, url);

        assertEquals(OWNER_UID, material.getOwnerUid());
        assertEquals(1, material.getType());
        assertEquals("身份证正面", material.getTypeName());
        assertEquals("sfz_front", material.getDirectoryName());
        assertEquals(url, material.getUrl());
        verify(tutorMaterialMapper).insertTutorMaterial(material);
    }

    @Test
    void createOwnedMaterialRejectsPathForAnotherType() {
        String url = "/profile/certification/wx-user-1/sfz_back/sfz_back-uuid.png";

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.createOwnedMaterial(OWNER_UID, 1, url));

        assertEquals("材料图片地址与类型不匹配", error.getMessage());
        verifyNoInteractions(tutorMaterialMapper);
    }

    @Test
    void createOwnedMaterialRejectsPathForAnotherUser() {
        String url = "/profile/certification/another-user/sfz_front/sfz_front-uuid.png";

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.createOwnedMaterial(OWNER_UID, 1, url));

        assertEquals("材料图片地址与类型不匹配", error.getMessage());
        verifyNoInteractions(tutorMaterialMapper);
    }

    @Test
    void createOwnedMaterialRejectsInvalidType() {
        ServiceException error = assertThrows(ServiceException.class,
                () -> service.createOwnedMaterial(OWNER_UID, 5, "/upload/material.jpg"));

        assertEquals("材料类型非法", error.getMessage());
        verifyNoInteractions(tutorMaterialMapper);
    }

    @Test
    void bindTutorMaterialsRejectsMalformedIdList() {
        ServiceException error = assertThrows(ServiceException.class,
                () -> service.bindTutorMaterials(TUTOR_ID, OWNER_UID, "11,invalid"));

        assertEquals("材料ID列表格式错误", error.getMessage());
        verifyNoInteractions(tutorMaterialMapper);
    }

    @Test
    void bindTutorMaterialsRejectsMaterialOwnedByAnotherUser() {
        List<Long> ids = Arrays.asList(11L);
        when(tutorMaterialMapper.selectTutorMaterialsByIds(ids))
                .thenReturn(Arrays.asList(material(11L, "wx-user-2", 1)));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.bindTutorMaterials(TUTOR_ID, OWNER_UID, "11"));

        assertEquals("无权使用该材料", error.getMessage());
    }

    @Test
    void bindTutorMaterialsRejectsDuplicateSingletonType() {
        List<Long> ids = Arrays.asList(11L, 12L);
        when(tutorMaterialMapper.selectTutorMaterialsByIds(ids)).thenReturn(Arrays.asList(
                material(11L, OWNER_UID, 1),
                material(12L, OWNER_UID, 1)
        ));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.bindTutorMaterials(TUTOR_ID, OWNER_UID, "11,12"));

        assertEquals("身份证或学生证材料只能各上传一张", error.getMessage());
    }

    @Test
    void attachMaterialsPreservesCertificateIdOrder() {
        Tutors tutors = new Tutors();
        tutors.setCertificates("12,11");
        List<Long> ids = Arrays.asList(12L, 11L);
        when(tutorMaterialMapper.selectTutorMaterialsByIds(ids)).thenReturn(Arrays.asList(
                material(11L, OWNER_UID, 4),
                material(12L, OWNER_UID, 1)
        ));

        service.attachMaterials(tutors);

        assertEquals(Arrays.asList(12L, 11L), Arrays.asList(
                tutors.getMaterials().get(0).getId(),
                tutors.getMaterials().get(1).getId()
        ));
    }

    private TutorMaterial material(Long id, String ownerUid, Integer type) {
        TutorMaterial material = new TutorMaterial();
        material.setId(id);
        material.setOwnerUid(ownerUid);
        material.setType(type);
        return material;
    }
}
