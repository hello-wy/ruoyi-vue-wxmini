package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.bo.MiniUserQueryBo;
import com.ruoyi.system.domain.vo.MiniUserVo;
import com.ruoyi.system.service.IMiniUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "小程序用户管理")
@RestController
@RequestMapping("/system/mini-user")
public class MiniUserController extends BaseController {

    @Resource
    private IMiniUserService miniUserService;

    @ApiOperation("查询小程序用户分页列表")
    @PreAuthorize("@ss.hasPermi('system:mini-user:list')")
    @GetMapping("/list")
    public TableDataInfoVo<MiniUserVo> list(MiniUserQueryBo query) {
        startPage();
        List<MiniUserVo> list = miniUserService.listMiniUsers(query);
        return getDataTable(list);
    }

    @ApiOperation("查询小程序用户详情")
    @PreAuthorize("@ss.hasPermi('system:mini-user:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        MiniUserVo user = miniUserService.getMiniUser(id);
        return user == null ? error("小程序用户不存在") : success(user);
    }
}
