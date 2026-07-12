package com.ruoyi.wxmini.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.CourseNote;
import com.ruoyi.system.service.ICourseNoteService;
import com.ruoyi.system.service.ICoursePayOrderService;
import com.ruoyi.wxmini.bo.WxCourseNoteSaveBo;
import com.ruoyi.wxmini.bo.WxCourseNoteUpdateBo;
import com.ruoyi.wxmini.service.IWxCourseNoteService;
import com.ruoyi.wxmini.vo.WxCourseNoteVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WxCourseNoteServiceImpl implements IWxCourseNoteService {
    private static final int MAX_CONTENT_LENGTH = 2000;

    private final ICourseNoteService courseNoteService;
    private final ICoursePayOrderService coursePayOrderService;

    public WxCourseNoteServiceImpl(ICourseNoteService courseNoteService,
                                   ICoursePayOrderService coursePayOrderService) {
        this.courseNoteService = courseNoteService;
        this.coursePayOrderService = coursePayOrderService;
    }

    @Override
    public List<WxCourseNoteVo> listMyNotes(String userId) {
        return courseNoteService.selectCourseNotesByUserId(userId).stream()
                .map(this::toVo)
                .collect(Collectors.toList());
    }

    @Override
    public WxCourseNoteVo createMyNote(String userId, WxCourseNoteSaveBo bo) {
        requireEnrolledCourse(userId, bo.getCourseId());
        CourseNote note = new CourseNote();
        note.setUserId(userId);
        note.setCourseId(bo.getCourseId());
        note.setContent(normalizeContent(bo.getContent()));
        CourseNote created = courseNoteService.createCourseNote(note);
        return toVo(requireOwnedNote(userId, created.getId()));
    }

    @Override
    public WxCourseNoteVo updateMyNote(String userId, Long noteId, WxCourseNoteUpdateBo bo) {
        CourseNote note = requireOwnedNote(userId, noteId);
        note.setContent(normalizeContent(bo.getContent()));
        courseNoteService.updateCourseNote(note);
        return toVo(requireOwnedNote(userId, noteId));
    }

    @Override
    public void deleteMyNote(String userId, Long noteId) {
        requireOwnedNote(userId, noteId);
        courseNoteService.deleteCourseNoteByIdAndUserId(noteId, userId);
    }

    private void requireEnrolledCourse(String userId, Long courseId) {
        if (coursePayOrderService.selectLatestPaidOrder(userId, courseId) == null) {
            throw new ServiceException("只能为已报名课程创建笔记");
        }
    }

    private CourseNote requireOwnedNote(String userId, Long noteId) {
        CourseNote note = courseNoteService.selectCourseNoteByIdAndUserId(noteId, userId);
        if (note == null) {
            throw new ServiceException("笔记不存在");
        }
        return note;
    }

    private String normalizeContent(String content) {
        String value = content == null ? "" : content.trim();
        if (value.isEmpty()) {
            throw new ServiceException("笔记内容不能为空");
        }
        if (value.length() > MAX_CONTENT_LENGTH) {
            throw new ServiceException("笔记内容不能超过2000字");
        }
        return value;
    }

    private WxCourseNoteVo toVo(CourseNote note) {
        WxCourseNoteVo vo = new WxCourseNoteVo();
        vo.setId(note.getId());
        vo.setCourseId(note.getCourseId());
        vo.setCourseName(note.getCourseName());
        vo.setContent(note.getContent());
        vo.setCreateTime(note.getCreateTime());
        vo.setUpdateTime(note.getUpdateTime());
        return vo;
    }
}
