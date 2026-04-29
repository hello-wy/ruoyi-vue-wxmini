package com.ruoyi.wxmini.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.wxmini.domain.UserServiceAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserServiceAddressMapper extends BaseMapper<UserServiceAddress> {

    List<UserServiceAddress> selectUserServiceAddressListByUserId(@Param("userId") String userId);

    UserServiceAddress selectUserServiceAddressByIdAndUserId(@Param("id") Long id, @Param("userId") String userId);

    UserServiceAddress selectDefaultAddressByUserId(@Param("userId") String userId);

    int insertUserServiceAddress(UserServiceAddress address);

    int updateUserServiceAddress(UserServiceAddress address);

    int clearDefaultByUserId(@Param("userId") String userId);

    int setDefaultByIdAndUserId(@Param("id") Long id, @Param("userId") String userId);

    int deleteUserServiceAddressByIdAndUserId(@Param("id") Long id, @Param("userId") String userId);
}
