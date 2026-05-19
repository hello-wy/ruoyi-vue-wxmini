# POST /wxmini/wallet/withdraw

## 用途

- `POST`：发起微信提现到当前登录用户的微信零钱。
- 提现前必须已完成实名认证，并且当前登录小程序账号已绑定微信 `openid`。
- 同一用户同时只能有一笔处理中的提现，重复提交会被拒绝。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## Request Body

```json
{
  "amount": "50.00"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| amount | string | 是 | 提现金额，单位元，最多两位小数，最低 1.00 |

## 响应结构

### 成功响应（status=1，提现已到账）

```json
{
  "code": 200,
  "msg": "微信提现成功",
  "data": {
    "success": true,
    "status": 1,
    "withdrawId": 123,
    "outBatchNo": "WD20250101120000000123",
    "failType": null,
    "userMessage": null,
    "msg": "微信提现成功"
  }
}
```

### 处理中响应（status=0，已发起但未到账）

```json
{
  "code": 200,
  "msg": "微信提现处理中",
  "data": {
    "success": true,
    "status": 0,
    "withdrawId": 123,
    "outBatchNo": "WD20250101120000000123",
    "failType": null,
    "userMessage": null,
    "msg": "微信提现处理中"
  }
}
```

### 失败响应（code != 200）

业务失败时返回 `code != 200`，`data` 中包含结构化错误信息：

```json
{
  "code": 500,
  "msg": "未获取到微信账户信息，请重新登录后重试",
  "data": {
    "success": false,
    "status": 2,
    "withdrawId": null,
    "outBatchNo": null,
    "failType": "OPENID_MISSING",
    "userMessage": "未获取到微信账户信息，请重新登录后重试",
    "msg": "未获取到微信账户信息，请重新登录后重试"
  }
}
```

## data 字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| success | boolean | 是否成功发起（true=成功或处理中，false=失败） |
| status | int | 提现状态：`0`=处理中，`1`=成功，`2`=失败 |
| withdrawId | Long / null | 提现记录 ID，前置校验失败时为 null |
| outBatchNo | String / null | 微信转账批次号，前置校验失败时为 null |
| failType | String / null | 失败类型枚举值，成功/处理中时为 null |
| userMessage | String / null | 面向用户的中文失败提示，成功/处理中时为 null |
| msg | String | 兼容旧版的消息字段 |

## failType 枚举值

| failType | userMessage |
|----------|-------------|
| `MERCHANT_PERMISSION_NOT_GRANTED` | 商户号未开通商家转账功能，请联系客服 |
| `APPID_MCHID_NOT_BOUND` | 小程序与商户号未绑定，请联系客服 |
| `CERT_OR_KEY_INVALID` | 支付证书配置异常，请联系客服 |
| `USER_NOT_REALNAME` | 请先完成实名认证后再提现 |
| `OPENID_MISSING` | 未获取到微信账户信息，请重新登录后重试 |
| `AMOUNT_OUT_OF_LIMIT` | 提现金额超出限制 |
| `BALANCE_INSUFFICIENT` | 可用余额不足 |
| `PENDING_WITHDRAW_EXISTS` | 您有一笔提现正在处理中，请等待完成后再试 |
| `RECIPIENT_NOT_REALNAME` | 收款人需在微信内完成实名认证后才能收款 |
| `MERCHANT_BALANCE_INSUFFICIENT` | 商户余额不足，请联系客服 |
| `RECONCILE_TIMEOUT` | 提现超时未完成，请联系客服 |
| `UNKNOWN` | 提现失败，请稍后重试 |

## 前置校验失败说明

以下情况属于前置校验失败，**不会创建 `wallet_withdraw` 记录**（即 `withdrawId` 和 `outBatchNo` 为 null）：

- 金额为空或格式不正确
- 金额低于最低限额（默认 1.00 元）
- 金额精度超过两位小数
- 可用余额不足（`BALANCE_INSUFFICIENT`）
- 未完成实名认证（`USER_NOT_REALNAME`）
- 未绑定微信 openId（`OPENID_MISSING`）
- 存在处理中的提现（`PENDING_WITHDRAW_EXISTS`）

## 提现记录状态

| status | 含义 | 说明 |
|--------|------|------|
| 0 | 打款中 | 已发起微信转账，等待到账确认 |
| 1 | 已打款 | 资金已到达用户微信零钱 |
| 2 | 打款失败 | 转账失败，可查看 failType 了解原因 |

## 前端处理建议

- `code === 200 && data.status === 1`：提现成功，关闭弹窗，刷新余额
- `code === 200 && data.status === 0`：处理中，关闭弹窗，提示"提现处理中"，启动轮询（每 5s，最多 6 次）
- `code !== 200`：失败，保持弹窗打开，显示 `data.userMessage` 或 `msg`

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxWalletController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/WalletServiceImpl.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/enums/WithdrawFailType.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/dto/WithdrawResult.java`
