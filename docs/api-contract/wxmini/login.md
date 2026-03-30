# GET /wxmini/login

## 用途

微信小程序登录。前端传 `appid` 和 `code`，后端换取微信会话信息，自动注册用户并返回接口 token。

## 鉴权

- 公开接口

## Query 参数

- `appid`：必填，小程序 AppID
- `code`：必填，微信登录临时凭证

## 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "apiToken": "jwt-token",
    "openId": "openid",
    "unionId": "unionid"
  }
}
```

## 失败说明

- `code != 200` 时，`msg` 可能为 `empty jscode` 或 `can not find appid=[xxx] config`

## 实现来源

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxLoginController.java`
