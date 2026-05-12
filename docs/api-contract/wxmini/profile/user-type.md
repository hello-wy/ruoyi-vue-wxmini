# PUT /wxmini/profile/user-type

## 用途

- `PUT`：切换当前登录用户身份。
- 当目标身份为 `2=商家` 时，服务端会按当前用户实名认证信息校验商家身份白名单。

## 鉴权

- 需要 `Wx-Authorization: Bearer <token>`。

## 请求头

- `Wx-Authorization: Bearer <token>`

## Path 参数

- 无。

## PUT 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "userType": 2
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- `userType` 为空时：`msg = 请选择用户身份`。
- 当前服务实现允许切换为 `0=家长`、`1=学生`、`2=商家`、`3=阿姨`。
- 当 `userType = 2` 时，服务端按当前用户 `user_info.id_card` 精确查询启用状态的商家白名单记录，并要求白名单 `real_name` 与当前用户 `user_info.real_name` 完全一致。
- 当商家白名单不存在、未启用、身份证为空、真实姓名为空或实名姓名不匹配时：`msg = 当前账号暂未开通商家身份`。

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxUserProfileController.java`
