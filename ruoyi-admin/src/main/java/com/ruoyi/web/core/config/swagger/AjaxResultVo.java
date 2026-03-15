package com.ruoyi.web.core.config.swagger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Swagger 专用 —— AjaxResult 响应体示例模型
 * <p>
 * AjaxResult 实际继承自 HashMap，Swagger 无法解析其字段，
 * 本类仅用于 Swagger 文档展示，通过 SwaggerConfig 中的 alternateTypeRules 替换。
 *
 * @author ruoyi
 */
@ApiModel(value = "AjaxResult", description = "统一响应结果")
public class AjaxResultVo {

    @ApiModelProperty(value = "状态码：200=成功，500=失败，401=未授权，403=无权限", example = "200")
    private Integer code;

    @ApiModelProperty(value = "响应消息", example = "操作成功")
    private String msg;

    @ApiModelProperty(value = "响应数据，具体类型根据接口不同而不同")
    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

