# 兼职报名支付接口契约

## 变更目的

为兼职岗位增加“报名支付”链路，支持：
- 当前用户查询自己的兼职报名订单列表
- 创建兼职报名支付订单并拉起微信 JSAPI 支付
- 查询单笔兼职报名订单状态
- 接收微信支付回调并按名额处理报名成功或自动退款

## 相关数据约定

### 岗位字段

`daily_jobs.signup_limit`
- 类型：`int`
- 含义：岗位报名人数上限
- 说明：剩余名额不单独落库，通过 `signup_limit - 已支付成功订单数` 推导

### 订单表

`job_signup_order`
- 主键：`id`
- 业务单号：`order_no`
- 用户：`user_id`
- 岗位：`job_id`
- 金额：`amount`
- 状态：`status`
- 微信交易号：`wechat_transaction_id`
- 退款单号：`refund_no`
- 支付时间：`pay_time`
- 退款时间：`refund_time`
- 回调请求号：`request_id`

### 订单状态枚举

| 值 | 枚举 | 含义 |
|---|---|---|
| 0 | PENDING | 待支付 |
| 1 | PAID | 已支付/报名成功 |
| 2 | REFUNDING | 退款中 |
| 3 | REFUNDED | 已退款 |
| 4 | CANCELED | 已取消 |

前端“当前岗位已报名”的判定规则：
- 仅 `status = 1` 视为已报名
- `0/2/3/4` 均不视为已报名

### 金额约定

- 报名费由后端固定常量控制：`50.00`
- 前端只展示和消费返回结果，不上传自定义金额

## 鉴权约定

- `/wxmini/pay/jobs/orders/my`
- `/wxmini/pay/jobs/orders/create`
- `/wxmini/pay/jobs/orders/{orderNo}`

以上接口需要小程序登录态，使用请求头：
- `Wx-Authorization: Bearer <token>`

回调接口：
- `/wxmini/pay/jobs/notify`

该接口由微信服务器调用，允许匿名访问，需要加入小程序 JWT 白名单。

---

## 1. 查询当前用户兼职报名订单列表

### 请求

- 方法：`GET`
- 路径：`/wxmini/pay/jobs/orders/my`

### 说明

- 仅返回当前登录用户自己的兼职报名订单
- 用于前端统一拉取报名订单列表并缓存到 Pinia / 本地存储

### 响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "orderNo": "JOBSIGN202604161200001234",
      "jobId": 123,
      "jobTitle": "日结兼职助教",
      "amount": 50.00,
      "status": 1,
      "payTime": "2026-04-16 12:00:00",
      "refundTime": null
    }
  ]
}
```

### 字段说明

| 字段 | 类型 | 说明 |
|---|---|---|
| orderNo | string | 平台订单号 |
| jobId | number | 岗位 ID |
| jobTitle | string | 岗位标题 |
| amount | number | 报名金额，固定 50.00 |
| status | number | 订单状态枚举值 |
| payTime | string\|null | 支付时间 |
| refundTime | string\|null | 退款时间 |

---

## 2. 创建兼职报名支付订单

### 请求

- 方法：`POST`
- 路径：`/wxmini/pay/jobs/orders/create`
- Content-Type：`application/json`

### 请求体

```json
{
  "jobId": 123
}
```

### 校验与处理规则

- 岗位不存在：拒绝
- 岗位状态不是 `0-招募中`：拒绝
- 当前用户该岗位已存在 `PAID(1)` 订单：拒绝重复报名
- 当前用户该岗位存在旧 `PENDING(0)` 订单：
  - 先关闭旧微信订单
  - 再把旧订单更新为 `CANCELED(4)`
- 创建新的微信 JSAPI 订单
- 保存新的 `PENDING(0)` 报名订单

### 响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "orderNo": "JOBSIGN202604161200001234",
    "payParam": {
      "appId": "wx0ac1366c34e4ce68",
      "timeStamp": "1713240000",
      "nonceStr": "random-string",
      "packageValue": "prepay_id=wx123",
      "paySign": "SIGNATURE"
    }
  }
}
```

### 返回说明

| 字段 | 类型 | 说明 |
|---|---|---|
| orderNo | string | 平台订单号 |
| payParam | object | 微信 JSAPI 调起参数 |

---

## 3. 查询兼职报名订单详情

### 请求

- 方法：`GET`
- 路径：`/wxmini/pay/jobs/orders/{orderNo}`

### 说明

- 仅允许查询当前登录用户自己的订单
- 返回订单状态和用于页面展示的岗位/金额信息
- 当前库内状态仍为 `PENDING(0)` 时，接口会先主动向微信查单并同步本地订单状态，再返回最新结果
- 微信查单返回 `SUCCESS` 时，订单会在返回前修正为 `PAID(1)`
- 微信查单不是 `SUCCESS` 时，订单会在返回前收敛为 `CANCELED(4)`

### 响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "orderNo": "JOBSIGN202604161200001234",
    "jobId": 123,
    "jobTitle": "日结兼职助教",
    "amount": 50.00,
    "status": 3,
    "payTime": "2026-04-16 12:00:00",
    "refundTime": "2026-04-16 12:01:10"
  }
}
```

---

## 4. 微信兼职报名支付回调

### 请求

- 方法：`POST`
- 路径：`/wxmini/pay/jobs/notify`

### 说明

- 由微信支付服务端异步调用
- 后端解析通知后，仅当支付结果为 `SUCCESS` 才继续处理

### 回调处理逻辑

1. 按 `orderNo` 查询本地订单
2. 若订单已是 `PAID(1)`，直接按幂等成功处理
3. 锁定对应岗位记录
4. 统计当前岗位已支付成功订单数
5. 根据 `signup_limit` 判断：
   - 若仍有名额：
     - 当前订单更新为 `PAID(1)`
     - 若支付后正好满额，将岗位状态更新为 `1-已满员`
   - 若已无名额或岗位不可报名：
     - 当前订单先更新为 `REFUNDING(2)`
     - 调用微信退款
     - 再更新为 `REFUNDED(3)`

### 返回值

- 成功：`<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>`
- 失败：`<xml><return_code><![CDATA[FAIL]]></return_code></xml>`

---

## 前端联动说明

### 详情页按钮状态

- 岗位 `status = 0` 且本地无 `PAID(1)` 订单：显示 `立即报名`
- 岗位 `status = 0` 且本地存在 `PAID(1)` 订单：显示 `已报名`
- 岗位 `status = 1`：显示 `已满员`
- 岗位 `status = 2`：显示 `已结束`

### 支付结果轮询建议

前端在支付完成后轮询 `/wxmini/pay/jobs/orders/{orderNo}`：
- `status = 1`：提示报名成功
- `status = 2` 或 `3`：提示名额已满，已自动退款
- 之后刷新 `/wxmini/pay/jobs/orders/my` 本地缓存
