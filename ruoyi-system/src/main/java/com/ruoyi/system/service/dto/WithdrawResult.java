package com.ruoyi.system.service.dto;

import com.ruoyi.system.enums.WithdrawFailType;

/**
 * 提现结果 DTO
 * 用于 applyWithdraw 返回结构化结果，替代原来的 String 返回值
 */
public class WithdrawResult {

    /** 是否成功（含处理中也视为"非失败"） */
    private boolean success;

    /** 提现状态：0=处理中, 1=成功, 2=失败 */
    private Integer status;

    /** 提现单 ID */
    private Long withdrawId;

    /** 商户批次号 */
    private String outBatchNo;

    /** 失败类型枚举名称 (WithdrawFailType.name()) */
    private String failType;

    /** 面向用户的中文文案 */
    private String userMessage;

    /** 兼容旧 msg 字段 */
    private String msg;

    public WithdrawResult()
    {
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Long getWithdrawId()
    {
        return withdrawId;
    }

    public void setWithdrawId(Long withdrawId)
    {
        this.withdrawId = withdrawId;
    }

    public String getOutBatchNo()
    {
        return outBatchNo;
    }

    public void setOutBatchNo(String outBatchNo)
    {
        this.outBatchNo = outBatchNo;
    }

    public String getFailType()
    {
        return failType;
    }

    public void setFailType(String failType)
    {
        this.failType = failType;
    }

    public String getUserMessage()
    {
        return userMessage;
    }

    public void setUserMessage(String userMessage)
    {
        this.userMessage = userMessage;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    // ========== Static factory methods ==========

    /**
     * 提现成功
     */
    public static WithdrawResult success(Long withdrawId, String outBatchNo, String msg)
    {
        WithdrawResult result = new WithdrawResult();
        result.setSuccess(true);
        result.setStatus(1);
        result.setWithdrawId(withdrawId);
        result.setOutBatchNo(outBatchNo);
        result.setMsg(msg);
        return result;
    }

    /**
     * 提现处理中
     */
    public static WithdrawResult processing(Long withdrawId, String outBatchNo)
    {
        WithdrawResult result = new WithdrawResult();
        result.setSuccess(true);
        result.setStatus(0);
        result.setWithdrawId(withdrawId);
        result.setOutBatchNo(outBatchNo);
        result.setMsg("微信提现处理中");
        return result;
    }

    /**
     * 提现失败（使用枚举默认 userMessage）
     */
    public static WithdrawResult fail(WithdrawFailType failType)
    {
        WithdrawResult result = new WithdrawResult();
        result.setSuccess(false);
        result.setStatus(2);
        result.setFailType(failType.name());
        result.setUserMessage(failType.getUserMessage());
        result.setMsg(failType.getUserMessage());
        return result;
    }

    /**
     * 提现失败（自定义 userMessage）
     */
    public static WithdrawResult fail(WithdrawFailType failType, String customMessage)
    {
        WithdrawResult result = new WithdrawResult();
        result.setSuccess(false);
        result.setStatus(2);
        result.setFailType(failType.name());
        result.setUserMessage(customMessage);
        result.setMsg(customMessage);
        return result;
    }
}
