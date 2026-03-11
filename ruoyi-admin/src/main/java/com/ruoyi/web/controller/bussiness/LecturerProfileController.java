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
import com.ruoyi.system.domain.LecturerProfile;
import com.ruoyi.system.service.ILecturerProfileService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 讲师风采Controller
 * 
 * @author ruoyi
 * @date 2026-03-07
 */
@RestController
@RequestMapping("/system/profile")
public class LecturerProfileController extends BaseController
{
    @Autowired
    private ILecturerProfileService lecturerProfileService;

    /**
     * 查询讲师风采列表
     */
    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(LecturerProfile lecturerProfile)
    {
        startPage();
        List<LecturerProfile> list = lecturerProfileService.selectLecturerProfileList(lecturerProfile);
        return getDataTable(list);
    }

    /**
     * 导出讲师风采列表
     */
    @PreAuthorize("@ss.hasPermi('system:profile:export')")
    @Log(title = "讲师风采", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LecturerProfile lecturerProfile)
    {
        List<LecturerProfile> list = lecturerProfileService.selectLecturerProfileList(lecturerProfile);
        ExcelUtil<LecturerProfile> util = new ExcelUtil<LecturerProfile>(LecturerProfile.class);
        util.exportExcel(response, list, "讲师风采数据");
    }

    /**
     * 获取讲师风采详细信息
     */
    @Anonymous
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lecturerProfileService.selectLecturerProfileById(id));
    }

    /**
     * 新增讲师风采
     */
    @PreAuthorize("@ss.hasPermi('system:profile:add')")
    @Log(title = "讲师风采", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LecturerProfile lecturerProfile)
    {
        return toAjax(lecturerProfileService.insertLecturerProfile(lecturerProfile));
    }

    /**
     * 修改讲师风采
     */
    @PreAuthorize("@ss.hasPermi('system:profile:edit')")
    @Log(title = "讲师风采", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LecturerProfile lecturerProfile)
    {
        return toAjax(lecturerProfileService.updateLecturerProfile(lecturerProfile));
    }

    /**
     * 删除讲师风采
     */
    @PreAuthorize("@ss.hasPermi('system:profile:remove')")
    @Log(title = "讲师风采", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lecturerProfileService.deleteLecturerProfileByIds(ids));
    }
}
