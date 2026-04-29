package com.ruoyi.wxmini.service;

import com.ruoyi.wxmini.bo.WxRealVerifyRequestBo;
import com.ruoyi.wxmini.vo.WxRealVerifyResultVo;

public interface IWxRealVerifyService {

    WxRealVerifyResultVo verify(String userId, WxRealVerifyRequestBo request);
}
