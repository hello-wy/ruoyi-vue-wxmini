package com.ruoyi.system.domain.bo;

import javax.validation.constraints.Size;

public class StudentSituationUpdateBo {

    @Size(max = 1000, message = "学员情况不能超过1000字")
    private String studentSituation;

    public String getStudentSituation() {
        return studentSituation;
    }

    public void setStudentSituation(String studentSituation) {
        this.studentSituation = studentSituation;
    }
}
