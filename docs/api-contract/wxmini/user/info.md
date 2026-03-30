# GET /wxmini/user/info

## 用途

解密并同步微信用户昵称、头像等用户资料。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`

## Query 参数

- `appid`：必填
- `sessionKey`：必填
- `signature`：可选
- `rawData`：可选
- `encryptedData`：必填
- `iv`：必填

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "nickName": "昵称",
    "avatarUrl": "https://..."
  }
}
```

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxMaUserController.java`
