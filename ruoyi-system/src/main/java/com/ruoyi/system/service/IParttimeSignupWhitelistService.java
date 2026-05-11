package com.ruoyi.system.service;

import com.ruoyi.system.domain.ParttimeSignupWhitelist;

public interface IParttimeSignupWhitelistService {
    ParttimeSignupWhitelist selectParttimeSignupWhitelistByIdCard(String idCard);

    ParttimeSignupWhitelist selectEnabledParttimeSignupWhitelistByIdCard(String idCard);

    int insertParttimeSignupWhitelist(ParttimeSignupWhitelist whitelist);
}
