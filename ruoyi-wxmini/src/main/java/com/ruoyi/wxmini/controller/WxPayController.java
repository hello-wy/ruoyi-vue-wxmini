package com.ruoyi.wxmini.controller;

import cn.hutool.core.io.IoUtil;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.transfer.TransferBillsNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IWalletService;
import com.ruoyi.wxmini.bo.WxJobPayrollCreateOrderBo;
import com.ruoyi.wxmini.bo.WxJobSignupCreateOrderBo;
import com.ruoyi.wxmini.bo.WxCoursePayCreateOrderBo;
import com.ruoyi.wxmini.bo.WxSalonPayCreateOrderBo;
import com.ruoyi.wxmini.service.IWxCoursePayService;
import com.ruoyi.wxmini.service.IWxJobPayrollPayService;
import com.ruoyi.wxmini.service.IWxJobSignupPayService;
import com.ruoyi.wxmini.service.IWxGrowupPayService;
import com.ruoyi.wxmini.service.IWxMiniTutoringService;
import com.ruoyi.wxmini.service.IWxSalonPayService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "【小程序】微信支付")
@Slf4j
@RestController
@RequestMapping("/wxmini/pay")
public class WxPayController {

    @Resource
    private WxPayService wxPayService;
    @Resource
    private IWxSalonPayService wxSalonPayService;
    @Resource
    private IWxJobSignupPayService wxJobSignupPayService;
    @Resource
    private IWxJobPayrollPayService wxJobPayrollPayService;
    @Resource
    private IWxGrowupPayService wxGrowupPayService;
    @Resource
    private IWxMiniTutoringService wxMiniTutoringService;
    @Resource
    private IWxCoursePayService wxCoursePayService;
    @Resource
    private IWalletService walletService;

    @ApiOperation("查询当前用户沙龙订单列表（需登录）")
    @GetMapping("/salon/orders/my")
    public AjaxResult mySalonOrders() {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            return AjaxResult.success(wxSalonPayService.listMyOrders(userId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("创建沙龙微信支付订单，返回 JSAPI 支付参数（需登录）")
    @PostMapping("/salon/orders/create")
    public AjaxResult createSalonOrder(@RequestBody @Validated WxSalonPayCreateOrderBo bo) {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            return AjaxResult.success(wxSalonPayService.createSalonOrder(userId, bo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("查询沙龙支付订单状态（需登录）")
    @ApiImplicitParam(name = "orderNo", value = "平台订单号", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    @GetMapping("/salon/orders/{orderNo}")
    public AjaxResult querySalonOrder(@PathVariable("orderNo") String orderNo) {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            return AjaxResult.success(wxSalonPayService.querySalonOrder(userId, orderNo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("查询当前用户兼职报名订单列表（需登录）")
    @GetMapping("/jobs/orders/my")
    public AjaxResult myJobOrders() {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            return AjaxResult.success(wxJobSignupPayService.listMyOrders(userId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("创建兼职报名支付订单，返回 JSAPI 支付参数（需登录）")
    @PostMapping("/jobs/orders/create")
    public AjaxResult createJobOrder(@RequestBody @Validated WxJobSignupCreateOrderBo bo) {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            return AjaxResult.success(wxJobSignupPayService.createJobOrder(userId, bo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("查询兼职报名订单状态（需登录）")
    @ApiImplicitParam(name = "orderNo", value = "平台订单号", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    @GetMapping("/jobs/orders/{orderNo}")
    public AjaxResult queryJobOrder(@PathVariable("orderNo") String orderNo) {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            return AjaxResult.success(wxJobSignupPayService.queryJobOrder(userId, orderNo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("查询当前用户课程报名订单列表（需登录）")
    @GetMapping("/courses/orders/my")
    public AjaxResult myCourseOrders() {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            return AjaxResult.success(wxCoursePayService.listMyOrders(userId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("创建课程报名支付订单，返回 JSAPI 支付参数（需登录）")
    @PostMapping("/courses/orders/create")
    public AjaxResult createCourseOrder(@RequestBody @Validated WxCoursePayCreateOrderBo bo) {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            return AjaxResult.success(wxCoursePayService.createCourseOrder(userId, bo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("查询当前用户当前课程已支付订单（需登录）")
    @GetMapping("/courses/orders/paid")
    public AjaxResult queryPaidCourseOrder(@RequestParam("courseId") Long courseId) {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            return AjaxResult.success(wxCoursePayService.queryPaidCourseOrder(userId, courseId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("查询课程报名订单状态（需登录）")
    @GetMapping("/courses/orders/{orderNo}")
    public AjaxResult queryCourseOrder(@PathVariable("orderNo") String orderNo) {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            return AjaxResult.success(wxCoursePayService.queryCourseOrder(userId, orderNo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("取消课程报名订单（需登录）")
    @PostMapping("/courses/orders/{orderNo}/cancel")
    public AjaxResult cancelCourseOrder(@PathVariable("orderNo") String orderNo) {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            return AjaxResult.success(wxCoursePayService.cancelCourseOrder(userId, orderNo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("创建兼职工资支付订单，返回 JSAPI 支付参数（需登录）")
    @PostMapping("/payroll/orders/create")
    public AjaxResult createPayrollOrder(@RequestBody @Validated WxJobPayrollCreateOrderBo bo) {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            return AjaxResult.success(wxJobPayrollPayService.createPayrollOrder(userId, bo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("查询兼职工资支付订单状态（需登录）")
    @GetMapping("/payroll/orders/{orderNo}")
    public AjaxResult queryPayrollOrder(@PathVariable("orderNo") String orderNo) {
        try {
            String userId = WxMiniUserContext.getCurrentUserId();
            return AjaxResult.success(wxJobPayrollPayService.queryPayrollOrder(userId, orderNo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("微信沙龙支付结果异步回调通知（由微信服务器调用）")
    @PostMapping("/salon/notify")
    public String payNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletInputStream inputStream = request.getInputStream();
            String notifyData = IoUtil.readUtf8(inputStream);
            SignatureHeader signatureHeader = SignatureHeader.builder()
                    .serial(request.getHeader("Wechatpay-Serial"))
                    .signature(request.getHeader("Wechatpay-Signature"))
                    .nonce(request.getHeader("Wechatpay-Nonce"))
                    .timeStamp(request.getHeader("Wechatpay-Timestamp"))
                    .build();
            WxPayNotifyV3Result result = this.wxPayService.parseOrderNotifyV3Result(notifyData, signatureHeader);
            String tradeState = result.getResult().getTradeState();
            boolean isPaySuccess = "SUCCESS".equals(tradeState);
            if (!isPaySuccess) {
                return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
            }
            String requestId = request.getHeader("Request-ID");
            boolean handled = wxSalonPayService.handleSalonPaidCallback(result, requestId);
            return handled ? "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>" : "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
    }

    @ApiOperation("微信兼职报名支付结果异步回调通知（由微信服务器调用）")
    @PostMapping("/jobs/notify")
    public String jobPayNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletInputStream inputStream = request.getInputStream();
            String notifyData = IoUtil.readUtf8(inputStream);
            SignatureHeader signatureHeader = SignatureHeader.builder()
                    .serial(request.getHeader("Wechatpay-Serial"))
                    .signature(request.getHeader("Wechatpay-Signature"))
                    .nonce(request.getHeader("Wechatpay-Nonce"))
                    .timeStamp(request.getHeader("Wechatpay-Timestamp"))
                    .build();
            WxPayNotifyV3Result result = this.wxPayService.parseOrderNotifyV3Result(notifyData, signatureHeader);
            String tradeState = result.getResult().getTradeState();
            boolean isPaySuccess = "SUCCESS".equals(tradeState);
            if (!isPaySuccess) {
                return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
            }
            String requestId = request.getHeader("Request-ID");
            boolean handled = wxJobSignupPayService.handleJobPaidCallback(result, requestId);
            return handled ? "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>" : "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
    }

    @ApiOperation("微信课程报名支付结果异步回调通知（由微信服务器调用）")
    @PostMapping("/courses/notify")
    public String coursePayNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletInputStream inputStream = request.getInputStream();
            String notifyData = IoUtil.readUtf8(inputStream);
            SignatureHeader signatureHeader = SignatureHeader.builder()
                    .serial(request.getHeader("Wechatpay-Serial"))
                    .signature(request.getHeader("Wechatpay-Signature"))
                    .nonce(request.getHeader("Wechatpay-Nonce"))
                    .timeStamp(request.getHeader("Wechatpay-Timestamp"))
                    .build();
            WxPayNotifyV3Result result = this.wxPayService.parseOrderNotifyV3Result(notifyData, signatureHeader);
            boolean isPaySuccess = "SUCCESS".equals(result.getResult().getTradeState());
            if (!isPaySuccess) {
                return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
            }
            boolean handled = wxCoursePayService.handleCoursePaidCallback(result, request.getHeader("Request-ID"));
            return handled ? "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>" : "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
    }

    @ApiOperation("微信兼职工资支付结果异步回调通知（由微信服务器调用）")
    @PostMapping("/payroll/notify")
    public String payrollPayNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletInputStream inputStream = request.getInputStream();
            String notifyData = IoUtil.readUtf8(inputStream);
            SignatureHeader signatureHeader = SignatureHeader.builder()
                    .serial(request.getHeader("Wechatpay-Serial"))
                    .signature(request.getHeader("Wechatpay-Signature"))
                    .nonce(request.getHeader("Wechatpay-Nonce"))
                    .timeStamp(request.getHeader("Wechatpay-Timestamp"))
                    .build();
            WxPayNotifyV3Result result = this.wxPayService.parseOrderNotifyV3Result(notifyData, signatureHeader);
            String tradeState = result.getResult().getTradeState();
            boolean isPaySuccess = "SUCCESS".equals(tradeState);
            if (!isPaySuccess) {
                return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
            }
            String requestId = request.getHeader("Request-ID");
            boolean handled = wxJobPayrollPayService.handlePayrollPaidCallback(result, requestId);
            return handled ? "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>" : "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
    }

    @ApiOperation("微信家教支付结果异步回调通知（由微信服务器调用）")
    @PostMapping("/tutoring/notify")
    public String tutoringPayNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletInputStream inputStream = request.getInputStream();
            String notifyData = IoUtil.readUtf8(inputStream);
            SignatureHeader signatureHeader = SignatureHeader.builder()
                    .serial(request.getHeader("Wechatpay-Serial"))
                    .signature(request.getHeader("Wechatpay-Signature"))
                    .nonce(request.getHeader("Wechatpay-Nonce"))
                    .timeStamp(request.getHeader("Wechatpay-Timestamp"))
                    .build();
            WxPayNotifyV3Result result = this.wxPayService.parseOrderNotifyV3Result(notifyData, signatureHeader);
            String tradeState = result.getResult().getTradeState();
            boolean isPaySuccess = "SUCCESS".equals(tradeState);
            if (!isPaySuccess) {
                return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
            }
            String requestId = request.getHeader("Request-ID");
            boolean handled = wxMiniTutoringService.handleTutoringPaidCallback(result, requestId);
            return handled ? "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>" : "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
    }

    @ApiOperation("微信成长课程支付结果异步回调通知（由微信服务器调用）")
    @PostMapping("/growup/notify")
    public String growupPayNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServletInputStream inputStream = request.getInputStream();
            String notifyData = IoUtil.readUtf8(inputStream);
            SignatureHeader signatureHeader = SignatureHeader.builder()
                    .serial(request.getHeader("Wechatpay-Serial"))
                    .signature(request.getHeader("Wechatpay-Signature"))
                    .nonce(request.getHeader("Wechatpay-Nonce"))
                    .timeStamp(request.getHeader("Wechatpay-Timestamp"))
                    .build();
            WxPayNotifyV3Result result = this.wxPayService.parseOrderNotifyV3Result(notifyData, signatureHeader);
            if (!"SUCCESS".equals(result.getResult().getTradeState())) {
                return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
            }
            String requestId = request.getHeader("Request-ID");
            boolean handled = wxGrowupPayService.handleCoursePaidCallback(result, requestId);
            return handled ? "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>" : "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
    }

    @ApiOperation("微信提现结果异步回调通知（由微信服务器调用）")
    @PostMapping("/wallet/notify")
    public String walletTransferNotify(HttpServletRequest request) {
        try {
            ServletInputStream inputStream = request.getInputStream();
            String notifyData = IoUtil.readUtf8(inputStream);
            SignatureHeader signatureHeader = SignatureHeader.builder()
                    .serial(request.getHeader("Wechatpay-Serial"))
                    .signature(request.getHeader("Wechatpay-Signature"))
                    .nonce(request.getHeader("Wechatpay-Nonce"))
                    .timeStamp(request.getHeader("Wechatpay-Timestamp"))
                    .build();
            TransferBillsNotifyResult result = this.wxPayService.getTransferService()
                    .parseTransferBillsNotifyResult(notifyData, signatureHeader);
            boolean handled = walletService.syncWithdrawStatusByOutBatchNo(
                    result.getResult() == null ? null : result.getResult().getOutBillNo());
            return handled ? "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>" : "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
    }
}
