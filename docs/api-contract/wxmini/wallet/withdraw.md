# POST /wxmini/wallet/withdraw

## 用途

- `POST`：发起微信提现到当前登录用户的微信零钱。
- 提现前必须已完成实名认证，并且当前登录小程序账号已绑定微信 `openid`。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## Body 示例

```json
{
  "amount": "100.00"
}
```

## 成功响应示例

```json
{
  "code": 200,
  "msg": "微信提现成功"
}
```

## 失败场景

- 未实名：`msg = 请先完成实名认证后再提现`
- 未绑定 openid：`msg = 未获取到微信账户信息，请重新登录后重试`
- 余额不足：`msg = 可用余额不足`
- 金额非法：`msg = 提现金额不能低于1元` / `msg = 提现金额最多保留两位小数`
- 微信侧发起失败：返回微信或后端明确错误信息

## 记录状态说明

- `0`：打款中
- `1`：已打款
- `2`：打款失败

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxWalletController.java`
