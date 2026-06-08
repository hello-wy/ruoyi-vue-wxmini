# GET /wxmini/pay/courses/orders/{orderNo}

## 用途

查询课程报名订单详情。若订单仍为 `0`，后端会查询微信订单并在支付成功时补偿为 `1`。

## 鉴权

- `Wx-Authorization: Bearer <token>`

## Path 参数

- `orderNo`：平台课程订单号。

## 成功响应

返回字段同 `/wxmini/pay/courses/orders/my` 单条元素。

## 状态

- `0` 待支付
- `1` 已支付待签到
- `2` 已签到
- `3` 已退款
- `4` 已取消
