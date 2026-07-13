package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.domain.bo.CourseScanSignInBo;
import com.ruoyi.system.domain.vo.CourseScanSignInVo;
import com.ruoyi.system.service.ICoursePayOrderService;
import com.ruoyi.wxmini.service.IWxRefundService;
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
    private static final String SCAN_SIGN_IN_REFUND_REASON = "管理员扫码签到后自动退款";

    @Autowired
    private ICoursePayOrderService coursePayOrderService;
    @Autowired
    private IWxRefundService wxRefundService;

    @ApiOperation("管理员扫码课程签到并原路退款")
    @PreAuthorize("@ss.hasPermi('system:course-attendance:edit')")
    @PostMapping("/courses/{courseId}/scan-sign-in")
    public AjaxResult scanSignIn(@PathVariable("courseId") Long courseId,
                                 @RequestBody @Validated CourseScanSignInBo bo) {
        try {
            CourseScanSignInVo result = coursePayOrderService.scanSignInVo(courseId, bo.getUserId());
            wxRefundService.refundCourseOrder(result.getOrderNo(), SCAN_SIGN_IN_REFUND_REASON);
            updateRefundResult(result);
            return AjaxResult.success("签到成功，已原路退款", result);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    private void updateRefundResult(CourseScanSignInVo result) {
        CoursePayOrder order = coursePayOrderService.selectCoursePayOrderByOrderNo(result.getOrderNo());
        result.setStatus(order.getStatus());
        result.setRefundTime(order.getRefundTime());
    }
}
