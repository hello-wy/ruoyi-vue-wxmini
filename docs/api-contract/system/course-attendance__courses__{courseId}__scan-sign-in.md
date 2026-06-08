# POST /system/course-attendance/courses/{courseId}/scan-sign-in

## 用途

管理员扫码课程签到。扫码页提供用户 `userId`，后端按 `{courseId, userId}` 查最新课程订单。

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
  "msg": "操作成功",
  "data": {
    "orderNo": "COURSE202606072300001234",
    "userId": "wx-user-uuid",
    "courseId": 1,
    "status": 2,
    "signTime": "2026-06-07 23:30:00"
  }
}
```

## 错误

- 无订单：`未找到该用户当前课程已支付待签到订单`
- 未支付：`课程订单未支付，不能签到`
- 已签到：`课程订单已签到`
- 已退款：`课程订单已退款，不能签到`
