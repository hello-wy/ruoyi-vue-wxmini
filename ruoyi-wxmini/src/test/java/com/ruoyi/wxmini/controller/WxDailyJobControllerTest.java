package com.ruoyi.wxmini.controller;

import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.DailyJobs;
import com.ruoyi.system.service.IDailyJobsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

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

    @Mock
    private IDailyJobsService dailyJobsService;

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
}
