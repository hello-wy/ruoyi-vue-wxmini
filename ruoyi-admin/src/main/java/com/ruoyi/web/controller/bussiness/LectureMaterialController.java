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
import com.ruoyi.system.domain.LectureMaterial;
import com.ruoyi.system.service.ILectureMaterialService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfoVo;

/**
 * 资料中心数据Controller
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
@Api(tags = "资料中心管理")
@RestController
@RequestMapping("/system/material")
public class LectureMaterialController extends BaseController
{
    @Autowired
    private ILectureMaterialService lectureMaterialService;

    /**
     * 查询资料中心数据列表
     */
    @ApiOperation("查询资料中心数据列表")
    @PreAuthorize("@ss.hasPermi('system:material:list')")
    @GetMapping("/list")
    public TableDataInfoVo<LectureMaterial> list(LectureMaterial lectureMaterial)
    {
        startPage();
        List<LectureMaterial> list = lectureMaterialService.selectLectureMaterialList(lectureMaterial);
        return getDataTable(list);
    }

    /**
     * 导出资料中心数据列表
     */
    @ApiOperation("导出资料中心数据列表")
    @PreAuthorize("@ss.hasPermi('system:material:export')")
    @Log(title = "资料中心数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LectureMaterial lectureMaterial)
    {
        List<LectureMaterial> list = lectureMaterialService.selectLectureMaterialList(lectureMaterial);
        ExcelUtil<LectureMaterial> util = new ExcelUtil<LectureMaterial>(LectureMaterial.class);
        util.exportExcel(response, list, "资料中心数据数据");
    }

    /**
     * 获取资料中心数据详细信息
     */
    @ApiOperation("获取资料中心数据详细信息")
    @ApiImplicitParam(name = "id", value = "资料ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('system:material:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lectureMaterialService.selectLectureMaterialById(id));
    }

    /**
     * 新增资料中心数据
     */
    @ApiOperation("新增资料中心数据")
    @PreAuthorize("@ss.hasPermi('system:material:add')")
    @Log(title = "资料中心数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LectureMaterial lectureMaterial)
    {
        return toAjax(lectureMaterialService.insertLectureMaterial(lectureMaterial));
    }

    /**
     * 修改资料中心数据
     */
    @ApiOperation("修改资料中心数据")
    @PreAuthorize("@ss.hasPermi('system:material:edit')")
    @Log(title = "资料中心数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LectureMaterial lectureMaterial)
    {
        return toAjax(lectureMaterialService.updateLectureMaterial(lectureMaterial));
    }

    /**
     * 删除资料中心数据
     */
    @ApiOperation("删除资料中心数据")
    @ApiImplicitParam(name = "ids", value = "资料ID数组", required = true, dataType = "Long[]", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('system:material:remove')")
    @Log(title = "资料中心数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lectureMaterialService.deleteLectureMaterialByIds(ids));
    }
}
