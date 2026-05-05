# GET /wxmini/wallet/withdraw-records

## 用途

- `GET`：获取当前登录小程序用户的提现记录。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 返回字段说明

- `status`：提现状态，`0=打款中`、`1=已打款`、`2=打款失败`
- `remark`：失败原因或状态说明
- `wxTransferNo`：微信批次单号
- `outBatchNo`：商户批次号
- `outDetailNo`：商户明细单号
- `wxDetailNo`：微信明细单号

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxWalletController.java`
