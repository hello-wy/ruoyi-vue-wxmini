package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TutoringOrder;
import com.ruoyi.system.domain.TutoringPayrollItem;
import com.ruoyi.system.mapper.TutoringPayrollItemMapper;
import com.ruoyi.system.service.IWalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TutoringSettlementServiceImplTest {

    @Mock
    private TutoringPayrollItemMapper tutoringPayrollItemMapper;
    @Mock
    private IWalletService walletService;
    @Mock
    private TutoringBindingServiceSupport tutoringBindingServiceSupport;

    @InjectMocks
    private TutoringSettlementServiceImpl service;

    @Test
    void should_delegate_admin_pending_order_creation_to_binding_support() {
        TutoringOrder order = new TutoringOrder();
        order.setOrderNo("TUTOR-1");
        when(tutoringBindingServiceSupport.createPendingOrder(31L, 51L, "admin")).thenReturn(order);

        TutoringOrder result = service.createPendingOrder(31L, 51L, "admin");

        org.junit.jupiter.api.Assertions.assertSame(order, result);
    }

    @Test
    void audit_schedule_should_credit_net_amount_and_mark_settled() {
        TutoringPayrollItem item = new TutoringPayrollItem();
        item.setId(1L);
        item.setScheduleId(60001L);
        item.setTutorUserId(88L);
        item.setNetAmount(new BigDecimal("216.00"));
        item.setWalletBizId("TUTORING_PAYROLL:60001");
        item.setStatus(0);

        when(tutoringPayrollItemMapper.selectByScheduleId(60001L)).thenReturn(item);

        service.auditSchedule(60001L, 2, "确认课时无误", "admin");

        verify(tutoringBindingServiceSupport).auditSchedule(60001L, 2, "确认课时无误", "admin");
        verify(walletService).creditPayroll(88L, new BigDecimal("216.00"), "TUTORING_PAYROLL:60001", "家教课酬入账");
        ArgumentCaptor<TutoringPayrollItem> captor = ArgumentCaptor.forClass(TutoringPayrollItem.class);
        verify(tutoringPayrollItemMapper).updateTutoringPayrollItem(captor.capture());
        TutoringPayrollItem updated = captor.getValue();
        assertEquals(1, updated.getStatus());
        assertEquals("admin", updated.getPaidBy());
        assertNotNull(updated.getPaidTime());
        verify(tutoringBindingServiceSupport).markScheduleSettled(60001L, "admin");
    }

    @Test
    void should_skip_wallet_credit_when_payroll_item_already_paid() {
        TutoringPayrollItem item = new TutoringPayrollItem();
        item.setId(1L);
        item.setTutorUserId(88L);
        item.setNetAmount(new BigDecimal("216.00"));
        item.setPayrollNo("TPAY-1");
        item.setStatus(1);

        when(tutoringPayrollItemMapper.selectByIdsForUpdate(Collections.singletonList(1L)))
                .thenReturn(Collections.singletonList(item));

        service.batchPay(Collections.singletonList(1L), "admin");

        verify(walletService, never()).creditPayroll(any(), any(), any(), any());
        verify(tutoringPayrollItemMapper, never()).updateTutoringPayrollItem(any(TutoringPayrollItem.class));
    }
}
