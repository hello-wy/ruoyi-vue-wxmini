# POST /wxmini/wallet/withdraw

## 用途

- `POST`：发起微信提现到当前登录用户的微信零钱。
- 提现前必须已完成实名认证，并且当前登录小程序账号已绑定微信 `openid`。
- 同一用户同时只能有一笔处理中的提现，重复提交会被拒绝。
- 微信商家转账使用升级版接口：`POST /v3/fund-app/mch-transfer/transfer-bills`。
- 微信转账创建成功后，只要提现记录进入 `status=0` 打款中，就立即把提现金额从可用余额转入冻结金额，并写入提现支出流水；不是等 `status=1` 已到账后才占用余额。
- 如果后续微信侧返回成功，后端会释放冻结金额并累计到已提现；如果返回失败，则释放冻结金额并返还到可用余额。

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
| amount | string | 是 | 提现金额，单位元，最多两位小数，最低 1.00，最高 2000.00 |

> 后端会再次校验提现金额不能超过当前可用余额；超过时返回 `BALANCE_INSUFFICIENT`，不会创建提现记录。

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

### 处理中响应（status=0，已冻结，待微信侧完成）

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
    "msg": "微信提现处理中",
    "packageInfo": "package_info_from_wechat",
    "appId": "wx小程序 appId",
    "mchId": "微信支付商户号"
  }
}
```

> 当微信返回 `WAIT_USER_CONFIRM` 时，后端会在处理中响应里附带 `packageInfo/appId/mchId`，小程序端需要用这些字段调用 `uni.requestMerchantTransfer` 引导用户确认收款。非确认模式或微信未返回 `package_info` 时，这三个字段为 null 或不存在。

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
| packageInfo | String / null | 微信商家转账确认收款 `package_info`，仅 `WAIT_USER_CONFIRM` 时返回 |
| appId | String / null | 调用 `uni.requestMerchantTransfer` 所需小程序 appId，仅确认收款时返回 |
| mchId | String / null | 调用 `uni.requestMerchantTransfer` 所需微信支付商户号，仅确认收款时返回 |

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
- 金额高于最高限额（默认 2000.00 元）
- 金额精度超过两位小数
- 可用余额不足（`BALANCE_INSUFFICIENT`）
- 未完成实名认证（`USER_NOT_REALNAME`）
- 未绑定微信 openId（`OPENID_MISSING`）
- 存在处理中的提现（`PENDING_WITHDRAW_EXISTS`）

## 提现记录状态

| status | 含义 | 说明 |
|--------|------|------|
| 0 | 打款中 | 已发起微信转账，提现金额已从可用余额转入冻结金额，等待到账确认 |
| 1 | 已打款 | 资金已到达用户微信零钱，冻结金额转入累计提现，可用余额不再重复扣减 |
| 2 | 打款失败 | 转账失败，可查看 failType 了解原因；冻结金额已释放并返还到可用余额 |

## 状态同步

- `GET /wxmini/wallet/withdraw-records` 返回记录前，会主动同步当前用户仍处于 `status=0` 的提现单。
- 因此小程序轮询提现记录时，即使微信回调或 5 分钟定时对账未及时触发，也能推动已到账记录从“打款中”更新为“已打款”。

## 微信转账状态映射

后端以 `wallet_withdraw.status` 对前端收敛状态，微信升级版商家转账单状态按以下规则处理：

| 微信 state | wallet_withdraw.status | 说明 |
|------------|------------------------|------|
| `SUCCESS` | 1 | 已成功到账 |
| `WAIT_USER_CONFIRM` | 0 | 待用户在微信确认收款，响应中可能返回 `packageInfo/appId/mchId` |
| `FAILED` | 2 | 转账失败 |
| 其他状态 | 0 | 处理中，等待回调或轮询同步 |

## 前端处理建议

- `code === 200 && data.status === 1`：提现成功，关闭弹窗，刷新余额
- `code === 200 && data.status === 0 && data.packageInfo`：金额已冻结，先调用 `uni.requestMerchantTransfer`，用户确认后保留弹窗并启动轮询（每 5s，最多 6 次）
- `code === 200 && data.status === 0 && !data.packageInfo`：金额已冻结，保留弹窗，提示"提现处理中"，启动轮询（每 5s，最多 6 次）
- `code !== 200`：失败，保持弹窗打开，显示 `data.userMessage` 或 `msg`

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxWalletController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/WalletServiceImpl.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/enums/WithdrawFailType.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/dto/WithdrawResult.java`
