package com.ruoyi.system.service;

import com.ruoyi.system.domain.TutorMaterial;
import com.ruoyi.system.domain.Tutors;

public interface ITutorMaterialService {
    TutorMaterial createOwnedMaterial(String ownerUid, Integer type, String url);

    String bindTutorMaterials(Long tutorId, String ownerUid, String certificates);

    void attachMaterials(Tutors tutors);
}
