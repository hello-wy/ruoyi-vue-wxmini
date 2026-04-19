package com.ruoyi.wxmini.service;

import com.ruoyi.wxmini.domain.BabyInfo;

import java.util.List;

public interface IBabyInfoService {

    List<BabyInfo> selectBabyInfoListByUserId(String userId);

    BabyInfo selectBabyInfoByIdAndUserId(Long id, String userId);

    int insertBabyInfo(BabyInfo babyInfo);

    int updateBabyInfo(BabyInfo babyInfo);

    int deleteBabyInfoByIdAndUserId(Long id, String userId);
}
