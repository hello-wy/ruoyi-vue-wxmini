package com.ruoyi.system.service.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WalletTransferCreateRequest {

    private String appId;
    private String openId;
    private String realName;
    private BigDecimal amount;
    private String outBatchNo;
    private String outDetailNo;
    private String batchName;
    private String batchRemark;
    private String transferRemark;
    private String notifyUrl;
    private String transferSceneId;
    private String userRecvPerception;
    private List<TransferSceneReportInfo> transferSceneReportInfos = new ArrayList<>();

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOutBatchNo() {
        return outBatchNo;
    }

    public void setOutBatchNo(String outBatchNo) {
        this.outBatchNo = outBatchNo;
    }

    public String getOutDetailNo() {
        return outDetailNo;
    }

    public void setOutDetailNo(String outDetailNo) {
        this.outDetailNo = outDetailNo;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getBatchRemark() {
        return batchRemark;
    }

    public void setBatchRemark(String batchRemark) {
        this.batchRemark = batchRemark;
    }

    public String getTransferRemark() {
        return transferRemark;
    }

    public void setTransferRemark(String transferRemark) {
        this.transferRemark = transferRemark;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTransferSceneId() {
        return transferSceneId;
    }

    public void setTransferSceneId(String transferSceneId) {
        this.transferSceneId = transferSceneId;
    }

    public String getUserRecvPerception() {
        return userRecvPerception;
    }

    public void setUserRecvPerception(String userRecvPerception) {
        this.userRecvPerception = userRecvPerception;
    }

    public List<TransferSceneReportInfo> getTransferSceneReportInfos() {
        return transferSceneReportInfos;
    }

    public void setTransferSceneReportInfos(List<TransferSceneReportInfo> transferSceneReportInfos) {
        this.transferSceneReportInfos = transferSceneReportInfos;
    }

    public void addTransferSceneReportInfo(String infoType, String infoContent) {
        this.transferSceneReportInfos.add(new TransferSceneReportInfo(infoType, infoContent));
    }

    public static class TransferSceneReportInfo {
        private String infoType;
        private String infoContent;

        public TransferSceneReportInfo(String infoType, String infoContent) {
            this.infoType = infoType;
            this.infoContent = infoContent;
        }

        public String getInfoType() {
            return infoType;
        }

        public String getInfoContent() {
            return infoContent;
        }
    }
}
