# GET /system/parents/list

## 用途

- `GET`：GET /system/parents/list。

## 鉴权

- 需要 `Authorization: Bearer <token>`。
- 需要具备权限标识：`@ss.hasPermi('system:parents:list')`。

## 请求头

- `Authorization: Bearer <token>`

## Path 参数

- 无。

## GET 请求

### Query 参数

- `pageNum`：若依标准分页页码。
- `pageSize`：若依标准分页每页条数。
- `status`：订单状态；`0` 表示正常/有效。小程序管理端家教订单分类只查询有效 `parents` 数据，再通过 `bound` 区分是否已绑定。
- `bound`：绑定状态筛选；`true` 只返回有效且已绑定教员的家长订单，`false` 只返回有效且未绑定教员的家长订单。
- 其余筛选字段沿用 `Parents` 对象。

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
      "name": "初中数学辅导",
      "phone": "13800000000",
      "parentName": "张三",
      "parentPhone": "13800000000",
      "bound": true,
      "tutorName": "李四",
      "studentPhone": "13900000000"
    }
  ]
}
```

### 响应字段说明

- `id`：家长家教订单 ID。
- `name`：订单标题。
- `phone`：`parents` 表中的联系方式。
- `parentName`：联表 `user_info` 获取的家长姓名，优先 `real_name`，其次 `user_name`。
- `parentPhone`：联表 `user_info` 获取的手机号，若为空则回退到 `parents.phone`。
- `bound`：是否已存在 `tutoring_binding.parent_id = parents.id` 的绑定关系。
- `tutorName`：绑定学员真实姓名，取最新绑定关系中教员用户的 `real_name`，为空时回退 `user_name`；未绑定时为空。
- `studentPhone`：绑定学员手机号，取最新绑定关系中教员用户 `user_info.phone`；未绑定时为空。

### 失败场景或特殊说明

- 未登录或 token 无效时，请求会失败。
- 无权限时，请求会被拦截。

## 实现来源文件

- `ruoyi-admin/src/main/java/com/ruoyi/web/controller/bussiness/ParentsController.java`
