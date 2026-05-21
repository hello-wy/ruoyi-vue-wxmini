# POST /wxmini/tutoring/orders/create

## 用途

- `POST`：创建家教支付订单，返回 JSAPI 支付参数。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`
- `Content-Type: application/json`

## Body 示例

```json
{
  "bindingId": 20001
}
```

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "orderNo": "TUTOR202605191230001234",
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

## 失败场景或特殊说明

- `bindingId` 为空：`msg = 绑定关系不能为空`。
- 当前用户不存在：`msg = 用户不存在`。
- 当前用户缺少 openId：`msg = 当前用户缺少openId`。
- 绑定不存在或不属于当前家长：`msg = 绑定关系不存在`。
- 绑定已关闭：`msg = 该绑定已关闭`。
- 该绑定下已有已支付订单：`msg = 该绑定已完成下单`。
- 同一绑定下若存在旧的待支付订单，后端会先尝试关闭旧微信单，再把旧业务订单改为 `CANCELED`。
- 订单金额按家长需求的 `hourlyBudget * 每个服务时段小时数` 汇总计算。
- 微信支付回调成功后，会生成 `tutoring_schedule`，并把该家长的其它未关闭绑定自动置为关闭。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxTutoringPayServiceImpl.java`
