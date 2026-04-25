# POST|PUT /system/parents

## 用途

- `POST`：POST /system/parents。
- `PUT`：PUT /system/parents。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:parents:add')`。
- 需要具备权限标识：`@ss.hasPermi('system:parents:edit')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## POST 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "subject": "数学",
  "region": "鼓楼区",
  "methods": 1,
  "grade": "初一",
  "address": "南京市鼓楼区xx路",
  "salary": "200/次",
  "description": "希望一对一上门辅导"
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": 123456789012345678
}
```

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。

## PUT 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "subject": "数学",
  "region": "鼓楼区",
  "methods": 1,
  "grade": "初一",
  "address": "南京市鼓楼区xx路",
  "salary": "200/次",
  "description": "希望一对一上门辅导"
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
- 无权限时，请求会被拦截。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/ParentsController.java`
