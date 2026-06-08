# POST /wxmini/pay/courses/orders/create

## 用途

创建课程报名微信支付订单，返回 JSAPI 支付参数。

## 鉴权

- `Wx-Authorization: Bearer <token>`

## Body

```json
{
  "courseId": 1,
  "name": "张三",
  "gender": "男",
  "phone": "13800000000",
  "company": "知遇家",
  "accommodation": "需要",
  "enrollmentId": 10
}
```

## 成功响应

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "orderNo": "COURSE202606072300001234",
    "payParam": {}
  }
}
```

## 说明

- 金额取 `lectures.registration_fee`，必须大于 `0`。
- 已存在同课程已支付/已签到订单时返回 `当前课程已报名`。
- 旧待支付订单会先调用微信关单并改为 `4`。
