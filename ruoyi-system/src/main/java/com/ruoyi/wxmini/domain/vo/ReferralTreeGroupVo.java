package com.ruoyi.wxmini.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 两级邀请关系分组视图对象。
 */
public class ReferralTreeGroupVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private UserReferralVo level1Invitee;

    private List<UserReferralVo> level2Invitees;

    public UserReferralVo getLevel1Invitee() {
        return level1Invitee;
    }

    public void setLevel1Invitee(UserReferralVo level1Invitee) {
        this.level1Invitee = level1Invitee;
    }

    public List<UserReferralVo> getLevel2Invitees() {
        return level2Invitees;
    }

    public void setLevel2Invitees(List<UserReferralVo> level2Invitees) {
        this.level2Invitees = level2Invitees;
    }
}
