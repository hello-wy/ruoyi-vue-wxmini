# POST /system/refund/course/{orderNo}

## 用途

管理员发起课程订单退款。

## 鉴权

- `Authorization: Bearer <token>`
- 权限：`system:refund:edit`

## 行为

- 仅 `status=2` 已签到课程订单可退款。
- 调用微信支付 `refundV3`。
- 成功后写 `refundNo`、`refundTime`，并将 `status` 改为 `3`。

## 成功响应

```json
{
  "code": 200,
  "msg": "退款成功"
}
```
