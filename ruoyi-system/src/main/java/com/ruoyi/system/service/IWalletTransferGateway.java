package com.ruoyi.system.service;

import com.ruoyi.system.service.dto.WalletTransferCreateRequest;
import com.ruoyi.system.service.dto.WalletTransferCreateResult;
import com.ruoyi.system.service.dto.WalletTransferQueryResult;

public interface IWalletTransferGateway {

    WalletTransferCreateResult createTransfer(WalletTransferCreateRequest request) throws Exception;

    WalletTransferQueryResult queryTransfer(String outBatchNo, String outDetailNo) throws Exception;
}
