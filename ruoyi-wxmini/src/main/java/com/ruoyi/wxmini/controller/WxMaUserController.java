package com.ruoyi.wxmini.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.wxmini.bo.WxPhoneCodeRequest;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import com.ruoyi.wxmini.vo.WxPhoneInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Api(tags = "【小程序】微信用户信息")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/wxmini/user")
public class WxMaUserController {

    private final WxMaService wxMaService;
    @Resource
    private IUserInfoService userInfoService;

    /**
     * 获取用户信息接口
     */
    @ApiOperation("获取并同步微信用户信息（昵称/头像，需登录）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "appid", value = "小程序 AppID", required = true, dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "sessionKey", value = "会话密钥", required = true, dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "signature", value = "用户信息签名", dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "rawData", value = "原始数据字符串", dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "encryptedData", value = "加密数据", required = true, dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "iv", value = "加密算法初始向量", required = true, dataType = "String", paramType = "query", dataTypeClass = String.class)
    })
    @GetMapping("/info")
    public AjaxResult info(String appid, String sessionKey,
                           String signature, String rawData, String encryptedData, String iv) {
        if (!wxMaService.switchover(appid)) {
            return AjaxResult.error(String.format("can not find appid=[%s] config", appid));
        }

        try {
            // 用户信息校验
//            if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
//                return AjaxResult.error("user check failed");
//            }

            // 解密用户信息
            WxMaUserInfo wxUserInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
            UserInfo userInfo = userInfoService.selectUserInfoByUserId(WxMiniUserContext.getCurrentUserId());
            if (userInfo != null) {
                userInfo.setAvatarUrl(wxUserInfo.getAvatarUrl());
                userInfo.setUserName(wxUserInfo.getNickName());
                userInfoService.updateUserInfo(userInfo);
            }
            return AjaxResult.success(wxUserInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error();
        } finally {
            WxMaConfigHolder.remove();
        }
    }

    /**
     * 获取用户绑定手机号信息
     */
    @ApiOperation("通过微信实时手机号 code 获取并同步用户绑定手机号（需登录）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "body", value = "{\"appid\":\"小程序 AppID\",\"phoneCode\":\"实时手机号 code\"}", required = true, dataType = "String", paramType = "body", dataTypeClass = String.class)
    })
    @PostMapping("/phone")
    public AjaxResult phone(@RequestBody WxPhoneCodeRequest request) {
        if (request == null) {
            return AjaxResult.error("empty request body");
        }
        if (!wxMaService.switchover(request.getAppid())) {
            return AjaxResult.error(String.format("can not find appid=[%s] config", request.getAppid()));
        }

        try {
            UserInfo userInfo = userInfoService.selectUserInfoByUserId(WxMiniUserContext.getCurrentUserId());
            if (userInfo == null) {
                return AjaxResult.error("user not found");
            }
            String phone = resolvePhone(request.getPhoneCode());
            userInfo.setPhone(phone);
            userInfoService.updateUserInfo(userInfo);
            return AjaxResult.success(buildPhoneInfo(phone));
        } catch (IllegalStateException e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error();
        } finally {
            WxMaConfigHolder.remove();
        }
    }

    private String resolvePhone(String phoneCode) throws WxErrorException {
        if (phoneCode == null || phoneCode.trim().isEmpty()) {
            throw new IllegalStateException("empty phoneCode");
        }
        WxMaPhoneNumberInfo phoneNumberInfo = wxMaService.getUserService().getPhoneNumber(phoneCode);
        String phone = phoneNumberInfo == null ? null : phoneNumberInfo.getPhoneNumber();
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalStateException("empty phone number");
        }
        return phone;
    }

    private WxPhoneInfoVO buildPhoneInfo(String phone) {
        WxPhoneInfoVO phoneInfo = new WxPhoneInfoVO();
        phoneInfo.setPhone(phone);
        return phoneInfo;
    }

}
