package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.vo.EnrollmentWithLectureVo;
import com.ruoyi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.StudentEnrollmentMapper;
import com.ruoyi.system.domain.StudentEnrollment;
import com.ruoyi.system.service.IStudentEnrollmentService;

/**
 * 学籍信息Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class StudentEnrollmentServiceImpl implements IStudentEnrollmentService
{
    @Autowired
    private StudentEnrollmentMapper studentEnrollmentMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public StudentEnrollment selectStudentEnrollmentById(Long id)
    {
        return studentEnrollmentMapper.selectStudentEnrollmentById(id);
    }

    @Override
    public List<StudentEnrollment> selectStudentEnrollmentList(StudentEnrollment studentEnrollment)
    {
        return studentEnrollmentMapper.selectStudentEnrollmentList(studentEnrollment);
    }

    @Override
    public int insertStudentEnrollment(StudentEnrollment studentEnrollment)
    {
        studentEnrollment.setCreateTime(DateUtils.getNowDate());
        return studentEnrollmentMapper.insertStudentEnrollment(studentEnrollment);
    }

    @Override
    public int updateStudentEnrollment(StudentEnrollment studentEnrollment)
    {
        studentEnrollment.setUpdateTime(DateUtils.getNowDate());
        return studentEnrollmentMapper.updateStudentEnrollment(studentEnrollment);
    }

    @Override
    public int deleteStudentEnrollmentByIds(Long[] ids)
    {
        return studentEnrollmentMapper.deleteStudentEnrollmentByIds(ids);
    }

    @Override
    public int deleteStudentEnrollmentById(Long id)
    {
        return studentEnrollmentMapper.deleteStudentEnrollmentById(id);
    }

    @Override
    public List<EnrollmentWithLectureVo> selectMyEnrollment(Long uid)
    {
        return studentEnrollmentMapper.selectEnrollmentWithLectureByUid(uid);
    }

    @Override
    public StudentEnrollment selectEnrollmentByUidAndLectureId(Long uid, Long lectureId)
    {
        return studentEnrollmentMapper.selectStudentEnrollmentByUidAndLectureId(uid, lectureId);
    }

    @Override
    public void assertCourseEnrollmentAvailable(Long uid, Long lectureId)
    {
        StudentEnrollment enrollment = selectEnrollmentByUidAndLectureId(uid, lectureId);
        if (enrollment == null)
        {
            throw new ServiceException("未找到对应的学籍记录");
        }
        if (enrollment.getRemain() == null || enrollment.getRemain() <= 0)
        {
            throw new ServiceException("余额不足，无法核销");
        }
    }

    /**
     * 核销：将当前用户指定课程的剩余次数减1，余额不足时抛出异常并回滚
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void decreaseRemain(Long uid, Long lectureId, int num)
    {
        StudentEnrollment enrollment = studentEnrollmentMapper.selectEnrollmentByUidAndLectureId(uid, lectureId);
        if (enrollment == null)
        {
            throw new ServiceException("未找到对应的学籍记录");
        }
        int newRemain = enrollment.getRemain() - num;
        if (newRemain < 0)
        {
            throw new ServiceException("余额不足，无法核销");
        }
        enrollment.setRemain(newRemain);
        enrollment.setUpdateTime(DateUtils.getNowDate());
        studentEnrollmentMapper.updateStudentEnrollment(enrollment);
        sysUserMapper.updateUserEnrollmentDelta(uid, -num);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseRemain(Long uid, Long lectureId, int num)
    {
        StudentEnrollment enrollment = studentEnrollmentMapper.selectEnrollmentByUidAndLectureId(uid, lectureId);
        if (enrollment == null)
        {
            throw new ServiceException("未找到对应的学籍记录");
        }
        enrollment.setRemain(enrollment.getRemain() + num);
        enrollment.setUpdateTime(DateUtils.getNowDate());
        studentEnrollmentMapper.updateStudentEnrollment(enrollment);
        sysUserMapper.updateUserEnrollmentDelta(uid, num);
    }
}
