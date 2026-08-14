package com.ruoyi.wxmini.domain.vo;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 指定小程序用户的一、二级邀请关系。
 */
public class ReferralTreeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private ReferralUserVo selectedUser;
    private List<UserReferralVo> level1Invitees = Collections.emptyList();
    private List<UserReferralVo> level2Invitees = Collections.emptyList();

    public ReferralUserVo getSelectedUser() { return selectedUser; }
    public void setSelectedUser(ReferralUserVo selectedUser) { this.selectedUser = selectedUser; }
    public List<UserReferralVo> getLevel1Invitees() { return level1Invitees; }
    public void setLevel1Invitees(List<UserReferralVo> level1Invitees) { this.level1Invitees = level1Invitees; }
    public List<UserReferralVo> getLevel2Invitees() { return level2Invitees; }
    public void setLevel2Invitees(List<UserReferralVo> level2Invitees) { this.level2Invitees = level2Invitees; }
}
