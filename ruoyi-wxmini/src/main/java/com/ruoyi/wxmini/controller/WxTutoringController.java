package com.ruoyi.wxmini.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.service.IParentsService;
import com.ruoyi.system.service.ITutorsService;
import com.ruoyi.wxmini.bo.WxRealVerifyRequestBo;
import com.ruoyi.wxmini.domain.BabyInfo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.domain.UserServiceAddress;
import com.ruoyi.wxmini.service.IBabyInfoService;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.service.IUserServiceAddressService;
import com.ruoyi.wxmini.service.IWxRealVerifyService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import com.ruoyi.wxmini.vo.WxRealVerifyResultVo;
import com.ruoyi.wxmini.vo.WxTutorDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    @Resource
    private IUserServiceAddressService userServiceAddressService;

    @Resource
    private IWxRealVerifyService wxRealVerifyService;

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
        AjaxResult currentGradeValidation = validateTutorCurrentGrade(tutors);
        if (currentGradeValidation != null) {
            return currentGradeValidation;
        }
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
        AjaxResult currentGradeValidation = validateTutorCurrentGrade(tutors);
        if (currentGradeValidation != null) {
            return currentGradeValidation;
        }
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

    @ApiOperation("实名认证二要素核验（需登录）")
    @PostMapping("/real-verify/verify")
    public AjaxResult verifyRealName(@RequestBody WxRealVerifyRequestBo body) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        WxRealVerifyResultVo result = wxRealVerifyService.verify(userId, body);
        return AjaxResult.success(result);
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

    @ApiOperation("家教单详情（公开）")
    @Anonymous
    @GetMapping("/parents/{id}")
    public AjaxResult getParentDetail(@PathVariable("id") Long id) {
        return AjaxResult.success(parentsService.selectParentsById(id));
    }

    @ApiOperation("发布家教单（需登录）")
    @PostMapping("/parents")
    public AjaxResult addParents(@RequestBody Parents parents) {
        String userId = WxMiniUserContext.getCurrentUserId();
        AjaxResult guard = validateParentPayload(userId, parents, null, true);
        if (guard != null) {
            return guard;
        }
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        UserServiceAddress address = userServiceAddressService.selectAddressByIdAndUserId(parents.getAddressId(), userId);
        applyAddressSnapshot(parents, address);
        long snowflakeId = SnowflakeIdWorker.nextIdDefault();
        parents.setId(snowflakeId);
        parents.setWechatUid(userId);
        if (userInfo != null && userInfo.getId() != null) {
            parents.setSystemUid(userInfo.getId());
        }
        if (parents.getStatus() == null) {
            parents.setStatus(0L);
        }
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

    @ApiOperation("查看当前登录用户发布的单条家教单详情（需登录）")
    @GetMapping("/parents/mine/detail")
    public AjaxResult getMyParentDetail() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        Parents parents = parentsService.selectSingleParentByWechatUid(userId);
        return AjaxResult.success(parents);
    }

    @ApiOperation("修改当前登录用户发布的家教单（需登录）")
    @PutMapping("/parents/{id}")
    public AjaxResult updateMyParent(@PathVariable("id") Long id, @RequestBody Parents parents) {
        String userId = WxMiniUserContext.getCurrentUserId();
        AjaxResult guard = validateParentPayload(userId, parents, id, false);
        if (guard != null) {
            return guard;
        }
        Parents existing = parentsService.selectParentsById(id);
        UserServiceAddress address = userServiceAddressService.selectAddressByIdAndUserId(parents.getAddressId(), userId);
        applyAddressSnapshot(parents, address);
        parents.setId(existing.getId());
        parents.setWechatUid(existing.getWechatUid());
        parents.setSystemUid(existing.getSystemUid());
        if (parents.getStatus() == null) {
            parents.setStatus(existing.getStatus());
        }
        int rows = parentsService.updateParents(parents);
        return rows > 0 ? AjaxResult.success("操作成功") : AjaxResult.error("更新失败");
    }

    @ApiOperation("删除当前登录用户发布的家教单（需登录）")
    @DeleteMapping("/parents/{id}")
    public AjaxResult deleteMyParent(@PathVariable("id") Long id) {
        String userId = WxMiniUserContext.getCurrentUserId();
        AjaxResult guard = validateParentOwner(userId, null, id);
        if (guard != null) {
            return guard;
        }
        int rows = parentsService.deleteParentsById(id);
        return rows > 0 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    private AjaxResult validateParentPayload(String userId, Parents parents, Long demandId, boolean creating) {
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
        if (parents == null) {
            return AjaxResult.error("参数错误");
        }
        if (creating && parentsService.selectSingleParentByWechatUid(userId) != null) {
            return AjaxResult.error("你已发布需求，请前往详情编辑");
        }
        if (StringUtils.isBlank(parents.getName())) {
            return AjaxResult.error("请填写需求描述");
        }
        if (!StringUtils.defaultString(parents.getPhone()).matches("^1[3-9]\\d{9}$")) {
            return AjaxResult.error("请填写正确的手机号");
        }
        if (parents.getBabyId() == null) {
            return AjaxResult.error("请选择服务萌娃");
        }
        if (parents.getAddressId() == null) {
            return AjaxResult.error("请选择服务地址");
        }
        if (StringUtils.isBlank(parents.getGrade())) {
            return AjaxResult.error("请选择年级");
        }
        if (StringUtils.isBlank(parents.getSubject())) {
            return AjaxResult.error("请选择学科");
        }
        if (StringUtils.isBlank(parents.getServiceTimes())) {
            return AjaxResult.error("请选择服务时段");
        }
        if (parents.getMethods() == null) {
            return AjaxResult.error("请选择授课方式");
        }
        if (StringUtils.isBlank(parents.getDemandItems())) {
            return AjaxResult.error("请选择服务需求项目");
        }
        if (parents.getGenderRequirement() == null) {
            return AjaxResult.error("请选择陪伴官性别要求");
        }
        if (parents.getHourlyBudget() == null || parents.getHourlyBudget().compareTo(BigDecimal.ZERO) <= 0) {
            return AjaxResult.error("请填写时薪预算");
        }
        parents.setName(parents.getName().trim());
        parents.setPhone(parents.getPhone().trim());
        parents.setGrade(parents.getGrade().trim());
        parents.setSubject(parents.getSubject().trim());
        parents.setServiceTimes(parents.getServiceTimes().trim());
        parents.setDemandItems(parents.getDemandItems().trim());
        if (StringUtils.isNotBlank(parents.getRequirements())) {
            parents.setRequirements(parents.getRequirements().trim());
        }
        if (StringUtils.isNotBlank(parents.getBrief())) {
            parents.setBrief(parents.getBrief().trim());
        }
        AjaxResult ownerGuard = validateParentOwner(userId, parents.getBabyId(), demandId);
        if (ownerGuard != null) {
            return ownerGuard;
        }
        UserServiceAddress address = userServiceAddressService.selectAddressByIdAndUserId(parents.getAddressId(), userId);
        if (address == null) {
            return AjaxResult.error("服务地址不存在或无权使用");
        }
        AjaxResult slotGuard = normalizeScheduleFields(parents);
        if (slotGuard != null) {
            return slotGuard;
        }
        return null;
    }

    private AjaxResult validateParentOwner(String userId, Long babyId, Long demandId) {
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null) {
            return AjaxResult.error("用户不存在");
        }
        if (demandId != null) {
            Parents existing = parentsService.selectParentsById(demandId);
            if (existing == null) {
                return AjaxResult.error("需求不存在");
            }
            if (!StringUtils.equals(existing.getWechatUid(), userId)) {
                return AjaxResult.error("无权操作该需求");
            }
        }
        if (babyId == null) {
            return null;
        }
        BabyInfo babyInfo = babyInfoService.selectBabyInfoByIdAndUserId(babyId, userId);
        if (babyInfo == null) {
            return AjaxResult.error("萌娃信息不存在或无权使用");
        }
        return null;
    }

    private void applyAddressSnapshot(Parents parents, UserServiceAddress address) {
        parents.setAddressId(address.getId());
        parents.setRegion(StringUtils.defaultString(address.getRegion()));
        StringBuilder locationBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(address.getLocation())) {
            locationBuilder.append(address.getLocation().trim());
        }
        if (StringUtils.isNotBlank(address.getAddressDetail())) {
            if (locationBuilder.length() > 0) {
                locationBuilder.append(' ');
            }
            locationBuilder.append(address.getAddressDetail().trim());
        }
        if (StringUtils.isNotBlank(address.getDoorplate())) {
            if (locationBuilder.length() > 0) {
                locationBuilder.append(' ');
            }
            locationBuilder.append(address.getDoorplate().trim());
        }
        parents.setLocation(locationBuilder.toString().trim());
        parents.setGeo(address.getGeo());
    }

    private AjaxResult normalizeScheduleFields(Parents parents) {
        JSONArray array;
        try {
            array = JSON.parseArray(StringUtils.defaultString(parents.getServiceTimes()));
        } catch (Exception e) {
            return AjaxResult.error("请完善服务时段");
        }
        if (array == null || array.isEmpty()) {
            return AjaxResult.error("请完善服务时段");
        }
        List<JSONObject> normalizedSlots = new ArrayList<>();
        Set<String> serviceDateSet = new LinkedHashSet<>();
        Set<String> weekDaySet = new LinkedHashSet<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject item = array.getJSONObject(i);
            String serviceDate = StringUtils.trimToEmpty(item.getString("serviceDate"));
            String startTime = StringUtils.trimToEmpty(item.getString("startTime"));
            String endTime = StringUtils.trimToEmpty(item.getString("endTime"));
            if (!isValidDate(serviceDate) || !isValidTime(startTime) || !isValidTime(endTime)) {
                return AjaxResult.error("请完善服务时段");
            }
            JSONObject slot = new JSONObject();
            slot.put("serviceDate", serviceDate);
            slot.put("startTime", startTime);
            slot.put("endTime", endTime);
            normalizedSlots.add(slot);
            serviceDateSet.add(serviceDate);
            weekDaySet.add(resolveWeekDay(serviceDate));
        }
        if (normalizedSlots.isEmpty()) {
            return AjaxResult.error("请完善服务时段");
        }
        parents.setServiceTimes(JSON.toJSONString(normalizedSlots));
        parents.setServiceDates(String.join(",", serviceDateSet));
        parents.setDayOfWeek(String.join(",", weekDaySet));
        parents.setStartTime(LocalTime.parse(normalizedSlots.get(0).getString("startTime")));
        parents.setEndTime(LocalTime.parse(normalizedSlots.get(0).getString("endTime")));
        return null;
    }

    private boolean isValidTime(String value) {
        if (!StringUtils.isNotBlank(value)) {
            return false;
        }
        try {
            LocalTime.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidDate(String value) {
        if (!StringUtils.isNotBlank(value)) {
            return false;
        }
        try {
            LocalDate.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private String resolveWeekDay(String serviceDate) {
        int value = LocalDate.parse(serviceDate).getDayOfWeek().getValue();
        return String.valueOf(value);
    }

    private AjaxResult validateTutorCurrentGrade(Tutors tutors) {
        if (tutors == null) {
            return AjaxResult.error("参数错误");
        }
        if (Long.valueOf(0L).equals(tutors.getIdentity())) {
            if (StringUtils.isBlank(tutors.getCurrentGrade())) {
                return AjaxResult.error("请选择当前年级");
            }
            tutors.setCurrentGrade(tutors.getCurrentGrade().trim());
            return null;
        }
        tutors.setCurrentGrade(null);
        return null;
    }
}
