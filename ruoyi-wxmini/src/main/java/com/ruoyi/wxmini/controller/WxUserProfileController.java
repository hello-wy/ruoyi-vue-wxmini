package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.wxmini.bo.WxUserProfileUpdateBo;
import com.ruoyi.wxmini.bo.WxUserTypeUpdateBo;
import com.ruoyi.wxmini.domain.vo.WxUserProfileVo;
import com.ruoyi.wxmini.service.IWxUserProfileService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "【小程序】个人资料")
@RestController
@RequestMapping("/wxmini/profile")
public class WxUserProfileController extends BaseController {

    @Resource
    private IWxUserProfileService wxUserProfileService;

    @ApiOperation("获取当前登录用户个人资料")
    @GetMapping("/detail")
    public AjaxResult detail() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        WxUserProfileVo profileVo = wxUserProfileService.getCurrentUserProfile(userId);
        if (profileVo == null) {
            return error("用户不存在");
        }
        return success(profileVo);
    }

    @ApiOperation("编辑当前登录用户个人资料")
    @PutMapping
    public AjaxResult update(@RequestBody WxUserProfileUpdateBo bo) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        int rows = wxUserProfileService.updateCurrentUserProfile(userId, bo);
        return rows > 0 ? success() : error("保存失败");
    }

    @ApiOperation("首次设置当前登录用户身份")
    @PostMapping("/user-type/init")
    public AjaxResult initUserType(@RequestBody WxUserTypeUpdateBo bo) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        if (bo == null || bo.getUserType() == null) {
            return error("请选择用户身份");
        }
        try {
            int rows = wxUserProfileService.initCurrentUserType(userId, bo.getUserType());
            return rows > 0 ? success() : error("身份初始化失败");
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("切换当前登录用户身份")
    @PutMapping("/user-type")
    public AjaxResult switchUserType(@RequestBody WxUserTypeUpdateBo bo) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        if (bo == null || bo.getUserType() == null) {
            return error("请选择用户身份");
        }
        try {
            int rows = wxUserProfileService.switchCurrentUserType(userId, bo.getUserType());
            return rows > 0 ? success() : error("身份切换失败");
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }
}
