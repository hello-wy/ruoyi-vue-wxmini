package com.ruoyi.wxmini.vo;

import lombok.Data;

/**
 * 小程序端公开教员详情 VO
 */
@Data
public class WxTutorDetailVo {
    private Long id;
    private String realName;
    private String title;
    private String subjects;
    private String areas;
    private Long methods;
    private Long isCertified;
    private String experience;
    private String major;
    private String school;
    private Long degree;
    private String selfJudge;
    private String certificate;
    private String certificates;
    private String live;
    private String work;
}
