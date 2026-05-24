# GET /system/tutors/list

## 用途

- `GET`：查询大学生/教员列表。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:tutors:list')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## GET 请求

### Query 参数

- `pageNum`：若依标准分页页码。
- `pageSize`：若依标准分页每页条数。
- `status`：审核状态；管理端绑定学员弹框通常传 `1`，只查询审核通过的教员/学员。
- `realName`：按 `user_info.real_name` 模糊搜索。
- `phone`：按 `user_info.phone` 模糊搜索。
- 其余筛选字段沿用 `Tutors` 对象。

### Body 示例

- 无。

### 成功响应示例

```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "id": 1,
      "uid": "wx-user-id",
      "realName": "李四",
      "phone": "13900000000",
      "school": "南京大学",
      "major": "数学",
      "subjects": "1,2",
      "areas": "320100",
      "status": 1
    }
  ]
}
```

### 响应字段说明

- `id`：教员/学员库记录 ID，用于绑定接口的 `tutorId`。
- `realName`：联表 `user_info` 获取的真实姓名。
- `phone`：联表 `user_info` 获取的手机号。
- `school`：就读/毕业院校。
- `major`：专业。
- `subjects`：可授科目。
- `areas`：可授区域。
- `status`：审核状态，`1` 表示通过。

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/TutorsController.java`
