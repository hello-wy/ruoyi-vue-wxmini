package com.ruoyi.wxmini.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.CourseNote;
import com.ruoyi.system.domain.CoursePayOrder;
import com.ruoyi.system.service.ICourseNoteService;
import com.ruoyi.system.service.ICoursePayOrderService;
import com.ruoyi.wxmini.bo.WxCourseNoteSaveBo;
import com.ruoyi.wxmini.bo.WxCourseNoteUpdateBo;
import com.ruoyi.wxmini.vo.WxCourseNoteVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxCourseNoteServiceImplTest {
    private static final String USER_ID = "user-001";
    private static final Long COURSE_ID = 101L;
    private static final Long NOTE_ID = 9L;

    @Mock
    private ICourseNoteService courseNoteService;
    @Mock
    private ICoursePayOrderService coursePayOrderService;
    @InjectMocks
    private WxCourseNoteServiceImpl service;

    @Test
    void createMyNoteBindsTheAuthenticatedUserAndCourse() {
        WxCourseNoteSaveBo bo = new WxCourseNoteSaveBo();
        bo.setCourseId(COURSE_ID);
        bo.setContent("  课堂重点  ");
        CoursePayOrder order = new CoursePayOrder();
        CourseNote savedNote = new CourseNote();
        savedNote.setId(NOTE_ID);
        CourseNote selectedNote = note("课堂重点", "幸福解码");

        when(coursePayOrderService.selectLatestPaidOrder(USER_ID, COURSE_ID)).thenReturn(order);
        when(courseNoteService.createCourseNote(any(CourseNote.class))).thenReturn(savedNote);
        when(courseNoteService.selectCourseNoteByIdAndUserId(NOTE_ID, USER_ID)).thenReturn(selectedNote);

        WxCourseNoteVo result = service.createMyNote(USER_ID, bo);

        ArgumentCaptor<CourseNote> captor = ArgumentCaptor.forClass(CourseNote.class);
        verify(courseNoteService).createCourseNote(captor.capture());
        assertEquals(USER_ID, captor.getValue().getUserId());
        assertEquals(COURSE_ID, captor.getValue().getCourseId());
        assertEquals("课堂重点", captor.getValue().getContent());
        assertEquals("幸福解码", result.getCourseName());
    }

    @Test
    void createMyNoteRejectsCoursesNotOwnedByTheCurrentUser() {
        WxCourseNoteSaveBo bo = new WxCourseNoteSaveBo();
        bo.setCourseId(COURSE_ID);
        bo.setContent("课堂重点");
        when(coursePayOrderService.selectLatestPaidOrder(USER_ID, COURSE_ID)).thenReturn(null);

        ServiceException error = assertThrows(ServiceException.class, () -> service.createMyNote(USER_ID, bo));

        assertEquals("只能为已报名课程创建笔记", error.getMessage());
    }

    @Test
    void updateMyNoteRejectsContentLongerThanTwoThousandCharacters() {
        WxCourseNoteUpdateBo bo = new WxCourseNoteUpdateBo();
        bo.setContent(repeat('a', 2001));
        when(courseNoteService.selectCourseNoteByIdAndUserId(NOTE_ID, USER_ID)).thenReturn(note("旧内容", "幸福解码"));

        ServiceException error = assertThrows(ServiceException.class, () -> service.updateMyNote(USER_ID, NOTE_ID, bo));

        assertEquals("笔记内容不能超过2000字", error.getMessage());
    }

    private CourseNote note(String content, String courseName) {
        CourseNote note = new CourseNote();
        note.setId(NOTE_ID);
        note.setUserId(USER_ID);
        note.setCourseId(COURSE_ID);
        note.setCourseName(courseName);
        note.setContent(content);
        return note;
    }

    private String repeat(char value, int count) {
        StringBuilder builder = new StringBuilder(count);
        for (int index = 0; index < count; index++) {
            builder.append(value);
        }
        return builder.toString();
    }
}
