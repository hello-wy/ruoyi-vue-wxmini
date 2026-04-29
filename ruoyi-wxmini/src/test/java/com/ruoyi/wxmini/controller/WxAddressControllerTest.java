package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.wxmini.domain.UserServiceAddress;
import com.ruoyi.wxmini.service.IUserServiceAddressService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxAddressControllerTest {

    private static final String USER_ID = "wx-user-1";

    @Mock
    private IUserServiceAddressService userServiceAddressService;

    @InjectMocks
    private WxAddressController controller;

    @AfterEach
    void clearContext() {
        WxMiniUserContext.clear();
    }

    @Test
    void listShouldRequireLogin() {
        AjaxResult result = controller.list();

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("请先登录", result.get(AjaxResult.MSG_TAG));
    }

    @Test
    void listShouldReturnCurrentUserAddresses() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(userServiceAddressService.selectAddressListByUserId(USER_ID))
                .thenReturn(Collections.singletonList(new UserServiceAddress()));

        AjaxResult result = controller.list();

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        verify(userServiceAddressService).selectAddressListByUserId(USER_ID);
    }

    @Test
    void addShouldAllowBlankRegionWhenLocationPresent() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        UserServiceAddress request = buildValidAddress();
        request.setRegion("");
        when(userServiceAddressService.insertUserServiceAddress(any(UserServiceAddress.class))).thenReturn(1);

        AjaxResult result = controller.add(request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        ArgumentCaptor<UserServiceAddress> captor = ArgumentCaptor.forClass(UserServiceAddress.class);
        verify(userServiceAddressService).insertUserServiceAddress(captor.capture());
        UserServiceAddress saved = captor.getValue();
        assertEquals(USER_ID, saved.getUserId());
        assertEquals("", saved.getRegion());
        assertEquals("鼓楼区龙江新城市广场", saved.getLocation());
        assertEquals("张三", saved.getContactName());
        assertEquals("13800138000", saved.getContactPhone());
    }

    @Test
    void addShouldRejectBlankLocation() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        UserServiceAddress request = buildValidAddress();
        request.setLocation("   ");

        AjaxResult result = controller.add(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("请选择详细地址", result.get(AjaxResult.MSG_TAG));
        verify(userServiceAddressService, never()).insertUserServiceAddress(any(UserServiceAddress.class));
    }

    @Test
    void updateShouldBindPathIdAndTrimFields() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        UserServiceAddress request = buildValidAddress();
        request.setContactName(" 张三 ");
        request.setContactPhone("13800138000 ");
        request.setLocation(" 鼓楼区龙江新城市广场 ");
        request.setAddressDetail(" 2栋 ");
        request.setDoorplate(" 1201 ");
        request.setRemark(" 周末上门 ");
        request.setIsDefault(null);
        when(userServiceAddressService.updateUserServiceAddress(any(UserServiceAddress.class))).thenReturn(1);

        AjaxResult result = controller.update(9L, request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        ArgumentCaptor<UserServiceAddress> captor = ArgumentCaptor.forClass(UserServiceAddress.class);
        verify(userServiceAddressService).updateUserServiceAddress(captor.capture());
        UserServiceAddress saved = captor.getValue();
        assertEquals(9L, saved.getId());
        assertEquals(USER_ID, saved.getUserId());
        assertEquals("张三", saved.getContactName());
        assertEquals("13800138000", saved.getContactPhone());
        assertEquals("鼓楼区龙江新城市广场", saved.getLocation());
        assertEquals("2栋", saved.getAddressDetail());
        assertEquals("1201", saved.getDoorplate());
        assertEquals("周末上门", saved.getRemark());
        assertEquals(0, saved.getIsDefault());
    }

    @Test
    void setDefaultShouldReturnErrorWhenAddressNotOwned() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(userServiceAddressService.setDefaultAddress(9L, USER_ID)).thenReturn(0);

        AjaxResult result = controller.setDefault(9L);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("地址不存在或无权操作", result.get(AjaxResult.MSG_TAG));
    }

    @Test
    void deleteShouldReturnServiceExceptionMessage() {
        WxMiniUserContext.setCurrentUserId(USER_ID);
        when(userServiceAddressService.deleteUserServiceAddressByIdAndUserId(9L, USER_ID))
                .thenThrow(new ServiceException("该地址已被需求使用，无法删除"));

        AjaxResult result = controller.delete(9L);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("该地址已被需求使用，无法删除", result.get(AjaxResult.MSG_TAG));
    }

    private UserServiceAddress buildValidAddress() {
        UserServiceAddress address = new UserServiceAddress();
        address.setContactName("张三");
        address.setContactPhone("13800138000");
        address.setRegion("江苏省 南京市 鼓楼区");
        address.setLocation("鼓楼区龙江新城市广场");
        address.setAddressDetail("2栋");
        address.setDoorplate("1201");
        address.setRemark("周末上门");
        address.setIsDefault(1);
        return address;
    }
}
