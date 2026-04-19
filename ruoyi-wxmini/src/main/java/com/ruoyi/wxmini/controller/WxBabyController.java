package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.wxmini.bo.WxBabySaveBo;
import com.ruoyi.wxmini.domain.BabyInfo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IBabyInfoService;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "【小程序】萌娃管理")
@RestController
@RequestMapping("/wxmini/baby")
public class WxBabyController extends BaseController {

    @Resource
    private IBabyInfoService babyInfoService;

    @Resource
    private IUserInfoService userInfoService;

    @ApiOperation("查询当前登录用户萌娃列表")
    @GetMapping("/list")
    public AjaxResult list() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        List<BabyInfo> list = babyInfoService.selectBabyInfoListByUserId(userId);
        return success(list);
    }

    @ApiOperation("查询当前登录用户萌娃详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable("id") Long id) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        BabyInfo babyInfo = babyInfoService.selectBabyInfoByIdAndUserId(id, userId);
        return babyInfo == null ? error("萌娃信息不存在") : success(babyInfo);
    }

    @ApiOperation("新增萌娃")
    @PostMapping
    public AjaxResult add(@RequestBody @Validated WxBabySaveBo bo) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null) {
            return error("用户不存在");
        }
        BabyInfo babyInfo = buildEntity(bo, userId);
        long id = SnowflakeIdWorker.nextIdDefault();
        babyInfo.setId(id);
        int rows = babyInfoService.insertBabyInfo(babyInfo);
        return rows > 0 ? AjaxResult.success("新增成功", id) : AjaxResult.error("新增失败");
    }

    @ApiOperation("修改萌娃")
    @PutMapping
    public AjaxResult edit(@RequestBody @Validated WxBabySaveBo bo) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        if (bo.getId() == null) {
            return error("萌娃ID不能为空");
        }
        BabyInfo existing = babyInfoService.selectBabyInfoByIdAndUserId(bo.getId(), userId);
        if (existing == null) {
            return error("萌娃信息不存在");
        }
        BabyInfo babyInfo = buildEntity(bo, userId);
        babyInfo.setId(bo.getId());
        return toAjax(babyInfoService.updateBabyInfo(babyInfo));
    }

    @ApiOperation("删除萌娃")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable("id") Long id) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return error("请先登录");
        }
        BabyInfo existing = babyInfoService.selectBabyInfoByIdAndUserId(id, userId);
        if (existing == null) {
            return error("萌娃信息不存在");
        }
        return toAjax(babyInfoService.deleteBabyInfoByIdAndUserId(id, userId));
    }

    private BabyInfo buildEntity(WxBabySaveBo bo, String userId) {
        BabyInfo babyInfo = new BabyInfo();
        babyInfo.setUserId(userId);
        babyInfo.setRealName(StringUtils.trimToEmpty(bo.getRealName()));
        babyInfo.setNickName(StringUtils.trimToNull(bo.getNickName()));
        babyInfo.setBirthDate(bo.getBirthDate());
        babyInfo.setGender(bo.getGender());
        babyInfo.setSchoolName(StringUtils.trimToEmpty(bo.getSchoolName()));
        babyInfo.setGrade(StringUtils.trimToEmpty(bo.getGrade()));
        babyInfo.setSpecialNote(StringUtils.trimToNull(bo.getSpecialNote()));
        return babyInfo;
    }
}
