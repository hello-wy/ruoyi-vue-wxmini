# GET /wxmini/profile/detail

## 用途

- `GET`：获取当前登录用户个人资料。
- 返回前端可切换的身份列表，前端应据此决定是否展示商家身份选项。

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
    "birthday": "1992-08-18",
    "realName": "张三",
    "idCard": "11010519491231002X",
    "verified": true,
    "isRealnameAuth": 1,
    "userType": 2,
    "userTypeLabel": "商家",
    "canSwitchUserType": true,
    "primaryAction": "/pages/jobs/apply",
    "switchableUserTypes": [0, 1, 2, 3],
    "age": 24,
    "personalIntro": "认真负责",
    "availableTime": "周末全天",
    "workExperience": "做过活动协助与地推兼职"
  }
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- `isRealnameAuth` 取自 `user_info.is_realname_auth`，`1` 表示已完成实名认证，`0` 或空表示未完成。
- `displayName` 优先取 `user_info.real_name`；没有实名姓名时返回手机号脱敏值（前 3 位 + `****` + 后 4 位），不再回退到 `user_name`。
- `realName` 取自 `user_info.real_name`，即实名认证写入的真实姓名。
- 当 `isRealnameAuth = 1` 时，`idCard` 返回当前登录用户的完整认证身份证号，用于家教申请页回填；未认证时该字段为空或不返回。
- `birthday` 为可空生日字段，格式 `yyyy-MM-dd`。
- `switchableUserTypes` 由服务端动态计算。
- 默认始终返回 `0=家长`、`1=学生`、`3=阿姨`。
- 是否包含 `2=商家` 取决于当前用户是否满足商家白名单条件：
  - 当前用户 `user_info.id_card` 非空；
  - 当前用户 `user_info.real_name` 非空；
  - 存在 `status = 1` 的商家白名单记录，且白名单 `id_card` 与当前用户身份证完全一致；
  - 白名单 `real_name` 与当前用户实名姓名完全一致。
- 若不满足上述任一条件，则返回 `switchableUserTypes = [0, 1, 3]`，前端不应展示商家身份选项。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxUserProfileController.java`
