package com.ruoyi.system.domain.bo;

/**
 * 学员绑定请求对象。
 */
public class StudentBindingBo {

    /** 绑定员工用户ID；员工自助认领时可不传，服务端强制绑定当前登录员工。 */
    private Long sysUserId;

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }
}
