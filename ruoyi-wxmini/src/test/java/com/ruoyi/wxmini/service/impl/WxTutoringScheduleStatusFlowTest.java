package com.ruoyi.wxmini.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.TutoringSchedule;
import com.ruoyi.system.mapper.TutoringScheduleMapper;
import com.ruoyi.wxmini.domain.UserInfo;
import com.ruoyi.wxmini.service.IUserInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxTutoringScheduleStatusFlowTest {

    @Mock
    private IUserInfoService userInfoService;
    @Mock
    private TutoringScheduleMapper tutoringScheduleMapper;

    @InjectMocks
    private WxTutoringPayServiceImpl service;

    @Test
    void should_mark_student_check_in_from_pending_to_in_class() {
        TutoringSchedule schedule = buildSchedule(0, null);
        ArgumentCaptor<TutoringSchedule> scheduleCaptor = ArgumentCaptor.forClass(TutoringSchedule.class);

        when(userInfoService.selectUserInfoByUserId("wx-tutor-1")).thenReturn(buildTutorUser());
        when(tutoringScheduleMapper.selectByIdForUpdate(101L)).thenReturn(schedule);

        service.finishSchedule("wx-tutor-1", 101L, "已到达上课地点");

        verify(tutoringScheduleMapper).updateTutoringSchedule(scheduleCaptor.capture());
        TutoringSchedule updated = scheduleCaptor.getValue();
        assertEquals(1, updated.getStatus());
        assertNotNull(updated.getFinishTime());
        assertEquals("已到达上课地点", updated.getFinishRemark());
    }

    @Test
    void should_reject_student_check_in_when_schedule_is_not_pending() {
        when(userInfoService.selectUserInfoByUserId("wx-tutor-1")).thenReturn(buildTutorUser());
        when(tutoringScheduleMapper.selectByIdForUpdate(101L)).thenReturn(buildSchedule(1, new Date()));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.finishSchedule("wx-tutor-1", 101L, "重复签到"));

        assertEquals("当前课表状态不可签到", error.getMessage());
        verify(tutoringScheduleMapper, never()).updateTutoringSchedule(any());
    }

    @Test
    void should_reject_parent_complete_when_student_has_not_checked_in() {
        when(userInfoService.selectUserInfoByUserId("wx-user-1")).thenReturn(buildParentUser());
        when(tutoringScheduleMapper.selectByIdForUpdate(101L)).thenReturn(buildSchedule(1, null));

        ServiceException error = assertThrows(ServiceException.class,
                () -> service.confirmSchedule("wx-user-1", 101L, "确认完成"));

        assertEquals("学生尚未上课签到", error.getMessage());
        verify(tutoringScheduleMapper, never()).updateTutoringSchedule(any());
    }

    private UserInfo buildParentUser() {
        UserInfo currentUser = new UserInfo();
        currentUser.setId(41L);
        currentUser.setUserId("wx-user-1");
        return currentUser;
    }

    private UserInfo buildTutorUser() {
        UserInfo currentUser = new UserInfo();
        currentUser.setId(61L);
        currentUser.setUserId("wx-tutor-1");
        currentUser.setUserType(1);
        return currentUser;
    }

    private TutoringSchedule buildSchedule(Integer status, Date finishTime) {
        TutoringSchedule schedule = new TutoringSchedule();
        schedule.setId(101L);
        schedule.setParentUserId(41L);
        schedule.setTutorUserId(61L);
        schedule.setStatus(status);
        schedule.setFinishTime(finishTime);
        return schedule;
    }
}
