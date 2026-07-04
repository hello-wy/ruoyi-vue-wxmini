package com.ruoyi.wxmini.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.bo.StudentQueryBo;
import com.ruoyi.system.domain.vo.StudentDetailVo;
import com.ruoyi.system.domain.vo.StudentListVo;
import com.ruoyi.wxmini.domain.WxUserProfile;
import com.ruoyi.wxmini.domain.vo.WxUserProfileVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WxUserProfileMapper extends BaseMapper<WxUserProfile> {

    WxUserProfileVo selectProfileDetailByUserId(String userId);

    List<StudentListVo> selectAdminStudentList(StudentQueryBo queryBo);

    StudentDetailVo selectAdminStudentDetailById(Long id);

    int countAdminStudentById(@Param("id") Long id);

    int updateAdminStudentSituation(@Param("id") Long id, @Param("studentSituation") String studentSituation);
}
