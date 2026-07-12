package com.ruoyi.system.service;

import com.ruoyi.system.domain.CourseNote;

import java.util.List;

public interface ICourseNoteService {
    List<CourseNote> selectCourseNotesByUserId(String userId);

    CourseNote selectCourseNoteByIdAndUserId(Long id, String userId);

    CourseNote createCourseNote(CourseNote note);

    CourseNote updateCourseNote(CourseNote note);

    int deleteCourseNoteByIdAndUserId(Long id, String userId);
}
