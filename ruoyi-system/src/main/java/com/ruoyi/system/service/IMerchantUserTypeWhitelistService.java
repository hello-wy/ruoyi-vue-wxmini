package com.ruoyi.system.service;

import com.ruoyi.system.domain.MerchantUserTypeWhitelist;

import java.util.List;

public interface IMerchantUserTypeWhitelistService {
    MerchantUserTypeWhitelist selectMerchantUserTypeWhitelistByIdCard(String idCard);

    MerchantUserTypeWhitelist selectEnabledMerchantUserTypeWhitelistByIdCard(String idCard);

    List<MerchantUserTypeWhitelist> selectMerchantUserTypeWhitelistList(MerchantUserTypeWhitelist whitelist);

    int insertMerchantUserTypeWhitelist(MerchantUserTypeWhitelist whitelist);

    int updateMerchantUserTypeWhitelistAudit(MerchantUserTypeWhitelist whitelist);

    int updateMerchantUserTypeWhitelistApplication(MerchantUserTypeWhitelist whitelist);

    int deleteMerchantUserTypeWhitelistByIds(Long[] ids);
}
