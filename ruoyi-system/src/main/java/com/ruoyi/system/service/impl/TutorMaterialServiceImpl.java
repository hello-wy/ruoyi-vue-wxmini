package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.TutorMaterial;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.mapper.TutorMaterialMapper;
import com.ruoyi.system.service.ITutorMaterialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TutorMaterialServiceImpl implements ITutorMaterialService {
    private static final int ID_CARD_FRONT = 1;
    private static final int CERTIFICATE = 4;
    private static final int MAX_URL_LENGTH = 512;

    @Resource
    private TutorMaterialMapper tutorMaterialMapper;

    @Override
    public TutorMaterial createOwnedMaterial(String ownerUid, Integer type, String url) {
        validateCreateRequest(ownerUid, type, url);
        TutorMaterial material = new TutorMaterial();
        material.setId(SnowflakeIdWorker.nextIdDefault());
        material.setOwnerUid(ownerUid);
        material.setType(type);
        material.setUrl(url.trim());
        material.setSortOrder(0);
        tutorMaterialMapper.insertTutorMaterial(material);
        return material;
    }

    @Override
    public String bindTutorMaterials(Long tutorId, String ownerUid, String certificates) {
        validateBindingIdentity(tutorId, ownerUid);
        List<Long> ids = parseMaterialIds(certificates);
        if (ids.isEmpty()) {
            tutorMaterialMapper.deleteTutorMaterialsByTutorId(tutorId);
            return "";
        }
        List<TutorMaterial> materials = tutorMaterialMapper.selectTutorMaterialsByIds(ids);
        if (materials.size() != ids.size()) {
            throw new ServiceException("存在无效的材料ID");
        }
        validateOwnedMaterials(tutorId, ownerUid, materials);
        tutorMaterialMapper.deleteTutorMaterialsByTutorIdExcludingIds(tutorId, ids);
        tutorMaterialMapper.bindTutorMaterials(tutorId, ids);
        return joinIds(ids);
    }

    @Override
    public void attachMaterials(Tutors tutors) {
        if (tutors == null) {
            return;
        }
        List<Long> ids = parseMaterialIds(tutors.getCertificates());
        if (ids.isEmpty()) {
            tutors.setMaterials(new ArrayList<>());
            return;
        }
        List<TutorMaterial> materials = tutorMaterialMapper.selectTutorMaterialsByIds(ids);
        tutors.setMaterials(orderMaterials(ids, materials));
    }

    private void validateCreateRequest(String ownerUid, Integer type, String url) {
        if (StringUtils.isBlank(ownerUid)) {
            throw new ServiceException("请先登录");
        }
        if (type == null || type < ID_CARD_FRONT || type > CERTIFICATE) {
            throw new ServiceException("材料类型非法");
        }
        String normalizedUrl = StringUtils.trim(url);
        if (StringUtils.isBlank(normalizedUrl) || normalizedUrl.length() > MAX_URL_LENGTH) {
            throw new ServiceException("材料图片地址非法");
        }
        if (!normalizedUrl.startsWith("/") && !normalizedUrl.startsWith("http://")
                && !normalizedUrl.startsWith("https://")) {
            throw new ServiceException("材料图片地址非法");
        }
    }

    private void validateBindingIdentity(Long tutorId, String ownerUid) {
        if (tutorId == null) {
            throw new ServiceException("教员ID不能为空");
        }
        if (StringUtils.isBlank(ownerUid)) {
            throw new ServiceException("材料所属用户不能为空");
        }
    }

    private List<Long> parseMaterialIds(String certificates) {
        if (StringUtils.isBlank(certificates)) {
            return new ArrayList<>();
        }
        Map<Long, Long> uniqueIds = new LinkedHashMap<>();
        for (String value : certificates.split(",")) {
            Long id = parseMaterialId(value);
            uniqueIds.put(id, id);
        }
        return new ArrayList<>(uniqueIds.values());
    }

    private Long parseMaterialId(String value) {
        try {
            Long id = Long.valueOf(value.trim());
            if (id <= 0) {
                throw new NumberFormatException("material id must be positive");
            }
            return id;
        } catch (NumberFormatException ex) {
            throw new ServiceException("材料ID列表格式错误");
        }
    }

    private void validateOwnedMaterials(Long tutorId, String ownerUid, List<TutorMaterial> materials) {
        Map<Integer, Integer> typeCounts = new LinkedHashMap<>();
        for (TutorMaterial material : materials) {
            boolean wrongOwner = !ownerUid.equals(material.getOwnerUid());
            boolean boundElsewhere = material.getTutorId() != null && !tutorId.equals(material.getTutorId());
            if (wrongOwner || boundElsewhere) {
                throw new ServiceException("无权使用该材料");
            }
            typeCounts.merge(material.getType(), 1, Integer::sum);
        }
        validateSingletonTypes(typeCounts);
    }

    private void validateSingletonTypes(Map<Integer, Integer> typeCounts) {
        for (int type = ID_CARD_FRONT; type < CERTIFICATE; type++) {
            if (typeCounts.getOrDefault(type, 0) > 1) {
                throw new ServiceException("身份证或学生证材料只能各上传一张");
            }
        }
    }

    private List<TutorMaterial> orderMaterials(List<Long> ids, List<TutorMaterial> materials) {
        Map<Long, TutorMaterial> materialMap = materials.stream()
                .collect(Collectors.toMap(TutorMaterial::getId, item -> item));
        return ids.stream()
                .map(materialMap::get)
                .filter(item -> item != null)
                .collect(Collectors.toList());
    }

    private String joinIds(List<Long> ids) {
        return ids.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
