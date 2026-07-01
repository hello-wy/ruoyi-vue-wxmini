package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.wxmini.domain.vo.UserReferralVo;
import com.ruoyi.wxmini.service.IUserReferralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
