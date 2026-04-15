package com.ruoyi.wxmini.bo;

import javax.validation.constraints.NotNull;

public class WxSalonPayCreateOrderBo {
    @NotNull(message = "salonId不能为空")
    private Long salonId;

    public Long getSalonId() {
        return salonId;
    }

    public void setSalonId(Long salonId) {
        this.salonId = salonId;
    }
}
