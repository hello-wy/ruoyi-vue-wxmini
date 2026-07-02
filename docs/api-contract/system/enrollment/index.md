# POST|PUT /system/enrollment

## 用途

- `POST`：新增学籍信息，绑定一个学员和一个课程。
- `PUT`：修改学籍信息。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- `POST` 需要具备权限标识：`@ss.hasPermi('system:enrollment:add')`。
- `PUT` 需要具备权限标识：`@ss.hasPermi('system:enrollment:edit')`。

## 请求头

- `Authorization: Bearer <token>`
- `Content-Type: application/json`

## Path 参数

- 无。

## POST 请求

### Query 参数

- 无。

### Body 字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `uid` | number | 是 | 学员 ID，对应 `user_info.id` |
| `lectureId` | number | 是 | 课程 ID，对应 `lectures.id` |
| `total` | number | 是 | 总学籍数，不能小于 0 |
| `remain` | number | 否 | 剩余可用学籍数；为空时默认等于 `total`，不能小于 0 且不能大于 `total` |

### Body 示例

```json
{
  "uid": 1,
  "lectureId": 100,
  "total": 40,
  "remain": 40
}
```

### 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

## PUT 请求

### Query 参数

- 无。

### Body 字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | number | 是 | 学籍记录 ID |
| `uid` | number | 是 | 学员 ID，对应 `user_info.id` |
| `lectureId` | number | 是 | 课程 ID，对应 `lectures.id` |
| `total` | number | 是 | 总学籍数，不能小于 0 |
| `remain` | number | 否 | 剩余可用学籍数；为空时默认等于 `total`，不能小于 0 且不能大于 `total` |

### Body 示例

```json
{
  "id": 1898700000000030,
  "uid": 1,
  "lectureId": 100,
  "total": 40,
  "remain": 39
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
- `uid` 对应学员不存在时返回非 200，消息为 `学员不存在`。
- `lectureId` 对应课程不存在时返回非 200，消息为 `课程不存在`。
- 新增时同一学员同一课程已存在正常学籍，返回非 200，消息为 `该学员已绑定该课程学籍`。
- 若同一学员同一课程存在逻辑删除记录，新增会重新激活该记录。
- `remain > total` 时返回非 200，消息为 `剩余学籍数不能大于总学籍数`。

## DELETE 行为说明

- `DELETE /system/enrollment/{ids}` 保持原路径和权限，但后端改为逻辑删除：将 `is_deleted` 置为 `1`。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/StudentEnrollmentController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/StudentEnrollmentServiceImpl.java`
