package com.ruoyi.wxmini.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 微信支付商家转账配置启动校验器
 * <p>
 * 在应用启动时校验关键配置是否完整，缺失则阻止启动。
 * </p>
 */
@Component
public class WxPayTransferConfigValidator {

    private final WxPayProperties properties;

    public WxPayTransferConfigValidator(WxPayProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void validate() {
        List<String> missingFields = new ArrayList<>();

        // 校验基础支付配置
        if (StringUtils.isBlank(properties.getAppId())) {
            missingFields.add("wx.pay.appId");
        }
        if (StringUtils.isBlank(properties.getMchId())) {
            missingFields.add("wx.pay.mchId");
        }
        if (StringUtils.isBlank(properties.getPrivateKeyPath())) {
            missingFields.add("wx.pay.privateKeyPath");
        }
        if (StringUtils.isBlank(properties.getCertSerialNo())) {
            missingFields.add("wx.pay.certSerialNo");
        }
        if (StringUtils.isBlank(properties.getApiV3Key())) {
            missingFields.add("wx.pay.apiV3Key");
        }

        // 校验转账子配置
        WxPayProperties.Transfer transfer = properties.getTransfer();
        if (transfer == null) {
            missingFields.add("wx.pay.transfer (entire section)");
        } else {
            if (StringUtils.isBlank(transfer.getNotifyUrl())) {
                missingFields.add("wx.pay.transfer.notifyUrl");
            }
            if (StringUtils.isBlank(transfer.getSceneId())) {
                missingFields.add("wx.pay.transfer.sceneId");
            }
        }

        if (!missingFields.isEmpty()) {
            throw new IllegalStateException(
                    "微信支付商家转账配置不完整，应用无法启动。缺失配置项: " + String.join(", ", missingFields));
        }
    }
}
