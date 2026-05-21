# GET /wxmini/tutoring/orders/{orderNo}

## 用途

- `GET`：查询当前登录家长指定家教订单详情。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- `orderNo`：平台家教订单号。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 50001,
    "orderNo": "TUTOR202605191230001234",
    "bindingId": 20001,
    "parentId": 30001,
    "parentUserId": 101,
    "tutorId": 40001,
    "tutorUserId": 202,
    "serviceTimesSnapshot": "[{\"serviceDate\":\"2026-05-20\",\"startTime\":\"18:00\",\"endTime\":\"20:00\"}]",
    "lessonCount": 1,
    "hourlyPrice": 120.00,
    "totalAmount": 240.00,
    "commissionRate": 10.00,
    "status": 1,
    "wechatTransactionId": "wx-tutoring-1",
    "requestId": "req-1",
    "payTime": "2026-05-19 12:35:00"
  }
}
```

## 失败场景或特殊说明

- 订单不存在或不属于当前家长：`msg = 订单不存在`。
- 若订单仍处于 `PENDING`，后端会先向微信查询实际支付结果，并同步更新本地状态。
- 微信确认成功后，会补偿生成课表并写入支付成功信息。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxTutoringPayServiceImpl.java`
