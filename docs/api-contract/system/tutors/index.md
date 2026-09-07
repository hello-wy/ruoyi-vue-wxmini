# POST|PUT /system/tutors

## 用途

- `POST`：新增大学生/教员并自动生成雪花 ID。
- `PUT`：修改大学生/教员信息。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- `POST` 需要权限标识：`system:tutors:add`。
- `PUT` 需要权限标识：`system:tutors:edit`。

## 请求头

- `Authorization: Bearer <token>`
- `Content-Type: application/json`

## POST Body 示例

```json
{
  "uid": "wx-user-123",
  "realName": "张三",
  "school": "南京大学",
  "major": "数学",
  "subjects": "8,10",
  "areas": "320115,320114",
  "methods": 1,
  "degree": 1,
  "certificateList": "教师资格证,英语六级",
  "certificates": "1234567890123456789,1234567890123456790"
}
```

## POST 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": "1234567890123456700"
}
```

## PUT Body 示例

```json
{
  "id": "1234567890123456700",
  "uid": "wx-user-123",
  "school": "南京大学",
  "major": "数学",
  "certificates": "1234567890123456789,1234567890123456790"
}
```

## PUT 成功响应示例

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

## `certificates` 说明

- 值为英文逗号分隔的审核材料 ID，不再保存图片 URL。
- 材料必须属于 `uid` 对应用户，且不能已绑定到其他教员。
- 类型 `1`、`2`、`3` 各最多一张；类型 `4` 可以多张。
- `PUT` 传空字符串会清空全部审核材料；不传该字段则保持现有材料不变。

## 失败场景或特殊说明

- 未登录、token 无效或无对应权限时，请求会被拦截。
- 材料 ID 格式错误、材料不存在、归属不符或单张类型重复时，请求失败。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutorsController.java`
- `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/TutorMaterialServiceImpl.java`
