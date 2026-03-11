package com.ruoyi.wxmini.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.UserWallet;
import com.ruoyi.system.domain.WalletWithdraw;
import com.ruoyi.system.service.IWalletService;

/**
 * 用户钱包 Controller（用户侧）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/wallet")
public class WxWalletController extends BaseController
{
    @Autowired
    private IWalletService walletService;

    /**
     * 获取当前用户钱包信息
     * GET /system/wallet/info
     */
    @GetMapping("/info")
    public AjaxResult info()
    {
        Long uid = SecurityUtils.getUserId();
        UserWallet wallet = walletService.getOrCreateWallet(uid);
        return success(wallet);
    }

    /**
     * 申请提现到微信钱包
     * POST /system/wallet/withdraw
     * body: { "amount": "50.00" }
     */
    @PostMapping("/withdraw")
    public AjaxResult withdraw(@RequestBody Map<String, Object> body)
    {
        Long uid = SecurityUtils.getUserId();
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
        // 提现申请成功时返回 "提现申请已提交..." 前缀
        if (msg.startsWith("提现申请已提交"))
        {
            return success(msg);
        }
        return error(msg);
    }

    /**
     * 获取提现记录列表
     * GET /system/wallet/withdrawRecords
     */
    @GetMapping("/withdrawRecords")
    public AjaxResult withdrawRecords()
    {
        Long uid = SecurityUtils.getUserId();
        List<WalletWithdraw> list = walletService.getWithdrawRecords(uid);
        return success(list);
    }
}
