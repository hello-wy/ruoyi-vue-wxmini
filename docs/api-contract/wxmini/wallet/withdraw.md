# POST /wxmini/wallet/withdraw

## 用途

- `POST`：申请钱包提现。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## Body 示例

```json
{
  "amount": "100.00"
}
```

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxWalletController.java`
