package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.CourseDistributionCommissionConfig;
import com.ruoyi.system.domain.bo.CourseDistributionManualRecordBo;
import com.ruoyi.system.service.ICourseDistributionCommissionService;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Api(tags = "课程分销返现")
@RestController
@RequestMapping("/system/course-distribution-commission")
public class CourseDistributionCommissionController extends BaseController {
    @Resource private ICourseDistributionCommissionService commissionService;

    @PreAuthorize("@ss.hasPermi('system:course-distribution-commission:list')")
    @GetMapping("/summary")
    public AjaxResult summary() { return success(commissionService.selectSummary()); }

    @PreAuthorize("@ss.hasPermi('system:course-distribution-commission:list')")
    @GetMapping("/records")
    public TableDataInfoVo<Map<String, Object>> records() {
        startPage();
        return getDataTable(commissionService.selectRecords());
    }

    @PreAuthorize("@ss.hasPermi('system:course-distribution-commission:config')")
    @GetMapping("/config")
    public AjaxResult config() { return success(commissionService.selectConfig()); }

    @PreAuthorize("@ss.hasPermi('system:course-distribution-commission:config')")
    @PutMapping("/config")
    public AjaxResult config(@RequestBody CourseDistributionCommissionConfig config) {
        commissionService.saveConfig(config);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('system:course-distribution-commission:manual')")
    @PostMapping("/manual-records")
    public AjaxResult manualRecord(@RequestBody CourseDistributionManualRecordBo record) {
        commissionService.createManualRecord(record);
        return success();
    }
}
