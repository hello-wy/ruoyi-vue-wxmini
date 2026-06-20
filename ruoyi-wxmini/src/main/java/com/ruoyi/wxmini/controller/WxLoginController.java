package com.ruoyi.wxmini.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.ruoyi.wxmini.domain.WxAdminBinding;
import com.ruoyi.wxmini.service.IWxAdminBindingService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.wxmini.util.WxMiniUserContext;

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
    @Resource
    private IWxAdminBindingService wxAdminBindingService;
    @Resource
    private ISysUserService userService;
    @Resource
    private SysPermissionService permissionService;
    @Resource
    private TokenService tokenService;

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

    /**
     * 小程序管理员绑定状态
     */
    @ApiOperation("小程序获取管理员绑定状态")
    @GetMapping("/auth/admin-binding")
    public AjaxResult getAdminBinding() {
        String wxUserId = requireWxUserId();
        WxAdminBinding binding = wxAdminBindingService.selectByWxUserId(wxUserId);
        Map<String, Object> result = new HashMap<>();
        result.put("bound", binding != null && UserConstants.NORMAL.equals(binding.getStatus()));
        if (binding != null) {
            result.put("status", binding.getStatus());
            SysUser sysUser = userService.selectUserById(binding.getSysUserId());
            if (sysUser != null) {
                result.put("sysUserId", sysUser.getUserId());
                result.put("userName", sysUser.getUserName());
                result.put("nickName", sysUser.getNickName());
            }
        }
        return AjaxResult.success(result);
    }

    /**
     * 小程序绑定后台管理员账号
     */
    @ApiOperation("小程序绑定后台管理员账号")
    @PostMapping("/auth/admin-binding")
    public AjaxResult bindAdmin(@RequestBody AdminBindingBody body) {
        String wxUserId = requireWxUserId();
        if (body == null || StringUtils.isBlank(body.getUsername()) || StringUtils.isBlank(body.getPassword())) {
            return AjaxResult.error("请输入管理员账号和密码");
        }

        SysUser sysUser = userService.selectUserByUserName(body.getUsername());
        if (!isValidSysUser(sysUser) || !SecurityUtils.matchesPassword(body.getPassword(), sysUser.getPassword())) {
            return AjaxResult.error("管理员账号或密码错误");
        }

        WxAdminBinding existWx = wxAdminBindingService.selectByWxUserId(wxUserId);
        if (existWx != null && !sysUser.getUserId().equals(existWx.getSysUserId())) {
            return AjaxResult.error("该小程序用户已绑定其他管理员账号");
        }
        WxAdminBinding existSys = wxAdminBindingService.selectBySysUserId(sysUser.getUserId());
        if (existSys != null && !wxUserId.equals(existSys.getWxUserId())) {
            return AjaxResult.error("该管理员账号已绑定其他小程序用户");
        }

        if (existWx == null) {
            WxAdminBinding binding = new WxAdminBinding();
            binding.setWxUserId(wxUserId);
            binding.setSysUserId(sysUser.getUserId());
            binding.setStatus(UserConstants.NORMAL);
            binding.setCreateBy(sysUser.getUserName());
            wxAdminBindingService.insertWxAdminBinding(binding);
        } else if (!UserConstants.NORMAL.equals(existWx.getStatus())) {
            existWx.setStatus(UserConstants.NORMAL);
            existWx.setUpdateBy(sysUser.getUserName());
            wxAdminBindingService.updateWxAdminBinding(existWx);
        }

        return AjaxResult.success("绑定成功", buildAdminTokenResult(sysUser));
    }

    /**
     * 小程序解绑后台管理员账号
     */
    @ApiOperation("小程序解绑后台管理员账号")
    @DeleteMapping("/auth/admin-binding")
    public AjaxResult unbindAdmin() {
        String wxUserId = requireWxUserId();
        WxAdminBinding binding = wxAdminBindingService.selectByWxUserId(wxUserId);
        if (binding == null) {
            return AjaxResult.success("已解绑");
        }
        return wxAdminBindingService.deleteWxAdminBindingById(binding.getId()) > 0 ? AjaxResult.success("解绑成功") : AjaxResult.error("解绑失败");
    }

    /**
     * 小程序换取后台管理 Token 接口
     */
    @ApiOperation("小程序获取管理后台Token")
    @GetMapping("/auth/admin-token")
    public AjaxResult getAdminToken() {
        String wxUserId = requireWxUserId();
        WxAdminBinding binding = wxAdminBindingService.selectByWxUserId(wxUserId);
        if (binding == null || !UserConstants.NORMAL.equals(binding.getStatus())) {
            return AjaxResult.error("该用户未绑定管理员账号或绑定已停用");
        }

        SysUser sysUser = userService.selectUserById(binding.getSysUserId());
        if (!isValidSysUser(sysUser)) {
            return AjaxResult.error("绑定的管理员账号已被停用、删除或不存在");
        }
        return AjaxResult.success("获取成功", buildAdminTokenResult(sysUser));
    }

    private String requireWxUserId() {
        String wxUserId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isEmpty(wxUserId)) {
            throw new IllegalStateException("未登录或 Token 无效");
        }
        return wxUserId;
    }

    private boolean isValidSysUser(SysUser sysUser) {
        return sysUser != null && UserConstants.NORMAL.equals(sysUser.getStatus()) && !"2".equals(sysUser.getDelFlag());
    }

    private Map<String, Object> buildAdminTokenResult(SysUser sysUser) {
        Set<String> permissions = permissionService.getMenuPermission(sysUser);
        LoginUser loginUser = new LoginUser(sysUser.getUserId(), sysUser.getDeptId(), sysUser, permissions);
        String token = tokenService.createToken(loginUser);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("roles", permissionService.getRolePermission(sysUser));
        result.put("permissions", permissions);
        result.put("user", sysUser);
        return result;
    }

    public static class AdminBindingBody {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
