package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.web.controller.bussiness.bo.ReferralManualBindBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.domain.vo.UserReferralVo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IUserReferralService;
import com.ruoyi.wxmini.service.IUserReferralService.ReferralBindResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 邀请关系管理Controller
 *
 * @author ruoyi
 * @date 2026-07-01
 */
@Api(tags = "邀请关系管理")
@RestController
@RequestMapping("/system/referral")
public class UserReferralController extends BaseController {

    @Autowired
    private IUserReferralService userReferralService;

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 查询邀请关系列表
     */
    @ApiOperation("查询邀请关系列表")
    @PreAuthorize("@ss.hasPermi('system:referral:list')")
    @GetMapping("/list")
    public TableDataInfoVo<UserReferralVo> list(UserReferralVo userReferralVo) {
        startPage();
        List<UserReferralVo> list = userReferralService.selectUserReferralVoList(userReferralVo);
        return getDataTable(list);
    }

    @ApiOperation("按手机号查询邀请绑定用户")
    @PreAuthorize("@ss.hasPermi('system:referral:list')")
    @GetMapping("/users")
    public AjaxResult users(@RequestParam String phone) {
        if (!isPhone(phone)) {
            return error("请输入正确的11位手机号");
        }
        List<UserInfo> users = findUsersByPhone(phone);
        if (users.isEmpty()) {
            return error("未找到该手机号对应的用户");
        }
        if (users.size() > 1) {
            return error("该手机号对应多个用户，请联系管理员处理");
        }
        UserInfo user = users.get(0);
        return success(new ReferralUserVo(user));
    }

    @ApiOperation("手动绑定邀请关系")
    @PreAuthorize("@ss.hasPermi('system:referral:bind')")
    @PostMapping("/bind")
    public AjaxResult bind(@RequestBody ReferralManualBindBo bo) {
        if (bo == null || !isPhone(bo.getInviterPhone()) || !isPhone(bo.getInviteePhone())) {
            return error("请输入正确的11位手机号");
        }
        List<UserInfo> inviters = findUsersByPhone(bo.getInviterPhone());
        if (inviters.isEmpty()) {
            return error("未找到邀请人手机号对应的用户");
        }
        if (inviters.size() > 1) {
            return error("邀请人手机号对应多个用户，请联系管理员处理");
        }
        List<UserInfo> invitees = findUsersByPhone(bo.getInviteePhone());
        if (invitees.isEmpty()) {
            return error("未找到被邀请人手机号对应的用户");
        }
        if (invitees.size() > 1) {
            return error("被邀请人手机号对应多个用户，请联系管理员处理");
        }
        UserInfo inviter = inviters.get(0);
        UserInfo invitee = invitees.get(0);
        if (inviter.getUserId().equals(invitee.getUserId())) {
            return error("邀请人和被邀请人不能是同一用户");
        }
        ReferralBindResult result = userReferralService.bindReferralWithResult(inviter.getInviteCode(), invitee.getUserId());
        switch (result) {
            case SUCCESS:
                return success();
            case ALREADY_BOUND:
                return error("被邀请人已绑定邀请关系");
            case INVALID_INVITE_CODE:
                return error("邀请人邀请码无效");
            case SELF_INVITE:
                return error("邀请人和被邀请人不能是同一用户");
            default:
                return error("邀请关系绑定失败");
        }
    }

    @ApiOperation("审核并发放邀请新人奖金")
    @PreAuthorize("@ss.hasPermi('system:referral:reward')")
    @PostMapping("/{id}/reward")
    public AjaxResult reward(@PathVariable Long id) {
        userReferralService.rewardReferral(id, SecurityUtils.getUserId());
        return success();
    }

    private List<UserInfo> findUsersByPhone(String phone) {
        if (!isPhone(phone)) {
            return Collections.emptyList();
        }
        UserInfo query = new UserInfo();
        query.setPhone(phone.trim());
        return userInfoService.selectUserInfoList(query);
    }

    private boolean isPhone(String phone) {
        return phone != null && phone.trim().matches("^1\\d{10}$");
    }

    private static class ReferralUserVo {
        private final String userId;
        private final String phone;
        private final String realName;
        private final String userName;

        private ReferralUserVo(UserInfo user) {
            this.userId = user.getUserId();
            this.phone = user.getPhone();
            this.realName = user.getRealName();
            this.userName = user.getUserName();
        }

        public String getUserId() { return userId; }
        public String getPhone() { return phone; }
        public String getRealName() { return realName; }
        public String getUserName() { return userName; }
    }
}
