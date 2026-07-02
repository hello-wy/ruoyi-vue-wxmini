package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.StudentEnrollmentShareRecord;
import com.ruoyi.system.domain.bo.StudentEnrollmentShareBo;
import com.ruoyi.system.domain.vo.EnrollmentWithLectureVo;
import com.ruoyi.system.mapper.LecturesMapper;
import com.ruoyi.system.mapper.StudentEnrollmentShareRecordMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.mapper.UserInfoMapper;
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
    private LecturesMapper lecturesMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private StudentEnrollmentShareRecordMapper shareRecordMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

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
    public List<StudentEnrollment> selectAdminStudentEnrollmentList(StudentEnrollment studentEnrollment)
    {
        return studentEnrollmentMapper.selectAdminStudentEnrollmentList(studentEnrollment);
    }

    @Override
    public int insertStudentEnrollment(StudentEnrollment studentEnrollment)
    {
        validateEnrollment(studentEnrollment, false);
        if (studentEnrollment.getId() == null)
        {
            studentEnrollment.setId(SnowflakeIdWorker.nextIdDefault());
        }
        if (studentEnrollment.getRemain() == null)
        {
            studentEnrollment.setRemain(studentEnrollment.getTotal());
        }
        if (studentEnrollment.getIsDeleted() == null)
        {
            studentEnrollment.setIsDeleted(0L);
        }
        if (studentEnrollment.getRemain() > studentEnrollment.getTotal())
        {
            throw new ServiceException("剩余学籍数不能大于总学籍数");
        }
        studentEnrollment.setCreateTime(DateUtils.getNowDate());

        StudentEnrollment existing = studentEnrollmentMapper.selectAnyStudentEnrollmentByUidAndLectureId(
                studentEnrollment.getUid(), studentEnrollment.getLectureId());
        if (existing != null)
        {
            if (existing.getIsDeleted() != null && existing.getIsDeleted() == 1L)
            {
                studentEnrollment.setId(existing.getId());
                studentEnrollment.setUpdateTime(DateUtils.getNowDate());
                return studentEnrollmentMapper.reactivateStudentEnrollment(studentEnrollment);
            }
            throw new ServiceException("该学员已绑定该课程学籍");
        }
        return studentEnrollmentMapper.insertStudentEnrollment(studentEnrollment);
    }

    @Override
    public int updateStudentEnrollment(StudentEnrollment studentEnrollment)
    {
        validateEnrollment(studentEnrollment, true);
        if (studentEnrollment.getRemain() == null)
        {
            studentEnrollment.setRemain(studentEnrollment.getTotal());
        }
        if (studentEnrollment.getRemain() > studentEnrollment.getTotal())
        {
            throw new ServiceException("剩余学籍数不能大于总学籍数");
        }
        studentEnrollment.setUpdateTime(DateUtils.getNowDate());
        return studentEnrollmentMapper.updateStudentEnrollment(studentEnrollment);
    }

    @Override
    public int deleteStudentEnrollmentByIds(Long[] ids)
    {
        return studentEnrollmentMapper.softDeleteStudentEnrollmentByIds(ids);
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
    public List<EnrollmentWithLectureVo> selectAdminEnrollmentWithLectureByUid(Long uid)
    {
        return studentEnrollmentMapper.selectAdminEnrollmentWithLectureByUid(uid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shareEnrollment(StudentEnrollmentShareBo shareBo, String operator)
    {
        if (shareBo == null)
        {
            throw new ServiceException("分享参数不能为空");
        }
        Long sourceUid = shareBo.getSourceUid();
        Long targetUid = shareBo.getTargetUid();
        Long lectureId = shareBo.getLectureId();
        Integer count = shareBo.getCount();
        if (sourceUid == null || targetUid == null || lectureId == null || count == null || count <= 0)
        {
            throw new ServiceException("分享参数不完整");
        }
        if (sourceUid.equals(targetUid))
        {
            throw new ServiceException("不能分享给当前学员");
        }
        assertUserExists(sourceUid, "分享方学员不存在");
        assertUserExists(targetUid, "接收方学员不存在");
        if (lecturesMapper.selectLecturesById(lectureId) == null)
        {
            throw new ServiceException("课程不存在");
        }

        StudentEnrollment source = studentEnrollmentMapper.selectEnrollmentByUidAndLectureId(sourceUid, lectureId);
        if (source == null)
        {
            throw new ServiceException("未找到分享方学籍记录");
        }
        int sourceRemain = safeInt(source.getRemain());
        if (sourceRemain < count)
        {
            throw new ServiceException("剩余学籍不足，无法分享");
        }
        source.setRemain(sourceRemain - count);
        source.setUpdateTime(DateUtils.getNowDate());
        studentEnrollmentMapper.updateStudentEnrollment(source);

        StudentEnrollment target = studentEnrollmentMapper.selectEnrollmentByUidAndLectureId(targetUid, lectureId);
        if (target == null)
        {
            target = studentEnrollmentMapper.selectAnyStudentEnrollmentByUidAndLectureId(targetUid, lectureId);
        }
        if (target == null)
        {
            target = new StudentEnrollment();
            target.setId(SnowflakeIdWorker.nextIdDefault());
            target.setUid(targetUid);
            target.setLectureId(lectureId);
            target.setTotal(count);
            target.setRemain(count);
            target.setIsDeleted(0L);
            target.setCreateTime(DateUtils.getNowDate());
            studentEnrollmentMapper.insertStudentEnrollment(target);
        }
        else
        {
            target.setTotal(safeInt(target.getTotal()) + count);
            target.setRemain(safeInt(target.getRemain()) + count);
            Long originalDeleted = target.getIsDeleted();
            target.setIsDeleted(0L);
            target.setUpdateTime(DateUtils.getNowDate());
            if (originalDeleted != null && originalDeleted == 1L)
            {
                studentEnrollmentMapper.reactivateStudentEnrollment(target);
            }
            else
            {
                studentEnrollmentMapper.updateEnrollmentCounts(target.getId(), target.getTotal(), target.getRemain(), target.getUpdateTime());
            }
        }

        StudentEnrollmentShareRecord record = new StudentEnrollmentShareRecord();
        record.setId(SnowflakeIdWorker.nextIdDefault());
        record.setSourceUid(sourceUid);
        record.setTargetUid(targetUid);
        record.setLectureId(lectureId);
        record.setCount(count);
        record.setCreateBy(operator);
        record.setCreateTime(DateUtils.getNowDate());
        shareRecordMapper.insertStudentEnrollmentShareRecord(record);
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

    private void validateEnrollment(StudentEnrollment enrollment, boolean requireId)
    {
        if (enrollment == null)
        {
            throw new ServiceException("学籍信息不能为空");
        }
        if (requireId && enrollment.getId() == null)
        {
            throw new ServiceException("学籍ID不能为空");
        }
        if (enrollment.getUid() == null)
        {
            throw new ServiceException("学员不能为空");
        }
        if (enrollment.getLectureId() == null)
        {
            throw new ServiceException("课程不能为空");
        }
        if (enrollment.getTotal() == null || enrollment.getTotal() < 0)
        {
            throw new ServiceException("总学籍数不能小于0");
        }
        if (enrollment.getRemain() != null && enrollment.getRemain() < 0)
        {
            throw new ServiceException("剩余学籍数不能小于0");
        }
        assertUserExists(enrollment.getUid(), "学员不存在");
        if (lecturesMapper.selectLecturesById(enrollment.getLectureId()) == null)
        {
            throw new ServiceException("课程不存在");
        }
    }

    private void assertUserExists(Long uid, String message)
    {
        UserInfo userInfo = userInfoMapper.selectUserInfoById(uid);
        if (userInfo == null)
        {
            throw new ServiceException(message);
        }
    }

    private int safeInt(Integer value)
    {
        return value == null ? 0 : value;
    }
}
