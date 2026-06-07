package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.MerchantUserTypeWhitelist;
import com.ruoyi.system.service.IMerchantUserTypeWhitelistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MerchantUserTypeWhitelistControllerTest {

    @BeforeEach
    void setUpRequestContext() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Mock
    private IMerchantUserTypeWhitelistService whitelistService;

    @InjectMocks
    private MerchantUserTypeWhitelistController controller;

    @Test
    void addShouldNormalizePayloadAndDefaultStatus() {
        MerchantUserTypeWhitelist request = new MerchantUserTypeWhitelist();
        request.setRealName(" 张三 ");
        request.setIdCard(" 11010519900101123x ");
        request.setRemark(" 备注 ");
        when(whitelistService.selectMerchantUserTypeWhitelistByIdCard("11010519900101123X")).thenReturn(null);
        when(whitelistService.insertMerchantUserTypeWhitelist(any(MerchantUserTypeWhitelist.class))).thenReturn(1);

        AjaxResult result = controller.add(request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        ArgumentCaptor<MerchantUserTypeWhitelist> captor = ArgumentCaptor.forClass(MerchantUserTypeWhitelist.class);
        verify(whitelistService).insertMerchantUserTypeWhitelist(captor.capture());
        MerchantUserTypeWhitelist saved = captor.getValue();
        assertNotNull(saved.getId());
        assertNotNull(saved.getCreateTime());
        assertEquals("张三", saved.getRealName());
        assertEquals("11010519900101123X", saved.getIdCard());
        assertEquals(Integer.valueOf(1), saved.getStatus());
        assertEquals("备注", saved.getRemark());
    }

    @Test
    void addShouldRejectInvalidIdCard() {
        MerchantUserTypeWhitelist request = new MerchantUserTypeWhitelist();
        request.setRealName("张三");
        request.setIdCard("123");

        AjaxResult result = controller.add(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("请填写正确的18位身份证号", result.get(AjaxResult.MSG_TAG));
        verify(whitelistService, never()).insertMerchantUserTypeWhitelist(any(MerchantUserTypeWhitelist.class));
    }

    @Test
    void addShouldRejectInvalidStatus() {
        MerchantUserTypeWhitelist request = new MerchantUserTypeWhitelist();
        request.setRealName("张三");
        request.setIdCard("11010519900101123X");
        request.setStatus(3);
        when(whitelistService.selectMerchantUserTypeWhitelistByIdCard("11010519900101123X")).thenReturn(null);

        AjaxResult result = controller.add(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("状态值只能为0、1或2", result.get(AjaxResult.MSG_TAG));
        verify(whitelistService, never()).insertMerchantUserTypeWhitelist(any(MerchantUserTypeWhitelist.class));
    }

    @Test
    void auditShouldAcceptPendingApplication() {
        MerchantUserTypeWhitelist request = new MerchantUserTypeWhitelist();
        request.setStatus(1);
        request.setRemark("通过");
        when(whitelistService.updateMerchantUserTypeWhitelistAudit(any(MerchantUserTypeWhitelist.class))).thenReturn(1);

        AjaxResult result = controller.audit(10L, request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        ArgumentCaptor<MerchantUserTypeWhitelist> captor = ArgumentCaptor.forClass(MerchantUserTypeWhitelist.class);
        verify(whitelistService).updateMerchantUserTypeWhitelistAudit(captor.capture());
        assertEquals(Long.valueOf(10L), captor.getValue().getId());
        assertEquals(Integer.valueOf(1), captor.getValue().getStatus());
        assertEquals("通过", captor.getValue().getRemark());
        assertNotNull(captor.getValue().getUpdateTime());
    }

    @Test
    void auditShouldRejectUnknownStatus() {
        MerchantUserTypeWhitelist request = new MerchantUserTypeWhitelist();
        request.setStatus(9);

        AjaxResult result = controller.audit(10L, request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("状态值只能为0、1或2", result.get(AjaxResult.MSG_TAG));
        verify(whitelistService, never()).updateMerchantUserTypeWhitelistAudit(any(MerchantUserTypeWhitelist.class));
    }

    @Test
    void addShouldRejectDuplicateIdCardAfterNormalization() {
        MerchantUserTypeWhitelist request = new MerchantUserTypeWhitelist();
        request.setRealName("张三");
        request.setIdCard(" 11010519900101123x ");
        when(whitelistService.selectMerchantUserTypeWhitelistByIdCard("11010519900101123X"))
                .thenReturn(new MerchantUserTypeWhitelist());

        AjaxResult result = controller.add(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("该身份证已存在白名单记录", result.get(AjaxResult.MSG_TAG));
        verify(whitelistService, never()).insertMerchantUserTypeWhitelist(any(MerchantUserTypeWhitelist.class));
    }

    @Test
    void listShouldNormalizeIdCardAndRealNameFilters() {
        MerchantUserTypeWhitelist query = new MerchantUserTypeWhitelist();
        query.setRealName(" 张三 ");
        query.setIdCard(" 11010519900101123x ");
        MerchantUserTypeWhitelist row = new MerchantUserTypeWhitelist();
        row.setId(1L);
        when(whitelistService.selectMerchantUserTypeWhitelistList(any(MerchantUserTypeWhitelist.class)))
                .thenReturn(Collections.singletonList(row));

        TableDataInfoVo<MerchantUserTypeWhitelist> result = controller.list(query);

        assertEquals(1, result.getRows().size());
        ArgumentCaptor<MerchantUserTypeWhitelist> captor = ArgumentCaptor.forClass(MerchantUserTypeWhitelist.class);
        verify(whitelistService).selectMerchantUserTypeWhitelistList(captor.capture());
        assertEquals("张三", captor.getValue().getRealName());
        assertEquals("11010519900101123X", captor.getValue().getIdCard());
    }

    @Test
    void removeShouldDeleteByIds() {
        Long[] ids = new Long[] { 1L, 2L };
        when(whitelistService.deleteMerchantUserTypeWhitelistByIds(ids)).thenReturn(2);

        AjaxResult result = controller.remove(ids);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        verify(whitelistService).deleteMerchantUserTypeWhitelistByIds(ids);
    }
}
