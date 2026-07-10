package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.bo.PersonalityTestAnswerBo;
import com.ruoyi.system.service.IPersonalityTestService;
import com.ruoyi.wxmini.bo.WxPersonalityAnswerBatchBo;
import com.ruoyi.wxmini.bo.WxPersonalityAnswerBo;
import com.ruoyi.wxmini.bo.WxPersonalityAttemptBo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "【小程序】独立性格测试")
@RestController
@RequestMapping("/wxmini/personality-test")
public class WxPersonalityTestController extends BaseController {
    @Autowired
    private IPersonalityTestService personalityTestService;

    @Autowired
    private IUserInfoService userInfoService;

    @ApiOperation("获取性格测试入口状态")
    @GetMapping("/entry")
    public AjaxResult entry() {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return error("请先登录");
        }
        try {
            return success(personalityTestService.getEntry(userInfo.getId()));
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("获取已完成的性格测试记录")
    @GetMapping("/attempts")
    public AjaxResult attempts() {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return error("请先登录");
        }
        return success(personalityTestService.getCompletedAttempts(userInfo.getId()));
    }

    @ApiOperation("开始或继续性格测试")
    @PostMapping("/attempts")
    public AjaxResult start(@RequestBody(required = false) WxPersonalityAttemptBo bo) {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return error("请先登录");
        }
        try {
            boolean restart = bo != null && "restart".equals(bo.getMode());
            return success(personalityTestService.startAttempt(userInfo.getId(), userInfo.getUserId(), restart));
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("获取当前应答题目")
    @GetMapping("/attempts/{attemptId}/current-question")
    public AjaxResult currentQuestion(@PathVariable("attemptId") Long attemptId) {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return error("请先登录");
        }
        try {
            return success(personalityTestService.getCurrentQuestion(attemptId, userInfo.getId()));
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("获取全部性格测试题目及已选答案")
    @GetMapping("/attempts/{attemptId}/questions")
    public AjaxResult questions(@PathVariable("attemptId") Long attemptId) {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return error("请先登录");
        }
        try {
            return success(personalityTestService.getQuestions(attemptId, userInfo.getId()));
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("按题号获取性格测试题目")
    @GetMapping("/attempts/{attemptId}/questions/{questionNo}")
    public AjaxResult question(@PathVariable("attemptId") Long attemptId,
                               @PathVariable("questionNo") Integer questionNo) {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return error("请先登录");
        }
        try {
            return success(personalityTestService.getQuestion(attemptId, userInfo.getId(), questionNo));
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("保存当前题答案并返回下一题")
    @PostMapping("/attempts/{attemptId}/answers")
    public AjaxResult saveAnswer(@PathVariable("attemptId") Long attemptId,
                                 @RequestBody @Valid WxPersonalityAnswerBo bo) {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return error("请先登录");
        }
        try {
            return success(personalityTestService.saveAnswer(attemptId, userInfo.getId(), bo.getQuestionId(), bo.getAnswerValue()));
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("批量保存性格测试答案")
    @PostMapping("/attempts/{attemptId}/answers/batch")
    public AjaxResult saveAnswers(@PathVariable("attemptId") Long attemptId,
                                  @RequestBody @Valid WxPersonalityAnswerBatchBo bo) {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return error("请先登录");
        }
        try {
            return success(personalityTestService.saveAnswers(attemptId, userInfo.getId(), toServiceAnswers(bo.getAnswers())));
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("重新开始性格测试")
    @PostMapping("/restart")
    public AjaxResult restart() {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return error("请先登录");
        }
        try {
            return success(personalityTestService.startAttempt(userInfo.getId(), userInfo.getUserId(), true));
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }

    @ApiOperation("获取性格测试完成结果")
    @GetMapping("/attempts/{attemptId}/result")
    public AjaxResult result(@PathVariable("attemptId") Long attemptId) {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            return error("请先登录");
        }
        try {
            return success(personalityTestService.getResult(attemptId, userInfo.getId()));
        } catch (ServiceException e) {
            return error(e.getMessage());
        }
    }

    // Admin endpoints and helpers removed and migrated to PersonalityTestAdminController

    private List<PersonalityTestAnswerBo> toServiceAnswers(List<WxPersonalityAnswerBo> answers) {
        List<PersonalityTestAnswerBo> serviceAnswers = new ArrayList<>(answers.size());
        for (WxPersonalityAnswerBo answer : answers) {
            PersonalityTestAnswerBo serviceAnswer = new PersonalityTestAnswerBo();
            serviceAnswer.setQuestionId(answer.getQuestionId());
            serviceAnswer.setAnswerValue(answer.getAnswerValue());
            serviceAnswers.add(serviceAnswer);
        }
        return serviceAnswers;
    }

    private UserInfo getCurrentUserInfo() {
        String wxUserId = WxMiniUserContext.getCurrentUserId();
        if (StringUtils.isBlank(wxUserId)) {
            return null;
        }
        return userInfoService.selectUserInfoByUserId(wxUserId);
    }
}
