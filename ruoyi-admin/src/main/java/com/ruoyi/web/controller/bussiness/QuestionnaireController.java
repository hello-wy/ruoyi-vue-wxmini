package com.ruoyi.web.controller.bussiness;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Questionnaire;
import com.ruoyi.system.service.IQuestionnaireService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfoVo;

/**
 * 问卷调查配置Controller
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
@Api(tags = "问卷调查配置管理")
@RestController
@RequestMapping("/system/questionnaire")
public class QuestionnaireController extends BaseController
{
    @Autowired
    private IQuestionnaireService questionnaireService;

    /**
     * 查询问卷调查配置列表
     */
    @ApiOperation("查询问卷调查配置列表")
    @PreAuthorize("@ss.hasPermi('system:questionnaire:list')")
    @GetMapping("/list")
    public TableDataInfoVo<Questionnaire> list(Questionnaire questionnaire)
    {
        startPage();
        List<Questionnaire> list = questionnaireService.selectQuestionnaireList(questionnaire);
        return getDataTable(list);
    }

    /**
     * 导出问卷调查配置列表
     */
    @ApiOperation("导出问卷调查配置列表")
    @PreAuthorize("@ss.hasPermi('system:questionnaire:export')")
    @Log(title = "问卷调查配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Questionnaire questionnaire)
    {
        List<Questionnaire> list = questionnaireService.selectQuestionnaireList(questionnaire);
        ExcelUtil<Questionnaire> util = new ExcelUtil<Questionnaire>(Questionnaire.class);
        util.exportExcel(response, list, "问卷调查配置数据");
    }

    /**
     * 获取问卷调查配置详细信息
     */
    @ApiOperation("获取问卷调查配置详细信息")
    @ApiImplicitParam(name = "id", value = "问卷ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('system:questionnaire:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(questionnaireService.selectQuestionnaireById(id));
    }

    /**
     * 新增问卷调查配置
     */
    @ApiOperation("新增问卷调查配置")
    @PreAuthorize("@ss.hasPermi('system:questionnaire:add')")
    @Log(title = "问卷调查配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Questionnaire questionnaire)
    {
        return toAjax(questionnaireService.insertQuestionnaire(questionnaire));
    }

    /**
     * 修改问卷调查配置
     */
    @ApiOperation("修改问卷调查配置")
    @PreAuthorize("@ss.hasPermi('system:questionnaire:edit')")
    @Log(title = "问卷调查配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Questionnaire questionnaire)
    {
        return toAjax(questionnaireService.updateQuestionnaire(questionnaire));
    }

    /**
     * 删除问卷调查配置
     */
    @ApiOperation("删除问卷调查配置")
    @ApiImplicitParam(name = "ids", value = "问卷ID数组", required = true, dataType = "Long[]", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('system:questionnaire:remove')")
    @Log(title = "问卷调查配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(questionnaireService.deleteQuestionnaireByIds(ids));
    }

    /**
     * 根据课程ID查询关联的问卷列表
     */
    @ApiOperation("根据课程ID查询关联的问卷列表")
    @ApiImplicitParam(name = "lecturesId", value = "课程/讲座ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    @GetMapping("/getQuestionnaire/{lecturesId}")
    public TableDataInfoVo<Questionnaire> getQuestionnaire(@PathVariable Long lecturesId)
    {
        startPage();
        List<Questionnaire> list = questionnaireService.selectRecentQuestionnaireList(lecturesId);
        return getDataTable(list);
    }
}
