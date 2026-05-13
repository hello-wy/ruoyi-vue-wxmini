package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 签到与报名记录对象 sign_in_record
 * <p>
 * record_type=1: 讲座签到，lecture_id 有值<br>
 * record_type=2: 沙龙报名，salon_id 有值，contact_name/contact_phone 有值<br>
 * record_type=3: 兼职签到，job_id 有值
 * </p>
 *
 * @author ruoyi
 * @date 2026-03-06
 */
public class SignInRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 用户ID，关联sys_user表的user_id */
    @Excel(name = "用户ID")
    private Long uid;

    /** 记录类型: 1-讲座签到, 2-沙龙报名, 3-兼职签到 */
    @Excel(name = "记录类型", readConverterExp = "1=讲座签到,2=沙龙报名,3=兼职签到")
    private Integer recordType;

    /** 讲座ID，关联lectures表（record_type=1时有值） */
    @Excel(name = "讲座ID")
    private Long lectureId;

    /** 沙龙ID，关联salon_info表（record_type=2时有值） */
    @Excel(name = "沙龙ID")
    private Long salonId;

    /** 兼职岗位ID，关联daily_jobs表（record_type=3时有值） */
    @Excel(name = "兼职岗位ID")
    private Long jobId;

    /** 签到/报名时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "签到/报名时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date signTime;

    /** 状态: 0-已报名/待签到, 1-已签到/正常, 2-迟到, 3-已取消 */
    @Excel(name = "状态", readConverterExp = "0=已报名/待签到,1=已签到/正常,2=迟到,3=已取消")
    private Integer signStatus;

    /** 联系人姓名（沙龙报名时填写） */
    @Excel(name = "联系人姓名")
    private String contactName;

    /** 联系电话（沙龙报名时填写） */
    @Excel(name = "联系电话")
    private String contactPhone;

    /** 备注信息 */
    @Excel(name = "备注")
    private String remark;

    /** 兼职签到图片地址 */
    private String signImageUrl;

    /** 兼职签到图片相对路径 */
    private String signImageName;

    /** 审核状态：0-未提交，1-待审核，2-已通过，3-已驳回 */
    private Integer auditStatus;

    /** 审核备注 */
    private String auditRemark;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;

    /** 审核人 */
    private String auditBy;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;

    /** 签到设备或IP（可选，用于防作弊） */
    @Excel(name = "设备信息")
    private String deviceInfo;

    public void setId(Long id)                  { this.id = id; }
    public Long getId()                         { return id; }
    public void setUid(Long uid)                { this.uid = uid; }
    public Long getUid()                        { return uid; }
    public void setRecordType(Integer recordType) { this.recordType = recordType; }
    public Integer getRecordType()              { return recordType; }
    public void setLectureId(Long lectureId)    { this.lectureId = lectureId; }
    public Long getLectureId()                  { return lectureId; }
    public void setSalonId(Long salonId)        { this.salonId = salonId; }
    public Long getSalonId()                    { return salonId; }
    public void setJobId(Long jobId)            { this.jobId = jobId; }
    public Long getJobId()                      { return jobId; }
    public void setSignTime(Date signTime)      { this.signTime = signTime; }
    public Date getSignTime()                   { return signTime; }
    public void setSignStatus(Integer signStatus) { this.signStatus = signStatus; }
    public Integer getSignStatus()              { return signStatus; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getContactName()              { return contactName; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactPhone()             { return contactPhone; }
    public void setRemark(String remark)        { this.remark = remark; }
    public String getRemark()                   { return remark; }
    public void setSignImageUrl(String signImageUrl) { this.signImageUrl = signImageUrl; }
    public String getSignImageUrl()             { return signImageUrl; }
    public void setSignImageName(String signImageName) { this.signImageName = signImageName; }
    public String getSignImageName()            { return signImageName; }
    public void setAuditStatus(Integer auditStatus) { this.auditStatus = auditStatus; }
    public Integer getAuditStatus()             { return auditStatus; }
    public void setAuditRemark(String auditRemark) { this.auditRemark = auditRemark; }
    public String getAuditRemark()              { return auditRemark; }
    public void setAuditTime(Date auditTime)    { this.auditTime = auditTime; }
    public Date getAuditTime()                  { return auditTime; }
    public void setAuditBy(String auditBy)      { this.auditBy = auditBy; }
    public String getAuditBy()                  { return auditBy; }
    public void setSubmitTime(Date submitTime)  { this.submitTime = submitTime; }
    public Date getSubmitTime()                 { return submitTime; }
    public void setDeviceInfo(String deviceInfo) { this.deviceInfo = deviceInfo; }
    public String getDeviceInfo()               { return deviceInfo; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("uid", getUid())
                .append("recordType", getRecordType())
                .append("lectureId", getLectureId())
                .append("salonId", getSalonId())
                .append("jobId", getJobId())
                .append("signTime", getSignTime())
                .append("signStatus", getSignStatus())
                .append("contactName", getContactName())
                .append("contactPhone", getContactPhone())
                .append("remark", getRemark())
                .append("signImageUrl", getSignImageUrl())
                .append("signImageName", getSignImageName())
                .append("auditStatus", getAuditStatus())
                .append("auditRemark", getAuditRemark())
                .append("auditTime", getAuditTime())
                .append("auditBy", getAuditBy())
                .append("submitTime", getSubmitTime())
                .append("deviceInfo", getDeviceInfo())
                .toString();
    }
}
