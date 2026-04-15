package com.ruoyi.web.controller.bussiness;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.service.ITutorsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TutorsControllerTest {

    @Mock
    private ITutorsService tutorsService;

    @InjectMocks
    private TutorsController controller;

    @Test
    void reviewShouldUpdateOnlyCertificationStatus() {
        Tutors request = new Tutors();
        request.setId(100L);
        request.setIsCertified(1L);
        request.setTitle("不应更新的标题");
        when(tutorsService.updateTutors(any(Tutors.class))).thenReturn(1);

        AjaxResult result = controller.review(request);

        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        ArgumentCaptor<Tutors> captor = ArgumentCaptor.forClass(Tutors.class);
        verify(tutorsService).updateTutors(captor.capture());
        Tutors update = captor.getValue();
        assertEquals(100L, update.getId());
        assertEquals(1L, update.getIsCertified());
        assertEquals(null, update.getTitle());
    }

    @Test
    void reviewShouldRejectInvalidCertificationStatus() {
        Tutors request = new Tutors();
        request.setId(100L);
        request.setIsCertified(3L);

        AjaxResult result = controller.review(request);

        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("isCertified 参数非法，只允许 0/1/2", result.get(AjaxResult.MSG_TAG));
        verify(tutorsService, never()).updateTutors(any(Tutors.class));
    }
}
