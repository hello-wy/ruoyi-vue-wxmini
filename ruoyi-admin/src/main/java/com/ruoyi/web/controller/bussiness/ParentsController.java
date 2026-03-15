package com.ruoyi.web.controller.bussiness;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
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
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.service.IParentsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfoVo;

/**
 * 家教订单Controller
 * 
 * @author ruoyi
 * @date 2026-03-04
 */
@RestController
@RequestMapping("/system/parents")
public class ParentsController extends BaseController
{
    @Autowired
    private IParentsService parentsService;

    /**
     * 查询家教订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:parents:list')")
    @GetMapping("/list")
    public TableDataInfoVo<Parents> list(Parents parents)
    {
        startPage();
        List<Parents> list = parentsService.selectParentsList(parents);
        return getDataTable(list);
    }

    /**
     * 导出家教订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:parents:export')")
    @Log(title = "家教订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Parents parents)
    {
        List<Parents> list = parentsService.selectParentsList(parents);
        ExcelUtil<Parents> util = new ExcelUtil<Parents>(Parents.class);
        util.exportExcel(response, list, "家教订单数据");
    }

    /**
     * 获取家教订单详细信息
     */
    @Anonymous
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(parentsService.selectParentsById(id));
    }

    /**
     * 新增家教订单
     */
    @PreAuthorize("@ss.hasPermi('system:parents:add')")
    @Log(title = "家教订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Parents parents)
    {
        long snowflakeId = SnowflakeIdWorker.nextIdDefault();
        parents.setId(snowflakeId);
        parentsService.insertParents(parents);
        return AjaxResult.success("操作成功", snowflakeId);
    }

    /**
     * 修改家教订单
     */
    @PreAuthorize("@ss.hasPermi('system:parents:edit')")
    @Log(title = "家教订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Parents parents)
    {
        return toAjax(parentsService.updateParents(parents));
    }

    /**
     * 删除家教订单
     */
    @PreAuthorize("@ss.hasPermi('system:parents:remove')")
    @Log(title = "家教订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(parentsService.deleteParentsByIds(ids));
    }
}
