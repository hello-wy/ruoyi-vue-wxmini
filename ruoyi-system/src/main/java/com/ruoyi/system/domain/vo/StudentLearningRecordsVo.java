package com.ruoyi.system.domain.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentLearningRecordsVo {
    private List<CourseLearningRecordVo> enrollmentRecords = Collections.emptyList();
    private List<CourseLearningRecordVo> signInRecords = Collections.emptyList();

    public void addSignInRecords(List<CourseLearningRecordVo> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        List<CourseLearningRecordVo> merged = new ArrayList<>(signInRecords);
        merged.addAll(records);
        signInRecords = merged;
    }

    public List<CourseLearningRecordVo> getEnrollmentRecords() {
        return enrollmentRecords;
    }

    public void setEnrollmentRecords(List<CourseLearningRecordVo> enrollmentRecords) {
        this.enrollmentRecords = enrollmentRecords;
    }

    public List<CourseLearningRecordVo> getSignInRecords() {
        return signInRecords;
    }

    public void setSignInRecords(List<CourseLearningRecordVo> signInRecords) {
        this.signInRecords = signInRecords;
    }
}
