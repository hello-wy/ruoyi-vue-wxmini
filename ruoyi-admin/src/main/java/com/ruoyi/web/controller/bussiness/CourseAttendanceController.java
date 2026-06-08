package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.bo.CourseScanSignInBo;
import com.ruoyi.system.service.ICoursePayOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "课程签到管理")
@RestController
@RequestMapping("/system/course-attendance")
public class CourseAttendanceController extends BaseController {
    @Autowired
    private ICoursePayOrderService coursePayOrderService;

    @ApiOperation("管理员扫码课程签到")
    @PreAuthorize("@ss.hasPermi('system:course-attendance:edit')")
    @PostMapping("/courses/{courseId}/scan-sign-in")
    public AjaxResult scanSignIn(@PathVariable("courseId") Long courseId,
                                 @RequestBody @Validated CourseScanSignInBo bo) {
        try {
            return success(coursePayOrderService.scanSignInVo(courseId, bo.getUserId()));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }
}
