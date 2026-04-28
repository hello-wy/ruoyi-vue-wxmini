package com.ruoyi.wxmini.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.Parents;
import com.ruoyi.system.mapper.ParentsMapper;
import com.ruoyi.wxmini.mapper.BabyInfoMapper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BabyInfoServiceImplTest {

    @Mock
    private BabyInfoMapper babyInfoMapper;

    @Mock
    private ParentsMapper parentsMapper;

    @InjectMocks
    private BabyInfoServiceImpl service;

    @BeforeAll
    static void initTableInfo() {
        Configuration configuration = new Configuration();
        MapperBuilderAssistant assistant = new MapperBuilderAssistant(configuration, "test");
        TableInfo tableInfo = TableInfoHelper.initTableInfo(assistant, Parents.class);
        LambdaUtils.installCache(tableInfo);
    }

    @Test
    void deleteBabyInfoShouldAllowDeleteWhenOnlyHistoricalParentsExist() {
        when(parentsMapper.selectCount(any())).thenReturn(0L);
        when(babyInfoMapper.deleteBabyInfoByIdAndUserId(88L, "wx-user")).thenReturn(1);

        int rows = assertDoesNotThrow(() -> service.deleteBabyInfoByIdAndUserId(88L, "wx-user"));

        assertEquals(1, rows);
        verify(babyInfoMapper).deleteBabyInfoByIdAndUserId(88L, "wx-user");
        ArgumentCaptor<LambdaQueryWrapper<Parents>> captor = ArgumentCaptor.forClass(LambdaQueryWrapper.class);
        verify(parentsMapper).selectCount(captor.capture());
        assertEquals(true, captor.getValue().getSqlSegment().length() > 0);
        assertEquals(true, captor.getValue().getSqlSegment().contains("status"));
        assertEquals(true, captor.getValue().getParamNameValuePairs().containsValue(0L));
    }

    @Test
    void deleteBabyInfoShouldRejectDeleteWhenActiveParentsExist() {
        when(parentsMapper.selectCount(any())).thenReturn(1L);

        ServiceException ex = assertThrows(ServiceException.class,
                () -> service.deleteBabyInfoByIdAndUserId(88L, "wx-user"));

        assertEquals("该萌娃已关联家教需求，暂不能删除", ex.getMessage());
    }
}
