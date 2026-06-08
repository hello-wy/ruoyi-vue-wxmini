# GET /wxmini/pay/courses/orders/paid

## 用途

查询当前用户指定课程最新已支付订单，用于前端判断是否已报名。

## 鉴权

- `Wx-Authorization: Bearer <token>`

## Query 参数

- `courseId`：课程 ID，必填。

## 成功响应

- 有订单：`data` 为课程订单详情。
- 无订单：`data` 为 `null`。
