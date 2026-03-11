package com.ruoyi.system.domain.vo;

/**
 * 讲师简要信息 VO（用于讲座详情中的 speakers 列表）
 */
public class SpeakerVo {

    /** 讲师 ID */
    private Long id;

    /** 讲师姓名 */
    private String name;

    /** 讲师头像 URL */
    private String avatarUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

