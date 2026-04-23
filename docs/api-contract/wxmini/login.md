# GET /wxmini/login

## 用途

- `GET`：GET /wxmini/login。

## 鉴权

- 公开接口。

## 请求头

- 无。

## Path 参数

- 无。

## GET 请求

### Query 参数

- 无。

### Body 示例

- 无。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "apiToken": "jwt-token",
    "sessionKey": "session-key",
    "openId": "openid",
    "userName": "微信用户",
    "userType": null,
    "phone": null,
    "avatarUrl": null
  }
}
```

### 失败场景或特殊说明

- 以当前 controller/service 的实际校验结果为准。
- 登录成功后若 `phone` 仍为空，前端需要继续调用 `POST /wxmini/user/phone` 完成手机号补全。
- `userType` 允许为空，表示当前用户尚未选择身份。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxLoginController.java`
