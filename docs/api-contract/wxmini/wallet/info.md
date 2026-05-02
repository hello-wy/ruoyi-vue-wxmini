# GET /wxmini/wallet/info

## 用途

- `GET`：获取当前登录小程序用户的钱包信息。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "uid": 2001,
    "balance": 1200.00,
    "frozen": 0.00,
    "totalEarned": 3200.00,
    "totalWithdrawn": 2000.00
  }
}
```

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxWalletController.java`
