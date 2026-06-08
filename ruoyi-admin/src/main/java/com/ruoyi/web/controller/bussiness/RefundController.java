package com.ruoyi.web.controller.bussiness;

import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.JobSignupOrder;
import com.ruoyi.system.domain.vo.CourseRefundOrderVo;
import com.ruoyi.system.domain.vo.JobRefundOrderVo;
import com.ruoyi.system.domain.vo.SalonRefundOrderVo;
import com.ruoyi.system.service.ICoursePayOrderService;
import com.ruoyi.system.service.IJobSignupOrderService;
import com.ruoyi.system.service.ISalonPayOrderService;
import com.ruoyi.wxmini.service.IWxRefundService;

@Api(tags = "退款管理")
@RestController
@RequestMapping("/system/refund")
public class RefundController extends BaseController {

    @Autowired
    private IJobSignupOrderService jobSignupOrderService;
    @Autowired
    private ISalonPayOrderService salonPayOrderService;
    @Autowired
    private ICoursePayOrderService coursePayOrderService;
    @Autowired
    private IWxRefundService wxRefundService;
    @Autowired
    private JobSignAuditController jobSignAuditController;

    @ApiOperation("兼职退款列表")
    @PreAuthorize("@ss.hasPermi('system:refund:list')")
    @GetMapping("/job/list")
    public TableDataInfoVo<JobRefundOrderVo> listJobRefundOrders(@RequestParam("jobId") Long jobId) {
        startPage();
        List<JobRefundOrderVo> list = jobSignupOrderService.selectPaidOrdersWithSignIn(jobId);
        return getDataTable(list);
    }

    @ApiOperation("兼职订单退款")
    @PreAuthorize("@ss.hasPermi('system:refund:edit')")
    @PostMapping("/job/{orderNo}")
    public AjaxResult refundJobOrder(@PathVariable("orderNo") String orderNo) {
        try {
            JobSignupOrder order = jobSignupOrderService.selectJobSignupOrderByOrderNo(orderNo);
            jobSignAuditController.validateRefundOrder(order, orderNo);
            wxRefundService.refundJobOrder(orderNo, "后台管理员退款");
            return success("退款成功");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("沙龙退款列表")
    @PreAuthorize("@ss.hasPermi('system:refund:list')")
    @GetMapping("/salon/list")
    public TableDataInfoVo<SalonRefundOrderVo> listSalonRefundOrders(
            @RequestParam(value = "salonId", required = false) Long salonId,
            @RequestParam(value = "keyword", required = false) String keyword) {
        startPage();
        List<SalonRefundOrderVo> list = salonPayOrderService.selectPaidSalonOrders(salonId, keyword);
        return getDataTable(list);
    }

    @ApiOperation("沙龙订单退款")
    @PreAuthorize("@ss.hasPermi('system:refund:edit')")
    @PostMapping("/salon/{orderNo}")
    public AjaxResult refundSalonOrder(@PathVariable("orderNo") String orderNo) {
        try {
            wxRefundService.refundSalonOrder(orderNo, "后台管理员退款");
            return success("退款成功");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("课程退款列表")
    @PreAuthorize("@ss.hasPermi('system:refund:list')")
    @GetMapping("/course/list")
    public TableDataInfoVo<CourseRefundOrderVo> listCourseRefundOrders(
            @RequestParam(value = "courseId", required = false) Long courseId,
            @RequestParam(value = "keyword", required = false) String keyword) {
        startPage();
        List<CourseRefundOrderVo> list = coursePayOrderService.selectCourseRefundOrders(courseId, keyword);
        return getDataTable(list);
    }

    @ApiOperation("课程订单退款")
    @PreAuthorize("@ss.hasPermi('system:refund:edit')")
    @PostMapping("/course/{orderNo}")
    public AjaxResult refundCourseOrder(@PathVariable("orderNo") String orderNo) {
        try {
            wxRefundService.refundCourseOrder(orderNo, "后台管理员课程退款");
            return success("退款成功");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }
}
