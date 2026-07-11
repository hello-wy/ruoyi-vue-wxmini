package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.bo.MiniUserQueryBo;
import com.ruoyi.system.domain.vo.MiniUserVo;

import java.util.List;

public interface MiniUserMapper {
    List<MiniUserVo> selectMiniUserList(MiniUserQueryBo queryBo);

    MiniUserVo selectMiniUserById(Long id);
}
