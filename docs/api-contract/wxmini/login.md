# GET /wxmini/login

## 用途

微信小程序匿名登录入口。前端传 `appid` 和 `code`，后端换取微信会话信息，按 `openId` 自动注册或查找用户，并返回小程序业务 token 与临时会话信息。

## 鉴权

- 公开接口

## 请求头

- 无

## Path 参数

- 无

## Query 参数

- `appid`：必填，小程序 AppID
- `code`：必填，微信登录临时凭证

## Body 示例

- 无

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "apiToken": "jwt-token",
    "sessionKey": "session-key",
    "openId": "openid",
    "userName": "微信用户",
    "userType": "0",
    "phone": null,
    "avatarUrl": null
  }
}
```

## 失败场景或特殊说明

- `code != 200` 时，`msg` 可能为 `empty jscode` 或 `can not find appid=[xxx] config`
- 返回字段与 `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/bo/WxUserInfo.java` 一致，不返回 `unionId`
- `phone` 为空表示用户尚未完成手机号实时验证。前端必须继续调用 `POST /wxmini/user/phone`，登录态才算补全
- `userName` 为空时后端默认返回 `微信用户`
- `userType` 为空时后端默认返回 `0`

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxLoginController.java`
- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/bo/WxUserInfo.java`
