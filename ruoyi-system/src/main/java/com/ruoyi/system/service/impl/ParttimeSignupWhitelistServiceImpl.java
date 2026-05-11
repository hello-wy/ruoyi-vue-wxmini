package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.ParttimeSignupWhitelist;
import com.ruoyi.system.mapper.ParttimeSignupWhitelistMapper;
import com.ruoyi.system.service.IParttimeSignupWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<ParttimeSignupWhitelist> selectParttimeSignupWhitelistList(ParttimeSignupWhitelist whitelist) {
        return parttimeSignupWhitelistMapper.selectParttimeSignupWhitelistList(whitelist);
    }

    @Override
    public int insertParttimeSignupWhitelist(ParttimeSignupWhitelist whitelist) {
        return parttimeSignupWhitelistMapper.insertParttimeSignupWhitelist(whitelist);
    }

    @Override
    public int deleteParttimeSignupWhitelistById(Long id) {
        return parttimeSignupWhitelistMapper.deleteParttimeSignupWhitelistById(id);
    }
}
