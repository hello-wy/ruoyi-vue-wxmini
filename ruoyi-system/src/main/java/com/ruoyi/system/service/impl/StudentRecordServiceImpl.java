package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.domain.StudentFollowUpRecord;
import com.ruoyi.system.domain.bo.StudentFollowUpRecordBo;
import com.ruoyi.system.domain.vo.EnrollmentWithLectureVo;
import com.ruoyi.system.domain.vo.StudentEnrollmentGroupVo;
import com.ruoyi.system.domain.vo.StudentEnrollmentSummaryVo;
import com.ruoyi.system.domain.vo.StudentFollowUpRecordVo;
import com.ruoyi.system.domain.vo.StudentLearningRecordsVo;
import com.ruoyi.system.domain.vo.StudentSalonPurchaseRecordVo;
import com.ruoyi.system.domain.vo.StudentSituationVo;
import com.ruoyi.system.mapper.CoursePayOrderMapper;
import com.ruoyi.system.mapper.SalonPayOrderMapper;
import com.ruoyi.system.mapper.SignInRecordMapper;
import com.ruoyi.system.mapper.StudentFollowUpRecordMapper;
import com.ruoyi.system.service.ILecturesService;
import com.ruoyi.system.service.IStudentAccessService;
import com.ruoyi.system.service.IStudentEnrollmentService;
import com.ruoyi.system.service.IStudentRecordService;
import com.ruoyi.wxmini.mapper.WxUserProfileMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentRecordServiceImpl implements IStudentRecordService {

    @Resource
    private WxUserProfileMapper wxUserProfileMapper;

    @Resource
    private IStudentAccessService studentAccessService;

    @Resource
    private IStudentEnrollmentService studentEnrollmentService;

    @Resource
    private ILecturesService lecturesService;

    @Resource
    private CoursePayOrderMapper coursePayOrderMapper;

    @Resource
    private SignInRecordMapper signInRecordMapper;

    @Resource
    private SalonPayOrderMapper salonPayOrderMapper;

    @Resource
    private StudentFollowUpRecordMapper studentFollowUpRecordMapper;

    @Override
    public StudentEnrollmentSummaryVo getStudentEnrollments(Long id) {
        if (!hasStudentAccess(id)) {
            return null;
        }
        List<EnrollmentWithLectureVo> enrollments = studentEnrollmentService.selectAdminEnrollmentWithLectureByUid(id);
        StudentEnrollmentSummaryVo summary = new StudentEnrollmentSummaryVo();
        Map<String, StudentEnrollmentGroupVo> groups = buildLectureGroups();
        for (EnrollmentWithLectureVo enrollment : enrollments) {
            addEnrollment(groups, enrollment, summary);
        }
        summary.setGroups(new ArrayList<>(groups.values()));
        return summary;
    }

    @Override
    public StudentLearningRecordsVo getStudentLearningRecords(Long id) {
        if (!hasStudentAccess(id)) {
            return null;
        }
        StudentLearningRecordsVo records = new StudentLearningRecordsVo();
        records.setEnrollmentRecords(coursePayOrderMapper.selectStudentCourseEnrollmentRecords(id));
        records.setSignInRecords(coursePayOrderMapper.selectStudentCourseSignInRecords(id));
        records.addSignInRecords(signInRecordMapper.selectStudentLectureSignInRecords(id));
        return records;
    }

    @Override
    public List<StudentSalonPurchaseRecordVo> listStudentSalonPurchaseRecords(Long id) {
        if (!hasStudentAccess(id)) {
            return null;
        }
        return salonPayOrderMapper.selectStudentSalonPurchaseRecords(id);
    }

    @Override
    public List<StudentFollowUpRecordVo> listStudentFollowUpRecords(Long id) {
        if (!hasStudentAccess(id)) {
            return null;
        }
        return studentFollowUpRecordMapper.selectStudentFollowUpRecordsByStudentId(id);
    }

    @Override
    public StudentFollowUpRecordVo addStudentFollowUpRecord(Long id, StudentFollowUpRecordBo bo, Long operatorId, String operatorName) {
        if (!hasStudentAccess(id)) {
            return null;
        }
        StudentFollowUpRecord record = buildFollowUpRecord(id, bo, operatorId, operatorName);
        studentFollowUpRecordMapper.insertStudentFollowUpRecord(record);
        return studentFollowUpRecordMapper.selectStudentFollowUpRecordById(record.getId());
    }

    @Override
    public StudentSituationVo updateStudentSituation(Long id, String studentSituation) {
        if (!hasStudentAccess(id)) {
            return null;
        }
        String normalizedSituation = StringUtils.trimToEmpty(studentSituation);
        wxUserProfileMapper.updateAdminStudentSituation(id, normalizedSituation);
        StudentSituationVo result = new StudentSituationVo();
        result.setId(id);
        result.setStudentSituation(normalizedSituation);
        return result;
    }

    private boolean hasStudentAccess(Long id) {
        studentAccessService.checkStudentAccess(id);
        return wxUserProfileMapper.selectAdminStudentDetailById(id) != null;
    }

    private void addEnrollment(Map<String, StudentEnrollmentGroupVo> groups, EnrollmentWithLectureVo enrollment, StudentEnrollmentSummaryVo summary) {
        int total = safeInt(enrollment.getTotal());
        int remain = safeInt(enrollment.getRemain());
        int usedCount = safeInt(enrollment.getUsedCount());
        int sharedCount = safeInt(enrollment.getSharedCount());
        summary.setTotal(safeInt(summary.getTotal()) + total);
        summary.setRemain(safeInt(summary.getRemain()) + remain);
        summary.setUsedCount(safeInt(summary.getUsedCount()) + usedCount);
        summary.setSharedCount(safeInt(summary.getSharedCount()) + sharedCount);
        StudentEnrollmentGroupVo group = groups.computeIfAbsent(resolveLectureName(enrollment), this::createGroup);
        group.setTotal(safeInt(group.getTotal()) + total);
        group.setRemain(safeInt(group.getRemain()) + remain);
        group.setUsedCount(safeInt(group.getUsedCount()) + usedCount);
        group.setSharedCount(safeInt(group.getSharedCount()) + sharedCount);
        removeZeroPlaceholder(group);
        group.getItems().add(enrollment);
    }

    private StudentFollowUpRecord buildFollowUpRecord(Long id, StudentFollowUpRecordBo bo, Long operatorId, String operatorName) {
        StudentFollowUpRecord record = new StudentFollowUpRecord();
        record.setStudentId(id);
        record.setFollowUpTime(bo.getFollowUpTime());
        record.setFollowUpMethod(bo.getFollowUpMethod() == null ? 0 : bo.getFollowUpMethod());
        record.setFollowUpResult(bo.getFollowUpResult() == null ? 0 : bo.getFollowUpResult());
        record.setTitle(StringUtils.trimToNull(bo.getTitle()));
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
        return record;
    }

    private Map<String, StudentEnrollmentGroupVo> buildLectureGroups() {
        Map<String, StudentEnrollmentGroupVo> groups = new LinkedHashMap<>();
        List<Lectures> lectures = lecturesService.selectLecturesTemplateList();
        if (lectures == null || lectures.isEmpty()) {
            lectures = lecturesService.selectLecturesList(new Lectures());
        }
        for (Lectures lecture : lectures) {
            if (lecture != null) {
                groups.putIfAbsent(resolveLectureName(lecture), createGroup(lecture));
            }
        }
        return groups;
    }

    private StudentEnrollmentGroupVo createGroup(String lectureName) {
        StudentEnrollmentGroupVo group = new StudentEnrollmentGroupVo();
        group.setLectureName(lectureName);
        group.setTotal(0);
        group.setRemain(0);
        group.setUsedCount(0);
        group.setSharedCount(0);
        return group;
    }

    private StudentEnrollmentGroupVo createGroup(Lectures lecture) {
        StudentEnrollmentGroupVo group = createGroup(resolveLectureName(lecture));
        group.getItems().add(buildZeroEnrollmentItem(lecture));
        return group;
    }

    private EnrollmentWithLectureVo buildZeroEnrollmentItem(Lectures lecture) {
        EnrollmentWithLectureVo item = new EnrollmentWithLectureVo();
        item.setLectureId(lecture.getId());
        item.setLectureName(resolveLectureName(lecture));
        item.setLectureTime(lecture.getTime());
        item.setEndDate(lecture.getEndDate());
        item.setLocation(lecture.getLocation());
        item.setTotal(0);
        item.setRemain(0);
        item.setUsedCount(0);
        item.setSharedCount(0);
        item.setAvailableShareCount(0);
        return item;
    }

    private void removeZeroPlaceholder(StudentEnrollmentGroupVo group) {
        if (group.getItems().size() == 1 && group.getItems().get(0).getId() == null) {
            group.getItems().clear();
        }
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

    private String resolveLectureName(EnrollmentWithLectureVo enrollment) {
        return StringUtils.defaultIfBlank(enrollment.getLectureName(), "未命名课程");
    }

    private String resolveLectureName(Lectures lecture) {
        return StringUtils.defaultIfBlank(lecture.getName(), "未命名课程");
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }
}
