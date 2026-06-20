package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.mapper.LecturesMapper;
import com.ruoyi.system.mapper.QuestionnaireMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LecturesServiceImplTest {

    @Mock
    private LecturesMapper lecturesMapper;

    @Mock
    private QuestionnaireMapper questionnaireMapper;

    @InjectMocks
    private LecturesServiceImpl service;

    @Test
    void selectLecturesTemplateListReturnsExistingCourseConfiguration() {
        Lectures template = new Lectures();
        template.setId(18L);
        template.setName("幸福解码");
        template.setCover("legacy-cover");
        template.setCoverId(7L);
        template.setSpeaker("3,5");
        template.setDetail("原课程详情");
        template.setRegistrationFee(new BigDecimal("99.00"));
        template.setDeposit(new BigDecimal("20.00"));
        template.setRequiresEnrollment(Boolean.TRUE);

        when(lecturesMapper.selectLecturesTemplateList()).thenReturn(Collections.singletonList(template));

        List<Lectures> templates = service.selectLecturesTemplateList();

        assertEquals(1, templates.size());
        assertEquals("幸福解码", templates.get(0).getName());
        assertEquals("legacy-cover", templates.get(0).getCover());
        assertEquals(7L, templates.get(0).getCoverId());
        assertEquals("3,5", templates.get(0).getSpeaker());
        assertEquals(new BigDecimal("99.00"), templates.get(0).getRegistrationFee());
    }
}
