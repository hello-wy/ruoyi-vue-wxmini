package com.ruoyi.wxmini.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.service.IDailyJobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class WxDailyJobController extends BaseController {

    @Autowired
    private IDailyJobsService dailyJobsService;

    /**
     * 查询兼职日结工作列表
     */
    @GetMapping("/list")
    @Anonymous
    public TableDataInfo list(DailyJobs dailyJobs)
    {
        startPage();
        List<DailyJobs> list = dailyJobsService.selectDailyJobsList(dailyJobs);
        return getDataTable(list);
    }

}
