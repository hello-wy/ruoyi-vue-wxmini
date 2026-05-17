package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.wxmini.bo.WxJobSignSubmitBo;
import com.ruoyi.wxmini.bo.WxMerchantJobStatusUpdateBo;
import com.ruoyi.wxmini.service.IWxJobScheduleService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

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

    @ApiOperation("提交兼职签到图片")
    @PostMapping("/{jobId}/sign-in")
    public AjaxResult submitSignIn(@PathVariable("jobId") Long jobId,
                                   @RequestBody(required = false) WxJobSignSubmitBo bo) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        wxJobScheduleService.submitJobSignImage(userId, jobId, bo == null ? null : bo.getSignImageUrl());
        return success();
    }

    @ApiOperation("提交兼职签到图片")
    @PostMapping("/orders/{orderNo}/sign-image")
    public AjaxResult submitSignImage(@PathVariable("orderNo") String orderNo,
                                      @RequestBody Map<String, String> body) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        String signImageUrl = body == null ? null : body.get("signImageUrl");
        if (StringUtils.isBlank(signImageUrl)) {
            return error("签到图片不能为空");
        }
        return success(wxJobScheduleService.submitSignImage(userId, orderNo, signImageUrl));
    }

    @ApiOperation("修改商家岗位状态")
    @PostMapping("/{jobId}/status")
    public AjaxResult updateJobStatus(@PathVariable("jobId") Long jobId,
                                      @RequestBody WxMerchantJobStatusUpdateBo bo) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        wxJobScheduleService.updateMerchantJobStatus(userId, jobId, bo == null ? null : bo.getStatus());
        return success();
    }
}
