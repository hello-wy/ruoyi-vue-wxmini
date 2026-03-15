package com.ruoyi.wxmini.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.service.IDailyJobsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 微信小程序 - 兼职日结工作接口
 */
@Api(tags = "【小程序】兼职日结工作")
@RestController()
public class WxDailyJobController extends BaseController {

    @Autowired
    private IDailyJobsService dailyJobsService;

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

}
