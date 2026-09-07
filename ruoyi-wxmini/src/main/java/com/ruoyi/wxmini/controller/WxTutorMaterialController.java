package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.TutorMaterial;
import com.ruoyi.system.service.ITutorMaterialService;
import com.ruoyi.wxmini.bo.WxTutorMaterialBo;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "教员材料")
@RestController
@RequestMapping("/wxmini/tutoring/materials")
public class WxTutorMaterialController {
    @Resource
    private ITutorMaterialService tutorMaterialService;

    @ApiOperation("登记已上传的教员审核材料")
    @PostMapping
    public AjaxResult create(@RequestBody WxTutorMaterialBo body) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        if (body == null) {
            return AjaxResult.error("材料参数不能为空");
        }
        TutorMaterial material = tutorMaterialService.createOwnedMaterial(
                userId,
                body.getType(),
                body.getUrl()
        );
        return AjaxResult.success("材料登记成功", material);
    }
}
