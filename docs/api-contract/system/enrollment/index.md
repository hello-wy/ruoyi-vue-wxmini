# POST|PUT /system/enrollment

## 用途

- `POST`：新增学籍信息。
- `PUT`：修改学籍信息。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:enrollment:add')`。
- 需要具备权限标识：`@ss.hasPermi('system:enrollment:edit')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## POST 请求

### Query 参数

- 无。

### Body 示例

- 请求体为 `StudentEnrollment` JSON 对象，字段以对应 BO / domain 定义为准。

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

- 请求体为 `StudentEnrollment` JSON 对象，字段以对应 BO / domain 定义为准。

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

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/StudentEnrollmentController.java`
