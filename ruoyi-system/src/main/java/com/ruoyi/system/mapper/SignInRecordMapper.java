package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SignInRecord;
import com.ruoyi.system.domain.vo.SignInRecordWithLectureVo;
import org.apache.ibatis.annotations.Param;

/**
 * 签到与报名记录 Mapper 接口
 *
 * @author ruoyi
 * @date 2026-03-06
 */
public interface SignInRecordMapper
{
    /**
     * 查询记录详情
     *
     * @param id 主键
     * @return 签到/报名记录
     */
    SignInRecord selectSignInRecordById(Long id);

    /**
     * 查询记录列表
     *
     * @param signInRecord 查询条件
     * @return 记录集合
     */
    List<SignInRecord> selectSignInRecordList(SignInRecord signInRecord);

    /**
     * 新增记录
     *
     * @param signInRecord 签到/报名记录
     * @return 影响行数
     */
    int insertSignInRecord(SignInRecord signInRecord);

    /**
     * 修改记录
     *
     * @param signInRecord 签到/报名记录
     * @return 影响行数
     */
    int updateSignInRecord(SignInRecord signInRecord);

    int updateJobSignSubmitFields(SignInRecord signInRecord);

    int updateJobSignAuditFields(SignInRecord signInRecord);

    /**
     * 删除记录
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteSignInRecordById(Long id);

    /**
     * 批量删除记录
     *
     * @param ids 主键数组
     * @return 影响行数
     */
    int deleteSignInRecordByIds(Long[] ids);

    /**
     * 查询用户的讲座签到记录列表（联表，含讲座基本信息）
     *
     * @param uid 用户ID
     * @return 签到记录（含讲座信息）列表
     */
    List<SignInRecordWithLectureVo> selectSignInRecordWithLectureByUid(Long uid);

    /**
     * 查询用户的沙龙报名记录列表（联表，含沙龙基本信息）
     *
     * @param uid 用户ID
     * @return 报名记录（含沙龙信息）列表
     */
    List<SignInRecordWithLectureVo> selectSalonRegistrationByUid(Long uid);

    SignInRecord selectJobSignInRecord(@Param("jobId") Long jobId, @Param("uid") Long uid);

    /**
     * 检查用户是否已报名某沙龙
     *
     * @param uid     用户ID
     * @param salonId 沙龙ID
     * @return 记录数（>0 表示已报名）
     */
    int countSalonRegistration(Long uid, Long salonId);

    /**
     * 检查用户是否已签到某讲座
     *
     * @param uid       用户ID
     * @param lectureId 讲座ID
     * @return 记录数（>0 表示已签到）
     */
    int countLectureSignIn(Long uid, Long lectureId);
}
