package com.ruoyi.wxmini.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.service.IParentsService;
import com.ruoyi.system.service.ITutorsService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 微信小程序 - 教员 & 家教单 接口
 *
 * 鉴权：统一使用 WxMiniJwtFilter，通过 Wx-Authorization: Bearer {token} 请求头校验
 *
 * @author ruoyi
 */
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

    // ===================== Tutors =====================

    /**
     * 分页查询已认证教员列表（isCertified = 1），支持多条件筛选
     * 无需登录（公开接口）
     *
     * @param pageNum  页码，默认 1
     * @param pageSize 每页数量，默认 10
     * @param subject 科目筛选（模糊）
     * @param region    区域筛选（模糊）
     * @param methods  授课方式筛选（精确）
     * @param grade   学历筛选（精确）
     */
    @GetMapping("/tutors/list")
    public TableDataInfo listTutors(
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
        startPage();
        List<Tutors> list = tutorsService.selectCertifiedTutorsList(filter);
        return getDataTable(list);
    }

    /**
     * 当前登录用户申请成为教员（新增 tutors，isCertified 默认为 0 待审核）
     * 需要 Wx-Authorization 登录
     */
    @PostMapping("/apply")
    public AjaxResult applyTutor(@RequestBody Tutors tutors) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        // 检查是否已申请
        Tutors existing = tutorsService.selectTutorsByUid(Long.valueOf(userId));
        if (existing != null) {
            return AjaxResult.error("您已提交过申请，请勿重复提交");
        }
        tutors.setUid(Long.valueOf(userId));
        tutors.setIsCertified(0L); // 待审核
        int rows = tutorsService.insertTutors(tutors);
        return rows > 0 ? AjaxResult.success("申请成功，请等待审核") : AjaxResult.error("申请失败");
    }

    /**
     * 管理员审核教员（修改 isCertified：0-待审核 / 1-已通过 / 2-已拒绝）
     * 需要管理员权限（wx token 中 userType = "admin"）
     *
     * @param id          tutors 主键
     * @param isCertified 审核结果：0-待审核 1-已通过 2-已拒绝
     */
    @PutMapping("/review")
    public AjaxResult reviewTutor(
            @RequestParam Long id,
            @RequestParam Long isCertified) {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        // 校验管理员身份
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        if (userInfo == null || !"admin".equals(userInfo.getUserType())) {
            return AjaxResult.error("无权限操作");
        }
        if (!isCertified.equals(0L) && !isCertified.equals(1L) && !isCertified.equals(2L)) {
            return AjaxResult.error("isCertified 参数非法，只允许 0/1/2");
        }
        Tutors tutors = new Tutors();
        tutors.setId(id);
        tutors.setIsCertified(isCertified);
        int rows = tutorsService.updateTutors(tutors);
        return rows > 0 ? AjaxResult.success("审核操作成功") : AjaxResult.error("操作失败，记录不存在");
    }

    /**
     * 当前登录用户查看自己的教员信息（uid = 当前用户）
     * 多地区所有的是一个list
     * 需要 Wx-Authorization 登录
     */
    @GetMapping("/mine")
    public AjaxResult myTutor() {
        String userId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(userId)) {
            return AjaxResult.error("请先登录");
        }
        Tutors tutors = tutorsService.selectTutorsByUid(Long.valueOf(userId));
        return AjaxResult.success(tutors);
    }

    // ===================== Parents =====================

    /**
     * 分页查询有效家教单列表（status = 0），支持多条件筛选
     * 无需登录（公开接口）
     *
     * @param pageNum  页码，默认 1
     * @param pageSize 每页数量，默认 10
     * @param subject  科目筛选（模糊）
     * @param region   区域筛选（模糊）
     * @param methods  辅导方式筛选（精确）
     * @param grade    年级筛选（模糊）
     */
    @GetMapping("/parents/list")
    public TableDataInfo listParents(
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

    /**
     * 当前登录用户查看自己发布的家教单（uid = 当前用户）
     * 需要 Wx-Authorization 登录
     */
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
        List<Parents> list = parentsService.selectParentsByUid(userInfo.getId());
        return AjaxResult.success(list);
    }
}
