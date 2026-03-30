# GET /system/wallet/info

## 用途

获取当前登录用户钱包信息。

## 鉴权

- 走若依标准登录态，不是 `Wx-Authorization`

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "balance": 100.00
  }
}
```

## 特殊说明

- 当前 controller 路径是 `/system/wallet/**`，不是 `/wxmini/wallet/**`。

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxWalletController.java`
