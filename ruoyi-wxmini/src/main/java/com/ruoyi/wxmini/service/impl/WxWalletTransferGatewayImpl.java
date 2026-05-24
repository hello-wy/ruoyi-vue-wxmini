package com.ruoyi.wxmini.service.impl;

import com.github.binarywang.wxpay.bean.transfer.TransferBillsGetResult;
import com.github.binarywang.wxpay.bean.transfer.TransferBillsRequest;
import com.github.binarywang.wxpay.bean.transfer.TransferBillsResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.system.service.IWalletTransferGateway;
import com.ruoyi.system.service.dto.WalletTransferCreateRequest;
import com.ruoyi.system.service.dto.WalletTransferCreateResult;
import com.ruoyi.system.service.dto.WalletTransferQueryResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WxWalletTransferGatewayImpl implements IWalletTransferGateway {

    @Resource
    private WxPayService wxPayService;

    @Override
    public WalletTransferCreateResult createTransfer(WalletTransferCreateRequest request) throws Exception {
        TransferBillsRequest wxRequest = new TransferBillsRequest();
        wxRequest.setAppid(wxPayService.getConfig().getAppId());
        wxRequest.setOutBillNo(request.getOutBatchNo());
        wxRequest.setTransferSceneId(request.getTransferSceneId());
        wxRequest.setOpenid(request.getOpenId());
        wxRequest.setUserName(request.getRealName());
        wxRequest.setTransferAmount(toFen(request.getAmount()));
        wxRequest.setTransferRemark(request.getTransferRemark());
        wxRequest.setNotifyUrl(request.getNotifyUrl());
        wxRequest.setUserRecvPerception(request.getUserRecvPerception());
        wxRequest.setTransferSceneReportInfos(toWxReportInfos(request.getTransferSceneReportInfos()));

        TransferBillsResult createResult = wxPayService.getTransferService().transferBills(wxRequest);
        WalletTransferCreateResult result = new WalletTransferCreateResult();
        result.setBatchId(createResult.getTransferBillNo());
        result.setState(createResult.getState());
        result.setPackageInfo(createResult.getPackageInfo());
        return result;
    }

    @Override
    public WalletTransferQueryResult queryTransfer(String outBatchNo, String outDetailNo) throws Exception {
        TransferBillsGetResult detail = wxPayService.getTransferService().getBillsByOutBillNo(outBatchNo);
        if (detail == null) {
            return null;
        }
        WalletTransferQueryResult result = new WalletTransferQueryResult();
        result.setBatchId(detail.getTransferBillNo());
        result.setOutBatchNo(detail.getOutBillNo());
        result.setDetailId(detail.getTransferBillNo());
        result.setDetailStatus(detail.getState());
        result.setFailReason(detail.getFailReason());
        return result;
    }

    private List<TransferBillsRequest.TransferSceneReportInfo> toWxReportInfos(
            List<WalletTransferCreateRequest.TransferSceneReportInfo> reportInfos) {
        return reportInfos.stream().map(info ->
                new TransferBillsRequest.TransferSceneReportInfo(info.getInfoType(), info.getInfoContent())
        ).collect(Collectors.toList());
    }

    private int toFen(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100")).intValueExact();
    }
}
