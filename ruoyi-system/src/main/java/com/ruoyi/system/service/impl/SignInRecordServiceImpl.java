package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SignInRecordMapper;
import com.ruoyi.system.domain.SignInRecord;
import com.ruoyi.system.domain.vo.SignInRecordWithLectureVo;
import com.ruoyi.system.service.ISignInRecordService;

/**
 * 签到与报名记录 Service 业务层处理
 *
 * @author ruoyi
 * @date 2026-03-06
 */
@Service
public class SignInRecordServiceImpl implements ISignInRecordService
{
    @Autowired
    private SignInRecordMapper signInRecordMapper;

    @Override
    public SignInRecord selectSignInRecordById(Long id)
    {
        return signInRecordMapper.selectSignInRecordById(id);
    }

    @Override
    public List<SignInRecord> selectSignInRecordList(SignInRecord signInRecord)
    {
        return signInRecordMapper.selectSignInRecordList(signInRecord);
    }

    @Override
    public int insertSignInRecord(SignInRecord signInRecord)
    {
        return signInRecordMapper.insertSignInRecord(signInRecord);
    }

    @Override
    public int updateSignInRecord(SignInRecord signInRecord)
    {
        return signInRecordMapper.updateSignInRecord(signInRecord);
    }

    @Override
    public int deleteSignInRecordByIds(Long[] ids)
    {
        return signInRecordMapper.deleteSignInRecordByIds(ids);
    }

    @Override
    public int deleteSignInRecordById(Long id)
    {
        return signInRecordMapper.deleteSignInRecordById(id);
    }

    /**
     * 查询用户的讲座签到记录列表（联表，含讲座信息）
     */
    @Override
    public List<SignInRecordWithLectureVo> selectSignInRecordWithLectureByUid(Long uid)
    {
        return signInRecordMapper.selectSignInRecordWithLectureByUid(uid);
    }

    /**
     * 查询用户的沙龙报名记录列表（联表，含沙龙信息）
     */
    @Override
    public List<SignInRecordWithLectureVo> selectSalonRegistrationByUid(Long uid)
    {
        return signInRecordMapper.selectSalonRegistrationByUid(uid);
    }

    @Override
    public SignInRecord selectJobSignInRecordByOrderId(Long jobOrderId)
    {
        return signInRecordMapper.selectJobSignInRecordByOrderId(jobOrderId);
    }

    @Override
    public int updateJobSignSubmitFields(SignInRecord signInRecord)
    {
        return signInRecordMapper.updateJobSignSubmitFields(signInRecord);
    }

    @Override
    public int updateJobSignAuditFields(SignInRecord signInRecord)
    {
        return signInRecordMapper.updateJobSignAuditFields(signInRecord);
    }

    /**
     * 检查用户是否已报名某沙龙
     */
    @Override
    public boolean isSalonRegistered(Long uid, Long salonId)
    {
        return signInRecordMapper.countSalonRegistration(uid, salonId) > 0;
    }

    /**
     * 检查用户是否已签到某讲座
     */
    @Override
    public boolean isLectureSignedIn(Long uid, Long lectureId)
    {
        return signInRecordMapper.countLectureSignIn(uid, lectureId) > 0;
    }
}
