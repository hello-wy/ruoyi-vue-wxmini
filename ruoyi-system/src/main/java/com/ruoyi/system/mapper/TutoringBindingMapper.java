package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TutoringBinding;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TutoringBindingMapper {
    TutoringBinding selectById(@Param("id") Long id);

    TutoringBinding selectByIdForUpdate(@Param("id") Long id);

    TutoringBinding selectByParentAndTutor(@Param("parentId") Long parentId, @Param("tutorId") Long tutorId);

    List<TutoringBinding> selectAvailableBindingsByParentUserId(@Param("parentUserId") Long parentUserId);

    List<TutoringBinding> selectBindingsForAdmin(TutoringBinding query);

    int insertTutoringBinding(TutoringBinding binding);

    int updateTutoringBinding(TutoringBinding binding);

    int closeBindingsByParentId(@Param("parentId") Long parentId, @Param("excludeId") Long excludeId, @Param("operator") String operator);
}
