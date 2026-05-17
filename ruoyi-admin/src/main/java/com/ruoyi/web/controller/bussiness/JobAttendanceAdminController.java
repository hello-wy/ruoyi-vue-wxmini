package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IJobAttendanceAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/job-attendance")
public class JobAttendanceAdminController extends BaseController {

    @Autowired
    private IJobAttendanceAdminService jobAttendanceAdminService;

    @GetMapping("/orders/{orderNo}")
    public AjaxResult getOrder(@PathVariable("orderNo") String orderNo) {
        return success(jobAttendanceAdminService.getOrderAttendance(orderNo));
    }

    @PostMapping("/orders/{orderNo}/sign-in")
    public AjaxResult signIn(@PathVariable("orderNo") String orderNo) {
        return success(jobAttendanceAdminService.confirmAttendance(orderNo, null));
    }
}
