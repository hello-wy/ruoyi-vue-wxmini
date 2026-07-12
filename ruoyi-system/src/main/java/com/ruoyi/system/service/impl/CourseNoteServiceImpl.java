package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.CourseNote;
import com.ruoyi.system.mapper.CourseNoteMapper;
import com.ruoyi.system.service.ICourseNoteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseNoteServiceImpl implements ICourseNoteService {
    private final CourseNoteMapper courseNoteMapper;

    public CourseNoteServiceImpl(CourseNoteMapper courseNoteMapper) {
        this.courseNoteMapper = courseNoteMapper;
    }

    @Override
    public List<CourseNote> selectCourseNotesByUserId(String userId) {
        return courseNoteMapper.selectCourseNotesByUserId(userId);
    }

    @Override
    public CourseNote selectCourseNoteByIdAndUserId(Long id, String userId) {
        return courseNoteMapper.selectCourseNoteByIdAndUserId(id, userId);
    }

    @Override
    public CourseNote createCourseNote(CourseNote note) {
        note.setCreateTime(DateUtils.getNowDate());
        note.setUpdateTime(note.getCreateTime());
        courseNoteMapper.insertCourseNote(note);
        return note;
    }

    @Override
    public CourseNote updateCourseNote(CourseNote note) {
        note.setUpdateTime(DateUtils.getNowDate());
        courseNoteMapper.updateCourseNote(note);
        return note;
    }

    @Override
    public int deleteCourseNoteByIdAndUserId(Long id, String userId) {
        return courseNoteMapper.deleteCourseNoteByIdAndUserId(id, userId);
    }
}
