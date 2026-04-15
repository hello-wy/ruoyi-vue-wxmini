# GET /wxmini/profile/detail

## 用途
获取当前登录用户个人资料、身份类型以及个人中心主 CTA 信息。

## 鉴权
- 需要 `Wx-Authorization: Bearer <token>`

## 成功响应示例
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "userInfoId": 1,
    "displayName": "张三",
    "userName": "张三",
    "phone": "13800138000",
    "userType": 0,
    "userTypeLabel": "家长",
    "canSwitchUserType": true,
    "primaryAction": "/pages/tutoring/parent/apply",
    "switchableUserTypes": [0, 1]
  }
}
```
