package com.ruoyi.wxmini.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.system.mapper.SysDeptMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/wxmini/dept")
public class WxDeptController extends BaseController {

    @Resource
    private SysDeptMapper deptMapper;

    @Anonymous
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(required = false) String deptName) {
        List<SysDept> depts = deptMapper.selectPublicNormalDeptList(deptName);
        return success(depts);
    }
}
