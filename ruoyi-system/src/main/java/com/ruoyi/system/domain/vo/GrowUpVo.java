package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.Lectures;
import com.ruoyi.system.domain.Questionnaire;

import java.util.List;

public class GrowUpVo {

    private List<Questionnaire> questionnaireList;
    private List<Lectures> lecturesList;

    public List<Questionnaire> getQuestionnaireList() {
        return questionnaireList;
    }

    public void setQuestionnaireList(List<Questionnaire> questionnaireList) {
        this.questionnaireList = questionnaireList;
    }

    public List<Lectures> getLecturesList() {
        return lecturesList;
    }

    public void setLecturesList(List<Lectures> lecturesList) {
        this.lecturesList = lecturesList;
    }
    public GrowUpVo() {
    }

    public GrowUpVo(List<Questionnaire> questionnaireList, List<Lectures> lecturesList) {
        this.questionnaireList = questionnaireList;
        this.lecturesList = lecturesList;
    }
}
