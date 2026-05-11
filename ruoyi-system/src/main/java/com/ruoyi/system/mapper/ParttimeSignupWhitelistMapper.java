package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.ParttimeSignupWhitelist;

import java.util.List;

public interface ParttimeSignupWhitelistMapper extends BaseMapper<ParttimeSignupWhitelist> {
    ParttimeSignupWhitelist selectParttimeSignupWhitelistByIdCard(String idCard);

    ParttimeSignupWhitelist selectEnabledParttimeSignupWhitelistByIdCard(String idCard);

    List<ParttimeSignupWhitelist> selectParttimeSignupWhitelistList(ParttimeSignupWhitelist whitelist);

    int insertParttimeSignupWhitelist(ParttimeSignupWhitelist whitelist);

    int deleteParttimeSignupWhitelistById(Long id);
}
