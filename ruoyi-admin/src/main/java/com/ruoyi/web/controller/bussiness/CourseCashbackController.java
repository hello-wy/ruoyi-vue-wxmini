package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.CourseCashbackConfig;
import com.ruoyi.system.domain.CourseCashbackLedger;
import com.ruoyi.system.domain.bo.CourseCashbackDeductionBo;
import com.ruoyi.system.domain.bo.CourseFinanceQueryBo;
import com.ruoyi.system.domain.vo.CourseFinanceSummaryVo;
import com.ruoyi.system.service.ICourseCashbackService;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "课程财务")
@RestController
@RequestMapping("/system/course-finance")
public class CourseCashbackController extends BaseController {
    @Resource private ICourseCashbackService courseCashbackService;
    @PreAuthorize("@ss.hasPermi('system:course-finance:list')") @GetMapping("/summary")
    public AjaxResult summary() { return success(courseCashbackService.selectSummary()); }
    @PreAuthorize("@ss.hasPermi('system:course-finance:list')") @GetMapping("/payments/list")
    public TableDataInfoVo<CourseCashbackLedger> payments(CourseFinanceQueryBo query) { startPage(); return getDataTable(courseCashbackService.selectLedgerList(query)); }
    @PreAuthorize("@ss.hasPermi('system:course-finance:list')") @GetMapping("/payments/{orderNo}")
    public AjaxResult payment(@PathVariable String orderNo) { Map<String,Object> data=new HashMap<>(); data.put("payment",courseCashbackService.selectPayment(orderNo)); data.put("deductions",courseCashbackService.selectDeductionHistory(orderNo)); return success(data); }
    @PreAuthorize("@ss.hasPermi('system:course-finance:self')") @GetMapping("/cashback/my-summary")
    public AjaxResult mySummary() { CourseFinanceSummaryVo s=courseCashbackService.selectMySummary(); Map<String,Object> d=new HashMap<>(); d.put("earned",s.getGrossCashbackAmount());d.put("reversed",s.getReversedCashbackAmount());d.put("deducted",s.getDeductedCashbackAmount());d.put("available",s.getAvailableCashbackAmount());d.put("grossCashbackAmount",s.getGrossCashbackAmount());d.put("reversedCashbackAmount",s.getReversedCashbackAmount());d.put("deductedCashbackAmount",s.getDeductedCashbackAmount());d.put("availableCashbackAmount",s.getAvailableCashbackAmount());return success(d); }
    @PreAuthorize("@ss.hasPermi('system:course-finance:self')") @GetMapping("/cashback/my-records")
    public TableDataInfoVo<CourseCashbackLedger> myRecords() { startPage(); return getDataTable(courseCashbackService.selectMyLedgerList()); }
    @PreAuthorize("@ss.hasPermi('system:course-finance:config')") @GetMapping("/config") public AjaxResult config(){return success(courseCashbackService.selectConfig());}
    @PreAuthorize("@ss.hasPermi('system:course-finance:config')") @PutMapping("/config") public AjaxResult config(@RequestBody CourseCashbackConfig c){courseCashbackService.saveConfig(c);return success();}
    @PreAuthorize("@ss.hasPermi('system:course-finance:deduct')") @PostMapping("/deductions") public AjaxResult deduct(@RequestBody @Validated CourseCashbackDeductionBo d){courseCashbackService.deduct(d);return success();}
}
