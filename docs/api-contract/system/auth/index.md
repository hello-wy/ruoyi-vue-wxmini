# POST|PUT /system/auth

## 用途

- `POST`：新增用户实名认证。
- `PUT`：修改用户实名认证。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:auth:add')`。
- 需要具备权限标识：`@ss.hasPermi('system:auth:edit')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## POST 请求

### Query 参数

- 无。

### Body 示例

- 请求体为 `UserRealnameAuth` JSON 对象，字段以对应 BO / domain 定义为准。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。

## PUT 请求

### Query 参数

- 无。

### Body 示例

- 请求体为 `UserRealnameAuth` JSON 对象，字段以对应 BO / domain 定义为准。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/UserRealnameAuthController.java`
