package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.bo.MiniUserQueryBo;
import com.ruoyi.system.domain.vo.MiniUserVo;
import com.ruoyi.system.mapper.MiniUserMapper;
import com.ruoyi.system.service.IMiniUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MiniUserServiceImpl implements IMiniUserService {
    @Resource
    private MiniUserMapper miniUserMapper;

    @Override
    public List<MiniUserVo> listMiniUsers(MiniUserQueryBo queryBo) {
        return miniUserMapper.selectMiniUserList(queryBo == null ? new MiniUserQueryBo() : queryBo);
    }

    @Override
    public MiniUserVo getMiniUser(Long id) {
        return miniUserMapper.selectMiniUserById(id);
    }
}
