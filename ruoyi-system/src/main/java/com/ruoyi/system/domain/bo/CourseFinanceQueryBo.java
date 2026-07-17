package com.ruoyi.system.domain.bo;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

public class CourseFinanceQueryBo {
    private Long courseId;
    private String orderNo;
    private String studentKeyword;
    private Long employeeUserId;
    private Long ownerDeptId;
    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date payTimeBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date payTimeEnd;
    public Long getCourseId(){return courseId;} public void setCourseId(Long v){courseId=v;}
    public String getOrderNo(){return orderNo;} public void setOrderNo(String v){orderNo=v;}
    public String getStudentKeyword(){return studentKeyword;} public void setStudentKeyword(String v){studentKeyword=v;}
    public Long getEmployeeUserId(){return employeeUserId;} public void setEmployeeUserId(Long v){employeeUserId=v;}
    public Long getOwnerDeptId(){return ownerDeptId;} public void setOwnerDeptId(Long v){ownerDeptId=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public Date getPayTimeBegin(){return payTimeBegin;} public void setPayTimeBegin(Date v){payTimeBegin=v;}
    public Date getPayTimeEnd(){return payTimeEnd;} public void setPayTimeEnd(Date v){payTimeEnd=v;}
}
