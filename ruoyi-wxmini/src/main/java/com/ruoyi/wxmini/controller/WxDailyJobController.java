package com.ruoyi.wxmini.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信小程序 - 兼职日结工作接口
 */
@Api(tags = "【小程序】兼职日结工作")
@RestController()
@RequestMapping("/wxmini/jobs")
public class WxDailyJobController extends BaseController {

    private static final Integer USER_TYPE_MERCHANT = 2;

    @Autowired
    private IDailyJobsService dailyJobsService;

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 查询兼职日结工作列表
     */
    @ApiOperation(value = "查询兼职日结工作列表（公开，无需登录）")
    @GetMapping("/list")
    @Anonymous
    public TableDataInfoVo<DailyJobs> list(DailyJobs dailyJobs)
    {
        startPage();
        List<DailyJobs> list = dailyJobsService.selectDailyJobsList(dailyJobs);
        return getDataTable(list);
    }

    @ApiOperation(value = "获取当前商家发布招聘默认信息")
    @GetMapping("/mine/defaults")
    public AjaxResult defaults() {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return AjaxResult.error("用户不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("userType", userInfo.getUserType());
        data.put("contacts", StringUtils.defaultIfBlank(userInfo.getUserName(), ""));
        data.put("phone", StringUtils.defaultIfBlank(userInfo.getPhone(), ""));
        return AjaxResult.success(data);
    }

    @ApiOperation(value = "商家发布招聘")
    @PostMapping
    public AjaxResult create(@RequestBody DailyJobs dailyJobs) {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return AjaxResult.error("用户不存在");
        }
        if (!USER_TYPE_MERCHANT.equals(userInfo.getUserType())) {
            return AjaxResult.error("仅商家可以发布招聘");
        }
        String errorMessage = validateCreateRequest(dailyJobs);
        if (errorMessage != null) {
            return AjaxResult.error(errorMessage);
        }
        dailyJobs.setId(SnowflakeIdWorker.nextIdDefault());
        dailyJobs.setPublisherUid(userInfo.getId());
        if (dailyJobs.getStatus() == null) {
            dailyJobs.setStatus(0L);
        }
        if (dailyJobs.getSignupLimit() == null) {
            dailyJobs.setSignupLimit(1);
        }
        int rows = dailyJobsService.insertDailyJobs(dailyJobs);
        return rows > 0 ? AjaxResult.success("操作成功", dailyJobs.getId()) : AjaxResult.error("发布失败");
    }

    private UserInfo getCurrentUserInfo() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        return userInfoService.selectUserInfoByUserId(userId);
    }

    private String validateCreateRequest(DailyJobs dailyJobs) {
        if (dailyJobs == null) {
            return "请求参数不能为空";
        }
        if (StringUtils.isBlank(dailyJobs.getTitle())) {
            return "请填写工作标题";
        }
        if (dailyJobs.getCategory() == null) {
            return "请选择岗位分类";
        }
        if (dailyJobs.getSalaryDay() == null) {
            return "请填写日结薪资";
        }
        if (dailyJobs.getWorkDate() == null) {
            return "请选择工作日期";
        }
        if (StringUtils.isBlank(dailyJobs.getWorkTime())) {
            return "请选择工作时段";
        }
        if (StringUtils.isBlank(dailyJobs.getLocation())) {
            return "请填写工作地址";
        }
        if (StringUtils.isBlank(dailyJobs.getContacts())) {
            return "请填写联系人姓名";
        }
        if (StringUtils.isBlank(dailyJobs.getPhone())) {
            return "请填写联系电话";
        }
        if (StringUtils.isBlank(dailyJobs.getDescription())) {
            return "请填写工作要求描述";
        }
        if (dailyJobs.getSignupLimit() == null || dailyJobs.getSignupLimit() <= 0) {
            return "请填写正确的报名人数";
        }
        return null;
    }
}
