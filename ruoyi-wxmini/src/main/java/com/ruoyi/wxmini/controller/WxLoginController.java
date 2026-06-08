package com.ruoyi.wxmini.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.wxmini.bo.WxUserInfo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IWxMiniJwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 微信小程序登录接口
 *
 * @author weijiayu
 * @date 2025/4/25 22:15
 */
@Api(tags = "【小程序】微信登录")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/wxmini")
public class WxLoginController {

    private final WxMaService wxMaService;

    @Resource
    private IUserInfoService userInfoService;
    @Resource
    private IWxMiniJwtService jwtService;

    /**
     * 登陆接口
     */
    @ApiOperation("微信小程序登录（通过 code 换取 token，未注册时可携带手机号 code 注册）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "appid", value = "小程序 AppID", required = true, dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "code", value = "微信登录临时凭证 code", required = true, dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "phoneCode", value = "微信手机号实时验证 code，未注册时必填", dataType = "String", paramType = "query", dataTypeClass = String.class)
    })
    @Anonymous
    @GetMapping("/login")
    public AjaxResult login(String appid, String code, String phoneCode) {
        if (StringUtils.isEmpty(code)) {
            return AjaxResult.error("empty jscode");
        }
        if (!wxMaService.switchover(appid)) {
            return AjaxResult.error(String.format("can not find appid=[%s] config", appid));
        }

        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            UserInfo userInfo = resolveLoginUser(session, phoneCode);
            if (userInfo == null) {
                return AjaxResult.success(buildPhoneRequiredResult(session));
            }
            return AjaxResult.success(buildLoginResult(session, userInfo));
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

    private UserInfo resolveLoginUser(WxMaJscode2SessionResult session, String phoneCode) throws WxErrorException {
        String openId = session.getOpenid();
        UserInfo userInfo = userInfoService.selectUserInfoByOpenId(openId);
        if (userInfo != null) {
            updateUnionIdIfNeeded(userInfo, session.getUnionid());
            return userInfo;
        }

        if (StringUtils.isBlank(phoneCode)) {
            return null;
        }

        UserInfo createdUser = new UserInfo();
        createdUser.setUserId(UUID.randomUUID().toString());
        createdUser.setOpenId(openId);
        createdUser.setUnionId(session.getUnionid());
        createdUser.setPhone(resolvePhone(phoneCode));
        userInfoService.insertUserInfo(createdUser);
        return createdUser;
    }

    private String resolvePhone(String phoneCode) throws WxErrorException {
        WxMaPhoneNumberInfo phoneNumberInfo = wxMaService.getUserService().getPhoneNumber(phoneCode);
        String phone = phoneNumberInfo == null ? null : phoneNumberInfo.getPhoneNumber();
        if (StringUtils.isBlank(phone)) {
            throw new IllegalStateException("empty phone number");
        }
        return phone;
    }

    private void updateUnionIdIfNeeded(UserInfo userInfo, String unionId) {
        if (StringUtils.isEmpty(unionId) || StringUtils.equals(unionId, userInfo.getUnionId())) {
            return;
        }
        userInfo.setUnionId(unionId);
        userInfoService.updateUserInfo(userInfo);
    }

    private WxUserInfo buildLoginResult(WxMaJscode2SessionResult session, UserInfo userInfo) {
        WxUserInfo wxUserInfo = new WxUserInfo();
        wxUserInfo.wapper(session, userInfo);
        wxUserInfo.setApiToken(jwtService.createToken(userInfo.getUserId()));
        return wxUserInfo;
    }

    private WxUserInfo buildPhoneRequiredResult(WxMaJscode2SessionResult session) {
        WxUserInfo wxUserInfo = new WxUserInfo();
        wxUserInfo.setSessionKey(session.getSessionKey());
        wxUserInfo.setOpenId(session.getOpenid());
        wxUserInfo.setNeedPhoneCode(true);
        return wxUserInfo;
    }
}
