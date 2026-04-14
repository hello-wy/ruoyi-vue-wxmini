# POST /wxmini/pay/salon/orders/create

## 用途

从沙龙详情页创建微信 JSAPI 支付订单，返回小程序拉起支付所需参数。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## Body

```json
{
  "salonId": 1
}
```

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "orderNo": "SALON202604141234561712345678901",
    "payParam": {
      "appId": "wx123",
      "timeStamp": "1710000000",
      "nonceStr": "nonce",
      "packageValue": "prepay_id=wx123",
      "paySign": "sign"
    }
  }
}
```

## 说明

- 支付金额以后端读取 `salon_info.current_price` 为准
- 前端不能将 `requestPayment` 返回直接视为支付成功，必须继续查询订单状态
