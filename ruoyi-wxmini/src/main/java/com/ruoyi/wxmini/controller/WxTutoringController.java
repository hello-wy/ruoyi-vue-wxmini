package com.ruoyi.wxmini.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.service.IParentsService;
import com.ruoyi.system.service.ITutorsService;
import com.ruoyi.wxmini.domain.BabyInfo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IBabyInfoService;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import com.ruoyi.wxmini.vo.WxTutorDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "【小程序】教员与家教单")
@Slf4j
@RestController
@RequestMapping("/wxmini/tutoring")
public class WxTutoringController extends BaseController {

    @Resource
    private ITutorsService tutorsService;

    @Resource
    private IParentsService parentsService;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private IBabyInfoService babyInfoService;

    @ApiOperation("分页查询已通过教员列表（公开，支持科目/区域/授课方式/学历筛选）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "页码，默认1", dataType = "long", paramType = "query", dataTypeClass = Long.class),
        @ApiImplicitParam(name = "pageSize", value = "每页数量，默认5", dataType = "long", paramType = "query", dataTypeClass = Long.class),
        @ApiImplicitParam(name = "subject", value = "科目筛选（模糊）", dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "region", value = "区域筛选（模糊）", dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "methods", value = "授课方式筛选（精确）", dataType = "Long", paramType = "query", dataTypeClass = Long.class),
        @ApiImplicitParam(name = "grade", value = "学历筛选（精确）", dataType = "Long", paramType = "query", dataTypeClass = Long.class)
    })
    @Anonymous
    @GetMapping("/tutors/list")
    public TableDataInfoVo<Tutors> listTutors(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "5") long pageSize,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Long methods,
            @RequestParam(required = false) Long grade) {
        Tutors filter = new Tutors();
        filter.setSubjects(subject);
        filter.setAreas(region);
        filter.setMethods(methods);
        filter.setDegree(grade);
        startPage();
        List<Tutors> list = tutorsService.selectCertifiedTutorsList(filter);
        return getDataTable(list);
    }

    @ApiOperation("教员详情（公开）")
    @ApiImplicitParam(name = "id", value = "教员ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    @Anonymous
    @GetMapping("/tutors/{id}")
    public AjaxResult tutorDetail(@PathVariable("id") Long id) {
        Tutors tutors = tutorsService.selectTutorsById(id);
        if (tutors == null || tutors.getStatus() == null || !tutors.getStatus().equals(1L)) {
            return AjaxResult.error("教员不存在");
        }
        WxTutorDetailVo detailVo = new WxTutorDetailVo();
        BeanUtils.copyProperties(tutors, detailVo);
        return AjaxResult.success(detailVo);
    }

    @ApiOperation("申请成为教员（status 默认 0 待审核，需登录）")
    @PostMapping("/apply")
    public AjaxResult applyTutor(@RequestBody Tutors tutors) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        Tutors existing = tutorsService.selectTutorsByUid(userId);
        if (existing != null) {
            return AjaxResult.error("您已提交过申请，请勿重复提交");
        }
        int updated = userInfoService.updateRealnameInfo(userId, tutors.getRealName(), tutors.getIdCard());
        if (updated <= 0) {
            return AjaxResult.error("用户不存在");
        }
        tutors.setUid(userId);
        tutors.setStatus(0L);
        tutors.setRealName(null);
        tutors.setIdCard(null);
        int rows = tutorsService.insertTutors(tutors);
        return rows > 0 ? AjaxResult.success("申请成功，请等待审核", tutors.getId()) : AjaxResult.error("申请失败");
    }

    @ApiOperation("更新当前登录用户的教员资料（重新提交审核，需登录）")
    @PostMapping("/mine/update")
    public AjaxResult updateMyTutor(@RequestBody Tutors tutors) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        Tutors existing = tutorsService.selectTutorsByUid(userId);
        if (existing == null) {
            return AjaxResult.error("请先提交申请");
        }
        int updated = userInfoService.updateRealnameInfo(userId, tutors.getRealName(), tutors.getIdCard());
        if (updated <= 0) {
            return AjaxResult.error("用户不存在");
        }
        tutors.setId(existing.getId());
        tutors.setUid(userId);
        tutors.setStatus(0L);
        tutors.setRealName(null);
        tutors.setIdCard(null);
        int rows = tutorsService.updateTutors(tutors);
        return rows > 0 ? AjaxResult.success("资料已更新，请等待审核") : AjaxResult.error("更新失败");
    }

    @ApiOperation("查看当前登录用户的教员信息（需登录）")
    @GetMapping("/mine")
    public AjaxResult myTutor() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        Tutors tutors = tutorsService.selectTutorsByUid(userId);
        return AjaxResult.success(tutors);
    }

    @ApiOperation("分页查询有效家教单列表（公开，支持科目/区域/辅导方式/年级筛选）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "页码，默认1", dataType = "long", paramType = "query", dataTypeClass = Long.class),
        @ApiImplicitParam(name = "pageSize", value = "每页数量，默认10", dataType = "long", paramType = "query", dataTypeClass = Long.class),
        @ApiImplicitParam(name = "subject", value = "科目筛选（模糊）", dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "region", value = "区域筛选（模糊）", dataType = "String", paramType = "query", dataTypeClass = String.class),
        @ApiImplicitParam(name = "methods", value = "辅导方式筛选（精确）", dataType = "Long", paramType = "query", dataTypeClass = Long.class),
        @ApiImplicitParam(name = "grade", value = "年级筛选（模糊）", dataType = "String", paramType = "query", dataTypeClass = String.class)
    })
    @Anonymous
    @GetMapping("/parents/list")
    public TableDataInfoVo<Parents> listParents(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Long methods,
            @RequestParam(required = false) String grade) {
        Parents filter = new Parents();
        filter.setSubject(subject);
        filter.setRegion(region);
        filter.setMethods(methods);
        filter.setGrade(grade);
        startPage();
        List<Parents> list = parentsService.selectActiveParentsList(filter);
        return getDataTable(list);
    }

    @ApiOperation("发布家教单（需登录）")
    @PostMapping("/parents")
    public AjaxResult addParents(@RequestBody Parents parents) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null) {
            return AjaxResult.error("用户不存在");
        }
        if (userInfo.getUserType() == null || userInfo.getUserType() != 0) {
            return AjaxResult.error("请先切换为家长身份");
        }
        if (parents.getBabyId() == null) {
            return AjaxResult.error("请选择服务萌娃");
        }
        BabyInfo babyInfo = babyInfoService.selectBabyInfoByIdAndUserId(parents.getBabyId(), userId);
        if (babyInfo == null) {
            return AjaxResult.error("萌娃信息不存在或无权使用");
        }
        long snowflakeId = SnowflakeIdWorker.nextIdDefault();
        parents.setId(snowflakeId);
        parents.setWechatUid(userId);
        parentsService.insertParents(parents);
        return AjaxResult.success("操作成功", snowflakeId);
    }

    @ApiOperation("查看当前登录用户发布的家教单（需登录）")
    @GetMapping("/parents/mine")
    public AjaxResult myParents() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null) {
            return AjaxResult.error("用户不存在");
        }
        List<Parents> list = parentsService.selectParentsByWechatUid(userId);
        return AjaxResult.success(list);
    }
}
