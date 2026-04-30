package com.ruoyi.wxmini.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.mapper.ParentsMapper;
import com.ruoyi.wxmini.domain.UserServiceAddress;
import com.ruoyi.wxmini.mapper.UserServiceAddressMapper;
import com.ruoyi.wxmini.service.IUserServiceAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceAddressServiceImpl implements IUserServiceAddressService {

    @Resource
    private UserServiceAddressMapper userServiceAddressMapper;

    @Resource
    private ParentsMapper parentsMapper;

    @Override
    public List<UserServiceAddress> selectAddressListByUserId(String userId) {
        return userServiceAddressMapper.selectUserServiceAddressListByUserId(userId);
    }

    @Override
    public UserServiceAddress selectAddressByIdAndUserId(Long id, String userId) {
        return userServiceAddressMapper.selectUserServiceAddressByIdAndUserId(id, userId);
    }

    @Override
    public UserServiceAddress selectDefaultAddressByUserId(String userId) {
        return userServiceAddressMapper.selectDefaultAddressByUserId(userId);
    }

    @Override
    public int insertUserServiceAddress(UserServiceAddress address) {
        address.setCreateTime(DateUtils.getNowDate());
        address.setUpdateTime(DateUtils.getNowDate());
        long count = userServiceAddressMapper.selectCount(new LambdaQueryWrapper<UserServiceAddress>()
                .eq(UserServiceAddress::getUserId, address.getUserId()));
        if (count == 0) {
            address.setIsDefault(1);
        }
        if (Integer.valueOf(1).equals(address.getIsDefault())) {
            userServiceAddressMapper.clearDefaultByUserId(address.getUserId());
        }
        return userServiceAddressMapper.insertUserServiceAddress(address);
    }

    @Override
    public int updateUserServiceAddress(UserServiceAddress address) {
        UserServiceAddress existing = userServiceAddressMapper.selectUserServiceAddressByIdAndUserId(address.getId(), address.getUserId());
        if (existing == null) {
            return 0;
        }
        address.setCreateTime(existing.getCreateTime());
        address.setUpdateTime(DateUtils.getNowDate());
        if (address.getIsDefault() == null) {
            address.setIsDefault(existing.getIsDefault());
        }
        if (Integer.valueOf(1).equals(address.getIsDefault())) {
            userServiceAddressMapper.clearDefaultByUserId(address.getUserId());
        }
        return userServiceAddressMapper.updateUserServiceAddress(address);
    }

    @Override
    public int setDefaultAddress(Long id, String userId) {
        UserServiceAddress existing = userServiceAddressMapper.selectUserServiceAddressByIdAndUserId(id, userId);
        if (existing == null) {
            return 0;
        }
        userServiceAddressMapper.clearDefaultByUserId(userId);
        return userServiceAddressMapper.setDefaultByIdAndUserId(id, userId);
    }

    @Override
    public int deleteUserServiceAddressByIdAndUserId(Long id, String userId) {
//        long refCount = parentsMapper.selectCount(new LambdaQueryWrapper<Parents>()
//                .eq(Parents::getAddressId, id)
//                .eq(Parents::getStatus, 0L));
//        if (refCount > 0) {
//            throw new ServiceException("该地址已关联家教需求，暂不能删除");
//        }
        return userServiceAddressMapper.deleteUserServiceAddressByIdAndUserId(id, userId);
    }
}
