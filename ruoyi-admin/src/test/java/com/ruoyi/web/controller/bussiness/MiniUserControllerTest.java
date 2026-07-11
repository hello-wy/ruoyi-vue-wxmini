package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfoVo;
import com.ruoyi.system.domain.bo.MiniUserQueryBo;
import com.ruoyi.system.domain.vo.MiniUserVo;
import com.ruoyi.system.service.IMiniUserService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MiniUserControllerTest {

    @Mock
    private IMiniUserService miniUserService;

    @InjectMocks
    private MiniUserController controller;

    @BeforeEach
    void setUpRequestContext() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Test
    void listShouldReturnPagedRows() {
        MiniUserQueryBo query = new MiniUserQueryBo();
        MiniUserVo user = new MiniUserVo();
        user.setId(1L);
        when(miniUserService.listMiniUsers(query)).thenReturn(Collections.singletonList(user));

        TableDataInfoVo<MiniUserVo> result = controller.list(query);

        assertEquals(1, result.getRows().size());
    }

    @Test
    void getInfoShouldReturnUserOrNotFound() {
        MiniUserVo user = new MiniUserVo();
        user.setId(1L);
        when(miniUserService.getMiniUser(1L)).thenReturn(user);

        AjaxResult found = controller.getInfo(1L);
        AjaxResult missing = controller.getInfo(2L);

        assertEquals(200, found.get(AjaxResult.CODE_TAG));
        assertEquals(false, Integer.valueOf(200).equals(missing.get(AjaxResult.CODE_TAG)));
    }
}
