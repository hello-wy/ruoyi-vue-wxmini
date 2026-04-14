# POST /wxmini/pay/salon/notify

## 用途

微信支付异步回调地址，仅供微信支付服务端调用。

## 鉴权

- 无前端鉴权
- 由微信支付签名校验保障真实性

## 处理规则

- 解析并校验微信支付回调报文
- 仅在 `tradeState=SUCCESS` 时更新本地订单
- 回调处理必须幂等
- 记录微信 `transactionId`、`successTime`、`Request-ID`

## 成功返回

```xml
<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>
```
