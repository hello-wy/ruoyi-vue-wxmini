package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.MerchantUserTypeWhitelist;
import com.ruoyi.system.mapper.MerchantUserTypeWhitelistMapper;
import com.ruoyi.system.service.IMerchantUserTypeWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantUserTypeWhitelistServiceImpl implements IMerchantUserTypeWhitelistService {

    @Autowired
    private MerchantUserTypeWhitelistMapper merchantUserTypeWhitelistMapper;

    @Override
    public MerchantUserTypeWhitelist selectMerchantUserTypeWhitelistByIdCard(String idCard) {
        return merchantUserTypeWhitelistMapper.selectMerchantUserTypeWhitelistByIdCard(idCard);
    }

    @Override
    public MerchantUserTypeWhitelist selectEnabledMerchantUserTypeWhitelistByIdCard(String idCard) {
        return merchantUserTypeWhitelistMapper.selectEnabledMerchantUserTypeWhitelistByIdCard(idCard);
    }

    @Override
    public List<MerchantUserTypeWhitelist> selectMerchantUserTypeWhitelistList(MerchantUserTypeWhitelist whitelist) {
        return merchantUserTypeWhitelistMapper.selectMerchantUserTypeWhitelistList(whitelist);
    }

    @Override
    public int insertMerchantUserTypeWhitelist(MerchantUserTypeWhitelist whitelist) {
        return merchantUserTypeWhitelistMapper.insertMerchantUserTypeWhitelist(whitelist);
    }

    @Override
    public int updateMerchantUserTypeWhitelistAudit(MerchantUserTypeWhitelist whitelist) {
        return merchantUserTypeWhitelistMapper.updateMerchantUserTypeWhitelistAudit(whitelist);
    }

    @Override
    public int updateMerchantUserTypeWhitelistApplication(MerchantUserTypeWhitelist whitelist) {
        return merchantUserTypeWhitelistMapper.updateMerchantUserTypeWhitelistApplication(whitelist);
    }

    @Override
    public int deleteMerchantUserTypeWhitelistByIds(Long[] ids) {
        return merchantUserTypeWhitelistMapper.deleteMerchantUserTypeWhitelistByIds(ids);
    }
}
