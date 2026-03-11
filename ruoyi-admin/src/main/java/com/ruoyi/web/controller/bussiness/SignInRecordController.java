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
import com.ruoyi.system.domain.SignInRecord;
import com.ruoyi.system.domain.vo.SignInRecordWithLectureVo;
import com.ruoyi.system.service.ISignInRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;

/**
 * 讲座签到记录Controller
 * 
 * @author ruoyi
 * @date 2026-03-06
 */
@RestController
@RequestMapping("/system/record")
public class SignInRecordController extends BaseController
{
    @Autowired
    private ISignInRecordService signInRecordService;

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 查询讲座签到记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(SignInRecord signInRecord)
    {
        startPage();
        List<SignInRecord> list = signInRecordService.selectSignInRecordList(signInRecord);
        return getDataTable(list);
    }

    /**
     * 导出讲座签到记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "讲座签到记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SignInRecord signInRecord)
    {
        List<SignInRecord> list = signInRecordService.selectSignInRecordList(signInRecord);
        ExcelUtil<SignInRecord> util = new ExcelUtil<SignInRecord>(SignInRecord.class);
        util.exportExcel(response, list, "讲座签到记录数据");
    }

    /**
     * 获取讲座签到记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(signInRecordService.selectSignInRecordById(id));
    }

    /**
     * 新增讲座签到记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "讲座签到记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SignInRecord signInRecord)
    {
        return toAjax(signInRecordService.insertSignInRecord(signInRecord));
    }

    /**
     * 修改讲座签到记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "讲座签到记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SignInRecord signInRecord)
    {
        return toAjax(signInRecordService.updateSignInRecord(signInRecord));
    }

    /**
     * 删除讲座签到记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "讲座签到记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(signInRecordService.deleteSignInRecordByIds(ids));
    }


    /**
     * 查询用户个人讲座签到记录（微信小程序端，联表含讲座 name/time/location/cover）
     */
    @GetMapping("/myRecords")
    public AjaxResult mySignInRecords() {
        Long userId = getUserId();
        List<SignInRecordWithLectureVo> list =
                signInRecordService.selectSignInRecordWithLectureByUid(userId);
        return success(list);
    }
}
