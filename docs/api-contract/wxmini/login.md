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

- `appid`：小程序 AppID，必填。
- `code`：微信登录临时凭证，必填。
- `phoneCode`：微信实时手机号验证 code，可选。仅当首次微信登录返回 `needPhoneCode: true` 后，由前端再次调用本接口时传入。
- `inviteCode`：历史兼容字段，可选。当前邀请关系推荐在登录成功取得 `apiToken` 后调用 `POST /wxmini/referral/bind` 完成绑定。

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
    "userId": "user-123",
    "userName": "微信用户",
    "userType": null,
    "phone": null,
    "avatarUrl": null,
    "needPhoneCode": false
  }
}
```

### 失败场景或特殊说明

- 以当前 controller/service 的实际校验结果为准。
- 本接口已合并微信登录与手机号快捷注册逻辑。
- 已注册用户：后端根据 `code` 换取 `openId`，查询到用户后直接返回登录态 `apiToken`。
- 未注册用户且未传 `phoneCode`：返回 `code: 200`，`data.needPhoneCode: true`，不返回 `apiToken`，前端需要引导用户授权手机号后再次调用本接口。
- 未注册用户且传入 `phoneCode`：后端解析手机号并创建用户，返回登录态 `apiToken`。
- 邀请关系绑定：本接口不再作为最终绑定入口。前端若本地存在待绑定邀请码，应在登录成功取得 `apiToken` 后调用 `POST /wxmini/referral/bind`，由登录态确定当前被邀请用户。
- `userType` 允许为空，表示当前用户尚未选择身份。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxLoginController.java`
