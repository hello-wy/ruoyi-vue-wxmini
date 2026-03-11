package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SignInRecord;
import com.ruoyi.system.domain.vo.SignInRecordWithLectureVo;

/**
 * 签到与报名记录 Service 接口
 *
 * @author ruoyi
 * @date 2026-03-06
 */
public interface ISignInRecordService
{
    SignInRecord selectSignInRecordById(Long id);

    List<SignInRecord> selectSignInRecordList(SignInRecord signInRecord);

    int insertSignInRecord(SignInRecord signInRecord);

    int updateSignInRecord(SignInRecord signInRecord);

    int deleteSignInRecordByIds(Long[] ids);

    int deleteSignInRecordById(Long id);

    /**
     * 查询用户的讲座签到记录列表（联表，含讲座信息）
     *
     * @param uid 用户ID
     * @return 讲座签到记录列表
     */
    List<SignInRecordWithLectureVo> selectSignInRecordWithLectureByUid(Long uid);

    /**
     * 查询用户的沙龙报名记录列表（联表，含沙龙信息）
     *
     * @param uid 用户ID
     * @return 沙龙报名记录列表
     */
    List<SignInRecordWithLectureVo> selectSalonRegistrationByUid(Long uid);

    /**
     * 检查用户是否已报名某沙龙
     *
     * @param uid     用户ID
     * @param salonId 沙龙ID
     * @return true=已报名
     */
    boolean isSalonRegistered(Long uid, Long salonId);

    /**
     * 检查用户是否已签到某讲座
     *
     * @param uid       用户ID
     * @param lectureId 讲座ID
     * @return true=已签到
     */
    boolean isLectureSignedIn(Long uid, Long lectureId);
}
