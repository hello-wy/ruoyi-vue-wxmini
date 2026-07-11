package com.ruoyi.system.service;

import com.ruoyi.system.domain.bo.MiniUserQueryBo;
import com.ruoyi.system.domain.vo.MiniUserVo;

import java.util.List;

public interface IMiniUserService {
    List<MiniUserVo> listMiniUsers(MiniUserQueryBo queryBo);

    MiniUserVo getMiniUser(Long id);
}
