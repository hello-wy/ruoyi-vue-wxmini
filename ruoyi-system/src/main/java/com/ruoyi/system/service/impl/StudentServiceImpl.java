package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.StudentFollowUpRecord;
import com.ruoyi.system.domain.bo.StudentFollowUpRecordBo;
import com.ruoyi.system.domain.bo.StudentQueryBo;
import com.ruoyi.system.domain.vo.StudentDetailVo;
import com.ruoyi.system.domain.vo.StudentFollowUpRecordVo;
import com.ruoyi.system.domain.vo.StudentLearningRecordsVo;
import com.ruoyi.system.domain.vo.StudentListVo;
import com.ruoyi.system.domain.vo.StudentSituationVo;
import com.ruoyi.system.mapper.CoursePayOrderMapper;
import com.ruoyi.system.mapper.SignInRecordMapper;
import com.ruoyi.system.mapper.StudentFollowUpRecordMapper;
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

    @Resource
    private CoursePayOrderMapper coursePayOrderMapper;

    @Resource
    private SignInRecordMapper signInRecordMapper;

    @Resource
    private StudentFollowUpRecordMapper studentFollowUpRecordMapper;

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
    public StudentLearningRecordsVo getStudentLearningRecords(Long id) {
        if (wxUserProfileMapper.selectAdminStudentDetailById(id) == null) {
            return null;
        }
        StudentLearningRecordsVo records = new StudentLearningRecordsVo();
        records.setEnrollmentRecords(coursePayOrderMapper.selectStudentCourseEnrollmentRecords(id));
        records.setSignInRecords(coursePayOrderMapper.selectStudentCourseSignInRecords(id));
        records.addSignInRecords(signInRecordMapper.selectStudentLectureSignInRecords(id));
        return records;
    }

    @Override
    public List<StudentFollowUpRecordVo> listStudentFollowUpRecords(Long id) {
        if (wxUserProfileMapper.selectAdminStudentDetailById(id) == null) {
            return null;
        }
        return studentFollowUpRecordMapper.selectStudentFollowUpRecordsByStudentId(id);
    }

    @Override
    public StudentFollowUpRecordVo addStudentFollowUpRecord(Long id, StudentFollowUpRecordBo bo, Long operatorId, String operatorName) {
        if (wxUserProfileMapper.selectAdminStudentDetailById(id) == null) {
            return null;
        }
        StudentFollowUpRecord record = new StudentFollowUpRecord();
        record.setStudentId(id);
        record.setFollowUpTime(bo.getFollowUpTime() == null ? DateUtils.getNowDate() : bo.getFollowUpTime());
        record.setFollowUpMethod(bo.getFollowUpMethod() == null ? 0 : bo.getFollowUpMethod());
        record.setFollowUpResult(bo.getFollowUpResult() == null ? 0 : bo.getFollowUpResult());
        record.setContent(StringUtils.trimToNull(bo.getContent()));
        record.setFormData(normalizeFormData(bo.getFormData()));
        record.setNextFollowUpTime(bo.getNextFollowUpTime());
        record.setOperatorId(operatorId);
        record.setOperatorName(StringUtils.trimToNull(operatorName));
        record.setRemark(StringUtils.trimToNull(bo.getRemark()));
        record.setIsDeleted(0);
        record.setCreateBy(StringUtils.trimToEmpty(operatorName));
        record.setCreateTime(DateUtils.getNowDate());
        record.setUpdateBy(StringUtils.trimToEmpty(operatorName));
        record.setUpdateTime(DateUtils.getNowDate());
        studentFollowUpRecordMapper.insertStudentFollowUpRecord(record);
        return studentFollowUpRecordMapper.selectStudentFollowUpRecordById(record.getId());
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

    private String normalizeFormData(String formData) {
        if (StringUtils.isBlank(formData)) {
            return null;
        }
        try {
            return JSON.toJSONString(JSON.parse(formData));
        } catch (Exception e) {
            throw new ServiceException("回访表单数据不是合法JSON");
        }
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
