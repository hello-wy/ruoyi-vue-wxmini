package com.ruoyi.system.service;

import com.ruoyi.system.domain.ParttimeSignupWhitelist;

import java.util.List;

public interface IParttimeSignupWhitelistService {
    ParttimeSignupWhitelist selectParttimeSignupWhitelistByIdCard(String idCard);

    ParttimeSignupWhitelist selectEnabledParttimeSignupWhitelistByIdCard(String idCard);

    List<ParttimeSignupWhitelist> selectParttimeSignupWhitelistList(ParttimeSignupWhitelist whitelist);

    int insertParttimeSignupWhitelist(ParttimeSignupWhitelist whitelist);

    int deleteParttimeSignupWhitelistById(Long id);
}
