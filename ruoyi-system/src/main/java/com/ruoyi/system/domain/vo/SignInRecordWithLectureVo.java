package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 签到/报名记录 VO（含关联业务信息）
 * <p>
 * recordType=1 时，携带讲座信息（lectureName / lectureTime / lectureLocation）<br>
 * recordType=2 时，携带沙龙信息（salonTitle / salonStartTime / salonCoverImg）
 * </p>
 */
public class SignInRecordWithLectureVo {

    /** 记录主键 */
    private Long id;

    /** 用户ID */
    private Long uid;

    /** 记录类型: 1-讲座签到, 2-沙龙报名 */
    private Integer recordType;

    /** 讲座ID（record_type=1时有值） */
    private Long lectureId;

    /** 沙龙ID（record_type=2时有值） */
    private Long salonId;

    /** 签到/报名时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date signTime;

    /** 状态: 0-已报名/待签到, 1-已签到/正常, 2-迟到, 3-已取消 */
    private Integer signStatus;

    /** 联系人姓名（沙龙报名时有值） */
    private String contactName;

    /** 联系电话（沙龙报名时有值） */
    private String contactPhone;

    /** 备注 */
    private String remark;

    /** 签到设备或IP */
    private String deviceInfo;

    // ---- 讲座信息（record_type=1 时填充） ----

    /** 讲座名称 */
    private String lectureName;

    /** 讲座开讲时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date lectureTime;

    /** 讲座详细地址 */
    private String lectureLocation;

    /** 讲座封面图 */
    private String lectureCover;

    // ---- 沙龙信息（record_type=2 时填充） ----

    /** 沙龙标题 */
    private String salonTitle;

    /** 沙龙副标题 */
    private String salonSubtitle;

    /** 沙龙开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date salonStartTime;

    /** 沙龙封面图 */
    private String salonCoverImg;

    // ---- Getters & Setters ----

    public Long getId()                             { return id; }
    public void setId(Long id)                      { this.id = id; }
    public Long getUid()                            { return uid; }
    public void setUid(Long uid)                    { this.uid = uid; }
    public Integer getRecordType()                  { return recordType; }
    public void setRecordType(Integer recordType)   { this.recordType = recordType; }
    public Long getLectureId()                      { return lectureId; }
    public void setLectureId(Long lectureId)        { this.lectureId = lectureId; }
    public Long getSalonId()                        { return salonId; }
    public void setSalonId(Long salonId)            { this.salonId = salonId; }
    public Date getSignTime()                       { return signTime; }
    public void setSignTime(Date signTime)          { this.signTime = signTime; }
    public Integer getSignStatus()                  { return signStatus; }
    public void setSignStatus(Integer signStatus)   { this.signStatus = signStatus; }
    public String getContactName()                  { return contactName; }
    public void setContactName(String contactName)  { this.contactName = contactName; }
    public String getContactPhone()                 { return contactPhone; }
    public void setContactPhone(String contactPhone){ this.contactPhone = contactPhone; }
    public String getRemark()                       { return remark; }
    public void setRemark(String remark)            { this.remark = remark; }
    public String getDeviceInfo()                   { return deviceInfo; }
    public void setDeviceInfo(String deviceInfo)    { this.deviceInfo = deviceInfo; }
    public String getLectureName()                  { return lectureName; }
    public void setLectureName(String lectureName)  { this.lectureName = lectureName; }
    public Date getLectureTime()                    { return lectureTime; }
    public void setLectureTime(Date lectureTime)    { this.lectureTime = lectureTime; }
    public String getLectureLocation()              { return lectureLocation; }
    public void setLectureLocation(String lectureLocation) { this.lectureLocation = lectureLocation; }
    public String getLectureCover()                 { return lectureCover; }
    public void setLectureCover(String lectureCover){ this.lectureCover = lectureCover; }
    public String getSalonTitle()                   { return salonTitle; }
    public void setSalonTitle(String salonTitle)    { this.salonTitle = salonTitle; }
    public String getSalonSubtitle()                { return salonSubtitle; }
    public void setSalonSubtitle(String salonSubtitle) { this.salonSubtitle = salonSubtitle; }
    public Date getSalonStartTime()                 { return salonStartTime; }
    public void setSalonStartTime(Date salonStartTime) { this.salonStartTime = salonStartTime; }
    public String getSalonCoverImg()                { return salonCoverImg; }
    public void setSalonCoverImg(String salonCoverImg) { this.salonCoverImg = salonCoverImg; }
}
