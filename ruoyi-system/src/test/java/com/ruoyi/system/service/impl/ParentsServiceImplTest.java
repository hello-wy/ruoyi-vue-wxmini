package com.ruoyi.system.service.impl;

import com.ruoyi.system.mapper.ParentsMapper;
import com.ruoyi.system.mapper.TutoringBindingMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParentsServiceImplTest {

    @Mock
    private ParentsMapper parentsMapper;

    @Mock
    private TutoringBindingMapper tutoringBindingMapper;

    @InjectMocks
    private ParentsServiceImpl service;

    @Test
    void should_close_bindings_before_deleting_parent_demand() {
        when(parentsMapper.deleteParentsById(31L)).thenReturn(1);

        int rows = service.deleteParentsById(31L);

        assertEquals(1, rows);
        InOrder inOrder = inOrder(tutoringBindingMapper, parentsMapper);
        inOrder.verify(tutoringBindingMapper).closeBindingsByParentId(31L, null, "system");
        inOrder.verify(parentsMapper).deleteParentsById(31L);
    }
}
