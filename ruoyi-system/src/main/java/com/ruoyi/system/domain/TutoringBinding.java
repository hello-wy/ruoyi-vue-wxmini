package com.ruoyi.system.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.core.domain.BaseEntity;

public class TutoringBinding extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentUserId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tutorId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tutorUserId;
    private Integer status;
    private String serviceTimesSnapshot;
    private String remark;
    private String parentName;
    private String parentSubject;
    private String parentGrade;
    private String parentRegion;
    private String tutorName;
    private String tutorSchool;
    private String tutorCurrentGrade;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Long getParentUserId() { return parentUserId; }
    public void setParentUserId(Long parentUserId) { this.parentUserId = parentUserId; }
    public Long getTutorId() { return tutorId; }
    public void setTutorId(Long tutorId) { this.tutorId = tutorId; }
    public Long getTutorUserId() { return tutorUserId; }
    public void setTutorUserId(Long tutorUserId) { this.tutorUserId = tutorUserId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getServiceTimesSnapshot() { return serviceTimesSnapshot; }
    public void setServiceTimesSnapshot(String serviceTimesSnapshot) { this.serviceTimesSnapshot = serviceTimesSnapshot; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }
    public String getParentSubject() { return parentSubject; }
    public void setParentSubject(String parentSubject) { this.parentSubject = parentSubject; }
    public String getParentGrade() { return parentGrade; }
    public void setParentGrade(String parentGrade) { this.parentGrade = parentGrade; }
    public String getParentRegion() { return parentRegion; }
    public void setParentRegion(String parentRegion) { this.parentRegion = parentRegion; }
    public String getTutorName() { return tutorName; }
    public void setTutorName(String tutorName) { this.tutorName = tutorName; }
    public String getTutorSchool() { return tutorSchool; }
    public void setTutorSchool(String tutorSchool) { this.tutorSchool = tutorSchool; }
    public String getTutorCurrentGrade() { return tutorCurrentGrade; }
    public void setTutorCurrentGrade(String tutorCurrentGrade) { this.tutorCurrentGrade = tutorCurrentGrade; }
}
