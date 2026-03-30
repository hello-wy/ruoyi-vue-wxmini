# POST /wxmini/pay/order/create

## 用途

创建微信 JSAPI 支付订单，返回小程序唤起支付所需参数。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## Body

- 当前 controller 无请求体

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "appId": "appid",
    "timeStamp": "1710000000",
    "nonceStr": "nonce",
    "packageValue": "prepay_id=xxx",
    "paySign": "sign"
  }
}
```

## 特殊说明

- 当前实现中订单描述、回调地址、金额等参数仍是写死示例，前端联调时不要自行推断真实业务参数。

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxPayController.java`
