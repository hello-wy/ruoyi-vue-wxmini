package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.domain.StudentFollowUpRecord;
import com.ruoyi.system.domain.StudentStaffAssignment;
import com.ruoyi.system.domain.bo.StudentAccessScope;
import com.ruoyi.system.domain.bo.StudentBindingBo;
import com.ruoyi.system.domain.bo.StudentFollowUpRecordBo;
import com.ruoyi.system.domain.bo.StudentQueryBo;
import com.ruoyi.system.domain.bo.StudentStaffCandidateQueryBo;
import com.ruoyi.system.domain.vo.EnrollmentWithLectureVo;
import com.ruoyi.system.domain.vo.StudentDetailVo;
import com.ruoyi.system.domain.vo.StudentEnrollmentGroupVo;
import com.ruoyi.system.domain.vo.StudentEnrollmentSummaryVo;
import com.ruoyi.system.domain.vo.StudentFollowUpRecordVo;
import com.ruoyi.system.domain.vo.StudentLearningRecordsVo;
import com.ruoyi.system.domain.vo.StudentListVo;
import com.ruoyi.system.domain.vo.StudentSituationVo;
import com.ruoyi.system.domain.vo.StudentStaffAssignmentVo;
import com.ruoyi.system.domain.vo.StudentStaffCandidateVo;
import com.ruoyi.system.mapper.CoursePayOrderMapper;
import com.ruoyi.system.mapper.SignInRecordMapper;
import com.ruoyi.system.mapper.StudentFollowUpRecordMapper;
import com.ruoyi.system.mapper.StudentStaffAssignmentMapper;
import com.ruoyi.system.service.ILecturesService;
import com.ruoyi.system.service.IStudentAccessService;
import com.ruoyi.system.service.IStudentEnrollmentService;
import com.ruoyi.system.service.IStudentService;
import com.ruoyi.wxmini.mapper.WxUserProfileMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements IStudentService {

    private static final Integer USER_TYPE_PARENT = 0;
    private static final Integer USER_TYPE_STUDENT = 1;
    private static final Integer USER_TYPE_MERCHANT = 2;
    private static final Integer USER_TYPE_AUNT = 3;
    private static final String ASSIGNMENT_STATUS_BOUND = "bound";
    private static final String ASSIGNMENT_STATUS_UNBOUND = "unbound";

    @Resource
    private WxUserProfileMapper wxUserProfileMapper;

    @Resource
    private CoursePayOrderMapper coursePayOrderMapper;

    @Resource
    private SignInRecordMapper signInRecordMapper;

    @Resource
    private StudentFollowUpRecordMapper studentFollowUpRecordMapper;

    @Resource
    private StudentStaffAssignmentMapper studentStaffAssignmentMapper;

    @Resource
    private IStudentAccessService studentAccessService;

    @Resource
    private IStudentEnrollmentService studentEnrollmentService;

    @Resource
    private ILecturesService lecturesService;

    @Override
    public List<StudentListVo> listStudents(StudentQueryBo queryBo) {
        StudentQueryBo effectiveQuery = queryBo == null ? new StudentQueryBo() : queryBo;
        effectiveQuery.setAssignmentStatus(normalizeAssignmentStatus(effectiveQuery.getAssignmentStatus()));
        StudentAccessScope scope = studentAccessService.resolveCurrentScope();
        effectiveQuery.setAccessScope(scope);
        List<StudentListVo> list = wxUserProfileMapper.selectAdminStudentList(effectiveQuery);
        for (StudentListVo item : list) {
            item.setUserTypeLabel(resolveUserTypeLabel(item.getUserType()));
            item.setBound(item.getBoundUserId() != null);
            StudentStaffAssignment assignment = toAssignment(item);
            item.setCanBind(studentAccessService.canBind(assignment, scope));
            item.setCanClaim(studentAccessService.canClaim(assignment, scope));
        }
        return list;
    }

    @Override
    public StudentDetailVo getStudentDetail(Long id) {
        studentAccessService.checkStudentAccess(id);
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
    public StudentEnrollmentSummaryVo getStudentEnrollments(Long id) {
        studentAccessService.checkStudentAccess(id);
        StudentDetailVo detail = wxUserProfileMapper.selectAdminStudentDetailById(id);
        if (detail == null) {
            return null;
        }
        List<EnrollmentWithLectureVo> enrollments = studentEnrollmentService.selectAdminEnrollmentWithLectureByUid(id);
        StudentEnrollmentSummaryVo summary = new StudentEnrollmentSummaryVo();
        Map<String, StudentEnrollmentGroupVo> groupMap = buildLectureGroups();
        int total = 0;
        int remain = 0;
        int usedCount = 0;
        int sharedCount = 0;
        for (EnrollmentWithLectureVo enrollment : enrollments) {
            int itemTotal = safeInt(enrollment.getTotal());
            int itemRemain = safeInt(enrollment.getRemain());
            int itemUsed = safeInt(enrollment.getUsedCount());
            int itemShared = safeInt(enrollment.getSharedCount());
            total += itemTotal;
            remain += itemRemain;
            usedCount += itemUsed;
            sharedCount += itemShared;

            String lectureName = StringUtils.defaultIfBlank(enrollment.getLectureName(), "未命名课程");
            StudentEnrollmentGroupVo group = groupMap.get(lectureName);
            if (group == null) {
                group = new StudentEnrollmentGroupVo();
                group.setLectureName(lectureName);
                group.setTotal(0);
                group.setRemain(0);
                group.setUsedCount(0);
                group.setSharedCount(0);
                groupMap.put(lectureName, group);
            }
            group.setTotal(safeInt(group.getTotal()) + itemTotal);
            group.setRemain(safeInt(group.getRemain()) + itemRemain);
            group.setUsedCount(safeInt(group.getUsedCount()) + itemUsed);
            group.setSharedCount(safeInt(group.getSharedCount()) + itemShared);
            removeZeroPlaceholder(group);
            group.getItems().add(enrollment);
        }
        summary.setTotal(total);
        summary.setRemain(remain);
        summary.setUsedCount(usedCount);
        summary.setSharedCount(sharedCount);
        summary.setGroups(new ArrayList<>(groupMap.values()));
        return summary;
    }

    @Override
    public StudentLearningRecordsVo getStudentLearningRecords(Long id) {
        studentAccessService.checkStudentAccess(id);
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
        studentAccessService.checkStudentAccess(id);
        if (wxUserProfileMapper.selectAdminStudentDetailById(id) == null) {
            return null;
        }
        return studentFollowUpRecordMapper.selectStudentFollowUpRecordsByStudentId(id);
    }

    @Override
    public StudentFollowUpRecordVo addStudentFollowUpRecord(Long id, StudentFollowUpRecordBo bo, Long operatorId, String operatorName) {
        studentAccessService.checkStudentAccess(id);
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
        studentAccessService.checkStudentAccess(id);
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

    @Override
    public List<StudentStaffCandidateVo> listStaffCandidates(StudentStaffCandidateQueryBo queryBo) {
        StudentStaffCandidateQueryBo effectiveQuery = queryBo == null ? new StudentStaffCandidateQueryBo() : queryBo;
        effectiveQuery.setAdminLevel(StudentAccessServiceImpl.ADMIN_LEVEL_EMPLOYEE);
        effectiveQuery.setAccessScope(studentAccessService.resolveCurrentScope());
        return studentStaffAssignmentMapper.selectStaffCandidates(effectiveQuery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentStaffAssignmentVo bindStudentStaff(Long id, StudentBindingBo bo, Long operatorId, String operatorName) {
        if (wxUserProfileMapper.countAdminStudentById(id) <= 0) {
            return null;
        }
        StudentAccessScope scope = studentAccessService.resolveCurrentScope();
        StudentStaffAssignment current = studentStaffAssignmentMapper.selectByStudentIdForUpdate(id);
        if (current == null) {
            if (!scope.isFullAccess()) {
                throw new ServiceException("学员未设置归属部门，无法绑定员工");
            }
            current = new StudentStaffAssignment();
            current.setStudentId(id);
        }
        if (!studentAccessService.canBind(current, scope)) {
            throw new ServiceException("无权限绑定该学员", HttpStatus.FORBIDDEN);
        }

        Long targetUserId = bo == null ? null : bo.getSysUserId();
        if (scope.isEmployeeAccess()) {
            targetUserId = scope.getCurrentUserId();
            if (current.getEmployeeUserId() != null && !current.getEmployeeUserId().equals(scope.getCurrentUserId())) {
                throw new ServiceException("当前学员已分配，不能自认领", HttpStatus.FORBIDDEN);
            }
        }
        SysUser target = studentAccessService.checkBindableTarget(targetUserId, scope);

        StudentStaffAssignment assignment = new StudentStaffAssignment();
        assignment.setId(current.getId());
        assignment.setStudentId(id);
        assignment.setOwnerDeptId(target.getDeptId());
        assignment.setEmployeeUserId(target.getUserId());
        assignment.setBindBy(operatorId);
        assignment.setBindByName(StringUtils.trimToNull(operatorName));
        assignment.setBindTime(DateUtils.getNowDate());
        assignment.setCreateBy(StringUtils.trimToEmpty(operatorName));
        assignment.setCreateTime(DateUtils.getNowDate());
        assignment.setUpdateBy(StringUtils.trimToEmpty(operatorName));
        assignment.setUpdateTime(DateUtils.getNowDate());
        studentStaffAssignmentMapper.upsertAssignment(assignment);
        return decorateAssignment(studentStaffAssignmentMapper.selectAssignmentVoByStudentId(id), scope);
    }

    private StudentStaffAssignmentVo decorateAssignment(StudentStaffAssignmentVo assignment, StudentAccessScope scope) {
        if (assignment == null) {
            return null;
        }
        assignment.setBound(assignment.getBoundUserId() != null);
        StudentStaffAssignment entity = new StudentStaffAssignment();
        entity.setId(assignment.getAssignmentId());
        entity.setStudentId(assignment.getStudentId());
        entity.setOwnerDeptId(assignment.getOwnerDeptId());
        entity.setEmployeeUserId(assignment.getBoundUserId());
        assignment.setCanBind(studentAccessService.canBind(entity, scope));
        assignment.setCanClaim(studentAccessService.canClaim(entity, scope));
        return assignment;
    }

    private StudentStaffAssignment toAssignment(StudentListVo item) {
        StudentStaffAssignment assignment = new StudentStaffAssignment();
        assignment.setId(item.getAssignmentId());
        assignment.setStudentId(item.getId());
        assignment.setOwnerDeptId(item.getOwnerDeptId());
        assignment.setEmployeeUserId(item.getBoundUserId());
        return assignment;
    }

    private String normalizeAssignmentStatus(String assignmentStatus) {
        if (ASSIGNMENT_STATUS_BOUND.equals(assignmentStatus) || ASSIGNMENT_STATUS_UNBOUND.equals(assignmentStatus)) {
            return assignmentStatus;
        }
        return null;
    }

    private Map<String, StudentEnrollmentGroupVo> buildLectureGroups() {
        Map<String, StudentEnrollmentGroupVo> groupMap = new LinkedHashMap<>();
        Lectures query = new Lectures();
        List<Lectures> lectures = lecturesService.selectLecturesTemplateList();
        if (lectures == null || lectures.isEmpty()) {
            lectures = lecturesService.selectLecturesList(query);
        }
        for (Lectures lecture : lectures) {
            if (lecture == null) {
                continue;
            }
            String lectureName = StringUtils.defaultIfBlank(lecture.getName(), "未命名课程");
            if (groupMap.containsKey(lectureName)) {
                continue;
            }
            StudentEnrollmentGroupVo group = new StudentEnrollmentGroupVo();
            group.setLectureName(lectureName);
            group.setTotal(0);
            group.setRemain(0);
            group.setUsedCount(0);
            group.setSharedCount(0);
            group.getItems().add(buildZeroEnrollmentItem(lecture));
            groupMap.put(lectureName, group);
        }
        return groupMap;
    }

    private EnrollmentWithLectureVo buildZeroEnrollmentItem(Lectures lecture) {
        EnrollmentWithLectureVo item = new EnrollmentWithLectureVo();
        item.setLectureId(lecture.getId());
        item.setLectureName(StringUtils.defaultIfBlank(lecture.getName(), "未命名课程"));
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

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }
}
