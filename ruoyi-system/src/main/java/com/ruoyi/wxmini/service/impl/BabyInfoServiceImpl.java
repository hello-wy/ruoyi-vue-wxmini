package com.ruoyi.wxmini.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.mapper.ParentsMapper;
import com.ruoyi.wxmini.domain.BabyInfo;
import com.ruoyi.wxmini.mapper.BabyInfoMapper;
import com.ruoyi.wxmini.service.IBabyInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BabyInfoServiceImpl implements IBabyInfoService {

    @Resource
    private BabyInfoMapper babyInfoMapper;

    @Resource
    private ParentsMapper parentsMapper;

    @Override
    public List<BabyInfo> selectBabyInfoListByUserId(String userId) {
        return babyInfoMapper.selectBabyInfoListByUserId(userId);
    }

    @Override
    public BabyInfo selectBabyInfoByIdAndUserId(Long id, String userId) {
        return babyInfoMapper.selectBabyInfoByIdAndUserId(id, userId);
    }

    @Override
    public int insertBabyInfo(BabyInfo babyInfo) {
        babyInfo.setCreateTime(DateUtils.getNowDate());
        babyInfo.setUpdateTime(DateUtils.getNowDate());
        return babyInfoMapper.insertBabyInfo(babyInfo);
    }

    @Override
    public int updateBabyInfo(BabyInfo babyInfo) {
        babyInfo.setUpdateTime(DateUtils.getNowDate());
        return babyInfoMapper.updateBabyInfo(babyInfo);
    }

    @Override
    public int deleteBabyInfoByIdAndUserId(Long id, String userId) {
        long refCount = parentsMapper.selectCount(new LambdaQueryWrapper<Parents>().eq(Parents::getBabyId, id));
        if (refCount > 0) {
            throw new ServiceException("该萌娃已关联家教需求，暂不能删除");
        }
        return babyInfoMapper.deleteBabyInfoByIdAndUserId(id, userId);
    }
}
