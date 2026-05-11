package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.ParttimeSignupWhitelist;
import com.ruoyi.system.service.IParttimeSignupWhitelistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParttimeSignupWhitelistControllerTest {

    @Mock
    private IParttimeSignupWhitelistService whitelistService;

    @InjectMocks
    private ParttimeSignupWhitelistController controller;

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
}
