# GET /wxmini/profile/detail

## 用途

- `GET`：获取当前登录用户个人资料。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

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
    "userInfoId": 1,
    "displayName": "张三",
    "userName": "张三",
    "phone": "13800138000",
    "userType": 2,
    "userTypeLabel": "商家",
    "canSwitchUserType": true,
    "primaryAction": "/pages/jobs/apply",
    "switchableUserTypes": [0, 1, 2, 3]
  }
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 当前实现会返回 `switchableUserTypes = [0, 1, 2, 3]`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxUserProfileController.java`
