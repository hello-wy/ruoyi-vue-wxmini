package com.ruoyi.wxmini.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

/**
 * 微信支付配置
 *
 * @author weijiayu
 */
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {

    private String appId;

    private String mchId;

    private String mchKey;

    private String subAppId;

    private String subMchId;

    private String keyPath;

    private String serviceId;

    private String certSerialNo;

    private String apiV3Key;

    private String payScoreNotifyUrl;

    private String privateKeyPath;

    private String privateCertPath;

    private String publicKeyPath;

    private String publicKeyId;

    private boolean useSandboxEnv;

    /**
     * 商家转账到零钱配置
     */
    private Transfer transfer = new Transfer();

    /**
     * 商家转账到零钱子配置
     */
    @Data
    public static class Transfer {
        /** 转账结果回调通知地址 */
        private String notifyUrl;

        /** 转账场景ID */
        private String sceneId = "1005";

        /** 最小提现金额（元） */
        private BigDecimal minAmount = new BigDecimal("1.00");

        /** 最大提现金额（元） */
        private BigDecimal maxAmount = new BigDecimal("5000.00");

        /** 转账批次名称 */
        private String batchName = "钱包提现";
    }
}
