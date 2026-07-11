# POST /wxmini/pay/courses/notify

## 用途

微信课程报名支付回调。

## 鉴权

- 微信支付平台签名校验；不使用 `Wx-Authorization`。

## 行为

- `tradeState=SUCCESS` 时，将 `course_pay_order.status` 从 `0` 改为 `1`。
- 写入 `wechatTransactionId`、`requestId`、`payTime`，并在同一事务内将订单用户的 `user_info.is_student` 条件更新为 `1`。
- 重复回调幂等返回成功；已支付订单仍会补偿学员标记。

## 返回

```xml
<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>
```
