package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.domain.TutoringBinding;
import com.ruoyi.system.domain.TutoringOrder;
import com.ruoyi.system.domain.Tutors;
import com.ruoyi.system.mapper.ParentsMapper;
import com.ruoyi.system.mapper.TutoringBindingMapper;
import com.ruoyi.system.mapper.TutoringOrderMapper;
import com.ruoyi.system.mapper.TutorsMapper;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TutoringBindingServiceSupportTest {

    @Mock
    private ParentsMapper parentsMapper;
    @Mock
    private TutorsMapper tutorsMapper;
    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private TutoringBindingMapper tutoringBindingMapper;
    @Mock
    private TutoringOrderMapper tutoringOrderMapper;
    @Mock
    private ISysConfigService sysConfigService;

    @InjectMocks
    private TutoringBindingServiceSupport support;

    @Test
    void should_bind_tutor_and_create_pending_order_for_admin() {
        Parents parent = new Parents();
        parent.setId(31L);
        parent.setSystemUid(41L);
        parent.setHourlyBudget(new BigDecimal("120.00"));
        parent.setServiceTimes("[{\"serviceDate\":\"2026-05-24\",\"startTime\":\"18:00\",\"endTime\":\"20:00\"},{\"serviceDate\":\"2026-05-25\",\"startTime\":\"19:00\",\"endTime\":\"20:30\"}]");

        Tutors tutor = new Tutors();
        tutor.setId(51L);
        tutor.setUid("61");
        tutor.setStatus(1L);

        UserInfo tutorUser = new UserInfo();
        tutorUser.setId(71L);

        TutoringBinding binding = new TutoringBinding();
        binding.setId(21L);
        binding.setParentId(31L);
        binding.setParentUserId(41L);
        binding.setTutorId(51L);
        binding.setTutorUserId(71L);
        binding.setStatus(0);
        binding.setServiceTimesSnapshot(parent.getServiceTimes());

        when(parentsMapper.selectParentsById(31L)).thenReturn(parent);
        when(tutorsMapper.selectTutorsById(51L)).thenReturn(tutor);
        when(userInfoService.selectUserInfoByUserId("61")).thenReturn(tutorUser);
        when(tutoringBindingMapper.selectByParentAndTutor(31L, 51L)).thenReturn(null);
        when(tutoringBindingMapper.selectById(any())).thenReturn(binding);
        when(tutoringOrderMapper.selectLatestByBindingId(21L)).thenReturn(null);
        when(sysConfigService.selectConfigByKey("wxmini.tutoring.defaultCommissionRate")).thenReturn("10.00");

        TutoringOrder order = support.createPendingOrder(31L, 51L, "admin");

        ArgumentCaptor<TutoringOrder> orderCaptor = ArgumentCaptor.forClass(TutoringOrder.class);
        verify(tutoringOrderMapper).insertTutoringOrder(orderCaptor.capture());
        TutoringOrder inserted = orderCaptor.getValue();
        assertEquals(inserted.getOrderNo(), order.getOrderNo());
        assertEquals(21L, inserted.getBindingId());
        assertEquals(31L, inserted.getParentId());
        assertEquals(41L, inserted.getParentUserId());
        assertEquals(51L, inserted.getTutorId());
        assertEquals(71L, inserted.getTutorUserId());
        assertEquals(0, inserted.getStatus());
        assertEquals(2, inserted.getLessonCount());
        assertEquals(new BigDecimal("120.00"), inserted.getHourlyPrice());
        assertEquals(new BigDecimal("420.00"), inserted.getTotalAmount());
        assertEquals(new BigDecimal("10.00"), inserted.getCommissionRate());
        assertEquals(parent.getServiceTimes(), inserted.getServiceTimesSnapshot());
    }
}
