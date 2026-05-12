package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.MerchantUserTypeWhitelist;

import java.util.List;

public interface MerchantUserTypeWhitelistMapper extends BaseMapper<MerchantUserTypeWhitelist> {
    MerchantUserTypeWhitelist selectMerchantUserTypeWhitelistByIdCard(String idCard);

    MerchantUserTypeWhitelist selectEnabledMerchantUserTypeWhitelistByIdCard(String idCard);

    List<MerchantUserTypeWhitelist> selectMerchantUserTypeWhitelistList(MerchantUserTypeWhitelist whitelist);

    int insertMerchantUserTypeWhitelist(MerchantUserTypeWhitelist whitelist);

    int deleteMerchantUserTypeWhitelistByIds(Long[] ids);
}
