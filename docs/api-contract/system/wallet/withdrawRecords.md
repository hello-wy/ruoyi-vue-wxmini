# GET /system/wallet/withdrawRecords

## 用途

获取当前登录用户的提现记录列表。

## 鉴权

- 走若依标准登录态，不是 `Wx-Authorization`

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "amount": 50.00
    }
  ]
}
```

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxWalletController.java`
