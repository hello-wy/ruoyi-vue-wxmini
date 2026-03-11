package com.ruoyi.web.controller.bussiness;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
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
import com.ruoyi.system.domain.SalonInfo;
import com.ruoyi.system.service.ISalonInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 沙龙活动信息主Controller
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
@RestController
@RequestMapping("/system/info")
public class SalonInfoController extends BaseController
{
    @Autowired
    private ISalonInfoService salonInfoService;

    /**
     * 查询沙龙活动信息主列表
     */
    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(SalonInfo salonInfo)
    {
        startPage();
        List<SalonInfo> list = salonInfoService.selectSalonInfoList(salonInfo);
        return getDataTable(list);
    }

    /**
     * 导出沙龙活动信息主列表
     */
    @PreAuthorize("@ss.hasPermi('system:info:export')")
    @Log(title = "沙龙活动信息主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SalonInfo salonInfo)
    {
        List<SalonInfo> list = salonInfoService.selectSalonInfoList(salonInfo);
        ExcelUtil<SalonInfo> util = new ExcelUtil<SalonInfo>(SalonInfo.class);
        util.exportExcel(response, list, "沙龙活动信息主数据");
    }

    /**
     * 获取沙龙活动信息主详细信息
     */
    @Anonymous
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(salonInfoService.selectSalonInfoById(id));
    }

    /**
     * 新增沙龙活动信息主
     */
    @PreAuthorize("@ss.hasPermi('system:info:add')")
    @Log(title = "沙龙活动信息主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SalonInfo salonInfo)
    {
        return toAjax(salonInfoService.insertSalonInfo(salonInfo));
    }

    /**
     * 修改沙龙活动信息主
     */
    @PreAuthorize("@ss.hasPermi('system:info:edit')")
    @Log(title = "沙龙活动信息主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SalonInfo salonInfo)
    {
        return toAjax(salonInfoService.updateSalonInfo(salonInfo));
    }

    /**
     * 删除沙龙活动信息主
     */
    @PreAuthorize("@ss.hasPermi('system:info:remove')")
    @Log(title = "沙龙活动信息主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(salonInfoService.deleteSalonInfoByIds(ids));
    }
}
