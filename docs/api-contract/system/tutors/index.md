# POST|PUT /system/tutors

## 用途

- `POST`：新增大学生/教员（自动生成雪花ID）。
- `PUT`：修改大学生/教员信息。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:tutors:add')`。
- 需要具备权限标识：`@ss.hasPermi('system:tutors:edit')`。

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
  "realName": "张三",
  "school": "南京大学",
  "major": "数学",
  "subjects": "数学,物理",
  "areas": "鼓楼区",
  "methods": 1,
  "degree": 1,
  "certificate": "教师资格证",
  "certificates": "https://example.com/cert.jpg"
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
  "realName": "张三",
  "school": "南京大学",
  "major": "数学",
  "subjects": "数学,物理",
  "areas": "鼓楼区",
  "methods": 1,
  "degree": 1,
  "certificate": "教师资格证",
  "certificates": "https://example.com/cert.jpg"
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

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutorsController.java`
