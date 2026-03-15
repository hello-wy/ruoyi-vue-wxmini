package com.ruoyi.wxmini.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 微信小程序消息推送接入校验及消息接收
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Api(tags = "【小程序】微信消息推送Portal")
@RestController
@AllArgsConstructor
@RequestMapping("/wxmini/portal/{appid}")
@Slf4j
public class WxPortalController {
    private final WxMaService wxMaService;
    private final WxMaMessageRouter wxMaMessageRouter;

    /**
     * 微信服务器接入校验（GET 请求，返回 echostr）
     */
    @ApiOperation("微信服务器接入校验（由微信服务器发起）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "appid", value = "小程序 AppID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class),
        @ApiImplicitParam(name = "signature", value = "微信加密签名", dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "timestamp", value = "时间戳", dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "nonce", value = "随机数", dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "echostr", value = "随机字符串", dataType = "String", paramType = "query", dataTypeClass = String.class)
    })
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@PathVariable String appid,
                          @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {
        log.info("\n接收到来自微信服务器的认证消息：signature = [{}], timestamp = [{}], nonce = [{}], echostr = [{}]",
                signature, timestamp, nonce, echostr);

        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        if (wxMaService.checkSignature(timestamp, nonce, signature)) {
            WxMaConfigHolder.remove();
            return echostr;
        }
        WxMaConfigHolder.remove();
        return "非法请求";
    }

    /**
     * 接收微信服务器推送的消息/事件（POST 请求）
     */
    @ApiOperation("接收微信服务器推送消息/事件（由微信服务器发起）")
    @ApiImplicitParam(name = "appid", value = "小程序 AppID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@PathVariable String appid,
                       @RequestBody String requestBody,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature,
                       @RequestParam(name = "encrypt_type", required = false) String encryptType,
                       @RequestParam(name = "signature", required = false) String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce) {
        log.info("\n接收微信请求：[msg_signature=[{}], encrypt_type=[{}], signature=[{}]," +
                        " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                msgSignature, encryptType, signature, timestamp, nonce, requestBody);

        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        final boolean isJson = Objects.equals(wxMaService.getWxMaConfig().getMsgDataFormat(),
                WxMaConstants.MsgDataFormat.JSON);
        if (StringUtils.isBlank(encryptType)) {
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromJson(requestBody);
            } else {
                inMessage = WxMaMessage.fromXml(requestBody);
            }
            this.route(inMessage);
            WxMaConfigHolder.remove();
            return "success";
        }

        if ("aes".equals(encryptType)) {
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromEncryptedJson(requestBody, wxMaService.getWxMaConfig());
            } else {
                inMessage = WxMaMessage.fromEncryptedXml(requestBody, wxMaService.getWxMaConfig(),
                        timestamp, nonce, msgSignature);
            }
            this.route(inMessage);
            WxMaConfigHolder.remove();
            return "success";
        }
        WxMaConfigHolder.remove();
        throw new RuntimeException("不可识别的加密类型：" + encryptType);
    }

    private void route(WxMaMessage message) {
        try {
            wxMaMessageRouter.route(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
