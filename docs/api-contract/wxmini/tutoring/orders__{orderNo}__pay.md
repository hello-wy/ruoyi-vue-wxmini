# POST /wxmini/tutoring/orders/{orderNo}/pay

## 用途

- `POST`：当前登录家长支付一个已经存在的待支付家教订单，返回 JSAPI 支付参数。

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

- 当前用户不存在：`msg = 用户不存在`。
- 当前用户缺少 openId：`msg = 当前用户缺少openId`。
- 订单不存在或不属于当前家长：`msg = 订单不存在`。
- 订单状态不是待支付：`msg = 当前订单状态不可支付`。
- 订单金额为空或小于等于 0：`msg = 订单金额不合法`。
- 本接口用于支付后台已通过 `POST /system/tutoring-admin/orders` 生成的待支付订单。
- 本接口不会插入新的 `tutoring_order` 业务订单，也不会关闭已有待支付订单；微信下单使用传入的原 `orderNo`。
- 调起微信支付前会把当前用户 openId 写回订单的 `wechatOpenId`，并更新订单修改时间。
- 微信支付成功后复用家教支付回调闭环：订单置为已支付、绑定置为已下单、同一家长需求的其他绑定关闭，并生成家教课表。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxTutoringPayServiceImpl.java`
