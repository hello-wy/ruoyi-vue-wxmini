package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TutoringBinding;
import com.ruoyi.system.domain.TutoringPayrollItem;
import com.ruoyi.system.domain.TutoringSchedule;
import com.ruoyi.system.service.ITutoringAdminService;
import com.ruoyi.web.controller.bussiness.bo.TutoringPayrollBatchPayBo;
import com.ruoyi.web.controller.bussiness.bo.TutoringScheduleAuditBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "家教履约后台")
@RestController
@RequestMapping("/system/tutoring-admin")
public class TutoringAdminController extends BaseController {

    @Resource
    private ITutoringAdminService tutoringAdminService;

    @ApiOperation("绑定家长需求与教员")
    @PreAuthorize("@ss.hasPermi('system:parents:edit')")
    @Log(title = "家教绑定", businessType = BusinessType.INSERT)
    @PostMapping("/bindings")
    public AjaxResult bindTutor(@RequestParam("parentId") Long parentId, @RequestParam("tutorId") Long tutorId) {
        return AjaxResult.success(tutoringAdminService.bindTutor(parentId, tutorId, getUsername()));
    }

    @ApiOperation("家教绑定列表")
    @PreAuthorize("@ss.hasPermi('system:parents:list')")
    @GetMapping("/bindings/list")
    public TableDataInfoVo<TutoringBinding> listBindings(TutoringBinding query) {
        startPage();
        List<TutoringBinding> list = tutoringAdminService.listBindings(query);
        return getDataTable(list);
    }

    @ApiOperation("家教课表审核列表")
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/schedules/list")
    public TableDataInfoVo<TutoringSchedule> listSchedules(TutoringSchedule query) {
        startPage();
        List<TutoringSchedule> list = tutoringAdminService.listSchedules(query);
        return getDataTable(list);
    }

    @ApiOperation("审核家教课表为待结算")
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "家教课表审核", businessType = BusinessType.UPDATE)
    @PostMapping("/schedules/{id}/audit")
    public AjaxResult auditSchedule(@PathVariable("id") Long id, @RequestBody(required = false) TutoringScheduleAuditBo bo) {
        tutoringAdminService.auditSchedule(id, bo == null ? null : bo.getTargetStatus(), bo == null ? null : bo.getRemark(), getUsername());
        return success();
    }

    @ApiOperation("家教结算单列表")
    @PreAuthorize("@ss.hasPermi('system:wallet:withdrawRecords')")
    @GetMapping("/payroll/list")
    public TableDataInfoVo<TutoringPayrollItem> listPayrollItems(TutoringPayrollItem query) {
        startPage();
        List<TutoringPayrollItem> list = tutoringAdminService.listPayrollItems(query);
        return getDataTable(list);
    }

    @ApiOperation("批量发放家教课酬")
    @PreAuthorize("@ss.hasPermi('system:wallet:withdraw')")
    @Log(title = "家教课酬发放", businessType = BusinessType.UPDATE)
    @PostMapping("/payroll/batch-pay")
    public AjaxResult batchPay(@RequestBody TutoringPayrollBatchPayBo bo) {
        tutoringAdminService.batchPay(bo == null ? null : bo.getItemIds(), getUsername());
        return success();
    }
}
