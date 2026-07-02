package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Param;

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
     * 查询指定用户的报名课程信息（含分享统计）。
     *
     * @param uid 用户ID
     * @return 报名讲座信息列表
     */
    public List<EnrollmentWithLectureVo> selectAdminEnrollmentWithLectureByUid(Long uid);

    /**
     * 查询学籍信息管理列表（含学员和课程展示字段）。
     *
     * @param studentEnrollment 学籍筛选条件
     * @return 学籍信息集合
     */
    public List<StudentEnrollment> selectAdminStudentEnrollmentList(StudentEnrollment studentEnrollment);

    /**
     * 重新激活逻辑删除的学籍记录。
     *
     * @param studentEnrollment 学籍信息
     * @return 结果
     */
    public int reactivateStudentEnrollment(StudentEnrollment studentEnrollment);

    /**
     * 逻辑删除学籍记录。
     *
     * @param ids 学籍记录ID
     * @return 结果
     */
    public int softDeleteStudentEnrollmentByIds(Long[] ids);

    /**
     * 更新学籍数量。
     *
     * @param id 学籍ID
     * @param total 总数
     * @param remain 剩余数
     * @param updateTime 更新时间
     * @return 结果
     */
    public int updateEnrollmentCounts(@Param("id") Long id,
                                      @Param("total") Integer total,
                                      @Param("remain") Integer remain,
                                      @Param("updateTime") java.util.Date updateTime);

    /**
     * 按用户和课程查询任意状态的学籍记录。
     *
     * @param uid 用户ID
     * @param lectureId 课程ID
     * @return 学籍记录
     */
    public StudentEnrollment selectAnyStudentEnrollmentByUidAndLectureId(@Param("uid") Long uid,
                                                                         @Param("lectureId") Long lectureId);

    /**
     * 根据用户ID和课程ID查询学籍记录
     *
     * @param uid       用户ID
     * @param lectureId 课程ID
     * @return 学籍信息
     */
    public StudentEnrollment selectStudentEnrollmentByUidAndLectureId(@Param("uid") Long uid,
                                                                      @Param("lectureId") Long lectureId);

    /**
     * 根据用户ID和课程ID查询学籍记录（加行锁）
     *
     * @param uid       用户ID
     * @param lectureId 课程ID
     * @return 学籍信息
     */
    public StudentEnrollment selectEnrollmentByUidAndLectureId(@Param("uid") Long uid,
                                                               @Param("lectureId") Long lectureId);
}
