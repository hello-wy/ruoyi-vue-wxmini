# GET /wxmini/user/phone

## 用途

解密并同步当前登录用户的手机号。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## Query 参数

- `appid`：必填
- `sessionKey`：必填
- `encryptedData`：必填
- `iv`：必填
- `signature`：可选
- `rawData`：可选

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "phoneNumber": "13800000000"
  }
}
```

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxMaUserController.java`
