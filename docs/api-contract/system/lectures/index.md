# POST|PUT /system/lectures

## 用途

- `POST`：新增课程活动/讲座。
- `PUT`：修改课程活动/讲座。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:lectures:add')`。
- 需要具备权限标识：`@ss.hasPermi('system:lectures:edit')`。

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
  "name": "幸福解码",
  "time": "2026-06-12",
  "registrationFee": 100.00,
  "deposit": 30.00,
  "coursePrice": 3980.00,
  "requiresEnrollment": true
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
  "id": 12,
  "name": "幸福解码",
  "coursePrice": null,
  "coursePriceUpdated": true,
  "requiresEnrollment": false
}
```

### 字段说明

- `coursePrice`：课程全价，仅用于课程信息展示；不参与报名费、定金或任何支付金额计算。
- `coursePriceUpdated`：仅用于 `PUT`。为 `true` 时将 `coursePrice` 写入数据库；`coursePrice` 可为 `null`，此时清空课程全价。未传或非 `true` 时不更新已有课程全价，以兼容历史部分更新请求。
- `requiresEnrollment`：是否报名前需要该课程学籍；缺省或 `true` 表示需要，`false` 表示不需要。

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

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/LecturesController.java`
