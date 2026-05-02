# POST /wxmini/pay/payroll/notify

## 用途

- `POST`：微信工资支付异步回调通知。

## 鉴权

- 微信服务端回调，无需业务 token。

## 说明

- 回调成功后会在事务内更新工资批次、工资明细、用户钱包余额与钱包流水。
- 已支付批次重复回调直接返回成功，不重复入账。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxPayController.java`
