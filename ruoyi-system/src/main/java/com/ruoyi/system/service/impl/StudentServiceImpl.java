package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.bo.StudentQueryBo;
import com.ruoyi.system.domain.vo.StudentDetailVo;
import com.ruoyi.system.domain.vo.StudentListVo;
import com.ruoyi.system.domain.vo.StudentSituationVo;
import com.ruoyi.system.service.IStudentService;
import com.ruoyi.wxmini.mapper.WxUserProfileMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentServiceImpl implements IStudentService {

    private static final Integer USER_TYPE_PARENT = 0;
    private static final Integer USER_TYPE_STUDENT = 1;
    private static final Integer USER_TYPE_MERCHANT = 2;
    private static final Integer USER_TYPE_AUNT = 3;

    @Resource
    private WxUserProfileMapper wxUserProfileMapper;

    @Override
    public List<StudentListVo> listStudents(StudentQueryBo queryBo) {
        List<StudentListVo> list = wxUserProfileMapper.selectAdminStudentList(queryBo);
        for (StudentListVo item : list) {
            item.setUserTypeLabel(resolveUserTypeLabel(item.getUserType()));
        }
        return list;
    }

    @Override
    public StudentDetailVo getStudentDetail(Long id) {
        StudentDetailVo detail = wxUserProfileMapper.selectAdminStudentDetailById(id);
        if (detail == null) {
            return null;
        }
        if (StringUtils.isBlank(detail.getDisplayName())) {
            detail.setDisplayName(detail.getUserName());
        }
        detail.setUserTypeLabel(resolveUserTypeLabel(detail.getUserType()));
        return detail;
    }

    @Override
    public StudentSituationVo updateStudentSituation(Long id, String studentSituation) {
        StudentDetailVo detail = wxUserProfileMapper.selectAdminStudentDetailById(id);
        if (detail == null) {
            return null;
        }
        String normalizedSituation = StringUtils.trimToEmpty(studentSituation);
        wxUserProfileMapper.updateAdminStudentSituation(id, normalizedSituation);
        StudentSituationVo result = new StudentSituationVo();
        result.setId(id);
        result.setStudentSituation(normalizedSituation);
        return result;
    }

    private String resolveUserTypeLabel(Integer userType) {
        if (USER_TYPE_PARENT.equals(userType)) {
            return "家长";
        }
        if (USER_TYPE_STUDENT.equals(userType)) {
            return "学生";
        }
        if (USER_TYPE_MERCHANT.equals(userType)) {
            return "商家";
        }
        if (USER_TYPE_AUNT.equals(userType)) {
            return "阿姨";
        }
        return "未知";
    }
}
