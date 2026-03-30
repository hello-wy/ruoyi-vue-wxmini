# POST /system/wallet/withdraw

## 用途

申请提现到微信钱包。

## 鉴权

- 走若依标准登录态，不是 `Wx-Authorization`

## Body 示例

```json
{
  "amount": "50.00"
}
```

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": "提现申请已提交"
}
```

## 失败说明

- 缺少金额：`msg = 提现金额不能为空`
- 金额非法：`msg = 金额格式不正确`

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxWalletController.java`
