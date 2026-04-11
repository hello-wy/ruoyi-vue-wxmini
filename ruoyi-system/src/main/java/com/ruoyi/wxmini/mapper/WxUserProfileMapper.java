package com.ruoyi.wxmini.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.wxmini.domain.WxUserProfile;
import com.ruoyi.wxmini.domain.vo.WxUserProfileVo;

public interface WxUserProfileMapper extends BaseMapper<WxUserProfile> {

    WxUserProfileVo selectProfileDetailByUserId(String userId);
}
