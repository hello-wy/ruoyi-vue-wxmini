package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.mapper.LecturesMapper;
import com.ruoyi.system.mapper.QuestionnaireMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LecturesServiceImplTest {

    private static final long CLOCK_TOLERANCE_SECONDS = 1L;

    @Mock
    private LecturesMapper lecturesMapper;

    @Mock
    private QuestionnaireMapper questionnaireMapper;

    @InjectMocks
    private LecturesServiceImpl service;

    @Test
    void selectRemainingMonthLecturesListVoPassesCurrentTimeAndNextMonthStart() {
        Lectures filter = new Lectures();
        when(lecturesMapper.selectRemainingMonthLecturesListVo(
                same(filter), any(Date.class),
                any(Date.class)))
                .thenReturn(Collections.emptyList());

        Instant beforeCall = Instant.now();
        service.selectRemainingMonthLecturesListVo(filter);
        Instant afterCall = Instant.now();

        ArgumentCaptor<Date> startCaptor = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Date> endCaptor = ArgumentCaptor.forClass(Date.class);
        verify(lecturesMapper).selectRemainingMonthLecturesListVo(
                same(filter), startCaptor.capture(), endCaptor.capture());

        Instant actualStart = startCaptor.getValue().toInstant();
        LocalDate expectedCurrentDate = LocalDate.now();
        LocalDate actualStartDate = actualStart.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate actualEnd = endCaptor.getValue().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        assertFalse(actualStart.isBefore(beforeCall.minusSeconds(CLOCK_TOLERANCE_SECONDS)));
        assertFalse(actualStart.isAfter(afterCall.plusSeconds(CLOCK_TOLERANCE_SECONDS)));
        assertEquals(expectedCurrentDate, actualStartDate);
        assertEquals(expectedCurrentDate.withDayOfMonth(1).plusMonths(1), actualEnd);
    }

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
