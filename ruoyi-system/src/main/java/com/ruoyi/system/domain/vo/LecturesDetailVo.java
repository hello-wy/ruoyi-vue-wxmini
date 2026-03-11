package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.Lectures;

import java.util.List;

/**
 * 讲座详情 VO — 在 Lectures 基础上附加讲师详细信息列表（id、name、avatarUrl）
 */
public class LecturesDetailVo extends Lectures {

    /** 讲师详细信息列表 */
    private List<SpeakerVo> speakers;

    public List<SpeakerVo> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<SpeakerVo> speakers) {
        this.speakers = speakers;
    }
}

