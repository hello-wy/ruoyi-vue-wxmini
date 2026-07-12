package com.ruoyi.system.domain;

import com.ruoyi.system.domain.bo.StudentFollowUpRecordBo;
import com.ruoyi.system.domain.vo.StudentFollowUpRecordVo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentFollowUpRecordModelTest {

    @Test
    void titleIsAvailableOnCreateAndReadModels() {
        StudentFollowUpRecordBo request = new StudentFollowUpRecordBo();
        request.setTitle("学习进度回访");
        StudentFollowUpRecordVo result = new StudentFollowUpRecordVo();
        result.setTitle(request.getTitle());

        assertEquals("学习进度回访", result.getTitle());
    }
}
