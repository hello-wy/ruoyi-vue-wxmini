# POST /system/course-attendance/courses/{courseId}/scan-sign-in

## 用途

管理员扫码课程签到。扫码页提供用户 `userId`，后端按 `{courseId, userId}` 查最新课程订单；签到成功后立即调用微信支付退款接口，退款金额原路退回付款账户。

## 鉴权

- `Authorization: Bearer <token>`
- 权限：`system:course-attendance:edit`

## Body

```json
{
  "userId": "wx-user-uuid"
}
```

## 成功响应

```json
{
  "code": 200,
  "msg": "签到成功，已原路退款",
  "data": {
    "orderNo": "CRS2026060723000012345678901",
    "userId": "wx-user-uuid",
    "courseId": 1,
    "status": 3,
    "signTime": "2026-06-07 23:30:00",
    "refundTime": "2026-06-07 23:30:01"
  }
}
```

## 行为

1. 仅允许 `status=1`（已支付待签到）的课程订单签到。
2. 签到后订单状态先更新为 `2`（已签到）。
3. 后端调用微信支付 `refundV3`，使用原订单号退款；退款成功后订单状态更新为 `3`（已退款），并记录 `refundNo`、`refundTime`。
4. 退款失败时接口返回微信退款错误，签到状态保留，以便管理员通过课程退款接口继续处理。

## 错误

- 无订单：`未找到该用户当前课程已支付待签到订单`
- 未支付：`课程订单未支付，不能签到`
- 已签到：`课程订单已签到`
- 已退款：`课程订单已退款，不能签到`
- 微信退款失败：`微信退款接口调用失败: ...`
