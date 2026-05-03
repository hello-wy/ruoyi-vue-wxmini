package com.ruoyi.wxmini.service;

import com.ruoyi.wxmini.vo.WxJobScheduleVo;
import com.ruoyi.wxmini.vo.WxMerchantJobVo;
import com.ruoyi.wxmini.vo.WxSignupUserVo;

import java.util.List;

public interface IWxJobScheduleService {
    List<WxJobScheduleVo> listMySchedules(String userId);

    List<WxMerchantJobVo> listMerchantJobs(String currentUserId);

    List<WxSignupUserVo> listSignupUsers(String currentUserId, Long jobId, String keyword);
}
