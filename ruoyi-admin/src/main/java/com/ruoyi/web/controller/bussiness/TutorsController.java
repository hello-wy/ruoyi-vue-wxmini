package com.ruoyi.web.controller.bussiness;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.service.ITutorsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 大学生/教员Controller
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/system/tutors")
public class TutorsController extends BaseController
{
    @Autowired
    private ITutorsService tutorsService;

    /**
     * 查询大学生/教员列表
     */
    @PreAuthorize("@ss.hasPermi('system:tutors:list')")
    @GetMapping("/list")
    public TableDataInfo list(Tutors tutors)
    {
        startPage();
        List<Tutors> list = tutorsService.selectTutorsList(tutors);
        return getDataTable(list);
    }

    /**
     * 导出大学生/教员列表
     */
    @PreAuthorize("@ss.hasPermi('system:tutors:export')")
    @Log(title = "大学生/教员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Tutors tutors)
    {
        List<Tutors> list = tutorsService.selectTutorsList(tutors);
        ExcelUtil<Tutors> util = new ExcelUtil<Tutors>(Tutors.class);
        util.exportExcel(response, list, "大学生/教员数据");
    }

    /**
     * 获取大学生/教员详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tutors:query')")
    @GetMapping(value = "/{uid}")
    public AjaxResult getInfo(@PathVariable("uid") Long uid)
    {
        return success(tutorsService.selectTutorsByUid(uid));
    }

    /**
     * 新增大学生/教员
     */
    @PreAuthorize("@ss.hasPermi('system:tutors:add')")
    @Log(title = "大学生/教员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Tutors tutors)
    {
        return toAjax(tutorsService.insertTutors(tutors));
    }

    /**
     * 修改大学生/教员
     */
    @PreAuthorize("@ss.hasPermi('system:tutors:edit')")
    @Log(title = "大学生/教员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Tutors tutors)
    {
        return toAjax(tutorsService.updateTutors(tutors));
    }

    /**
     * 删除大学生/教员
     */
    @PreAuthorize("@ss.hasPermi('system:tutors:remove')")
    @Log(title = "大学生/教员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{uids}")
    public AjaxResult remove(@PathVariable Long[] uids)
    {
        return toAjax(tutorsService.deleteTutorsByUids(uids));
    }
}
