package com.ruoyi.wxmini.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.wxmini.domain.BabyInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BabyInfoMapper extends BaseMapper<BabyInfo> {

    List<BabyInfo> selectBabyInfoListByUserId(@Param("userId") String userId);

    BabyInfo selectBabyInfoByIdAndUserId(@Param("id") Long id, @Param("userId") String userId);

    int insertBabyInfo(BabyInfo babyInfo);

    int updateBabyInfo(BabyInfo babyInfo);

    int deleteBabyInfoByIdAndUserId(@Param("id") Long id, @Param("userId") String userId);
}
