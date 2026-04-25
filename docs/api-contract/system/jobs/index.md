# POST|PUT /system/jobs

## 用途

- `POST`：新增兼职日结工作。
- `PUT`：修改兼职日结工作。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:jobs:add')`。
- 需要具备权限标识：`@ss.hasPermi('system:jobs:edit')`。

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
  "title": "初中数学辅导日结兼职",
  "category": 0,
  "salaryDay": 300,
  "workDate": "2026-04-20",
  "workTime": "09:00-12:00",
  "location": "南京市鼓楼区xx路xx号",
  "contacts": "王老师",
  "phone": "13800138000",
  "description": "负责初中数学辅导",
  "signupLimit": 10
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

## PUT 请求

### Query 参数

- 无。

### Body 示例

```json
{
  "title": "初中数学辅导日结兼职",
  "category": 0,
  "salaryDay": 300,
  "workDate": "2026-04-20",
  "workTime": "09:00-12:00",
  "location": "南京市鼓楼区xx路xx号",
  "contacts": "王老师",
  "phone": "13800138000",
  "description": "负责初中数学辅导",
  "signupLimit": 10
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

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/DailyJobsController.java`
