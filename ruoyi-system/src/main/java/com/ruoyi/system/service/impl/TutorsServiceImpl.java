package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.mapper.TutorMaterialMapper;
import com.ruoyi.system.mapper.TutorsMapper;
import com.ruoyi.system.service.ITutorMaterialService;
import com.ruoyi.system.service.ITutorsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class TutorsServiceImpl implements ITutorsService {
    @Resource
    private TutorsMapper tutorsMapper;

    @Resource
    private TutorMaterialMapper tutorMaterialMapper;

    @Resource
    private ITutorMaterialService tutorMaterialService;

    @Override
    public Tutors selectTutorsById(Long id) {
        Tutors tutors = tutorsMapper.selectTutorsById(id);
        tutorMaterialService.attachMaterials(tutors);
        return tutors;
    }

    @Override
    public List<Tutors> selectTutorsList(Tutors tutors) {
        return tutorsMapper.selectTutorsList(tutors);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTutors(Tutors tutors) {
        int rows = tutorsMapper.insertTutors(tutors);
        if (rows > 0 && tutors.getCertificates() != null) {
            synchronizeMaterials(tutors, tutors.getUid());
            persistMaterialIds(tutors);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTutors(Tutors tutors) {
        if (tutors.getCertificates() != null) {
            Tutors existing = requireExistingTutor(tutors.getId());
            String ownerUid = StringUtils.isNotBlank(tutors.getUid())
                    ? tutors.getUid()
                    : existing.getUid();
            synchronizeMaterials(tutors, ownerUid);
        }
        return tutorsMapper.updateTutors(tutors);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTutorsByIds(Long[] ids) {
        tutorMaterialMapper.deleteTutorMaterialsByTutorIds(Arrays.asList(ids));
        return tutorsMapper.deleteTutorsByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTutorsById(Long id) {
        tutorMaterialMapper.deleteTutorMaterialsByTutorId(id);
        return tutorsMapper.deleteTutorsById(id);
    }

    @Override
    public List<Tutors> selectCertifiedTutorsList(Tutors tutors) {
        List<Tutors> result = tutorsMapper.selectCertifiedTutorsList(tutors);
        result.forEach(this::hideAuditMaterials);
        return result;
    }

    @Override
    public Tutors selectTutorsByUid(String uid) {
        Tutors tutors = tutorsMapper.selectTutorsByUid(uid);
        tutorMaterialService.attachMaterials(tutors);
        return tutors;
    }

    private void synchronizeMaterials(Tutors tutors, String ownerUid) {
        String normalizedIds = tutorMaterialService.bindTutorMaterials(
                tutors.getId(),
                ownerUid,
                tutors.getCertificates()
        );
        tutors.setCertificates(normalizedIds);
    }

    private void persistMaterialIds(Tutors tutors) {
        Tutors materialUpdate = new Tutors();
        materialUpdate.setId(tutors.getId());
        materialUpdate.setCertificates(tutors.getCertificates());
        tutorsMapper.updateTutors(materialUpdate);
    }

    private Tutors requireExistingTutor(Long tutorId) {
        if (tutorId == null) {
            throw new ServiceException("教员ID不能为空");
        }
        Tutors existing = tutorsMapper.selectTutorsById(tutorId);
        if (existing == null) {
            throw new ServiceException("教员不存在");
        }
        return existing;
    }

    private void hideAuditMaterials(Tutors tutors) {
        tutors.setCertificates(null);
        tutors.setMaterials(null);
    }
}
