package com.ruoyi.system.service;

import com.ruoyi.system.domain.vo.JobAttendanceAdminOrderVo;

public interface IJobAttendanceAdminService {
    JobAttendanceAdminOrderVo getOrderAttendance(String orderNo);

    JobAttendanceAdminOrderVo confirmAttendance(String orderNo, String operator);

    JobAttendanceAdminOrderVo submitSignImage(String orderNo, String signImageUrl);
}
