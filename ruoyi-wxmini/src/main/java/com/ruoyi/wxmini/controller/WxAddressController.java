package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.wxmini.domain.UserServiceAddress;
import com.ruoyi.wxmini.service.IUserServiceAddressService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "【小程序】服务地址")
@Slf4j
@RestController
@RequestMapping("/wxmini/address")
public class WxAddressController extends BaseController {

    @Resource
    private IUserServiceAddressService userServiceAddressService;

    @ApiOperation("查询当前登录用户的服务地址列表")
    @GetMapping("/list")
    public AjaxResult list() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        List<UserServiceAddress> list = userServiceAddressService.selectAddressListByUserId(userId);
        return AjaxResult.success(list);
    }

    @ApiOperation("新增服务地址")
    @PostMapping
    public AjaxResult add(@RequestBody UserServiceAddress address) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        AjaxResult guard = validateAddress(address);
        if (guard != null) {
            return guard;
        }
        address.setId(SnowflakeIdWorker.nextIdDefault());
        address.setUserId(userId);
        int rows = userServiceAddressService.insertUserServiceAddress(address);
        return rows > 0 ? AjaxResult.success("操作成功", address.getId()) : AjaxResult.error("保存失败");
    }

    @ApiOperation("修改服务地址")
    @PutMapping("/{id}")
    public AjaxResult update(@PathVariable("id") Long id, @RequestBody UserServiceAddress address) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        AjaxResult guard = validateAddress(address);
        if (guard != null) {
            return guard;
        }
        address.setId(id);
        address.setUserId(userId);
        int rows = userServiceAddressService.updateUserServiceAddress(address);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error("地址不存在或无权修改");
    }

    @ApiOperation("设置默认服务地址")
    @PutMapping("/{id}/default")
    public AjaxResult setDefault(@PathVariable("id") Long id) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        int rows = userServiceAddressService.setDefaultAddress(id, userId);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error("地址不存在或无权操作");
    }

    @ApiOperation("删除服务地址")
    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        try {
            int rows = userServiceAddressService.deleteUserServiceAddressByIdAndUserId(id, userId);
            return rows > 0 ? AjaxResult.success() : AjaxResult.error("地址不存在或无权删除");
        } catch (ServiceException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    private AjaxResult validateAddress(UserServiceAddress address) {
        if (address == null) {
            return AjaxResult.error("参数错误");
        }
        address.setContactName(StringUtils.trimToEmpty(address.getContactName()));
        address.setContactPhone(StringUtils.trimToEmpty(address.getContactPhone()));
        address.setRegion(StringUtils.trimToEmpty(address.getRegion()));
        address.setLocation(StringUtils.trimToEmpty(address.getLocation()));
        if (StringUtils.isNotBlank(address.getAddressDetail())) {
            address.setAddressDetail(address.getAddressDetail().trim());
        }
        if (StringUtils.isNotBlank(address.getDoorplate())) {
            address.setDoorplate(address.getDoorplate().trim());
        }
        if (StringUtils.isNotBlank(address.getRemark())) {
            address.setRemark(address.getRemark().trim());
        }
        if (StringUtils.isBlank(address.getContactName())) {
            return AjaxResult.error("请填写联系人");
        }
        if (!address.getContactPhone().matches("^1[3-9]\\d{9}$")) {
            return AjaxResult.error("请填写正确的联系电话");
        }
        if (StringUtils.isBlank(address.getLocation())) {
            return AjaxResult.error("请选择详细地址");
        }
        if (address.getIsDefault() == null) {
            address.setIsDefault(0);
        }
        return null;
    }
}
