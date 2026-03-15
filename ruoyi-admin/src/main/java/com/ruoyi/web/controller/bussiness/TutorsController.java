package com.ruoyi.web.controller.bussiness;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.service.ITutorsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfoVo;

/**
 * 大学生/教员Controller
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
@Api(tags = "大学生/教员管理")
@RestController
@RequestMapping("/system/tutors")
public class TutorsController extends BaseController
{
    @Autowired
    private ITutorsService tutorsService;

    /**
     * 查询大学生/教员列表
     */
    @ApiOperation("查询大学生/教员列表")
    @PreAuthorize("@ss.hasPermi('system:tutors:list')")
    @GetMapping("/list")
    public TableDataInfoVo<Tutors> list(Tutors tutors)
    {
        startPage();
        List<Tutors> list = tutorsService.selectTutorsList(tutors);
        return getDataTable(list);
    }

    /**
     * 导出大学生/教员列表
     */
    @ApiOperation("导出大学生/教员列表")
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
    @ApiOperation("获取大学生/教员详细信息")
    @ApiImplicitParam(name = "id", value = "教员ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    @Anonymous
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tutorsService.selectTutorsById(id));
    }

    /**
     * 新增大学生/教员
     */
    @ApiOperation("新增大学生/教员（自动生成雪花ID）")
    @PreAuthorize("@ss.hasPermi('system:tutors:add')")
    @Log(title = "大学生/教员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Tutors tutors)
    {
        long snowflakeId = SnowflakeIdWorker.nextIdDefault();
        tutors.setId(snowflakeId);
        toAjax(tutorsService.insertTutors(tutors));
        return AjaxResult.success("操作成功", snowflakeId);
    }

    /**
     * 修改大学生/教员
     */
    @ApiOperation("修改大学生/教员信息")
    @PreAuthorize("@ss.hasPermi('system:tutors:edit')")
    @Log(title = "大学生/教员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Tutors tutors)
    {
        return toAjax(tutorsService.updateTutors(tutors));
    }

    /**
     * 审核教员（修改 isCertified：0-待审核 / 1-已通过 / 2-已拒绝）
     */
    @ApiOperation("审核教员认证状态（0-待审核 1-已通过 2-已拒绝）")
    @PreAuthorize("@ss.hasPermi('system:tutors:edit')")
    @Log(title = "大学生/教员审核", businessType = BusinessType.UPDATE)
    @PutMapping("/review")
    public AjaxResult review(@RequestBody Tutors tutors)
    {
        if (tutors == null || tutors.getId() == null || tutors.getIsCertified() == null)
        {
            return AjaxResult.error("参数不能为空");
        }
        Long isCertified = tutors.getIsCertified();
        if (!isCertified.equals(0L) && !isCertified.equals(1L) && !isCertified.equals(2L))
        {
            return AjaxResult.error("isCertified 参数非法，只允许 0/1/2");
        }
        Tutors update = new Tutors();
        update.setId(tutors.getId());
        update.setIsCertified(isCertified);
        return toAjax(tutorsService.updateTutors(update));
    }

    /**
     * 删除大学生/教员
     */
    @ApiOperation("删除大学生/教员")
    @ApiImplicitParam(name = "ids", value = "教员ID数组", required = true, dataType = "Long[]", paramType = "path", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermi('system:tutors:remove')")
    @Log(title = "大学生/教员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tutorsService.deleteTutorsByIds(ids));
    }
}
