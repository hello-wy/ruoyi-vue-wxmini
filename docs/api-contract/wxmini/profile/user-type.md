# PUT /wxmini/profile/user-type

## 用途

- `PUT`：切换当前登录用户身份。

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

## 实现来源文件

- `ruoyi-wxmini/src/main/java/com/ruoyi/wxmini/controller/WxUserProfileController.java`
