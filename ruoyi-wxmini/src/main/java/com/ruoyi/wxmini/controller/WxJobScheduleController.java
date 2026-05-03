package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.wxmini.service.IWxJobScheduleService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "【小程序】兼职安排与报名用户")
@RestController
@RequestMapping("/wxmini/jobs")
public class WxJobScheduleController extends BaseController {

    @Resource
    private IWxJobScheduleService wxJobScheduleService;

    @ApiOperation("获取当前登录兼职用户的工作安排")
    @GetMapping("/schedules/my")
    public AjaxResult mySchedules() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        return success(wxJobScheduleService.listMySchedules(userId));
    }

    @ApiOperation("获取当前商家发布的兼职日结岗位")
    @GetMapping("/mine/published")
    public AjaxResult myPublishedJobs() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        return success(wxJobScheduleService.listMerchantJobs(userId));
    }

    @ApiOperation("获取岗位已报名用户池")
    @GetMapping("/{jobId}/signup-users")
    public AjaxResult signupUsers(@PathVariable("jobId") Long jobId,
                                  @RequestParam(value = "keyword", required = false) String keyword) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        return success(wxJobScheduleService.listSignupUsers(userId, jobId, keyword));
    }
}
