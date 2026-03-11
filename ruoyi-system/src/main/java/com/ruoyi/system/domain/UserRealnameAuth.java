package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户实名认证对象 user_realname_auth
 * 
 * @author ruoyi
 * @date 2026-03-06
 */
public class UserRealnameAuth extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 用户ID，关联sys_user表 */
    @Excel(name = "用户ID，关联sys_user表")
    private Long uid;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 身份证号码 */
    @Excel(name = "身份证号码")
    private String idCard;

    /** 认证状态：0-待审核，1-已通过，2-已驳回 */
    @Excel(name = "认证状态：0-待审核，1-已通过，2-已驳回")
    private Long authStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUid(Long uid) 
    {
        this.uid = uid;
    }

    public Long getUid() 
    {
        return uid;
    }

    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }

    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }

    public void setAuthStatus(Long authStatus) 
    {
        this.authStatus = authStatus;
    }

    public Long getAuthStatus() 
    {
        return authStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("uid", getUid())
            .append("realName", getRealName())
            .append("idCard", getIdCard())
            .append("authStatus", getAuthStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
