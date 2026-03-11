package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.Lectures;

/**
 * 讲座列表 VO — 在 Lectures 基础上附加拼接的讲师姓名字符串
 */
public class LecturesListVo extends Lectures {

    /** 讲师姓名拼接字符串，如 "李老师, 王教授" */
    private String speakerNames;

    public String getSpeakerNames() {
        return speakerNames;
    }

    public void setSpeakerNames(String speakerNames) {
        this.speakerNames = speakerNames;
    }
}

