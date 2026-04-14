package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.service.IDailyJobsService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import com.ruoyi.wxmini.util.WxMiniUserContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxDailyJobControllerTest {

    @BeforeEach
    void setUpRequestContext() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @AfterEach
    void clearContext() {
        WxMiniUserContext.clear();
    }

    @Mock
    private IDailyJobsService dailyJobsService;

    @Mock
    private IUserInfoService userInfoService;

    @InjectMocks
    private WxDailyJobController controller;

    @Test
    void listShouldReturnEmptyRowsWhenServiceReturnsEmptyList() {
        when(dailyJobsService.selectDailyJobsList(any())).thenReturn(Collections.emptyList());

        TableDataInfoVo<DailyJobs> result = controller.list(new DailyJobs());

        assertEquals(200, result.getCode());
        assertNotNull(result.getRows());
        assertTrue(result.getRows().isEmpty());
    }

    @Test
    void listShouldReturnRowsWhenServiceReturnsData() {
        DailyJobs job = new DailyJobs();
        job.setId(1L);
        job.setTitle("数学家教");
        when(dailyJobsService.selectDailyJobsList(any())).thenReturn(Collections.singletonList(job));

        TableDataInfoVo<DailyJobs> result = controller.list(new DailyJobs());

        assertEquals(200, result.getCode());
        assertEquals(1, result.getRows().size());
        assertEquals("数学家教", result.getRows().get(0).getTitle());
    }

    @Test
    void defaultsShouldReturnPhoneWhenUserExists() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("123");
        userInfo.setUserName("商家A");
        userInfo.setPhone("13800138000");
        userInfo.setUserType("2");
        WxMiniUserContext.setCurrentUserId("123");
        when(userInfoService.selectUserInfoByUserId("123")).thenReturn(userInfo);

        AjaxResult result = controller.defaults();

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }

    @Test
    void createShouldRejectWhenUserIsNotMerchant() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("123");
        userInfo.setUserType("1");
        WxMiniUserContext.setCurrentUserId("123");
        when(userInfoService.selectUserInfoByUserId("123")).thenReturn(userInfo);

        AjaxResult result = controller.create(new DailyJobs());

        assertEquals(false, Integer.valueOf(200).equals(result.get(AjaxResult.CODE_TAG)));
    }

    @Test
    void createShouldReturn200WhenMerchantPublishes() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(9L);
        userInfo.setUserId("123");
        userInfo.setUserType("2");
        WxMiniUserContext.setCurrentUserId("123");
        when(userInfoService.selectUserInfoByUserId("123")).thenReturn(userInfo);
        when(dailyJobsService.insertDailyJobs(any(DailyJobs.class))).thenReturn(1);

        DailyJobs dailyJobs = new DailyJobs();
        dailyJobs.setTitle("招聘数学老师");
        dailyJobs.setCategory(0L);
        dailyJobs.setSalaryDay(new BigDecimal("300"));
        dailyJobs.setWorkDate(new Date());
        dailyJobs.setWorkTime("09:00-12:00");
        dailyJobs.setLocation("南京市鼓楼区");
        dailyJobs.setContacts("王老师");
        dailyJobs.setPhone("13800138000");
        dailyJobs.setDescription("负责初中数学辅导");

        AjaxResult result = controller.create(dailyJobs);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
    }
}
