package com.ruoyi.wxmini.service.impl;

import com.github.binarywang.wxpay.bean.merchanttransfer.DetailsQueryResult;
import com.github.binarywang.wxpay.bean.merchanttransfer.MerchantDetailsQueryRequest;
import com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.ruoyi.system.service.IWalletTransferGateway;
import com.ruoyi.system.service.dto.WalletTransferCreateRequest;
import com.ruoyi.system.service.dto.WalletTransferCreateResult;
import com.ruoyi.system.service.dto.WalletTransferQueryResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;

@Service
public class WxWalletTransferGatewayImpl implements IWalletTransferGateway {

    @Resource
    private WxPayService wxPayService;

    @Override
    public WalletTransferCreateResult createTransfer(WalletTransferCreateRequest request) throws Exception {
        com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateRequest wxRequest = new com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateRequest();
        wxRequest.setAppid(wxPayService.getConfig().getAppId());
        wxRequest.setOutBatchNo(request.getOutBatchNo());
        wxRequest.setBatchName(request.getBatchName());
        wxRequest.setBatchRemark(request.getBatchRemark());
        wxRequest.setTotalAmount(toFen(request.getAmount()));
        wxRequest.setTotalNum(1);
        wxRequest.setTransferSceneId(request.getTransferSceneId());
        wxRequest.setNotifyUrl(request.getNotifyUrl());

        com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateRequest.TransferDetailList detail = new com.github.binarywang.wxpay.bean.merchanttransfer.TransferCreateRequest.TransferDetailList();
        detail.setOutDetailNo(request.getOutDetailNo());
        detail.setTransferAmount(toFen(request.getAmount()));
        detail.setTransferRemark(request.getTransferRemark());
        detail.setOpenid(request.getOpenId());
        detail.setUserName(request.getRealName());
        wxRequest.setTransferDetailList(Collections.singletonList(detail));

        TransferCreateResult createResult = wxPayService.getMerchantTransferService().createTransfer(wxRequest);
        WalletTransferCreateResult result = new WalletTransferCreateResult();
        result.setBatchId(createResult.getBatchId());
        return result;
    }

    @Override
    public WalletTransferQueryResult queryTransfer(String outBatchNo, String outDetailNo) throws Exception {
        MerchantDetailsQueryRequest request = new MerchantDetailsQueryRequest();
        request.setOutBatchNo(outBatchNo);
        request.setOutDetailNo(outDetailNo);
        DetailsQueryResult detail = wxPayService.getMerchantTransferService().queryMerchantDetails(request);
        if (detail == null) {
            return null;
        }
        WalletTransferQueryResult result = new WalletTransferQueryResult();
        result.setBatchId(detail.getBatchId());
        result.setOutBatchNo(detail.getOutBatchNo());
        result.setDetailId(detail.getDetailId());
        result.setDetailStatus(detail.getDetailStatus());
        result.setFailReason(detail.getFailReason());
        return result;
    }

    private int toFen(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100")).intValueExact();
    }
}
