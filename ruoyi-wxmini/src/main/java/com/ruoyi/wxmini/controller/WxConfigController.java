package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "【小程序】配置")
@RestController
@RequestMapping("/wxmini/config")
public class WxConfigController extends BaseController {

    @Resource
    private ISysConfigService configService;

    @ApiOperation("获取兼职群二维码配置")
    @GetMapping("/parttime-group-qrcode")
    public AjaxResult parttimeGroupQrcode() {
        String qrcodeUrl = configService.selectConfigByKey("wxmini.parttime.group.qrcodeUrl");
        if (StringUtils.isBlank(qrcodeUrl)) {
            return error("兼职群二维码暂未配置");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("qrcodeUrl", qrcodeUrl);
        data.put("description", configService.selectConfigByKey("wxmini.parttime.group.description"));
        data.put("title", "兼职群二维码");
        data.put("buttonText", "我知道了");
        return success(data);
    }

    @ApiOperation("获取商家代理配置")
    @GetMapping("/merchant-agent")
    public AjaxResult merchantAgentConfig() {
        String content = configService.selectConfigByKey("wxmini.merchant.agent.content");
        String qrcodeUrl = configService.selectConfigByKey("wxmini.merchant.agent.qrcodeUrl");
        String contact = configService.selectConfigByKey("wxmini.merchant.agent.contact");
        if (StringUtils.isAllBlank(content, qrcodeUrl, contact)) {
            return error("代理配置暂未开放");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("title", defaultIfBlank(configService.selectConfigByKey("wxmini.merchant.agent.title"), "成为代理"));
        data.put("content", defaultIfBlank(content, "请联系平台了解代理合作详情。"));
        data.put("qrcodeUrl", qrcodeUrl);
        data.put("contact", contact);
        data.put("tips", configService.selectConfigByKey("wxmini.merchant.agent.tips"));
        data.put("buttonText", defaultIfBlank(configService.selectConfigByKey("wxmini.merchant.agent.buttonText"), "我知道了"));
        return success(data);
    }

    private String defaultIfBlank(String value, String defaultValue) {
        return StringUtils.isBlank(value) ? defaultValue : value;
    }
}
