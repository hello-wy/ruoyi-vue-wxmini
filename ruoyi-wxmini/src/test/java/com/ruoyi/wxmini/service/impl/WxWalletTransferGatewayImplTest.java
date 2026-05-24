package com.ruoyi.wxmini.service.impl;

import com.github.binarywang.wxpay.bean.transfer.TransferBillsRequest;
import com.github.binarywang.wxpay.bean.transfer.TransferBillsResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.TransferService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.system.service.dto.WalletTransferCreateRequest;
import com.ruoyi.system.service.dto.WalletTransferCreateResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxWalletTransferGatewayImplTest {

    @Mock
    private WxPayService wxPayService;
    @Mock
    private TransferService transferService;

    private WxWalletTransferGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        gateway = new WxWalletTransferGatewayImpl();
        ReflectionTestUtils.setField(gateway, "wxPayService", wxPayService);
    }

    @Test
    void createTransferShouldUseNewTransferBillsApi() throws Exception {
        WxPayConfig config = new WxPayConfig();
        config.setAppId("wx-app-1");
        when(wxPayService.getConfig()).thenReturn(config);
        when(wxPayService.getTransferService()).thenReturn(transferService);

        TransferBillsResult wxResult = new TransferBillsResult();
        wxResult.setTransferBillNo("transfer-bill-1");
        wxResult.setOutBillNo("WD1001");
        wxResult.setState("SUCCESS");
        when(transferService.transferBills(org.mockito.ArgumentMatchers.any())).thenReturn(wxResult);

        WalletTransferCreateResult result = gateway.createTransfer(buildRequest());

        ArgumentCaptor<TransferBillsRequest> captor = ArgumentCaptor.forClass(TransferBillsRequest.class);
        verify(transferService).transferBills(captor.capture());
        TransferBillsRequest wxRequest = captor.getValue();
        assertEquals("wx-app-1", wxRequest.getAppid());
        assertEquals("WD1001", wxRequest.getOutBillNo());
        assertEquals("1005", wxRequest.getTransferSceneId());
        assertEquals("openid-1", wxRequest.getOpenid());
        assertEquals(100, wxRequest.getTransferAmount());
        assertEquals("https://example.com/wallet/notify", wxRequest.getNotifyUrl());
        assertNull(wxRequest.getReceiptAuthorizationMode());
        assertNotNull(wxRequest.getTransferSceneReportInfos());
        assertEquals(2, wxRequest.getTransferSceneReportInfos().size());
        assertEquals("transfer-bill-1", result.getBatchId());
    }

    private WalletTransferCreateRequest buildRequest() {
        WalletTransferCreateRequest request = new WalletTransferCreateRequest();
        request.setOpenId("openid-1");
        request.setRealName("张三");
        request.setAmount(new BigDecimal("1.00"));
        request.setOutBatchNo("WD1001");
        request.setOutDetailNo("WDD1001");
        request.setBatchName("钱包提现");
        request.setBatchRemark("微信提现");
        request.setTransferRemark("微信提现");
        request.setNotifyUrl("https://example.com/wallet/notify");
        request.setTransferSceneId("1005");
        request.setUserRecvPerception("钱包提现");
        request.addTransferSceneReportInfo("岗位类型", "其他");
        request.addTransferSceneReportInfo("报酬说明", "微信提现");
        return request;
    }
}
