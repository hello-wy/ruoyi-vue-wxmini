package com.ruoyi.web.controller.bussiness.bo;

import java.util.List;

public class SurveyDistributeBo {
    private Long lectureId;
    private List<Long> userInfoIds;

    public Long getLectureId() { return lectureId; }
    public void setLectureId(Long lectureId) { this.lectureId = lectureId; }
    public List<Long> getUserInfoIds() { return userInfoIds; }
    public void setUserInfoIds(List<Long> userInfoIds) { this.userInfoIds = userInfoIds; }
}
