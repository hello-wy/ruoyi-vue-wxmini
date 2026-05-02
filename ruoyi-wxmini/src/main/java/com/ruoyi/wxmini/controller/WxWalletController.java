package com.ruoyi.wxmini.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletTransaction;
import com.ruoyi.system.domain.WalletWithdraw;
import com.ruoyi.system.service.IWalletService;
import com.ruoyi.wxmini.util.WxMiniUserContext;

/**
 * 用户钱包 Controller（用户侧）
 *
 * @author ruoyi
 */
@Api(tags = "【小程序】用户钱包")
@RestController
@RequestMapping("/wxmini/wallet")
public class WxWalletController extends BaseController
{
    @Autowired
    private IWalletService walletService;

    /**
     * 获取当前用户钱包信息
     * GET /wxmini/wallet/info
     */
    @ApiOperation("获取当前登录用户钱包余额信息（需登录）")
    @GetMapping("/info")
    public AjaxResult info()
    {
        Long uid = resolveCurrentUid();
        UserWallet wallet = walletService.getOrCreateWallet(uid);
        return success(wallet);
    }

    /**
     * 申请提现到微信钱包
     * POST /wxmini/wallet/withdraw
     * body: { "amount": "50.00" }
     */
    @ApiOperation("申请提现到微信钱包（需登录，body 传 amount 字段）")
    @PostMapping("/withdraw")
    public AjaxResult withdraw(@RequestBody Map<String, Object> body)
    {
        Long uid = resolveCurrentUid();
        Object amountObj = body.get("amount");
        if (amountObj == null)
        {
            return error("提现金额不能为空");
        }
        BigDecimal amount;
        try
        {
            amount = new BigDecimal(amountObj.toString());
        }
        catch (NumberFormatException e)
        {
            return error("金额格式不正确");
        }
        String msg = walletService.applyWithdraw(uid, amount);
        if (msg.startsWith("提现申请已提交"))
        {
            return success(msg);
        }
        return error(msg);
    }

    /**
     * 获取提现记录列表
     * GET /wxmini/wallet/withdraw-records
     */
    @ApiOperation("获取当前登录用户的提现记录列表（需登录）")
    @GetMapping("/withdraw-records")
    public AjaxResult withdrawRecords()
    {
        Long uid = resolveCurrentUid();
        List<WalletWithdraw> list = walletService.getWithdrawRecords(uid);
        return success(list);
    }

    @ApiOperation("获取当前登录用户的钱包流水（需登录）")
    @GetMapping("/transactions")
    public AjaxResult transactions()
    {
        Long uid = resolveCurrentUid();
        List<WalletTransaction> list = walletService.getTransactions(uid);
        return success(list);
    }

    private Long resolveCurrentUid() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            throw new IllegalStateException("请先登录");
        }
        return walletService.resolveCurrentUserUid(userId);
    }
}
