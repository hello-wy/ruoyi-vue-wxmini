package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TutoringPayrollItem;
import com.ruoyi.system.mapper.TutoringPayrollItemMapper;
import com.ruoyi.system.service.IWalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

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
