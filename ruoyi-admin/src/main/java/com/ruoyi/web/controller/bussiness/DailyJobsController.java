package com.ruoyi.web.controller.bussiness;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfoVo;

/**
 * 兼职日结工作Controller
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
@Api(tags = "兼职日结工作管理")
@RestController
@RequestMapping("/system/jobs")
public class DailyJobsController extends BaseController
{
    @Autowired
    private IDailyJobsService dailyJobsService;

    /**
     * 查询兼职日结工作列表
     */
    @ApiOperation("查询兼职日结工作列表")
    @Anonymous
    @GetMapping("/list")
    public TableDataInfoVo<DailyJobs> list(DailyJobs dailyJobs)
    {
        startPage();
        List<DailyJobs> list = dailyJobsService.selectDailyJobsList(dailyJobs);
        return getDataTable(list);
    }

    /**
     * 导出兼职日结工作列表
     */
    @ApiOperation("导出兼职日结工作列表")
    @PreAuthorize("@ss.hasPermi('system:jobs:export')")
    @Log(title = "兼职日结工作", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DailyJobs dailyJobs)
    {
        List<DailyJobs> list = dailyJobsService.selectDailyJobsList(dailyJobs);
        ExcelUtil<DailyJobs> util = new ExcelUtil<DailyJobs>(DailyJobs.class);
        util.exportExcel(response, list, "兼职日结工作数据");
    }

    /**
     * 获取兼职日结工作详细信息
     */
    @ApiOperation("获取兼职日结工作详细信息")
    @ApiImplicitParam(name = "id", value = "兼职工作ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    @Anonymous
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dailyJobsService.selectDailyJobsById(id));
    }

    /**
     * 新增兼职日结工作
     */
    @ApiOperation("新增兼职日结工作")
    @PreAuthorize("@ss.hasPermi('system:jobs:add')")
    @Log(title = "兼职日结工作", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DailyJobs dailyJobs)
    {
        return toAjax(dailyJobsService.insertDailyJobs(dailyJobs));
    }

    /**
     * 修改兼职日结工作
     */
    @ApiOperation("修改兼职日结工作")
    @PreAuthorize("@ss.hasPermi('system:jobs:edit')")
    @Log(title = "兼职日结工作", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DailyJobs dailyJobs)
    {
        return toAjax(dailyJobsService.updateDailyJobs(dailyJobs));
    }

    /**
     * 删除兼职日结工作
     */
    @ApiOperation("删除兼职日结工作")
    @ApiImplicitParam(name = "ids", value = "兼职工作ID数组", required = true, dataType = "Long[]", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('system:jobs:remove')")
    @Log(title = "兼职日结工作", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dailyJobsService.deleteDailyJobsByIds(ids));
    }
}
