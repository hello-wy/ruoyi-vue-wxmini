package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.StudentEnrollment;
import com.ruoyi.system.domain.bo.StudentEnrollmentShareBo;
import com.ruoyi.system.mapper.LecturesMapper;
import com.ruoyi.system.mapper.StudentEnrollmentMapper;
import com.ruoyi.system.mapper.StudentEnrollmentShareRecordMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.mapper.UserInfoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentEnrollmentServiceImplTest {

    @Mock
    private StudentEnrollmentMapper studentEnrollmentMapper;

    @Mock
    private LecturesMapper lecturesMapper;

    @Mock
    private SysUserMapper sysUserMapper;

    @Mock
    private StudentEnrollmentShareRecordMapper shareRecordMapper;

    @Mock
    private UserInfoMapper userInfoMapper;

    @InjectMocks
    private StudentEnrollmentServiceImpl service;

    @Test
    void shareEnrollmentShouldMoveOneRemainToExistingTarget() {
        StudentEnrollment source = enrollment(10L, 1L, 100L, 5, 3, 0L);
        StudentEnrollment target = enrollment(11L, 2L, 100L, 1, 1, 0L);
        when(userInfoMapper.selectUserInfoById(1L)).thenReturn(userInfo(1L));
        when(userInfoMapper.selectUserInfoById(2L)).thenReturn(userInfo(2L));
        when(lecturesMapper.selectLecturesById(100L)).thenReturn(new com.ruoyi.system.domain.Lectures());
        when(studentEnrollmentMapper.selectEnrollmentByUidAndLectureId(1L, 100L)).thenReturn(source);
        when(studentEnrollmentMapper.selectEnrollmentByUidAndLectureId(2L, 100L)).thenReturn(target);

        service.shareEnrollment(shareBo(1L, 2L, 100L, 1), "admin");

        assertEquals(2, source.getRemain());
        assertEquals(2, target.getTotal());
        assertEquals(2, target.getRemain());
        verify(studentEnrollmentMapper).updateStudentEnrollment(source);
        verify(studentEnrollmentMapper).updateEnrollmentCounts(any(), any(), any(), any());
        verify(shareRecordMapper).insertStudentEnrollmentShareRecord(any());
    }

    @Test
    void shareEnrollmentShouldRejectInsufficientRemain() {
        StudentEnrollment source = enrollment(10L, 1L, 100L, 5, 0, 0L);
        when(userInfoMapper.selectUserInfoById(1L)).thenReturn(userInfo(1L));
        when(userInfoMapper.selectUserInfoById(2L)).thenReturn(userInfo(2L));
        when(lecturesMapper.selectLecturesById(100L)).thenReturn(new com.ruoyi.system.domain.Lectures());
        when(studentEnrollmentMapper.selectEnrollmentByUidAndLectureId(1L, 100L)).thenReturn(source);

        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.shareEnrollment(shareBo(1L, 2L, 100L, 1), "admin"));

        assertEquals("剩余学籍不足，无法分享", ex.getMessage());
    }

    @Test
    void insertStudentEnrollmentShouldDefaultRemainAndId() {
        StudentEnrollment enrollment = enrollment(null, 1L, 100L, 5, null, null);
        when(userInfoMapper.selectUserInfoById(1L)).thenReturn(userInfo(1L));
        when(lecturesMapper.selectLecturesById(100L)).thenReturn(new com.ruoyi.system.domain.Lectures());
        when(studentEnrollmentMapper.insertStudentEnrollment(any(StudentEnrollment.class))).thenReturn(1);

        int result = service.insertStudentEnrollment(enrollment);

        assertEquals(1, result);
        assertEquals(5, enrollment.getRemain());
        assertEquals(0L, enrollment.getIsDeleted());
        verify(studentEnrollmentMapper).insertStudentEnrollment(enrollment);
    }

    private StudentEnrollment enrollment(Long id, Long uid, Long lectureId, Integer total, Integer remain, Long isDeleted) {
        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setId(id);
        enrollment.setUid(uid);
        enrollment.setLectureId(lectureId);
        enrollment.setTotal(total);
        enrollment.setRemain(remain);
        enrollment.setIsDeleted(isDeleted);
        return enrollment;
    }

    private StudentEnrollmentShareBo shareBo(Long sourceUid, Long targetUid, Long lectureId, Integer count) {
        StudentEnrollmentShareBo bo = new StudentEnrollmentShareBo();
        bo.setSourceUid(sourceUid);
        bo.setTargetUid(targetUid);
        bo.setLectureId(lectureId);
        bo.setCount(count);
        return bo;
    }

    private UserInfo userInfo(Long id) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        return userInfo;
    }
}
