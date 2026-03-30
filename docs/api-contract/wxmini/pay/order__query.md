# GET /wxmini/pay/order/query

## 用途

主动查询微信支付订单状态。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## Query 参数

- `outTradeNo`：必填，商户系统内部订单号

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "tradeState": "SUCCESS"
  }
}
```

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxPayController.java`
