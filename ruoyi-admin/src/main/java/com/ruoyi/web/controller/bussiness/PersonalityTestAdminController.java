package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.vo.PersonalityTestAdminAttemptVo;
import com.ruoyi.system.domain.vo.PersonalityTestAdminDetailVo;
import com.ruoyi.system.service.IPersonalityTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "【系统管理】性格测试管理")
@RestController
@RequestMapping("/system/personality-test")
public class PersonalityTestAdminController extends BaseController {
    @Autowired
    private IPersonalityTestService personalityTestService;

    @ApiOperation("获取性格测试填写人数")
    @PreAuthorize("@ss.hasPermi('system:personality-test:query')")
    @GetMapping("/count")
    public AjaxResult count() {
        try {
            return success(personalityTestService.countCompletedAttempts());
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("查询性格测试填写记录列表")
    @PreAuthorize("@ss.hasPermi('system:personality-test:list')")
    @GetMapping("/list")
    public TableDataInfoVo<PersonalityTestAdminAttemptVo> list(Long testId, String userInfoId, Integer status, String startTime, String endTime) {
        return personalityTestService.selectAdminAttemptList(testId, userInfoId, status, startTime, endTime);
    }

    @ApiOperation("获取性格测试填写记录详情")
    @PreAuthorize("@ss.hasPermi('system:personality-test:query')")
    @GetMapping("/{attemptId}")
    public AjaxResult detail(@PathVariable Long attemptId) {
        try {
            PersonalityTestAdminDetailVo detail = personalityTestService.selectAdminAttemptDetail(attemptId);
            return success(detail);
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }
}
