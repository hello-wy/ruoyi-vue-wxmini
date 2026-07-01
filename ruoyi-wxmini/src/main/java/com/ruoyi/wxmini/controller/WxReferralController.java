package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.wxmini.domain.UserReferral;
import com.ruoyi.wxmini.domain.vo.UserReferralVo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IUserReferralService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信小程序分销邀请关系Controller
 *
 * @author ruoyi
 * @date 2026-07-01
 */
@Api(tags = "【小程序】分销邀请管理")
@RestController
@RequestMapping("/wxmini/referral")
public class WxReferralController extends BaseController {

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserReferralService userReferralService;

    @ApiOperation("获取我的邀请码及邀请统计")
    @GetMapping("/my-code")
    public AjaxResult myCode() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }

        String inviteCode = userInfoService.getOrCreateInviteCode(userId);
        int inviteCount = userReferralService.getMyInviteeCount(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("inviteCode", inviteCode);
        data.put("inviteCount", inviteCount);

        return success(data);
    }

    @ApiOperation("获取我邀请的人列表（下级）")
    @GetMapping("/my-invitees")
    public TableDataInfoVo<UserReferralVo> myInvitees() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return getDataTable(null); // or empty list
        }
        startPage();
        List<UserReferralVo> list = userReferralService.getMyInvitees(userId);
        return getDataTable(list);
    }

    @ApiOperation("获取我的邀请人信息")
    @GetMapping("/my-inviter")
    public AjaxResult myInviter() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        UserReferral inviter = userReferralService.getMyInviter(userId);
        return success(inviter);
    }
}
