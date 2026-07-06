package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.vo.SurveyAssignmentListVo;
import com.ruoyi.system.domain.vo.SurveyFormVo;
import com.ruoyi.system.domain.vo.SurveyUserVo;
import com.ruoyi.system.service.ISurveyService;
import com.ruoyi.web.controller.bussiness.bo.SurveyDistributeBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "【系统管理】问卷管理")
@RestController
@RequestMapping("/system/survey")
public class SurveyAdminController extends BaseController {
    @Autowired
    private ISurveyService surveyService;

    @ApiOperation("查询问卷列表")
    @PreAuthorize("@ss.hasPermi('system:survey:list')")
    @GetMapping("/list")
    public TableDataInfoVo<SurveyFormVo> list(String title, Integer status) {
        return surveyService.selectSurveyFormList(title, status);
    }

    @ApiOperation("获取问卷内容")
    @PreAuthorize("@ss.hasPermi('system:survey:query')")
    @GetMapping("/{formId}")
    public AjaxResult detail(@PathVariable Long formId) {
        try {
            return success(surveyService.selectSurveyFormDetail(formId));
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("搜索小程序用户")
    @PreAuthorize("@ss.hasPermi('system:survey:distribute')")
    @GetMapping("/wx-users")
    public TableDataInfoVo<SurveyUserVo> users(String keyword) {
        return surveyService.searchWxUsers(keyword);
    }

    @ApiOperation("分发问卷")
    @PreAuthorize("@ss.hasPermi('system:survey:distribute')")
    @PostMapping("/{formId}/distribute")
    public AjaxResult distribute(@PathVariable Long formId, @RequestBody SurveyDistributeBo bo) {
        if (bo == null) {
            return error("请选择课程和分发用户");
        }
        try {
            SysUser user = getLoginUser() == null ? null : getLoginUser().getUser();
            String assignedBy = user == null ? null : user.getUserName();
            return success(surveyService.distributeSurvey(formId, bo.getLectureId(), bo.getUserInfoIds(), assignedBy));
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("查询问卷分发用户列表")
    @PreAuthorize("@ss.hasPermi('system:survey:query')")
    @GetMapping("/{formId}/assignments")
    public TableDataInfoVo<SurveyAssignmentListVo> assignments(@PathVariable Long formId, Long lectureId, Integer status, String keyword) {
        return surveyService.selectAssignmentList(formId, lectureId, status, keyword);
    }

    @ApiOperation("获取问卷答题详情")
    @PreAuthorize("@ss.hasPermi('system:survey:query')")
    @GetMapping("/assignments/{assignmentId}")
    public AjaxResult assignmentDetail(@PathVariable Long assignmentId) {
        try {
            return success(surveyService.selectAssignmentDetail(assignmentId));
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }
}
