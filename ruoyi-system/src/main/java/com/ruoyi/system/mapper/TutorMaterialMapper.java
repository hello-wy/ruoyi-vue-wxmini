package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TutorMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TutorMaterialMapper {
    int insertTutorMaterial(TutorMaterial material);

    List<TutorMaterial> selectTutorMaterialsByIds(@Param("ids") List<Long> ids);

    int bindTutorMaterials(@Param("tutorId") Long tutorId, @Param("ids") List<Long> ids);

    int deleteTutorMaterialsByTutorId(@Param("tutorId") Long tutorId);

    int deleteTutorMaterialsByTutorIds(@Param("tutorIds") List<Long> tutorIds);

    int deleteTutorMaterialsByTutorIdExcludingIds(
            @Param("tutorId") Long tutorId,
            @Param("ids") List<Long> ids
    );
}
