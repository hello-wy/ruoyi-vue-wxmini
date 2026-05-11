package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.ParttimeSignupWhitelist;

public interface ParttimeSignupWhitelistMapper extends BaseMapper<ParttimeSignupWhitelist> {
    ParttimeSignupWhitelist selectParttimeSignupWhitelistByIdCard(String idCard);

    ParttimeSignupWhitelist selectEnabledParttimeSignupWhitelistByIdCard(String idCard);

    int insertParttimeSignupWhitelist(ParttimeSignupWhitelist whitelist);
}
