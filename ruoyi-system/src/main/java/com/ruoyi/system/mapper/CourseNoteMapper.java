package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CourseNote;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseNoteMapper {
    List<CourseNote> selectCourseNotesByUserId(String userId);

    CourseNote selectCourseNoteByIdAndUserId(@Param("id") Long id,
                                              @Param("userId") String userId);

    int insertCourseNote(CourseNote note);

    int updateCourseNote(CourseNote note);

    int deleteCourseNoteByIdAndUserId(@Param("id") Long id,
                                       @Param("userId") String userId);
}
