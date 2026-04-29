package com.ruoyi.wxmini.service;

import com.ruoyi.wxmini.domain.UserServiceAddress;

import java.util.List;

public interface IUserServiceAddressService {

    List<UserServiceAddress> selectAddressListByUserId(String userId);

    UserServiceAddress selectAddressByIdAndUserId(Long id, String userId);

    UserServiceAddress selectDefaultAddressByUserId(String userId);

    int insertUserServiceAddress(UserServiceAddress address);

    int updateUserServiceAddress(UserServiceAddress address);

    int setDefaultAddress(Long id, String userId);

    int deleteUserServiceAddressByIdAndUserId(Long id, String userId);
}
