# POST /wxmini/pay/courses/notify

## 用途

微信课程报名支付回调。

## 鉴权

- 微信支付平台签名校验；不使用 `Wx-Authorization`。

## 行为

- `tradeState=SUCCESS` 时，将 `course_pay_order.status` 从 `0` 改为 `1`。
- 写入 `wechatTransactionId`、`requestId`、`payTime`。
- 重复回调幂等返回成功。

## 返回

```xml
<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>
```
