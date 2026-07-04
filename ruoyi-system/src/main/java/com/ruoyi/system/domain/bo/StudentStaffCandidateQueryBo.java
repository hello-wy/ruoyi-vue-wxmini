package com.ruoyi.system.domain.bo;

/**
 * 学员绑定员工候选人查询对象。
 */
public class StudentStaffCandidateQueryBo {

    /** 关键字：手机号/昵称/用户名模糊匹配 */
    private String keyword;

    /** 管理员层级，默认 employee */
    private String adminLevel;

    /** 服务端生成的访问范围，不接收前端传值 */
    private StudentAccessScope accessScope;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    public StudentAccessScope getAccessScope() {
        return accessScope;
    }

    public void setAccessScope(StudentAccessScope accessScope) {
        this.accessScope = accessScope;
    }
}
