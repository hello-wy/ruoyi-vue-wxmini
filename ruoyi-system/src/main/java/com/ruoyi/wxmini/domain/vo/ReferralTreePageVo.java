package com.ruoyi.wxmini.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 两级邀请关系分页结果。
 */
public class ReferralTreePageVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private long total;

    private List<ReferralTreeGroupVo> rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<ReferralTreeGroupVo> getRows() {
        return rows;
    }

    public void setRows(List<ReferralTreeGroupVo> rows) {
        this.rows = rows;
    }
}
