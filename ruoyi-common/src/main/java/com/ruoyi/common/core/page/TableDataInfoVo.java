package com.ruoyi.common.core.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Swagger 专用 —— 分页响应泛型包装类
 * <p>
 * TableDataInfo 的 rows 字段是 List<?>，Swagger 无法推断元素类型，
 * 本类通过泛型 T 让 Swagger 渲染出具体的行数据结构。
 *
 * @param <T> 行数据类型
 * @author ruoyi
 */
@ApiModel(value = "TableDataInfo", description = "分页响应结果")
public class TableDataInfoVo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息状态码：200=成功", example = "200")
    private int code;

    @ApiModelProperty(value = "消息内容", example = "查询成功")
    private String msg;

    @ApiModelProperty(value = "总记录数", example = "100")
    private long total;

    @ApiModelProperty(value = "列表数据")
    private List<T> rows;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}

