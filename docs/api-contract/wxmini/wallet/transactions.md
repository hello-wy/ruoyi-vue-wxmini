# GET /wxmini/wallet/transactions

## 用途

- `GET`：获取当前登录小程序用户的钱包流水。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "uid": 2001,
      "bizType": "PAYROLL",
      "bizId": "PAYROLL20260502123456",
      "direction": 1,
      "amount": 200.00,
      "balanceAfter": 1200.00,
      "remark": "兼职工资入账",
      "createTime": "2026-05-02 15:31:00"
    }
  ]
}
```

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxWalletController.java`
