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
  "bindingId": 20001,
  "serviceTimes": "[{\"serviceDate\":\"2026-05-20\",\"startTime\":\"18:00\",\"endTime\":\"20:30\"},{\"serviceDate\":\"2026-05-21\",\"startTime\":\"18:00\",\"endTime\":\"20:30\"}]"
}
```

## Body 字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| bindingId | Long | 是 | 当前家长与陪伴官的绑定关系 ID。 |
| serviceTimes | String | 否 | 当前下单选择的服务时间 JSON 字符串；每项包含 `serviceDate`、`startTime`、`endTime`。传入时以该字段作为订单快照和金额计算依据；未传时使用绑定快照或家长需求中的服务时间。 |

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
- 绑定对应的家长需求已不存在：`msg = 家长需求不存在`。
- 同一绑定下允许存在历史已支付订单；每次购买课程包都会生成新的待支付订单。
- 同一绑定下若存在旧的待支付订单，后端会先尝试关闭旧微信单，再把旧业务订单改为 `CANCELED`。
- 订单金额由后端按 `服务日期数量 * 每天小时数 * hourlyBudget` 计算；实现上会把 `serviceTimes` 中每一条 `serviceDate + startTime + endTime` 作为一个课时明细，逐条计算小时数后汇总。
- 微信支付回调成功后，会生成 `tutoring_schedule`；绑定关系作为家长与陪伴官关系继续保留，后续购买课程包仍可选择。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxTutoringController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/service/impl/WxTutoringPayServiceImpl.java`
