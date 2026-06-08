package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.StudentEnrollment;
import com.ruoyi.system.domain.vo.EnrollmentWithLectureVo;

/**
 * 学籍信息Service接口
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
public interface IStudentEnrollmentService 
{
    /**
     * 查询学籍信息
     * 
     * @param id 学籍信息主键
     * @return 学籍信息
     */
    public StudentEnrollment selectStudentEnrollmentById(Long id);

    /**
     * 查询学籍信息列表
     * 
     * @param studentEnrollment 学籍信息
     * @return 学籍信息集合
     */
    public List<StudentEnrollment> selectStudentEnrollmentList(StudentEnrollment studentEnrollment);

    /**
     * 新增学籍信息
     * 
     * @param studentEnrollment 学籍信息
     * @return 结果
     */
    public int insertStudentEnrollment(StudentEnrollment studentEnrollment);

    /**
     * 修改学籍信息
     * 
     * @param studentEnrollment 学籍信息
     * @return 结果
     */
    public int updateStudentEnrollment(StudentEnrollment studentEnrollment);

    /**
     * 批量删除学籍信息
     * 
     * @param ids 需要删除的学籍信息主键集合
     * @return 结果
     */
    public int deleteStudentEnrollmentByIds(Long[] ids);

    /**
     * 删除学籍信息信息
     * 
     * @param id 学籍信息主键
     * @return 结果
     */
    public int deleteStudentEnrollmentById(Long id);

    public List<EnrollmentWithLectureVo> selectMyEnrollment(Long uid);

    /**
     * 查询指定用户指定课程的学籍记录
     *
     * @param uid 用户ID
     * @param lectureId 课程ID
     * @return 学籍记录
     */
    StudentEnrollment selectEnrollmentByUidAndLectureId(Long uid, Long lectureId);

    /**
     * 校验指定用户指定课程存在可用学籍。
     *
     * @param uid 用户ID
     * @param lectureId 课程ID
     */
    void assertCourseEnrollmentAvailable(Long uid, Long lectureId);

    /**
     * 核销：将当前用户指定课程的剩余次数减num，余额不足时抛出异常并回滚事务
     *
     * @param uid       用户ID
     * @param lectureId 课程ID
     * @param num       核销数量
     */
    void decreaseRemain(Long uid, Long lectureId, int num);

    /**
     * 充值：将当前用户指定课程的剩余次数加num
     *
     * @param uid       用户ID
     * @param lectureId 课程ID
     * @param num       充值数量
     */
    void increaseRemain(Long uid, Long lectureId, int num);
}
