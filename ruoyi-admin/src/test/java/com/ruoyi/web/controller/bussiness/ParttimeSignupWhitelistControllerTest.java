package com.ruoyi.web.controller.bussiness;

import com.github.pagehelper.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.ParttimeSignupWhitelist;
import com.ruoyi.system.service.IParttimeSignupWhitelistService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParttimeSignupWhitelistControllerTest {

    @Mock
    private IParttimeSignupWhitelistService whitelistService;

    @InjectMocks
    private ParttimeSignupWhitelistController controller;

    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void addShouldNormalizePayloadAndDefaultStatus() {
        ParttimeSignupWhitelist request = new ParttimeSignupWhitelist();
        request.setRealName(" 张三 ");
        request.setIdCard(" 11010519900101123x ");
        request.setPrice(new BigDecimal("88.50"));
        request.setRemark(" 备注 ");
        when(whitelistService.selectParttimeSignupWhitelistByIdCard("11010519900101123X")).thenReturn(null);
        when(whitelistService.insertParttimeSignupWhitelist(any(ParttimeSignupWhitelist.class))).thenReturn(1);

        AjaxResult result = controller.add(request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        ArgumentCaptor<ParttimeSignupWhitelist> captor = ArgumentCaptor.forClass(ParttimeSignupWhitelist.class);
        verify(whitelistService).insertParttimeSignupWhitelist(captor.capture());
        ParttimeSignupWhitelist saved = captor.getValue();
        assertNotNull(saved.getId());
        assertEquals("张三", saved.getRealName());
        assertEquals("11010519900101123X", saved.getIdCard());
        assertEquals(new BigDecimal("88.50"), saved.getPrice());
        assertEquals(Integer.valueOf(1), saved.getStatus());
        assertEquals("备注", saved.getRemark());
    }

    @Test
    void addShouldRejectInvalidIdCard() {
        ParttimeSignupWhitelist request = new ParttimeSignupWhitelist();
        request.setRealName("张三");
        request.setIdCard("123");
        request.setPrice(new BigDecimal("88.50"));

        AjaxResult result = controller.add(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("请填写正确的18位身份证号", result.get(AjaxResult.MSG_TAG));
        verify(whitelistService, never()).insertParttimeSignupWhitelist(any(ParttimeSignupWhitelist.class));
    }

    @Test
    void listShouldReturnRowsForFuzzyQuery() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("pageNum", "1");
        request.addParameter("pageSize", "10");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ParttimeSignupWhitelist query = new ParttimeSignupWhitelist();
        query.setRealName(" 张 ");
        query.setIdCard("110105");

        ParttimeSignupWhitelist item = new ParttimeSignupWhitelist();
        item.setId(1L);
        item.setRealName("张三");
        item.setIdCard("11010519900101123X");
        item.setRemark("线下确认");

        Page<ParttimeSignupWhitelist> page = new Page<>(1, 10);
        page.setTotal(1);
        page.add(item);
        when(whitelistService.selectParttimeSignupWhitelistList(any(ParttimeSignupWhitelist.class))).thenReturn(page);

        TableDataInfoVo<ParttimeSignupWhitelist> result = controller.list(query);

        assertEquals(200, result.getCode());
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRows().size());
        assertEquals("张三", result.getRows().get(0).getRealName());
        ArgumentCaptor<ParttimeSignupWhitelist> captor = ArgumentCaptor.forClass(ParttimeSignupWhitelist.class);
        verify(whitelistService).selectParttimeSignupWhitelistList(captor.capture());
        assertEquals(" 张 ", captor.getValue().getRealName());
        assertEquals("110105", captor.getValue().getIdCard());
    }

    @Test
    void removeShouldDeleteSingleWhitelistRecord() {
        when(whitelistService.deleteParttimeSignupWhitelistById(9L)).thenReturn(1);

        AjaxResult result = controller.remove(9L);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        verify(whitelistService).deleteParttimeSignupWhitelistById(eq(9L));
    }
}
