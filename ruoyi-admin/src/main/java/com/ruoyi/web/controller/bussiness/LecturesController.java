package com.ruoyi.web.controller.bussiness;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.system.domain.vo.LecturesDetailVo;
import com.ruoyi.system.domain.vo.LecturesListVo;
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
import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.service.ILecturesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 课程活动/讲座Controller
 *
 * @author ruoyi
 * @date 2026-03-05
 */
@RestController
@RequestMapping("/system/lectures")
public class LecturesController extends BaseController
{
    @Autowired
    private ILecturesService lecturesService;

    /**
     * 查询课程活动/讲座列表（含讲师姓名拼接）
     */
    @PreAuthorize("@ss.hasPermi('system:lectures:list')")
    @GetMapping("/list")
    public TableDataInfo list(Lectures lectures)
    {
        startPage();
        List<LecturesListVo> list = lecturesService.selectLecturesListVo(lectures);
        return getDataTable(list);
    }

    /**
     * 查询最近一个月课程活动/讲座列表（前端接口）
     */
    @Anonymous
    @GetMapping("/recent")
    public TableDataInfo recentList()
    {
        startPage();
        List<Lectures> list = lecturesService.selectRecentLecturesList();
        return getDataTable(list);
    }

    /**
     * 导出课程活动/讲座列表
     */
    @PreAuthorize("@ss.hasPermi('system:lectures:export')")
    @Log(title = "课程活动/讲座", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Lectures lectures)
    {
        List<Lectures> list = lecturesService.selectLecturesList(lectures);
        ExcelUtil<Lectures> util = new ExcelUtil<Lectures>(Lectures.class);
        util.exportExcel(response, list, "课程活动/讲座数据");
    }

    /**
     * 获取课程活动/讲座详细信息（含讲师 id/name/avatarUrl）
     */
    @Anonymous
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        LecturesDetailVo vo = lecturesService.selectLecturesDetailById(id);
        return success(vo);
    }

    /**
     * 新增课程活动/讲座
     */
    @PreAuthorize("@ss.hasPermi('system:lectures:add')")
    @Log(title = "课程活动/讲座", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Lectures lectures)
    {
        return toAjax(lecturesService.insertLectures(lectures));
    }

    /**
     * 修改课程活动/讲座
     */
    @PreAuthorize("@ss.hasPermi('system:lectures:edit')")
    @Log(title = "课程活动/讲座", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Lectures lectures)
    {
        return toAjax(lecturesService.updateLectures(lectures));
    }

    /**
     * 删除课程活动/讲座
     */
    @PreAuthorize("@ss.hasPermi('system:lectures:remove')")
    @Log(title = "课程活动/讲座", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lecturesService.deleteLecturesByIds(ids));
    }
}
