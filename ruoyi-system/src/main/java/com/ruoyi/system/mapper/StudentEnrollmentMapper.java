package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.StudentEnrollment;
import com.ruoyi.system.domain.vo.EnrollmentWithLectureVo;

/**
 * 学籍信息Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
public interface StudentEnrollmentMapper extends BaseMapper<StudentEnrollment>
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
     * 删除学籍信息
     * 
     * @param id 学籍信息主键
     * @return 结果
     */
    public int deleteStudentEnrollmentById(Long id);

    /**
     * 批量删除学籍信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStudentEnrollmentByIds(Long[] ids);

    /**
     * 查询指定用户的报名课程信息（含讲座名称、总数、剩余数）
     *
     * @param uid 用户ID
     * @return 报名讲座信息列表
     */
    public List<EnrollmentWithLectureVo> selectEnrollmentWithLectureByUid(Long uid);

    /**
     * 根据用户ID和课程ID查询学籍记录（加行锁）
     *
     * @param uid       用户ID
     * @param lectureId 课程ID
     * @return 学籍信息
     */
    public StudentEnrollment selectEnrollmentByUidAndLectureId(@org.apache.ibatis.annotations.Param("uid") Long uid,
                                                               @org.apache.ibatis.annotations.Param("lectureId") Long lectureId);
}
