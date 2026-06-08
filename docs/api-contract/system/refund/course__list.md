# GET /system/refund/course/list

## 用途

课程退款管理列表。

## 鉴权

- `Authorization: Bearer <token>`
- 权限：`system:refund:list`

## Query 参数

- `courseId`：选填，课程 ID。
- `keyword`：选填，姓名、手机号、订单号或用户名。

## 返回字段

- `orderId`
- `orderNo`
- `userId`
- `courseId`
- `courseName`
- `userName`
- `amount`
- `status`
- `signedIn`
- `canRefund`
- `signTime`
- `refundTime`

## 说明

列表返回 `status in (2, 3)` 的课程订单；`status=2` 时 `canRefund=true`。
