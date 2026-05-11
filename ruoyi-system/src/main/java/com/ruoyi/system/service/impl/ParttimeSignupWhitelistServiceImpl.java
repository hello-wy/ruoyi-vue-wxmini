package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.ParttimeSignupWhitelist;
import com.ruoyi.system.mapper.ParttimeSignupWhitelistMapper;
import com.ruoyi.system.service.IParttimeSignupWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParttimeSignupWhitelistServiceImpl implements IParttimeSignupWhitelistService {

    @Autowired
    private ParttimeSignupWhitelistMapper parttimeSignupWhitelistMapper;

    @Override
    public ParttimeSignupWhitelist selectParttimeSignupWhitelistByIdCard(String idCard) {
        return parttimeSignupWhitelistMapper.selectParttimeSignupWhitelistByIdCard(idCard);
    }

    @Override
    public ParttimeSignupWhitelist selectEnabledParttimeSignupWhitelistByIdCard(String idCard) {
        return parttimeSignupWhitelistMapper.selectEnabledParttimeSignupWhitelistByIdCard(idCard);
    }

    @Override
    public int insertParttimeSignupWhitelist(ParttimeSignupWhitelist whitelist) {
        return parttimeSignupWhitelistMapper.insertParttimeSignupWhitelist(whitelist);
    }
}
