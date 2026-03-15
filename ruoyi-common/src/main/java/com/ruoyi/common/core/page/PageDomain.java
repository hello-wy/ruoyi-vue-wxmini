package com.ruoyi.common.core.page;

import com.ruoyi.common.utils.StringUtils;

/**
 * 分页数据
 * 
 * @author ruoyi
 */
public class PageDomain
{
    /** 当前记录起始索引 */
    private Integer pageNum;

    /** 每页显示记录数 */
    private Integer pageSize;

    /** 排序列 */
    private String orderByColumn;

    /** 排序的方向desc或者asc */
    private String isAsc = "asc";

    /** 分页参数合理化 */
    private Boolean reasonable = true;

    public String getOrderBy()
    {
        if (StringUtils.isEmpty(orderByColumn))
        {
            return "";
        }
        return StringUtils.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    public Integer getPageNum()
    {
        return pageNum;
    }

    public void setPageNum(Integer pageNum)
    {
        this.pageNum = pageNum;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    public String getOrderByColumn()
    {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn)
    {
        this.orderByColumn = orderByColumn;
    }

    public String getIsAsc()
    {
        return isAsc;
    }

    public void setIsAsc(String isAsc)
    {
        if (StringUtils.isEmpty(isAsc))
        {
            return;
        }

        String normalized = StringUtils.trim(isAsc).toLowerCase();
        int commaIndex = normalized.indexOf(',');
        if (commaIndex > -1)
        {
            // 避免传入 asc,desc 这类多值导致 order by 语法异常
            normalized = normalized.substring(0, commaIndex).trim();
        }

        // 兼容前端排序类型
        if ("ascending".equals(normalized) || "ascend".equals(normalized))
        {
            normalized = "asc";
        }
        else if ("descending".equals(normalized) || "descend".equals(normalized))
        {
            normalized = "desc";
        }

        if ("asc".equals(normalized) || "desc".equals(normalized))
        {
            this.isAsc = normalized;
        }
    }

    public Boolean getReasonable()
    {
        if (StringUtils.isNull(reasonable))
        {
            return Boolean.TRUE;
        }
        return reasonable;
    }

    public void setReasonable(Boolean reasonable)
    {
        this.reasonable = reasonable;
    }
}
